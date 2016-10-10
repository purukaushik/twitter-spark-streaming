package io.purush.spark.twitter.streaming

/**
  * Created by pswaminathan on 10/8/16.
  */

import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.{SparkConf, SparkContext}

object TwitterStreaming {

  def main(args: Array[String]) {
    val config = new SparkConf().setAppName("twitter-streaming-purush")
    val sc = new SparkContext(config)
    sc.setLogLevel("WARN")

    val ssc = new StreamingContext(sc, Seconds(5))

    val stream = TwitterUtils.createStream(ssc, None, Array("debates"))

//    stream.foreachRDD(rdd => rdd.take(10).foreach(println))

//    val hashTags = stream.flatMap(status => status.getHashtagEntities)
//
//    val hashTagPairs = hashTags.map(hashTag => ("#" + hashTag.getText, 1))
//
//    val topCounts10 = hashTagPairs.reduceByKeyAndWindow((l,r) => {l+r}, Seconds(10))
//
//    val sortedTopCounts10 = topCounts10.transform(rdd=> rdd.sortBy(hashTagPair => hashTagPair._2, false))
//
//    sortedTopCounts10.foreachRDD(rdd=>{
//      val topList = rdd.take(10)
//      println("\n Popular topics in last 10 seconds (%s, total): ".format(rdd.count()))
//      topList.foreach{case (tag,count) => println("%s (%d tweets)".format(tag,count))}
//    })
    val statusTexts = stream.filter(status => {
      status.getText.split(" ").map(_.toLowerCase).contains("debate")
    })

    val textSentimentPair = statusTexts.map(status => (SentimentAnalyzer.mainSentiment(status.getText),1))

    val topSentimentCounts = textSentimentPair.reduceByKeyAndWindow((l,r) => {l+r}, Minutes(2))

    val sortedTopCounts = topSentimentCounts.transform(rdd=> rdd.sortBy(pair=>pair._2,false))

    sortedTopCounts.foreachRDD(rdd => {
//      rdd.saveAsTextFile("./tweetSentiments.txt")
      val topList = rdd.take(10)
      println("\n Sentiment counts for 'debate' Tweets in the last 10 seconds")
      topList.foreach{case (tag,count)=> println("%s (%d tweets)".format(tag.toString, count))}

    })

//    val tweets = stream.filter { t =>
//     val tags = t.getText.split(" ").filter(_.startsWith("#")).map(_.toLowerCase())
//      tags.contains("trump")
//    }
//    val data = tweets.map{ tweet =>
//      val sentiment = SentimentAnalyzer.mainSentiment(tweet.getText)
//      val tags = tweet.getHashtagEntities.map(_.getText.toLowerCase)
//      println(sentiment.toString)
//
//      (tweet.getText, sentiment.toString, tags)
//
//    }
//    println("Sentiment Values")
//    data.foreachRDD(rdd =>{
//      val randList = rdd.take(10)
//      randList.foreach{case (text, sentiment, tags)=> println("%s -> %s".format(text,sentiment))}
//    })

    println("Start Streaming: I am very happy.")
    ssc.start()
    ssc.awaitTermination()
    println("Pinish")
  }



}
