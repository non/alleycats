package alleycats
package syntax

object empty extends EmptySyntax

trait EmptySyntax {
  implicit class EmptyOps[A](a: A)(implicit ev: Empty[A]) {
    def isEmpty: Boolean = ev.isEmpty(a)
    def nonEmpty: Boolean = ev.nonEmpty(a)
  }
}
