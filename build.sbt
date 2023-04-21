name := "twisearch_slackbot"

version := "1.8"

scalaVersion := "2.12.7"

test in assembly := {}

mainClass in assembly := Some("TwisearchSlackbot")

libraryDependencies ++= List(
  "com.typesafe" % "config" % "1.3.1",
  "com.typesafe.akka" %% "akka-actor" % "2.4.17",
  "org.twitter4j" % "twitter4j-core" % "4.0.6",
  "com.squareup.okhttp3" % "okhttp" % "3.8.1"
)
