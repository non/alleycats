package alleycats
package std

object list extends ListInstances

trait ListInstances {
  implicit val listEmptyK: EmptyK[List] =
    new EmptyK[List] {
      def empty[A]: List[A] = Nil
    }
}
