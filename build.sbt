name := "metrics-spray-routes"

organization := "org.zapodot"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.4"

crossScalaVersions := Seq("2.10.4", "2.11.1")

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.4"

libraryDependencies += "io.spray" %% "spray-routing" % "1.3.1"

libraryDependencies += "com.codahale.metrics" % "metrics-json" % "3.0.2"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.3.4" % "test"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.0" % "test"

