import java.io.File

import akka.actor.{ActorSystem, Props}

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import com.typesafe.config._

/**
 * Main application.
 */
object TwisearchSlackbot extends App {
  val conf = new TwisearchSlackbotConfig(args)
  val slackClient = new SlackClient(conf.webHookUrl)

  val twitter = TwitterFactory.getSingleton
  twitter.setOAuthConsumer(
    conf.consumerKey,
    conf.consumerSecret)
  twitter.setOAuthAccessToken(new AccessToken(
    conf.accessToken,
    conf.accessTokenSecret
  ))

  val actorSystem = ActorSystem("twisearch")
  val searcher = actorSystem.actorOf(Props(classOf[TwitterSearcher],
    slackClient,
    conf.intervalSec,
    conf.isSendRetweet,
    conf.messageFormat,
    twitter))

  actorSystem.scheduler.schedule(0 seconds, conf.intervalSec seconds, searcher, SearchTwitter(conf.keyword))
  println("Slackbot started successfully. keyword: " + conf.keyword)
}

/**
 * Configuration of this application.
 * @param args
 */
class TwisearchSlackbotConfig(args: Array[String]) {
  val configFilePath = if(args.length > 0) args(0) else "application.conf"
  private[this] val conf: Config = ConfigFactory.parseFile(new File(configFilePath)).getConfig("app")

  val webHookUrl = conf.getString("slackWebHookUrl")

  val intervalSec = conf.getInt("intervalSec")
  val keyword = conf.getString("keyword")
  val isSendRetweet = conf.getBoolean("isSendRetweet")
  val messageFormat = conf.getString("messageFormat")

  val consumerKey = conf.getString("consumerKey")
  val consumerSecret = conf.getString("consumerSecret")
  val accessToken = conf.getString("accessToken")
  val accessTokenSecret = conf.getString("accessTokenSecret")
}
