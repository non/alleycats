## Alleycats

> "But an outlaw can be defined as somebody who lives outside the law,
> beyond the law and not necessarily against it."
>
> -- Hunter S. Thompson, "The Art of Journalism No. 1"
>    The Paris Review, Fall 2000, Issue 156.

### Overview

Alleycats is an extension of the [Cats](http://github.com/non/cats)
project that exists to support types which are not entirely savory or
*law-abiding* but which may prove useful or necessary in some
situations.

In some cases, a type class instance can almost (but not quite) obey
the required laws (e.g. a `Monad` instance for `scala.util.Try`). In
other cases, type classes which lack laws or constraints may still be
useful in some cases (e.g. `Each[_]`, a type class which provides
`foreach`).

Rather than argue about whether to permit these types in Cats, we
provide a (slightly-disreputable) home for them here.

### Getting Alleycats

Alleycats supports Scala 2.10 and 2.11. It is currently unpublished.

You can build Alleycats using
[SBT](http://www.scala-sbt.org/0.13/tutorial/index.html).

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
