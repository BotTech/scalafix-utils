import _root_.scalafix.sbt.{BuildInfo => ScalafixBuildInfo}

lazy val rulesCrossVersions = Seq(ScalafixBuildInfo.scala213, ScalafixBuildInfo.scala212, ScalafixBuildInfo.scala211)
lazy val scala3Version = "3.0.1"

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

lazy val `scalafix-utils` = (project in file("."))
  .aggregate(
    rules.projectRefs ++
      input.projectRefs ++
      output.projectRefs ++
      tests.projectRefs: _*
  )
  .settings(
    publish / skip := true
  )

lazy val rules = projectMatrix
  .settings(
    moduleName := "scalafix",
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % ScalafixBuildInfo.scalafixVersion
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(rulesCrossVersions)

lazy val input = projectMatrix
  .settings(
    publish / skip := true
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(scalaVersions = rulesCrossVersions :+ scala3Version)

lazy val output = projectMatrix
  .settings(
    publish / skip := true
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(scalaVersions = rulesCrossVersions :+ scala3Version)

lazy val testsAggregate = Project("tests", file("target/testsAggregate"))
  .aggregate(tests.projectRefs: _*)
  .settings(
    publish / skip := true
  )

lazy val tests = projectMatrix
  .settings(
    publish / skip := true,
    scalafixTestkitOutputSourceDirectories :=
      TargetAxis
        .resolve(output, Compile / unmanagedSourceDirectories)
        .value,
    scalafixTestkitInputSourceDirectories :=
      TargetAxis
        .resolve(input, Compile / unmanagedSourceDirectories)
        .value,
    scalafixTestkitInputClasspath :=
      TargetAxis.resolve(input, Compile / fullClasspath).value,
    scalafixTestkitInputScalacOptions :=
      TargetAxis.resolve(input, Compile / scalacOptions).value,
    scalafixTestkitInputScalaVersion :=
      TargetAxis.resolve(input, Compile / scalaVersion).value
  )
  .defaultAxes(
    rulesCrossVersions.map(VirtualAxis.scalaABIVersion) :+ VirtualAxis.jvm: _*
  )
  .jvmPlatform(
    scalaVersions = Seq(ScalafixBuildInfo.scala212),
    axisValues = Seq(TargetAxis(scala3Version)),
    settings = Seq()
  )
  .jvmPlatform(
    scalaVersions = Seq(ScalafixBuildInfo.scala213),
    axisValues = Seq(TargetAxis(ScalafixBuildInfo.scala213)),
    settings = Seq()
  )
  .jvmPlatform(
    scalaVersions = Seq(ScalafixBuildInfo.scala212),
    axisValues = Seq(TargetAxis(ScalafixBuildInfo.scala212)),
    settings = Seq()
  )
  .jvmPlatform(
    scalaVersions = Seq(ScalafixBuildInfo.scala211),
    axisValues = Seq(TargetAxis(ScalafixBuildInfo.scala211)),
    settings = Seq()
  )
  .dependsOn(rules)
  .enablePlugins(ScalafixTestkitPlugin)
