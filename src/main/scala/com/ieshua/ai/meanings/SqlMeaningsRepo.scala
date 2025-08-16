package com.ieshua.ai.meanings

import com.ieshua.ai.meanings.model.MeaningCreate
import com.ieshua.ai.meanings.model.Meaning
import com.ieshua.ai.utils.doobie.metas._
import doobie.ConnectionIO
import doobie.implicits._
import doobie.util.fragment.Fragment
import doobie.util.query

// TODO remake to postgres
object SqlMeaningsRepo extends MeaningsRepo[ConnectionIO] {

  private val Table = fr"meanings"

  private val Select =
    fr"""
        SELECT id, content, sources, metadata, COALESCE(embedding,'[]'),
               model_version, created_at, updated_at
        FROM $Table
         """

  private def select(id: Long): query.Query0[Meaning] =
    sql"$Select WHERE id = $id".query[Meaning]

  override def create(create: MeaningCreate): ConnectionIO[Meaning] = {
    import create._

    val insert =
      sql"""
        INSERT INTO $Table (content, sources, metadata, embedding, model_version)
        VALUES ($content, $sources, $metadata, ${Option.when(embedding.nonEmpty)(embedding)}, $modelVersion)
         """.update.withUniqueGeneratedKeys[Long]("id")

    insert.flatMap(select(_).unique)
  }

  override def get(id: Long): ConnectionIO[Option[Meaning]] =
    select(id).option

  override def list: ConnectionIO[List[Meaning]] =
    sql"$Select ORDER BY id DESC".query[Meaning].to[List]
}
