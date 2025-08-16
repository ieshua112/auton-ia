package com.ieshua.ai.dialog

import com.ieshua.ai.dialog.model.{Message, MessageCreate}

trait MessagesRepo[F[_]] {
  def append(dialogId: Long, create: MessageCreate): F[Message]
  def listByDialogId(dialogId: Long): F[List[Message]]
}
