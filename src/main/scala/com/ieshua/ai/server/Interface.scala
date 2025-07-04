package com.ieshua.ai.server

import zio._
import zio.interop.catz._

import com.comcast.ip4s.IpLiteralSyntax
import com.ieshua.ai.logic.{Agent, Strategy}
import com.ieshua.ai.repo.Memory
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router

object Interface {
  def startServer: ZIO[Any, Throwable, Unit] = {
    val routes = HttpRoutes.of[Task] {
      case req @ POST -> Root / "remember" =>
        req.as[String].flatMap(Memory.remember).as(Response(Status.Ok))

      case GET -> Root / "recall" =>
        Memory.recall.map(Response(Status.Ok).withEntity(_))

      case req @ POST -> Root / "evaluate" =>
        req.as[String].map(Strategy.evaluateSituation).map(Response(Status.Ok).withEntity(_))

      case req @ POST -> Root / "act" =>
        req.as[String].map(Agent.act).map(Response(Status.Ok).withEntity(_))
    }

    val httpApp = Router("/" -> routes).orNotFound

    EmberServerBuilder
      .default[Task]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(httpApp)
      .build
      .useForever
  }
}
