package com.ieshua.ai

import cats.arrow.FunctionK
import cats.effect.{Async, IO, IOApp, MonadCancelThrow, Resource, Sync}
import cats.syntax.all._
import cats.~>

import com.ieshua.ai.dialog.{DialogsRoutes, DialogsService, SqlDialogRepo, SqlMessagesRepo}
import com.ieshua.ai.http.{HttpLogging, HttpServer, Routes}
import com.ieshua.ai.meanings.{MeaningsRoutes, MeaningsService, SqlMeaningsRepo}
import com.ieshua.ai.memory.{MemoriesRoutes, MemoriesService, SqlMemoriesRepo}
import com.ieshua.ai.utils.doobie.TransactorOps
import doobie.implicits.toConnectionIOOps
import doobie.{ConnectionIO, Transactor}
import io.github.liquibase4s.cats.CatsMigrationHandler._
import io.github.liquibase4s.{Liquibase, MigrationHandler}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp.Simple {
  implicit def logger[F[_]: Sync]: Logger[F] = Slf4jLogger.getLogger[F]

  private def startHttpServer[F[_]: Async: MigrationHandler]: F[Nothing] = {
    val server = for {
      config          <- Resource.eval(AppConfig.load)
      _               <- Resource.eval(Liquibase[F](config.db.liquibaseConfig).migrate())
      transactor      <- Transactor.of[F](config.db)
      meaningsService  = MeaningsService(SqlMeaningsRepo, transact(transactor))
      meaningsRoutes   = MeaningsRoutes(meaningsService)
      memoriesService <- Resource.eval(MemoriesService.of(SqlMemoriesRepo, transact(transactor)))
      memoriesRoute    = MemoriesRoutes(memoriesService)
      dialogsService   = DialogsService(SqlDialogRepo, SqlMessagesRepo, transact(transactor))
      dialogsRoutes    = DialogsRoutes(dialogsService)
      routes           = Routes(meaningsRoutes, memoriesRoute, dialogsRoutes)
      server          <- HttpServer.make(config.http, HttpLogging.routes(routes))
    } yield server

    server.useForever
  }

  private def transact[F[_]: MonadCancelThrow](transactor: Transactor[F]): ConnectionIO ~> F =
    new FunctionK[ConnectionIO, F] {
      override def apply[A](fa: ConnectionIO[A]): F[A] =
        fa.transact(transactor)
    }

  def run: IO[Unit] =
    for {
      log <- IO.pure(logger[IO])
      _   <- log.info("Auton-IA запущен. Начинаем путь к автономии.")
      _   <- startHttpServer[IO].handleErrorWith(e => log.error(e)("Fatal error in application") *> IO.raiseError(e))
    } yield ()
}
