package alleycats
package std

import cats.std.{FutureInstances => CatsFutureInstances}

trait FutureInstances extends CatsFutureInstances

object future extends FutureInstances {
  object Implicits {
    implicit val global = scala.concurrent.ExecutionContext.Implicits.global
  }
}
