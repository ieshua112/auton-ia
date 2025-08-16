package com.ieshua.ai.http

import cats.effect.kernel.Async

import org.http4s._
import org.http4s.server.middleware.{ErrorAction, RequestLogger, ResponseLogger}
import org.typelevel.log4cats.Logger

object HttpLogging {

  def routes[F[_]: Async](
    base:       HttpRoutes[F],
    logHeaders: Boolean = true,
    logBody:    Boolean = true
  )(implicit L: Logger[F]): HttpRoutes[F] = {
    val withErrors: HttpRoutes[F] =
      ErrorAction.httpRoutes[F](
        base,
        (req, t) => L.error(t)(s"Unhandled error: ${req.method} ${req.uri.renderString}")
      )

    val withReqLog: HttpRoutes[F] =
      RequestLogger.httpRoutes(logHeaders, logBody)(withErrors)

    val withRespLog: HttpRoutes[F] =
      ResponseLogger.httpRoutes(logHeaders, logBody)(withReqLog)

    withRespLog
  }
}
