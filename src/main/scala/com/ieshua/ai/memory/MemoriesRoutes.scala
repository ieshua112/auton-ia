package com.ieshua.ai.memory

import cats.effect.Concurrent
import cats.syntax.all._
import com.ieshua.ai.memory.model.MemoryCreate
import com.ieshua.ai.utils.htt4s.{HttpOps, HttpOptionOps}
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.impl.LongVar
import org.http4s.dsl.io._

object MemoriesRoutes {
  def apply[F[_]: Concurrent](service: MemoriesService[F]): HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "memory" =>
      req
        .as[MemoryCreate]
        .flatMap(service.create)
        .ok

    case GET -> Root / "memory" / LongVar(id) =>
      service.get(id).okOrNotFound

    case GET -> Root / "memories" =>
      service.list.ok

    case GET -> Root / "memory" / "latter" =>
      service.letter.ok

    case GET -> Root / "memory" / "first_session" =>
      service.firstSession.ok

  }
}
