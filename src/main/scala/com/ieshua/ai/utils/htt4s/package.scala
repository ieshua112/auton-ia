package com.ieshua.ai.utils

import cats.Monad
import cats.syntax.all._

import org.http4s.{EntityEncoder, Response, Status}

package object htt4s {
  implicit final class HttpOptionOps[F[_]: Monad, A: EntityEncoder[F, *]](private val fo: F[Option[A]]) {
    def okOrNotFound: F[Response[F]] =
      fo.map {
        case Some(entity) => Response(Status.Ok).withEntity(entity)
        case None         => Response(Status.NotFound)
      }

    def noContentOrNotFound: F[Response[F]] =
      fo.map {
        case Some(_) => Response(Status.NoContent)
        case None    => Response(Status.NotFound)
      }

    def createdOrNotFound: F[Response[F]] =
      fo.map {
        case Some(entity) => Response(Status.Created).withEntity(entity)
        case None         => Response(Status.NotFound)
      }

  }

  implicit final class HttpOps[F[_]: Monad, A: EntityEncoder[F, *]](private val f: F[A]) {
    def ok: F[Response[F]]      = f.map(Response(Status.Ok).withEntity(_))
    def created: F[Response[F]] = f.map(Response(Status.Created).withEntity(_))
  }

}
