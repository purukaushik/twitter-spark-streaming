#!//bin/zsh
sbt assembly
spark-submit --class io.purush.spark.twitter.streaming.TwitterStreaming target/scala-2.10/twitter-spark-assembly-1.0.jar build.sbt
