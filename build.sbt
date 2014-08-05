name := "metrics-spray-routes"

organization := "org.zapodot"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.4"

scalacOptions += "-target:jvm-1.7"

crossScalaVersions := Seq("2.10.4", "2.11.1")

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.4"

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.1.3"

libraryDependencies += "io.spray" %% "spray-can" % "1.3.1"

libraryDependencies += "io.spray" %% "spray-http" % "1.3.1"

libraryDependencies += "com.codahale.metrics" % "metrics-json" % "3.0.2"

libraryDependencies += "com.codahale.metrics" % "metrics-servlets" % "3.0.2" withSources()

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.3.4" % "test"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.0" % "test"

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.7" % "test"


