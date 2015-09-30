package alleycats

import algebra.ring.{AdditiveMonoid, AdditiveSemigroup}
import cats.{Eq, Monoid}
import cats.syntax.eq._
import export.imports
import simulacrum.typeclass
import scala.collection.generic.CanBuildFrom

@typeclass trait Zero[A] {
  def zero: A

  def isZero(a: A)(implicit ev: Eq[A]): Boolean =
    zero === a

  def nonZero(a: A)(implicit ev: Eq[A]): Boolean =
    zero =!= a
}

object Zero extends Zero0 {
  def apply[A](a: => A): Zero[A] =
    new Zero[A] { lazy val zero: A = a }

  // Ideally this would be an exported subclass instance provided by AdditiveMonoid
  implicit def additiveMonoidIsZero[A](implicit ev: AdditiveMonoid[A]): Zero[A] =
    Zero(ev.zero)

  // Ideally this would be an instance exported to AdditiveMonoid
  implicit def zeroWithSemigroupIsMonoid[A](implicit z: Zero[A], s: AdditiveSemigroup[A]): AdditiveMonoid[A] =
    new AdditiveMonoid[A] {
      def zero: A = z.zero
      def plus(x: A, y: A): A = s.plus(x, y)
    }
}

@imports[Zero]
trait Zero0
