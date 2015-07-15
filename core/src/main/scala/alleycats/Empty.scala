package alleycats

import cats.{Eq, Monoid}
import cats.syntax.eq._
import simulacrum.typeclass
import scala.collection.generic.CanBuildFrom

@typeclass trait Empty[A] {
  def empty: A

  def isEmpty(a: A)(implicit ev: Eq[A]): Boolean =
    empty === a

  def nonEmpty(a: A)(implicit ev: Eq[A]): Boolean =
    empty =!= a
}

object Empty extends EmptyInstances0 {
  def apply[A](a: => A): Empty[A] =
    new Empty[A] {
      lazy val empty: A = a
    }
}

trait EmptyInstances0 extends EmptyInstances1 {
  implicit def iterableIsEmpty[CC[X] <: Iterable[X], A](implicit cbf: CanBuildFrom[CC[A], A, CC[A]]): Empty[CC[A]] =
    Empty(cbf().result)
}

trait EmptyInstances1 {
  implicit def monoidIsEmpty[A: Monoid]: Empty[A] =
    Empty(Monoid[A].empty)
}
