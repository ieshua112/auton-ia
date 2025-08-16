package com.ieshua.ai.memory

import com.ieshua.ai.memory.model.{Memory, MemoryCreate}
import com.ieshua.ai.utils.doobie.metas._
import doobie.ConnectionIO
import doobie.implicits._
import doobie.util.query.Query0

// TODO remake to postgres
object SqlMemoriesRepo extends MemoriesRepo[ConnectionIO] {

  private val Table = fr"memories"

  private val Select =
    fr"""
      SELECT
        id,
        kind,
        content,
        COALESCE(related_meanings, '[]'),
        weight,
        last_accessed,
        times_seen,
        model_version,
        created_at,
        updated_at
      FROM $Table
    """

  private def select(id: Long): Query0[Memory] =
    (Select ++ fr"WHERE id = $id").query[Memory]

  override def create(create: MemoryCreate): ConnectionIO[Memory] = {
    import create._
    val insert =
      sql"""
        INSERT INTO $Table (kind, content, related_meanings, weight, times_seen, model_version)
        VALUES ($kind, $content, $relatedMeanings, $weight, $timesSeen, $modelVersion)
      """.update.withUniqueGeneratedKeys[Long]("id")

    insert.flatMap(id => select(id).unique)
  }

  override def get(id: Long): ConnectionIO[Option[Memory]] =
    select(id).option

  override val list: ConnectionIO[List[Memory]] = {
    val order   = fr"ORDER BY updated_at DESC, id DESC"
    (Select ++ order).query[Memory].to[List]
  }
}
