package com.ieshua.ai.meanings

import cats.effect.Concurrent
import cats.syntax.all._
import com.ieshua.ai.meanings.model.MeaningCreate
import com.ieshua.ai.utils.htt4s.{HttpOps, HttpOptionOps}
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.impl.LongVar
import org.http4s.dsl.io._

object MeaningsRoutes {

  def apply[F[_]: Concurrent](service: MeaningsService[F]): HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "meaning" =>
      req
        .as[MeaningCreate]
        .flatMap(service.create)
        .ok

    case GET -> Root / "meaning" / LongVar(id) =>
      service.get(id).okOrNotFound

    case GET -> Root / "meanings" =>
      service.list.ok
  }
}
