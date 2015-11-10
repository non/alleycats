import org.typelevel.{Dependencies => typelevel}
import org.typelevel.alleycats.{Dependencies => alleycats}
/**
 * These aliases serialise the build for the benefit of Travis-CI, also useful for pre-PR testing.
 * If new projects are added to the build, these must be updated.
 */
addCommandAlias("buildJVM", ";coreJVM/compile;lawsJVM/compile;testsJVM/test")
addCommandAlias("validateJVM", ";scalastyle;buildJVM")
addCommandAlias("validateJS", ";coreJS/compile;lawsJS/compile;testsJS/test")
addCommandAlias("validate", ";validateJS;validateJVM")
addCommandAlias("gitSnapshots", ";set version in ThisBuild := git.gitDescribedVersion.value.get + \"-SNAPSHOT\"")

/**
 * Project settings
 */
val gh = GitHubSettings(org = "non", proj = "alleycats", publishOrg = "org.typelevel", license = mit)
val devs = Seq(Dev("Erik Osheim", "non"))

val updates = Map(
  "macro-compat" -> "1.1.0",
  "export-hook" -> "1.0.3-SNAPSHOT",
  "simulacrum" -> "0.5.0-SNAPSHOT"
)
val updatesSettings = Seq( resolvers += Resolver.sonatypeRepo("snapshots"))

val vers = typelevel.versions ++ alleycats.versions ++ updates
val libs = typelevel.libraries ++ alleycats.libraries
val addins = typelevel.scalacPlugins ++ alleycats.scalacPlugins
val vAll = Versions(vers, libs, addins)

/**
 * alleycats - This is the root project that aggregates the alleycatsJVM and alleycatsJS sub projects
 */
lazy val rootSettings = buildSettings ++ commonSettings ++ publishSettings ++ scoverageSettings ++ updatesSettings

lazy val module = mkModuleFactory(gh.proj, mkConfig(rootSettings, commonJvmSettings, commonJsSettings))
lazy val prj = mkPrjFactory(rootSettings)

lazy val rootPrj = project
  .configure(mkRootConfig(rootSettings,rootJVM))
  .aggregate(rootJVM, rootJS)
  .dependsOn(rootJVM, rootJS, testsJVM % "test-internal -> test")

lazy val rootJVM = project
  .configure(mkRootJvmConfig(gh.proj, rootSettings, commonJvmSettings))
  .aggregate(coreJVM, lawsJVM, testsJVM)
  .dependsOn(coreJVM, lawsJVM, testsJVM % "compile;test-internal -> test")

lazy val rootJS = project
  .configure(mkRootJsConfig(gh.proj, rootSettings, commonJsSettings))
  .aggregate(coreJS, lawsJS, testsJS)

/**
 * Core project
 */
lazy val core    = prj(coreM)
lazy val coreJVM = coreM.jvm
lazy val coreJS  = coreM.js
lazy val coreM   = module("core", CrossType.Pure)
  .settings(typelevel.macroCompatSettings(vAll):_*)
  .settings(addLibs(vAll, "cats-core", "export-hook"):_*)

/**
 * Laws project
 */
lazy val laws    = prj(lawsM)
lazy val lawsJVM = lawsM.jvm
lazy val lawsJS  = lawsM.js
lazy val lawsM   = module("laws", CrossType.Pure)
  .dependsOn(coreM)
  .settings(disciplineDependencies:_*)
  .settings(addLibs(vAll, "cats-laws"):_*)

/**
 * Tests project
 */
lazy val tests    = prj(testsM)
lazy val testsJVM = testsM.jvm
lazy val testsJS  = testsM.js
lazy val testsM   = module("tests", CrossType.Pure)
  .dependsOn(coreM, lawsM)
  .settings(disciplineDependencies:_*)
  .settings(noPublishSettings:_*)
  .settings(addTestLibs(vAll, "scalatest" ):_*)

/**
 * Settings
 */
lazy val buildSettings = sharedBuildSettings(gh, vAll)

lazy val commonSettings = sharedCommonSettings ++
  addTestLibs(vAll, "simulacrum", "machinist") ++
  addCompilerPlugins(vAll, "kind-projector") ++ Seq(
  scalacOptions ++= scalacAllOptions,
  parallelExecution in Test := false
) /* ++ warnUnusedImport */ ++ unidocCommonSettings // spurious warnings from macro annotations expected

lazy val commonJsSettings = Seq(
  scalaJSStage in Global := FastOptStage
)

lazy val commonJvmSettings = Seq()

lazy val disciplineDependencies = Seq(addLibs(vAll, "discipline", "scalacheck" ):_*)

lazy val publishSettings = sharedPublishSettings(gh, devs) ++ credentialSettings ++ sharedReleaseProcess

lazy val scoverageSettings = sharedScoverageSettings(60)
