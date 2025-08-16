package com.ieshua.ai.utils.doobie

import cats.effect.Resource
import cats.effect.Resource.ExitCase
import cats.effect.syntax.all._

import com.ieshua.ai.utils.transactional.{Level, Transactional}
import doobie.enumerated.TransactionIsolation
import doobie.free.connection
import doobie.free.connection.ConnectionIO

object TransactionalInstance {
  implicit val ConnectionIOTransactional: Transactional[ConnectionIO] =
    new Transactional[ConnectionIO] {

      private def toInt(level: Level): Int = level match {
        case Level.ReadCommitted  => TransactionIsolation.TransactionReadCommitted.toInt
        case Level.RepeatableRead => TransactionIsolation.TransactionRepeatableRead.toInt
        case Level.Serializable   => TransactionIsolation.TransactionSerializable.toInt
      }

      private val restore = connection.setTransactionIsolation(toInt(Level.ReadCommitted))

      override def transactionally[A](level: Level)(fa: ConnectionIO[A]): ConnectionIO[A] =
        Resource
          .makeCase(connection.setTransactionIsolation(toInt(level))) {
            case (_, ExitCase.Succeeded) => connection.commit.guarantee(restore)
            case _                       => connection.rollback.guarantee(restore)
          }
          .use(_ => fa)
    }
}
