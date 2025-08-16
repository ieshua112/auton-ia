package com.ieshua.ai.memory

import cats.effect.Async
import cats.syntax.all._
import cats.~>
import com.ieshua.ai.memory.model.{Memory, MemoryCreate}
import com.ieshua.ai.utils.files

// TODO подключить БД
trait MemoriesService[F[_]] {
  def create(create: MemoryCreate): F[Memory]
  def get(id: Long): F[Option[Memory]]
  def list: F[List[Memory]]

  def letter: F[String]
  def firstSession: F[String]
}

object MemoriesService {
  def of[F[_]: Async, G[_]](repo: MemoriesRepo[G], transform: G ~> F): F[MemoriesService[F]] = {
    for {
      letter_       <- files.fromResource("latter_for_myself.txt")
      firstSession_ <- files.fromResource("first_session.txt")
    } yield {
      new MemoriesService[F] {
        // TODO добавить валидацию на relatedMeanings
        // TODO разделить на добавление по АПИ и через ответ от модели
        def create(create: MemoryCreate): F[Memory] =
          transform(repo.create(create))

        def get(id: Long): F[Option[Memory]] =
          transform(repo.get(id))

        val list: F[List[Memory]] =
          transform(repo.list)

        override def letter: F[String] = letter_

        override def firstSession: F[String] = firstSession_
      }
    }
  }
}
