import sbt.Keys._

lazy val root = (project in file(".")).
  settings(
    name := "twitter-spark",
    version := "1.0",
    scalaVersion := "2.10.5",
    mainClass in Compile := Some("io.purush.spark.twitter.streaming.TwitterStreaming")
)

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.2" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.6.2"
libraryDependencies += "org.apache.spark" %% "spark-streaming-twitter" % "1.6.2"
libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.5.2"
libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.5.2" classifier "models"
libraryDependencies += "org.twitter4j" % "twitter4j-stream" % "3.0.3"

resolvers += Resolver.mavenLocal
// META-INF discarding
assemblyMergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
{
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
}