package io.purush.spark.twitter.streaming

import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import io.purush.spark.twitter.streaming.Sentiment._

import scala.collection.convert.wrapAll._
/**
  * Created by pswaminathan on 10/9/16.
  */
object SentimentAnalyzer {
  val props = new Properties()
  // corenlp pipeline annotator configuration
  props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
  val pipeline: StanfordCoreNLP = new StanfordCoreNLP(props)

  def mainSentiment(input:String): Sentiment= Option(input) match{
    case Some(text) if !text.isEmpty => extractSentiment(text)
    case _ => throw new IllegalArgumentException("Input can't be empty")
  }

  def extractSentiment(text:String): Sentiment ={
    val (_, sentiment) = extractSentiments(text)
      .maxBy{case (sentence, _) => sentence.length}
    sentiment
  }

  def extractSentiments(text:String): List[(String, Sentiment)] = {
    val annotation: Annotation = pipeline.process(text)
    val sentences = annotation.get(classOf[CoreAnnotations.SentencesAnnotation])
    sentences.map(sentence => (sentence, sentence.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree])))
      .map{ case (sentence, tree) => (sentence.toString, Sentiment.toSentiment(RNNCoreAnnotations.getPredictedClass(tree)))}
      .toList

  }
}
