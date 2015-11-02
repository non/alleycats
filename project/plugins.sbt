
resolvers += Resolver.url(
  "tpolecat-sbt-plugin-releases",
    url("http://dl.bintray.com/content/tpolecat/sbt-plugin-releases"))(
        Resolver.ivyStylePatterns)

addSbtPlugin("com.github.inthenow" % "sbt-catalysts" % "0.1.6")

// Workaround for scalastyle issue https://github.com/scalastyle/scalastyle/issues/156
// When fixed, remove the excludeAll statement and the scalariform dependency.
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.7.0" excludeAll(
  ExclusionRule(organization = "com.danieltrinh")))
libraryDependencies += "org.scalariform" %% "scalariform" % "0.1.7"
