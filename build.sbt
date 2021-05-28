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
  scalaVersion := "2.13.6",
  crossScalaVersions := Seq("2.12.14", "2.13.6")
)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .settings(commonsettings: _*)
  .settings(
    name := "intervaltree",
    libraryDependencies ++=
      Seq(
        "org.typelevel" %%% "cats-kernel" % "2.6.0",
        "org.scalatest" %%% "scalatest" % "3.2.8" % "test",
        "org.scalatestplus" %%% "scalacheck-1-15" % "3.2.9.0" % "test"
      ),
    mimaPreviousArtifacts := Set(
      organization.value %% moduleName.value % "1.1.5"
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
