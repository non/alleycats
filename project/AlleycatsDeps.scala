package org
package typelevel
package alleycats

import sbt._
import sbtcatalysts.CatalystsPlugin.autoImport._

object Dependencies {

  // Versions for libraries and packages
  // Package -> version
  val versions = Map[String, String]()

  // library definitions and links to their versions
  // Note that one version may apply to more than one library.
  // Library name -> version key, org, library
  val libraries = Map[String, (String, String, String)]()

  // compiler plugins definitions and links to their versions
  // Note that one version may apply to more than one plugin.
  // Library name -> version key, org, librar, crossVersion
  val scalacPlugins = Map[String, (String, String, String, CrossVersion)]()

  // Some helper methods to combine libraries
}
