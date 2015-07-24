import com.typesafe.sbt.pgp.PgpKeys.publishSigned
import com.typesafe.sbt.SbtSite.SiteKeys._
import com.typesafe.sbt.SbtGhPages.GhPagesKeys._
import pl.project13.scala.sbt.SbtJmh._
import sbtunidoc.Plugin.UnidocKeys._
import ScoverageSbtPlugin._
import ReleaseTransformations._

lazy val scoverageSettings = Seq(
  ScoverageKeys.coverageMinimum := 60,
  ScoverageKeys.coverageFailOnMinimum := false,
  ScoverageKeys.coverageHighlighting := scalaBinaryVersion.value != "2.10")

lazy val buildSettings = Seq(
  organization := "org.spire-math",
  scalaVersion := "2.11.7",
  crossScalaVersions := Seq("2.10.5", "2.11.7")
)

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:experimental.macros",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-Yinline-warnings",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture"
  ) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 11)) => Seq("-Ywarn-unused-import")
    case _             => Seq.empty
  }),
  scalacOptions in (Compile, console) ~= (_ filterNot (_ == "-Ywarn-unused-import")),
  scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value,
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),
  libraryDependencies ++= Seq(
    "com.github.mpilquist" %% "simulacrum" % "0.3.0",
    "org.typelevel" %% "machinist" % "0.3.0",
    compilerPlugin("org.scalamacros" % "paradise" % "2.1.0-M5" cross CrossVersion.full),
    compilerPlugin("org.spire-math" %% "kind-projector" % "0.6.3")
  ),
  scmInfo := Some(ScmInfo(url("https://github.com/non/alleycats"),
    "scm:git:git@github.com:non/alleycats.git")),
  commands += gitSnapshots
)

lazy val alleycatsSettings = buildSettings ++ commonSettings ++ publishSettings ++ scoverageSettings

lazy val disciplineDependencies = Seq(
  "org.scalacheck" %% "scalacheck" % "1.12.4",
  "org.typelevel" %% "discipline" % "0.3"
)

lazy val alleycats = project.in(file("."))
  .settings(moduleName := "alleycats")
  .settings(alleycatsSettings)
  .aggregate(core, laws, tests)
  .dependsOn(core, laws, tests % "test-internal -> test")

lazy val core = project
  .settings(moduleName := "alleycats-core")
  .settings(alleycatsSettings)
  .settings(libraryDependencies += "org.spire-math" %% "cats-core" % "0.1.2")

lazy val laws = project.dependsOn(core)
  .settings(moduleName := "alleycats-laws")
  .settings(alleycatsSettings)
  .settings(libraryDependencies ++= disciplineDependencies)
  .settings(libraryDependencies += "org.spire-math" %% "cats-laws" % "0.1.2")

lazy val tests = project.dependsOn(core, laws)
  .settings(moduleName := "alleycats-tests")
  .settings(alleycatsSettings)
  .settings(noPublishSettings)
  .settings(libraryDependencies ++= disciplineDependencies)
  .settings(libraryDependencies +="org.scalatest" %% "scalatest" % "2.2.5" % "test")

lazy val publishSettings = Seq(
  homepage := Some(url("https://github.com/non/alleycats")),
  licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
  autoAPIMappings := true,
  releaseCrossBuild := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  publishMavenStyle := true,
  publishArtifact in packageDoc := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  publishTo <<= version { (v: String) =>
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra := (
    <developers>
      <developer>
        <id>non</id>
        <name>Erik Osheim</name>
        <url>http://github.com/non/</url>
      </developer>
    </developers>
  ),
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    setNextVersion,
    commitNextVersion,
    ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
    pushChanges))

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

addCommandAlias("validate", ";compile;test;scalastyle;test:scalastyle")

def gitSnapshots = Command.command("gitSnapshots") { state =>
  val extracted = Project extract state
  val newVersion = Seq(version in ThisBuild := git.gitDescribedVersion.value.get + "-SNAPSHOT")
  extracted.append(newVersion, state)
}

// For Travis CI - see http://www.cakesolutions.net/teamblogs/publishing-artefacts-to-oss-sonatype-nexus-using-sbt-and-travis-ci
credentials ++= (for {
  username <- Option(System.getenv().get("SONATYPE_USERNAME"))
  password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
} yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
