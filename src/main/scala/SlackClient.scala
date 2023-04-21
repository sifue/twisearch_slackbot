import okhttp3._

class SlackClient(client: OkHttpClient, webHookUrls: Seq[String]) {

  private val JSON = MediaType.parse("application/json")

  def postMessage(message: String): Unit = {
    webHookUrls.foreach((webHookUrl) => {
      val body = RequestBody.create(JSON, "{ \"blocks\": [ { \"type\": \"section\", \"text\": { \"type\": \"mrkdwn\", \"text\": \"" + message + "\" } } ] }")
      val request = new Request.Builder().url(webHookUrl).post(body).build
      val response = client.newCall(request).execute
      if (!response.body.string.contains("ok")) {
        System.err.println("Fail to post to slack. body:" + response.body.string)
      }
    })
  }

}