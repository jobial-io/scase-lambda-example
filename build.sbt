/*
 * Copyright (c) 2020 Jobial OÜ. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
name := "scase-lambda-example"

ThisBuild / organization := "io.jobial"
ThisBuild / scalaVersion := "2.12.13"
ThisBuild / crossScalaVersions := Seq("2.11.12", "2.12.13", "2.13.6")
ThisBuild / version := "0.3.0"
ThisBuild / scalacOptions += "-target:jvm-1.8"
ThisBuild / publishArtifact in(Test, packageBin) := true
ThisBuild / publishArtifact in(Test, packageSrc) := true
ThisBuild / publishArtifact in(Test, packageDoc) := true

import com.lightbend.sbt.SbtProguard.autoImport.proguardOptions
import sbt.Keys.{description, libraryDependencies, publishConfiguration}
import sbt.{addCompilerPlugin, addSbtPlugin}
import sbtassembly.AssemblyPlugin.autoImport.{ShadeRule, assemblyPackageScala}
import xerial.sbt.Sonatype._

lazy val commonSettings = Seq(
  publishConfiguration := publishConfiguration.value.withOverwrite(true),
  publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true),
  publishTo := publishTo.value.orElse(sonatypePublishToBundle.value),
  sonatypeProjectHosting := Some(GitHubHosting("jobial-io", "scase", "orbang@jobial.io")),
  organizationName := "Jobial OÜ",
  licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  description := "Run functional Scala code as a portable serverless function or microservice",
  addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
)

lazy val ScaseVersion = "0.3.0"

lazy val root: Project = project
  .in(file("."))
  .settings(commonSettings)
  .enablePlugins(SbtProguard)
  //.enablePlugins(SbtScaseCloudformationPlugin)
  .settings(
    //cloudformationStackClass := "io.jobial.scase.example.greeting.lambda.GreetingServiceLambdaRequestHandler",
    libraryDependencies ++= Seq(
      //      "io.jobial" %% "scase-cloudformation" % "0.3.0",
      "io.jobial" %% "scase-cloudformation" % ScaseVersion,
      "io.jobial" %% "scase-aws" % ScaseVersion exclude("commons-logging", "commons-logging-api"),
      "io.jobial" %% "scase-circe" % ScaseVersion exclude("commons-logging", "commons-logging-api"),
    ),
    proguardOptions in Proguard ++= Seq("-dontobfuscate", "-dontoptimize", 
      "-keepclassmembers", "class", "io.jobial.scase.example.greeting.lambda.GreetingServiceLambdaRequestHandler", "{", "*;", "}",
      "-keepclassmembers", "class", "io.jobial.scase.aws.lambda.LambdaRequestHandler", "{", "*;", "}",
      "-keep", "class", "com.amazonaws.services.**", "{", "*;", "}",
      "-keep", "class", "scala.Symbol", "{", "*;", "}",
      "-dontnote", "-ignorewarnings"),
    proguardInputFilter in Proguard := { file =>
      file.name match {
        case _ => Some("!META-INF/**,!about.html,!org/apache/commons/logging/**")
      }
    },
    proguardOptions in Proguard += ProguardOptions.keepMain("io.jobial.scase.example.greeting.lambda.GreetingServiceLambdaRequestHandler"),
    javaOptions in (Proguard, proguard) := Seq("-Xmx2G")
  )

