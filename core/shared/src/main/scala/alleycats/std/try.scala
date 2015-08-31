package alleycats
package std

import cats.{Bimonad, Comonad, Monad}
import scala.util.Try

object try_ extends TryInstances

trait TryInstances {
  implicit val tryMonad: Monad[Try] =
    new Monad[Try] {
      def pure[A](a: A): Try[A] = Try(a)
      override def map[A, B](fa: Try[A])(f: A => B): Try[B] = fa.map(f)
      def flatMap[A, B](fa: Try[A])(f: A => Try[B]): Try[B] = fa.flatMap(f)
    }

  implicit val  tryComonad: Comonad[Try]  =
    new Comonad[Try] {
      def coflatMap[A, B](fa: Try[A])(f: Try[A] => B): Try[B] = Try(f(fa))
      override def map[A, B](fa: Try[A])(f: A => B): Try[B] = fa.map(f)
      def extract[A](p: Try[A]): A = p.get
    }

  implicit val  tryBimonad: Bimonad[Try]  =
    new Bimonad[Try] {                               
      def pure[A](a:  A): Try[A] = Try(a)
      def flatMap[A, B](fa: Try[A])(f: A => Try[B]): Try[B] = fa flatMap f
      override def map[A, B](fa: Try[A])(f: A => B): Try[B] = fa map f
      def coflatMap[A, B](fa: Try[A])(f: Try[A] => B): Try[B] = Try(f(fa))
      def extract[A](p: Try[A]): A = p.get
    }
}
