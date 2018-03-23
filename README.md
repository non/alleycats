## Alleycats

# ❗Deprecated❗ This project was moved into [typelevel/cats](https://github.com/typelevel/cats). 

> "But an outlaw can be defined as somebody who lives outside the law,
> beyond the law and not necessarily against it."
>
> -- Hunter S. Thompson, "The Art of Journalism No. 1"
>    The Paris Review, Fall 2000, Issue 156.

[![Build Status](https://api.travis-ci.org/non/alleycats.png)](https://travis-ci.org/non/alleycats)
[![Chat](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/non/alleycats)
[![Maven Central](https://img.shields.io/maven-central/v/org.typelevel/alleycats_2.11.svg)](https://maven-badges.herokuapp.com/maven-central/org.typelevel/alleycats_2.11)

### Overview

Alleycats is an extension of the [Cats](http://github.com/non/cats)
project that exists to support types which are not entirely savory or
*law-abiding* but which may prove useful or necessary in some
situations.

In some cases, a type class instance can almost (but not quite) obey
the required laws (e.g. a `Monad` instance for `scala.util.Try`). In
other cases, type classes which lack laws or constraints may still be
useful in some cases (e.g. `Empty[_]`, a type class which provides
some notion of "emptiness").

Rather than argue about whether to permit these types in Cats, we
provide a (slightly-disreputable) home for them here.

### Getting Alleycats

Alleycats is currently available for Scala 2.10, 2.11 and 2.12.

To get started with SBT, simply add the following to your `build.sbt`
file:

```scala
resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies += "org.typelevel" %% "alleycats-core" % "0.1.9"
```

You can also build Alleycats using
[SBT](http://www.scala-sbt.org/0.13/tutorial/index.html).

Alleycats uses [sbt-catalysts][] 0.1.9 to provide a consistent view of
its dependencies on Typelevel projects. As of sbt-catalysts 0.1.9 the
base version profile can be found [here][typelevel-deps].

[sbt-catalysts]: https://github.com/typelevel/sbt-catalysts
[typelevel-deps]: https://github.com/typelevel/sbt-catalysts/blob/master/src/main/scala/org/typelevel/TypelevelDeps.scala


### Type classes

Alleycats introduces several new type classes. Here is an overview of
the instances introduced.

#### Empty[A], Zero[A], and One[A]

A commonly-requested type class is one that encodes the idea of a set
having an identity element. Normally this would be done by defining a
`Monoid[A]` instance, but in some cases the idea of emptiness is
independent of a particular associative operation.

In this case, `Empty[A]` may be used in place of `Monoid[A]`. It
provides access to the *identity* element (via the `.empty` method),
and can also provide `.isEmpty` and `.nonEmpty` if an `Eq[A]` is
available.

The two other type classes, `Zero[A]` and `One[A]`, are similar,
except they correspond to `AdditiveMonoid[A]` and
`MultiplicativeMonoid[A]` (found in the `algebra.ring` package). Their
methods are called `zero` and `one`, respectively.

While none of these type classes have their own laws, they are
required not to violate the monoid laws when applicable. This means
that if `Empty[A]` and `Semigroup[A]` are both available, that
`Empty[A].empty` **must** function as an identity element for
`Semigroup[A].combine`. In fact, together these instances can be
viewed as a `Monoid[A]` (and there is an implicit method to that
effect).

The same rules apply to `Zero[A]` and `One[A]` and their respective
associative operations.

#### Pure[F[\_]] and Extract[F[\_]]

The `Pure[F]` type class represents the `pure` method of
`Applicative[F]` separated from its `map` and `ap` methods. Like the
previous type classes, if `Pure[F]` and `Apply[F]` are both available
they are required to be consistent (and should provide a valid
`Applicative[F]` instance).

Similarly, `Extract[F]` represents the `extract` method of
`Comonad[F]` without `coflatMap` and `map` methods. When `Extract[F]`
and `CoflatMap[F]` are available, they should provide a valid
`Comonad[F]` instance.

#### EmptyK[F[\_]]

Finally, `EmptyK[F]` generalizes the `empty[A]` method from
`MonoidK[F]`. The pattern here is the same as before --
`SemigroupK[F]` and `EmptyK[F]` should provide a valid `MonoidK[F]`
instance.

### Instances

Alleycats also provides some "disreputable" type class instances.

#### Set[\_] instances

Scala's `Set[_]` takes advantage of the universal availability of
`.hashCode` and `.equals`. This makes it difficult to use
[parametricity](http://failex.blogspot.jp/2013/06/fake-theorems-for-free.html)
to reason about sets, and casts some doubt on their use with functors
and monads.

Alleycats provides `Monad[Set]` and `Traverse[Set]`. You can import
these instances via `import alleycats.std.set._`.

#### Try[\_] instances

Scala's `Try[_]` is intended to replace the need for `try { ... }
catch { ... }` syntax in Scala programs, to ease error-handling, and
to transport exceptions as data. Due to the way `Try` transparently
catches exceptions in `.map` and `.flatMap`, some people are skeptical
that `Try` fulfills the necessary functor/monad laws.

Alleycats provides a `Monad[Try]`. You can import this instance via
`import alleycats.std.try._`.

#### Iterable[\_] instances

Scala's `collection.Iterable[_]` offers no guarantees that it's immutable, 
since it abstracts over the `mutable` and `immutable` variants. However it's 
the type used to represent a `Map`s `values`, and its often desirable to treat the 
values of a map as a `Foldable` collection. Alleycats provides a `Foldable[Iterable]`, eg:

```
import cats.implicits._
import alleycats.std.iterable._

//Result "AppleOrange"
Map(1 -> "Apple", 2 -> "Orange").values.combineAll
```

### Contributing

This project's goal is to be very liberal about accepting type class
instances, but to only provide instances which are absent from
Cats. Feel free to open a pull-request on either project --
law-abiding instances will end up in Cats, and everything else will
end up here.

We are using the [Cats Gitter channel](https://gitter.im/non/cats) to
discuss the Alleycats project as well. As with the Cats project,
people are expected to follow the
[Typelevel Code of Conduct](http://typelevel.org/conduct.html) when
discussing Alleycats on the Github page, Gitter channel, or in other
venues.

### Copyright and License

All code is available to you under the MIT license, available at
http://opensource.org/licenses/mit-license.php and also in the
[COPYING](COPYING) file.

Copyright Erik Osheim, 2015.
