# Twitter Streaming with Spark

## Prerequisites
1. Spark 1.6.2
2. Scala 2.10.x or 2.11.x
3. IntelliJ Idea/ eclipse + sbt 0.9.x
4. Twitter API credentials loaded in System Properties/ Environment Variables --> `src/main/java/resources/twitter4j.properties`
## Setup project in iDea
1. Add new project -> Scala SBT
2. In `build.sbt` add dependencies:
    ```
        libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.2"
        libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.6.2"
        libraryDependencies += "org.apache.spark" %% "spark-streaming-twitter" % "1.6.2"
        libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.5.1"
        libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.5.1" classifier "models"
        resolvers += Resolver.mavenLocal
    ```
3. Create sparkContext, streamingContext
    ```
        val config = new SparkConf().setAppName("twitter-streaming-purush")
        val sc = new SparkContext(config)
        sc.setLogLevel("WARN")
    
        val ssc = new StreamingContext(sc, Seconds(5))
        
    ```
4. Create twitter stream