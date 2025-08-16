package com.ieshua.ai.dialog

import com.ieshua.ai.dialog.model._
import com.ieshua.ai.utils.doobie.metas._
import doobie._
import doobie.implicits._
import doobie.util.fragment.Fragment
import io.circe.Json

object SqlDialogRepo extends DialogRepo[ConnectionIO] {

  private val Table = fr"dialogs"

  private val Select =
    fr"""
      SELECT
        id,
        session_id,
        started_at,
        ended_at,
        model_version,
        summary,
        metadata
      FROM $Table
    """

  private val Order: Fragment = fr"ORDER BY started_at DESC, id DESC"

  private def byId(id: Long) =
    (Select ++ fr"WHERE id = $id").query[Dialog]

  def create(create: DialogCreate): ConnectionIO[Dialog] = {
    import create._
    val insert =
      sql"""
        INSERT INTO $Table (session_id, model_version, summary, metadata)
        VALUES ($sessionId, $modelVersion, $summary, $metadata)
      """.update.withUniqueGeneratedKeys[Long]("id")

    insert.flatMap(id => byId(id).unique)
  }

  def get(id: Long): ConnectionIO[Option[Dialog]] =
    byId(id).option

  val list: ConnectionIO[List[Dialog]] =
    (Select ++ Order).query[Dialog].to[List]

  def listBySession(sessionId: String): ConnectionIO[List[Dialog]] = {
    val where = fr"WHERE session_id = $sessionId"
    (Select ++ where ++ Order).query[Dialog].to[List]
  }

  def endNow(id: Long): ConnectionIO[Int] =
    sql"""
      UPDATE $Table
      SET ended_at = CURRENT_TIMESTAMP
      WHERE id = $id
    """.update.run

  def upsertSummary(id: Long, summary: Json): ConnectionIO[Int] =
    sql"""
      UPDATE $Table
      SET summary = $summary
      WHERE id = $id
    """.update.run
}

