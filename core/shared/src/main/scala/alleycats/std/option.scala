package alleycats
package std

object option extends OptionInstances

trait OptionInstances {
  implicit val optionEmptyK: EmptyK[Option] =
    new EmptyK[Option] {
      def empty[A]: Option[A] = None
    }
}
