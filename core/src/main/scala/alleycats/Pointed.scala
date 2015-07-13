package alleycats

import cats.{Applicative, FlatMap, Monad}

trait Pointed[F[_]] {
  def point[A](a: A): F[A]
}

object Pointed {
  implicit def applicativeIsPointed[F[_]](implicit ev: Applicative[F]): Pointed[F] =
    new Pointed[F] {
      def point[A](a: A): F[A] = ev.pure(a)
    }

  implicit def pointedFlatMapIsMonad[F[_]](implicit p: Pointed[F], fm: FlatMap[F]): Monad[F] =
    new Monad[F] {
      def pure[A](a: A): F[A] = p.point(a)
      override def map[A, B](fa: F[A])(f: A => B): F[B] = fm.map(fa)(f)
      def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] = fm.flatMap(fa)(f)
    }
}
