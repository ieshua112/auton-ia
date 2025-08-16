package com.ieshua.ai.utils.transactional

import cats.data.EitherT

trait Transactional[F[_]] {
  def transactionally[A](level: Level)(fa: F[A]): F[A]
}

object Transactional {
  def apply[F[_], A](level: Level)(fa: F[A])(implicit ev: Transactional[F]): F[A] =
    ev.transactionally(level)(fa)

  implicit def eitherT[F[_], E](implicit ev: Transactional[F]): Transactional[EitherT[F, E, *]] =
    new Transactional[EitherT[F, E, *]] {
      override def transactionally[A](level: Level)(f: EitherT[F, E, A]): EitherT[F, E, A] =
        EitherT(ev.transactionally(level)(f.value))
    }
}
