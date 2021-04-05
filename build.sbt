inThisBuild(
  List(
    organization := "io.github.pityka",
    homepage := Some(url("https://pityka.github.io/utils-interval/")),
    licenses := List(("MIT", url("https://opensource.org/licenses/MIT"))),
    developers := List(
      Developer(
        "pityka",
        "Istvan Bartha",
        "bartha.pityu@gmail.com",
        url("https://github.com/pityka/utils-interval")
      )
    )
  )
)

val commonsettings = Seq(
  organization := "io.github.pityka",
  scalaVersion := "2.13.5",
  crossScalaVersions := Seq("2.12.13", "2.13.5")
)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .settings(commonsettings: _*)
  .settings(
    name := "intervaltree",
    libraryDependencies ++=
      Seq(
        "org.typelevel" %%% "cats-kernel" % "2.5.0",
        "org.scalatest" %%% "scalatest" % "3.2.7" % "test",
        "org.scalatestplus" %%% "scalacheck-1-15" % "3.2.7.0" % "test"
      )
  )

lazy val root = project
  .in(file("."))
  .settings(commonsettings)
  .settings(
    skip in publish := true,
    crossScalaVersions := Nil
  )
  .aggregate(core.js, core.jvm)
