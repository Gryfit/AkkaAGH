name := "RozprochyAkka"

version := "0.1"

scalaVersion := "2.12.8"

val akkaVersion = "2.5.22"
val catsVerison = "2.0.0-M1"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-remote" % akkaVersion

libraryDependencies += "org.typelevel" %% "cats-core" % catsVerison

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.26" 
