package alleycats

import cats.{Eq, Monoid}
import cats.syntax.eq._
import simulacrum.typeclass
import scala.collection.generic.CanBuildFrom

@typeclass trait Empty[A] {
  def empty: A
  def isEmpty(a: A): Boolean
  def nonEmpty(a: A): Boolean = !isEmpty(a)
}

object Empty extends EmptyInstances0

trait EmptyInstances0 extends EmptyInstances1 {
  implicit def iterableIsEmpty[CC[X] <: Iterable[X], A: Eq](implicit cbf: CanBuildFrom[CC[A], A, CC[A]]): Empty[CC[A]] =
    new Empty[CC[A]] {
      def empty: CC[A] = cbf().result
      def isEmpty(ca: CC[A]): Boolean = ca.isEmpty
      override def nonEmpty(ca: CC[A]): Boolean = ca.nonEmpty
    }
}

trait EmptyInstances1 {
  implicit def monoidWithEqIsEmpty[A: Monoid: Eq]: Empty[A] = {
    val e = Monoid[A].empty
    new Empty[A] {
      def empty: A = e
      def isEmpty(a: A): Boolean = a === e
    }
  }
}
