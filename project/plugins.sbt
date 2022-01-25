addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.5")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.1")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "1.1.0")
addSbtPlugin("io.jobial" % "sbt-scase-cloudformation" % "0.3.0")
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full)
libraryDependencies ++= Seq(
  "io.jobial" %% "scase-cloudformation" % "0.2.1"
)
addSbtPlugin("com.github.sbt" % "sbt-proguard" % "0.5.0")
