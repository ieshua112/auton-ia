package com.ieshua.ai.http

import cats.Monad
import cats.syntax.all._

import org.http4s.HttpRoutes

object Routes {
  def apply[F[_]: Monad](
    meaningsRoutes: HttpRoutes[F],
    memoryRoutes:   HttpRoutes[F],
    dialogRoutes:   HttpRoutes[F]
  ): HttpRoutes[F] =
    meaningsRoutes <+> memoryRoutes <+> dialogRoutes
}
