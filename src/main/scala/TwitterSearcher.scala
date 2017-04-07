import akka.actor.{ActorLogging, Actor}
import twitter4j.Twitter
import twitter4j.Query
import scala.collection.JavaConversions._
import twitter4j.Status

/**
 * Message object of searching.
 * @param keyword
 */
case class SearchTwitter(keyword: String)

/**
 * Actor of Searcher.
 * @param client
 * @param intervalSec
 * @param isSendRetweet
 * @param messageFormat
 * @param twitter
 */
class TwitterSearcher(
                       client: SlackClient,
                       intervalSec: Int,
                       isSendRetweet: Boolean,
                       messageFormat: String,
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

      val tweets = if (isSendRetweet) {
        twitter.search(query).getTweets().reverse
      } else {
        twitter.search(query).getTweets().reverse.filter(t => !t.isRetweet)
      }

      if (maxId != 0L) tweets.filter(t => t.getId() > maxId).foreach(sendTweetToSlack)

      tweets.foreach(t => {
        if (t.getId() > maxId) maxId = t.getId()
      })
    case m =>
      log.error(s"Not supported command. ${m.toString}")
  }

  private def sendTweetToSlack(t: Status): Unit = {
    val message = messageFormat.format(t.getUser().getScreenName(), t.getId())
    client.postMessage(message)
    log.info(s"Tweet: @${t.getUser().getScreenName()} ${t.getText()}")
  }
}