val commonsettings = Seq(
  version := "1.1.2",
  organization := "io.github.pityka",
  scalaVersion := "2.13.5",
  publishTo := sonatypePublishTo.value
)

commonsettings

lazy val root = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("."))
  .settings(commonsettings: _*)
  .settings(
    name := "intervaltree",
    libraryDependencies ++=
      Seq(
        "org.typelevel" %%% "cats-kernel" % "2.4.2",
        "org.scalatest" %%% "scalatest" % "3.2.5" % "test",
        "org.scalatestplus" %%% "scalacheck-1-15" % "3.2.5.0" % "test"
      )
  )

pomExtra in Global := {
  <url>https://pityka.github.io/utils-interval</url>
  <licenses>
    <license>
      <name>MIT</name>
      <url>https://opensource.org/licenses/MIT</url>
    </license>
  </licenses>
  <scm>
    <connection>scm:git:github.com/pityka/utils-interval</connection>
    <developerConnection>scm:git:git@github.com:pityka/utils-interval</developerConnection>
    <url>github.com/pityka/utils-interval</url>
  </scm>
  <developers>
    <developer>
      <id>pityka</id>
      <name>Istvan Bartha</name>
      <url>https://pityka.github.io/utils-interval/</url>
    </developer>
  </developers>
}
