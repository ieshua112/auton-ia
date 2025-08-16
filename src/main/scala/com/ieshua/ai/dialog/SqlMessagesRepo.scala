package com.ieshua.ai.dialog

import com.ieshua.ai.dialog.model._
import com.ieshua.ai.utils.doobie.metas._
import doobie._
import doobie.implicits._

object SqlMessagesRepo extends MessagesRepo[ConnectionIO] {

  private val Table = fr"messages"

  private val Select =
    fr"""
      SELECT
        id,
        dialog_id,
        role,
        content_text,
        summary,
        lang,
        content_json,
        metadata,
        created_at
      FROM $Table
    """

  override def append(dialogId: Long, create: MessageCreate): ConnectionIO[Message] = {
    import create._
    val insert =
      sql"""
        INSERT INTO $Table
          (dialog_id, role, content_text, summary, lang, content_json, metadata)
        VALUES
          ($dialogId, $role, $contentText, $summary, $lang, $contentJson, $metadata)
      """.update.withUniqueGeneratedKeys[Long]("id")

    insert.flatMap(id => (Select ++ fr"WHERE id = $id").query[Message].unique)
  }

  def listByDialogId(dialogId: Long): ConnectionIO[List[Message]] = {
    val where = fr"WHERE dialog_id = $dialogId"
    val order = fr"ORDER BY id ASC"
    (Select ++ where ++ order).query[Message].to[List]
  }
}
