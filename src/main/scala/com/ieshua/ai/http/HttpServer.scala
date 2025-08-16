package com.ieshua.ai.http

import cats.effect.{Async, Resource}

import com.ieshua.ai.AppConfig.HttpConfig
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server

object HttpServer {
  def make[F[_]: Async](config: HttpConfig, routes: HttpRoutes[F]): Resource[F, Server] =
    EmberServerBuilder.default
      .withHost(config.host)
      .withPort(config.port)
      .withHttpApp(routes.orNotFound)
      .build
}
