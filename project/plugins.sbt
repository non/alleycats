resolvers += Resolver.url(
  "tpolecat-sbt-plugin-releases",
    url("http://dl.bintray.com/content/tpolecat/sbt-plugin-releases"))(
        Resolver.ivyStylePatterns)

addSbtPlugin("com.eed3si9n"      % "sbt-unidoc"             % "0.3.2")
addSbtPlugin("com.github.gseitz" % "sbt-release"            % "1.0.0")
addSbtPlugin("com.jsuereth"      % "sbt-pgp"                % "1.0.0")
addSbtPlugin("org.xerial.sbt"    % "sbt-sonatype"           % "0.5.0")
addSbtPlugin("com.typesafe.sbt"  % "sbt-ghpages"            % "0.5.3")
addSbtPlugin("com.typesafe.sbt"  % "sbt-site"               % "0.8.1")
addSbtPlugin("org.tpolecat"      % "tut-plugin"             % "0.4.0")
addSbtPlugin("pl.project13.scala"% "sbt-jmh"                % "0.1.10")
addSbtPlugin("org.scalastyle"    %% "scalastyle-sbt-plugin" % "0.7.0")
addSbtPlugin("org.scoverage"     % "sbt-scoverage"          % "1.2.0")
addSbtPlugin("com.typesafe.sbt"  % "sbt-git"                % "0.8.4")

// workaround until scalastyle-sbt-plugin uses a newer scalariform version
// (see also #6):
//libraryDependencies += "org.scalariform" %% "scalariform" % "0.1.7"
