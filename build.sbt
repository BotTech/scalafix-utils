import _root_.scalafix.sbt.{BuildInfo => ScalafixBuildInfo}

lazy val rulesCrossVersions = Seq(
  ScalafixBuildInfo.scala213,
  ScalafixBuildInfo.scala212,
  ScalafixBuildInfo.scala211
)

inThisBuild(
  List(
    organization := "nz.co.bottech",
    homepage := Some(url("https://github.com/BotTech/scalafix-utils")),
    licenses := List(
      "MIT" -> url("https://github.com/BotTech/scalafix-utils/blob/main/LICENSE")
    ),
    developers := List(
      Developer(
        "steinybot",
        "Jason Pickens",
        "jason@bottech.co.nz",
        url("https://github.com/steinybot")
      )
    ),
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision
  )
)

lazy val `scalafix-utils` = (projectMatrix in file("."))
  .settings(
    libraryDependencies ++= Seq(
      "ch.epfl.scala" %% "scalafix-core" % ScalafixBuildInfo.scalafixVersion,
      "org.scalatest" %% "scalatest" % "3.2.15" % Test
    )
  )
  .jvmPlatform(rulesCrossVersions)
