package alleycats.std

import cats.{Applicative, Eval, Foldable, Monad, Traverse}

object set extends SetInstances

trait SetInstances {

  // This method advertises parametricity, but relies on using
  // universal hash codes and equality, which hurts our ability to
  // rely on free theorems.
  //
  // Another problem some people have pointed out is the
  // non-associativity of map when using impure functions. For
  // example, consider the following expressions:
  //
  //   import scala.util.Random
  //
  //   val f = (_: Int) => 1
  //   val g = (_: Int) => Random.nextInt
  //
  //   Set(1, 2, 3).map(f).map(g)
  //   Set(1, 2, 3).map(f andThen g)
  //
  // The first Set will contain one random number, and the second will
  // contain three. Since `g` is not a function (speaking strictly)
  // this would not be considered a law violation, but it still makes
  // people uncomfortable.
  implicit val setMonad: Monad[Set] =
    new Monad[Set] {
      def pure[A](a: A): Set[A] = Set(a)
      override def map[A, B](fa: Set[A])(f: A => B): Set[B] = fa.map(f)
      def flatMap[A, B](fa: Set[A])(f: A => Set[B]): Set[B] = fa.flatMap(f)
    }

  // Since iteration order is not guaranteed for sets, folds and other
  // traversals may produce different results for input sets which
  // appear to be the same.
  implicit val setTraverse: Traverse[Set] =
    new Traverse[Set] {
      def foldLeft[A, B](fa: Set[A], b: B)(f: (B, A) => B): B =
        fa.foldLeft(b)(f)
      def foldRight[A, B](fa: Set[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        Foldable.iterateRight(fa.iterator, lb)(f)
      def traverse[G[_]: Applicative, A, B](sa: Set[A])(f: A => G[B]): G[Set[B]] = {
        val G = Applicative[G]
        sa.foldLeft(G.pure(Set.empty[B])) { (buf, a) =>
          G.map2(buf, f(a))(_ + _)
        }
      }
    }
}
