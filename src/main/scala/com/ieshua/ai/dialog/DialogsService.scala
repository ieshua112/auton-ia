package com.ieshua.ai.dialog

import cats.data.OptionT
import cats.syntax.all._
import cats.{Monad, ~>}
import com.ieshua.ai.dialog.model._
import io.circe.Json

trait DialogsService[F[_]] {
  def create(create: DialogCreate): F[Dialog]
  def get(id: Long): F[Option[Dialog]]
  def list: F[List[Dialog]]
  def listBySessionId(sessionId: String): F[List[Dialog]]
  def endDialog(id: Long): F[Option[Unit]]
  def upsertSummary(id: Long, summary: Json): F[Option[Unit]]

  // TODO не вставлять в завершенный диалог
  def appendMessage(dialogId: Long, create: MessageCreate): F[Option[Message]]
  def listMessages(dialogId: Long): F[Option[List[Message]]]
}

object DialogsService {
  def apply[F[_], G[_]: Monad](
    logs:      DialogRepo[G],
    messages:  MessagesRepo[G],
    transform: G ~> F
  ): DialogsService[F] =
    new DialogsService[F] {

      override def create(create: DialogCreate): F[Dialog] =
        transform(logs.create(create))

      override def get(id: Long): F[Option[Dialog]] =
        transform(logs.get(id))

      override val list: F[List[Dialog]] =
        transform(logs.list)

      override def listBySessionId(sessionId: String): F[List[Dialog]] =
        transform(logs.listBySession(sessionId))

      override def endDialog(id: Long): F[Option[Unit]] =
        transform(logs.endNow(id).map(count => Option.when(count > 0)(())))

      private def withDialogExists[T](dialogId: Long, g: G[T]): G[Option[T]] =
        OptionT(logs.get(dialogId))
          .semiflatMap(_ => g)
          .value

      override def upsertSummary(id: Long, summary: Json): F[Option[Unit]] =
        transform(withDialogExists(id, logs.upsertSummary(id, summary).void))

      override def appendMessage(dialogId: Long, create: MessageCreate): F[Option[Message]] =
        transform(withDialogExists(dialogId, messages.append(dialogId, create)))

      override def listMessages(
        dialogId: Long
      ): F[Option[List[Message]]] =
        transform(withDialogExists(dialogId, messages.listByDialogId(dialogId)))
    }
}
