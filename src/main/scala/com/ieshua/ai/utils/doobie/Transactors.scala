package com.ieshua.ai.utils.doobie

import scala.concurrent.ExecutionContext

import cats.effect.{Async, Resource}

import com.evolutiongaming.catshelper.Runtime
import com.ieshua.ai.AppConfig.DbConfig
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor

object Transactors {

  def mkTransactor[F[_]: Async](dbConfig: DbConfig): Resource[F, Transactor[F]] =
    for {
      cores      <- Resource.eval(Runtime[F].availableCores)
      context    <- ExecutionContexts.fixedThreadPool[F](cores)
      transactor <- mkHikariTransactor(dbConfig, context)
    } yield transactor

  private def mkHikariTransactor[F[_]: Async](
    config:    DbConfig,
    connectEc: ExecutionContext
  ): Resource[F, Transactor[F]] =
    HikariTransactor.newHikariTransactor(
      config.driver,
      config.url,
      config.user,
      config.password,
      connectEc
    )
}
