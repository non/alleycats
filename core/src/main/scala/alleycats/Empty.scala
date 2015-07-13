package alleycats

import cats.{Eq, Monoid}
import cats.syntax.eq._
import simulacrum.typeclass

@typeclass trait Empty[A] {
  def empty: A
  def isEmpty(a: A): Boolean
  def nonEmpty(a: A): Boolean = !isEmpty(a)
}

object Empty {
  implicit def monoidWithEq[A: Monoid: Eq]: Empty[A] = {
    val e = Monoid[A].empty
    new Empty[A] {
      def empty: A = e
      def isEmpty(a: A): Boolean = a === e
    }
  }
}
