package alleycats
package std

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import cats.std.{AwaitResult, FutureInstances => CatsFutureInstances}

trait FutureInstances extends CatsFutureInstances {

  override def await:AwaitResult = new AwaitResult {
    def result[A](f: Future[A], atMost: FiniteDuration): A = f.value match {
      case Some(v) => v.get
      case None => throw new IllegalStateException()
    }
  }
}

object future extends FutureInstances {
  object Implicits {
    implicit val global = scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
  }
}
