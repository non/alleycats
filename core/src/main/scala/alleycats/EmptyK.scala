package alleycats

import export._
import simulacrum.typeclass

@typeclass trait EmptyK[F[_]] { self =>
  def empty[A]: F[A]

  def synthesize[A]: Empty[F[A]] =
    new Empty[F[A]] {
      def empty: F[A] = self.empty[A]
    }
}

object EmptyK extends EmptyK0

// Currently a simulacrum bug prevents this trait from being folded into
// the object
@imports[EmptyK]
trait EmptyK0

@exports
object EmptyKInstances {
  @export(Instantiated)
  implicit def instantiate[F[_], T](implicit ekf: EmptyK[F]): Empty[F[T]] = ekf.synthesize[T]
}
