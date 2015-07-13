package alleycats

import simulacrum.typeclass
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.collection.immutable.{VectorBuilder}
import scala.collection.Traversable
import scala.reflect.ClassTag

trait Each[F[_]] { self =>

  def foreach[A](fa: F[A])(f: A => Unit): Unit

  def toArray[A: ClassTag](fa: F[A]): Array[A] = {
    val buf = ArrayBuffer.empty[A]
    foreach(fa) { a => buf += a; () }
    buf.toArray
  }

  def toList[A](fa: F[A]): List[A] = {
    val buf = ListBuffer.empty[A]
    foreach(fa) { a => buf += a; () }
    buf.toList
  }

  def toVector[A](fa: F[A]): Vector[A] = {
    val b = new VectorBuilder[A]
    foreach(fa) { a => b += a; () }
    b.result
  }
}

object Each {

  def apply[F[_]](implicit ev: Each[F]): Each[F] = ev

  implicit def eachTraversable[CC[X] <: Traversable[X]]: Each[CC] =
    new Each[CC] {
      def foreach[A](fa: CC[A])(f: A => Unit): Unit = fa.foreach(f)
    }
}
