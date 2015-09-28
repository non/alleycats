import Base._
import com.typesafe.sbt.pgp.PgpKeys.publishSigned
import com.typesafe.sbt.SbtSite.SiteKeys._
import com.typesafe.sbt.SbtGhPages.GhPagesKeys._
import sbtunidoc.Plugin.UnidocKeys._
import ScoverageSbtPlugin._
import ReleaseTransformations._


/**
 * These aliases serialise the build for the benefit of Travis-CI, also useful for pre-PR testing.
 * If new projects are added to the build, these must be updated.
 */
addCommandAlias("buildJVM", ";coreJVM/compile;lawsJVM/compile;testsJVM/test")
addCommandAlias("validateJVM", ";scalastyle;buildJVM")
addCommandAlias("validateJS", ";coreJS/compile;lawsJS/compile;testsJS/test")
addCommandAlias("validate", ";validateJS;validateJVM")


/**
 * Build settings
 */
val home = "https://github.com/non/alleycats"
val repo = "git@github.com:non/alleycats.git"
val api = "https://non.github.io/alleycats/api/"
val license = ("MIT", url("http://opensource.org/licenses/MIT"))

val catsVersion = "0.2.0"
val disciplineVersion = "0.4"
val kindProjectorVersion = "0.6.3"
val machinistVersion = "0.4.1"
val macroCompatVersion = "1.0.2"
val paradiseVersion = "2.1.0-M5"
val scalacheckVersion = "1.12.4"
val scalatestVersion = "3.0.0-M7"
val scalacVersion = "2.11.7"
val simulacrumVersion= "0.4.0"

lazy val buildSettings = Seq(
  organization := "org.spire-math",
  scalaVersion := "2.11.7",
  crossScalaVersions := Seq("2.10.6", scalacVersion)
)

/**
 * Common settings
 */
lazy val commonSettings = sharedCommonSettings ++ Seq(
  scalacOptions ++= commonScalacOptions,
  parallelExecution in Test := false,
  // resolvers += Resolver.sonatypeRepo("snapshots")
  libraryDependencies ++= Seq(
    "com.github.mpilquist" %%% "simulacrum" % simulacrumVersion,
    "org.typelevel" %%% "machinist" % machinistVersion,
    compilerPlugin("org.spire-math" %% "kind-projector" % kindProjectorVersion)
  )
) ++ warnUnusedImport ++ unidocCommonSettings

lazy val commonJsSettings = Seq(
  scalaJSStage in Global := FastOptStage
)

lazy val commonJvmSettings = Seq(
 // testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF)
)

/**
 * Alleycats - This is the root project that aggregates the alleycatsJVM and alleycatsJS sub projects
 */
lazy val alleycatsSettings = buildSettings ++ commonSettings ++ publishSettings ++ scoverageSettings

lazy val alleycats = project.in(file("."))
  .settings(moduleName := "root")
  .settings(alleycatsSettings)
  .settings(noPublishSettings)
  .settings(console <<= console in (alleycatsJVM, Compile))
  .aggregate(alleycatsJVM, alleycatsJS)
  .dependsOn(alleycatsJVM, alleycatsJS, testsJVM % "test-internal -> test")

lazy val alleycatsJVM = project.in(file(".alleycatsJVM"))
  .settings(moduleName := "alleycats")
  .settings(alleycatsSettings)
  .settings(commonJvmSettings)
  .aggregate(coreJVM, lawsJVM, testsJVM)
  .dependsOn(coreJVM, lawsJVM, testsJVM % "test-internal -> test")

lazy val alleycatsJS = project.in(file(".alleycatsJS"))
  .settings(moduleName := "alleycats")
  .settings(alleycatsSettings)
  .settings(commonJsSettings)
  .aggregate(coreJS, lawsJS, testsJS)
  .dependsOn(coreJS, lawsJS, testsJS % "test-internal -> test")
  .enablePlugins(ScalaJSPlugin)

/**
 * Core project
 */
lazy val core = crossProject.crossType(CrossType.Pure)
  .settings(moduleName := "alleycats-core")
  .settings(alleycatsSettings:_*)
  .settings(scalaMacrosParadise(paradiseVersion):_*)
  .settings(libraryDependencies += "org.spire-math" %% "cats-core" % catsVersion)
  .jsSettings(commonJsSettings:_*)
  .jvmSettings(commonJvmSettings:_*)

lazy val coreJVM = core.jvm
lazy val coreJS = core.js

/**
 * Laws project
 */
lazy val laws = crossProject.crossType(CrossType.Pure)
  .dependsOn(core)
  .settings(moduleName := "alleycats-laws")
  .settings(alleycatsSettings:_*)
  .settings(disciplineDependencies:_*)
  .settings(libraryDependencies += "org.spire-math" %%% "cats-laws" % catsVersion)
  .jsSettings(commonJsSettings:_*)
  .jvmSettings(commonJvmSettings:_*)

lazy val lawsJVM = laws.jvm
lazy val lawsJS = laws.js

/**
 * Tests - internal test project
 */
lazy val tests = crossProject.crossType(CrossType.Pure)
  .dependsOn(core, laws)
  .settings(moduleName := "alleycats-tests")
  .settings(alleycatsSettings:_*)
  .settings(disciplineDependencies:_*)
  .settings(noPublishSettings:_*)
  .settings(libraryDependencies += "org.scalatest" %%% "scalatest" % scalatestVersion % "test")
  .jsSettings(commonJsSettings:_*)
  .jvmSettings(commonJvmSettings:_*)

lazy val testsJVM = tests.jvm
lazy val testsJS = tests.js

/**
 * Plugin and other settings
 */
lazy val disciplineDependencies = Seq(
  libraryDependencies += "org.scalacheck" %%% "scalacheck" % scalacheckVersion,
  libraryDependencies += "org.typelevel" %%% "discipline" % disciplineVersion
)

lazy val publishSettings = sharedPublishSettings(home, repo, api, license) ++ Seq(
  autoAPIMappings := true,
   pomExtra := (
    <developers>
      <developer>
        <id>non</id>
        <name>Erik Osheim</name>
        <url>http://github.com/non/</url>
      </developer>
    </developers>
  )
) ++ credentialSettings ++ sharedReleaseProcess

lazy val scoverageSettings = Seq(
  ScoverageKeys.coverageMinimum := 60,
  ScoverageKeys.coverageFailOnMinimum := false,
  ScoverageKeys.coverageHighlighting := scalaBinaryVersion.value != "2.10")
