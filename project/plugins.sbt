
resolvers += Resolver.url(
  "tpolecat-sbt-plugin-releases",
    url("http://dl.bintray.com/content/tpolecat/sbt-plugin-releases"))(
        Resolver.ivyStylePatterns)

resolvers += Resolver.url("typelevel-sbt-plugins", url("http://dl.bintray.com/content/typelevel/sbt-plugins"))(Resolver.ivyStylePatterns)

addSbtPlugin("org.typelevel" % "sbt-catalysts" % "0.1.8")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")
