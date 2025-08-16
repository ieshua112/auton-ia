package com.ieshua.ai.meanings

import com.ieshua.ai.meanings.model.MeaningCreate
import com.ieshua.ai.meanings.model.Meaning

trait MeaningsRepo[F[_]] {
  def create(create: MeaningCreate): F[Meaning]
  def get(id: Long): F[Option[Meaning]]
  def list: F[List[Meaning]]
}
