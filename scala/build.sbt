val scala3Version = "3.3.0"

val zioVersion = "2.0.15"

lazy val readFileSettings = Seq(
  libraryDependencies ++= Seq(
    "com.lihaoyi" %% "os-lib" % "0.9.1"
  )
)

lazy val zioSettings = Seq(
  libraryDependencies ++= Seq(
    "dev.zio" %% "zio" % zioVersion,
    "dev.zio" %% "zio-test" % zioVersion % Test,
    "dev.zio" %% "zio-test-sbt" % zioVersion % Test
  )
)

lazy val userUtil = project.settings(zioSettings).settings(readFileSettings)

lazy val day01 = project.settings(zioSettings) dependsOn (userUtil)

lazy val day02 = project.settings(zioSettings) dependsOn (userUtil)

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29" % Test
    )
  )
