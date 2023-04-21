import akka.actor.{Actor, ActorLogging}
import twitter4j.Twitter
import twitter4j.Query

import scala.collection.JavaConversions._
import twitter4j.Status

import scala.util.matching.Regex

/**
 * Message object of searching.
 * @param keyword
 */
case class SearchTwitter(keyword: String)

/**
  * Actor of Searcher.
  * @param slackClient
  * @param hubotClient
  * @param intervalSec
  * @param ignoreScreenNames
  * @param ignoreRegex
  * @param isSendRetweet
  * @param messageFormat
  * @param rtMessageFormat
  * @param twitter
  */
class TwitterSearcher(
                       slackClient: SlackClient,
                       hubotClient: HubotClient,
                       intervalSec: Int,
                       ignoreScreenNames: Seq[String],
                       ignoreRegex: String,
                       isSendRetweet: Boolean,
                       messageFormat: String,
                       rtMessageFormat: String,
                       twitter: Twitter
  ) extends Actor with ActorLogging {
  var maxId = 0L

  override def receive: Receive = {
    case SearchTwitter(keyword) =>
      log.info(s"Twitter searching. keyword: ${keyword}")
      val query = new Query
      query.setQuery(keyword)
      query.setSinceId(maxId)
      query.setResultType(Query.RECENT)
      query.setCount(100) // max 100 https://dev.twitter.com/rest/reference/get/search/tweets

      var tweets = if (isSendRetweet) {
        twitter.search(query)
          .getTweets()
          .reverse
      } else {
        twitter.search(query)
          .getTweets()
          .reverse
          .filter(t => !t.isRetweet)
      }.filter(t => !ignoreScreenNames.contains(t.getUser.getScreenName))

      if(!ignoreRegex.isEmpty) {
        val regexp = new Regex(ignoreRegex)
        tweets = tweets.filter(t => regexp.findFirstIn(t.getText).isEmpty)
      }

      if (maxId != 0L) {
        tweets
          .filter(t => t.getId() > maxId)
          .foreach(t => {
            sendTweetToSlack(t)
            sendTweetToHubot(t)
          })
      }

      tweets.foreach(t => {
        if (t.getId() > maxId) maxId = t.getId()
      })
    case m =>
      log.error(s"Not supported command. ${m.toString}")
  }

  private def sendTweetToSlack(t: Status): Unit = {
    val message = if(t.isRetweet) {
      rtMessageFormat.format(
        t.getUser().getScreenName(),
        t.getId(),
        t.getUser().getName(),
        t.getText().substring(0, 25) + "…",
        t.getRetweetCount()
      )
    } else {
      messageFormat.format(t.getUser().getScreenName(), t.getId(), t.getText())
    }
    slackClient.postMessage(message)
    log.info(s"Tweet to Slack: @${t.getUser().getScreenName()} ${t.getText()}")
  }

  private def sendTweetToHubot(t: Status): Unit = {
    val message = if(t.isRetweet) {
      rtMessageFormat.format(
        t.getUser().getScreenName(),
        t.getId(),
        t.getUser().getName(),
        t.getText().substring(0, 25) + "…",
        t.getRetweetCount()
      )
    } else {
      messageFormat.format(t.getUser().getScreenName(), t.getId(), t.getText())
    }
    hubotClient.postMessage(message)
    log.info(s"Tweet to Hubot: @${t.getUser().getScreenName()}")
  }
}