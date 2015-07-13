package alleycats.std

import cats.Monad
import scala.util.Try

object try_ extends TryInstances

trait TryInstances {
  implicit val tryMonad: Monad[Try] =
    new Monad[Try] {
      def pure[A](a: A): Try[A] = Try(a)
      override def map[A, B](fa: Try[A])(f: A => B): Try[B] = fa.map(f)
      def flatMap[A, B](fa: Try[A])(f: A => Try[B]): Try[B] = fa.flatMap(f)
    }
}
