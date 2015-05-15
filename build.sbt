name := """play-scala-mitch"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  "mysql" % "mysql-connector-java" % "5.1.35",
  anorm,
  cache,
  ws
)

//libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.27"
