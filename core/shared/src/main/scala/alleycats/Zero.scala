package alleycats

import algebra.ring.{AdditiveMonoid, AdditiveSemigroup}
import cats.{Eq, Monoid}
import cats.syntax.eq._
import simulacrum.typeclass
import scala.collection.generic.CanBuildFrom

@typeclass trait Zero[A] {
  def zero: A

  def isZero(a: A)(implicit ev: Eq[A]): Boolean =
    zero === a

  def nonZero(a: A)(implicit ev: Eq[A]): Boolean =
    zero =!= a
}

object Zero {
  def apply[A](a: => A): Zero[A] =
    new Zero[A] { lazy val zero: A = a }

  implicit def additiveMonoidIsZero[A](implicit ev: AdditiveMonoid[A]): Zero[A] =
    Zero(ev.zero)

  implicit def zeroWithSemigroupIsMonoid[A](implicit z: Zero[A], s: AdditiveSemigroup[A]): AdditiveMonoid[A] =
    new AdditiveMonoid[A] {
      def zero: A = z.zero
      def plus(x: A, y: A): A = s.plus(x, y)
    }
}
