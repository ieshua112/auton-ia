package com.ieshua.ai.meanings

import cats.~>
import com.ieshua.ai.meanings.model.MeaningCreate
import com.ieshua.ai.meanings.model.Meaning

trait MeaningsService[F[_]] {
  def create(create: MeaningCreate): F[Meaning]
  def get(id: Long): F[Option[Meaning]]
  def list: F[List[Meaning]]
}

object MeaningsService {
  def apply[F[_], G[_]](repo: MeaningsRepo[G], transform: G ~> F): MeaningsService[F] =
    new MeaningsService[F]() {
      override def create(create: MeaningCreate): F[Meaning] =
        transform(repo.create(create))

      override def get(id: Long): F[Option[Meaning]] =
        transform(repo.get(id))

      override val list: F[List[Meaning]] =
        transform(repo.list)
    }
}
