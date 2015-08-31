package alleycats

import algebra.ring.{MultiplicativeMonoid, MultiplicativeSemigroup}
import cats.{Eq, Monoid}
import cats.syntax.eq._
import simulacrum.typeclass
import scala.collection.generic.CanBuildFrom

@typeclass trait One[A] {
  def one: A

  def isOne(a: A)(implicit ev: Eq[A]): Boolean =
    one === a

  def nonOne(a: A)(implicit ev: Eq[A]): Boolean =
    one =!= a
}

object One {
  def apply[A](a: => A): One[A] =
    new One[A] { lazy val one: A = a }

  implicit def multiplicativeMonoidIsOne[A](implicit ev: MultiplicativeMonoid[A]): One[A] =
    One(ev.one)

  implicit def oneWithSemigroupIsMonoid[A](implicit z: One[A], s: MultiplicativeSemigroup[A]): MultiplicativeMonoid[A] =
    new MultiplicativeMonoid[A] {
      def one: A = z.one
      def times(x: A, y: A): A = s.times(x, y)
    }
}
