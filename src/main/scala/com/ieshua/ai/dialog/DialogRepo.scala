package com.ieshua.ai.dialog

import com.ieshua.ai.dialog.model.{Dialog, DialogCreate}
import io.circe.Json

trait DialogRepo[F[_]] {
  def create(in: DialogCreate): F[Dialog]
  def get(id: Long): F[Option[Dialog]]
  def list: F[List[Dialog]]
  def listBySession(sessionId: String): F[List[Dialog]]
  def endNow(id: Long): F[Int]
  def upsertSummary(id: Long, summary: Json): F[Int]
}
