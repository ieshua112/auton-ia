package com.ieshua.ai.dialog

import cats.effect.Concurrent
import cats.syntax.all._

import com.ieshua.ai.dialog.model._
import com.ieshua.ai.utils.htt4s.{HttpOps, HttpOptionOps}
import io.circe.Json
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.impl.LongVar
import org.http4s.dsl.io._
import org.http4s.dsl.io.{GET, POST}

object DialogsRoutes {

  def apply[F[_]: Concurrent](service: DialogsService[F]): HttpRoutes[F] = {

    HttpRoutes.of[F] {
      case req @ POST -> Root / "dialog" =>
        req
          .as[DialogCreate]
          .flatMap(service.create)
          .created

      case GET -> Root / "dialog" / LongVar(id) =>
        service.get(id).okOrNotFound

      case GET -> Root / "dialogs" =>
        service.list.ok

      case GET -> Root / "dialog" / "session" / sessionId =>
        service.listBySessionId(sessionId).ok

      case PUT -> Root / "dialog" / LongVar(id) / "end" =>
        service.endDialog(id).noContentOrNotFound

      case req @ PUT -> Root / "dialog" / LongVar(id) / "summary" =>
        req
          .as[Json]
          .flatMap(service.upsertSummary(id, _))
          .noContentOrNotFound

      case req @ POST -> Root / "dialog" / LongVar(dialogId) / "message" =>
        req
          .as[MessageCreate]
          .flatMap(service.appendMessage(dialogId, _))
          .createdOrNotFound

      case GET -> Root / "dialog" / LongVar(dialogId) / "messages" =>
        service
          .listMessages(dialogId)
          .okOrNotFound
    }
  }
}
