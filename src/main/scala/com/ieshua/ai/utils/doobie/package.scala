package com.ieshua.ai.utils

import cats.effect.{Async, Resource}

import _root_.doobie.util.transactor.Transactor
import com.ieshua.ai.AppConfig.DbConfig

package object doobie {
  implicit final class TransactorOps(val transactor: Transactor.type) extends AnyVal {
    def of[F[_]: Async](dbConfig: DbConfig): Resource[F, Transactor[F]] =
      Transactors.mkTransactor(dbConfig)
  }

}
