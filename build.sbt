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

//ThisBuild / assemblyMergeStrategy := {
//  //case PathList("META-INF", xs @ _*) => MergeStrategy.discard
//  case x => MergeStrategy.first
//}
//assemblyMergeStrategy in assembly := {
//  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
//  case PathList("com", "monsanto", xs @ _*) => MergeStrategy.discard
//  case PathList("picocli", xs @ _*) => MergeStrategy.discard
//  case PathList("spray", xs @ _*) => MergeStrategy.discard
//  case PathList("sbtassembly", xs @ _*) => MergeStrategy.discard
//  //case PathList("io", "jobial", "sclap", xs @ _*) => MergeStrategy.discard
////  case PathList("io", "jobial", "scase", "cloudformation", "CloudformationStackApp$") => MergeStrategy.discard
//  case PathList("io", "jobial", "scase", "example", "greeting", "lambda", "GreetingLambdaClient$") => MergeStrategy.discard
//  case PathList("io", "jobial", "scase", "example", "greeting", "lambda", "GreetingServiceStack$") => MergeStrategy.discard
//  case x =>
////    MergeStrategy.first
//    MergeStrategy.last
//  //    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
//  //    oldStrategy(x)
//}

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

lazy val CatsVersion = "2.0.0"
lazy val ScalaLoggingVersion = "3.9.2"
lazy val ScalatestVersion = "3.2.3"
lazy val SourcecodeVersion = "0.2.3"
lazy val AwsVersion = "1.11.557"
lazy val AmazonSqsJavaExtendedClientLibVersion = "1.2.2"
lazy val AwsLambdaJavaCoreVersion = "1.2.1"
lazy val CommonsIoVersion = "2.8.0"
lazy val CommonsLangVersion = "3.12.0"
lazy val CloudformationTemplateGeneratorVersion = "3.10.4"
lazy val SclapVersion = "1.1.5"
lazy val CirceVersion = "0.12.0-M3"
lazy val SprayJsonVersion = "1.3.6"
lazy val PulsarVersion = "2.9.0"
lazy val ZioVersion = "2.0.0.0-RC13" // TODO: upgrade when Cats version is upgraded
lazy val ScalaJava8CompatVersion = "1.0.2"
lazy val LogbackVersion = "1.2.3"
lazy val ShapelessVersion = "2.3.3"


lazy val root: Project = project
  .in(file("."))
  .settings(commonSettings)
  .enablePlugins(SbtProguard)
  //.enablePlugins(SbtScaseCloudformationPlugin)
  //.enablePlugins(AssemblyPlugin)
  .settings(
    //fullClasspath in assembly := (fullClasspath in Compile).value,
    logLevel in assembly := Level.Debug,
    //mainClass in assembly := Some("io.jobial.scase.example.greeting.lambda.GreetingServiceLambdaRequestHandler"),
    //assemblyPackageScala / assembleArtifact := true,
    //assemblyPackageDependency / assembleArtifact := true,
    //assembly / assemblyJarName := "utils.jar",
    assemblyShadeRules := Seq(
      ShadeRule.keep("io.jobial.scase.example.greeting.lambda.GreetingServiceLambdaRequestHandler").inAll,
      //ShadeRule.keep("io.jobial.scase.example.greeting.lambda.GreetingServiceLambdaRequestHandler").inAll,
      //ShadeRule.keep("io.jobial.scase.**").inAll,
      //ShadeRule.keep("cats.**").inAll,
    ),
    //cloudformationStackClass := "io.jobial.scase.example.greeting.lambda.GreetingServiceLambdaRequestHandler",
    libraryDependencies ++= Seq(
      //      "io.jobial" %% "scase-cloudformation" % "0.3.0",
      "io.jobial" %% "scase-cloudformation" % "0.3.0",
      "io.jobial" %% "scase-aws" % "0.3.0" exclude("commons-logging", "commons-logging-api"),
      "io.jobial" %% "scase-circe" % "0.3.0" exclude("commons-logging", "commons-logging-api"),
    ),
    proguardOptions in Proguard ++= Seq("-dontobfuscate", "-dontoptimize", 
      "-keepclassmembers", "class", "io.jobial.scase.example.greeting.lambda.GreetingServiceLambdaRequestHandler", "{", "*;", "}",
      "-keepclassmembers", "class", "io.jobial.scase.aws.lambda.LambdaRequestHandler", "{", "*;", "}",
      "-keep", "class", "com.amazonaws.services.**", "{", "*;", "}",
      "-keep", "class", "scala.**", "{", "*;", "}",
      "-dontnote", "-ignorewarnings"),
    proguardInputFilter in Proguard := { file =>
      file.name match {
        case _ => Some("!META-INF/**,!about.html,!org/apache/commons/logging/**")
      }
    },
    proguardOptions in Proguard += ProguardOptions.keepMain("io.jobial.scase.example.greeting.lambda.GreetingServiceLambdaRequestHandler"),
    javaOptions in (Proguard, proguard) := Seq("-Xmx2G"),
    addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "1.1.0")
  )

