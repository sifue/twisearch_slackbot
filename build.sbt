import AssemblyKeys._

assemblySettings

name := "twisearch_slackbot"

version := "1.3"

scalaVersion := "2.11.8"

mainClass in assembly := Some("TwisearchSlackbot")

libraryDependencies ++= List(
  "com.typesafe" % "config" % "1.3.1",
  "com.typesafe.akka" %% "akka-actor" % "2.4.17",
  "org.twitter4j" % "twitter4j-core" % "4.0.6",
  "com.squareup.okhttp3" % "okhttp" % "3.8.1"
)
