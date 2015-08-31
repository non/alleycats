package alleycats
package tests

import alleycats.std.future.Implicits.global
import alleycats.std.FutureInstances
import cats.tests.{FutureTests => CatsFutureTests}

class FutureTests extends CatsFutureTests with FutureInstances
