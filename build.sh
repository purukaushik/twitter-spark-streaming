#!//bin/zsh
sbt assembly
scp -P 22 ./target/scala-2.10/twitter-spark-assembly-1.0.jar hduser@192.168.99.100:~
