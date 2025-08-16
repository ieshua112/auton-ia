package com.ieshua.ai.utils

import cats.effect.Async

import scala.io.Source

object files {
  def fromResource[F[_]: Async](path: String): F[F[String]] =
    Async[F].memoize {
      Async[F].delay {
        Source
          .fromResource(path)
          .getLines()
          .filter(_.nonEmpty)
          .mkString("\n")
      }
    }
}
