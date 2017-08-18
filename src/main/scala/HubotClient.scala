import okhttp3._

class HubotClient(client: OkHttpClient, webHookUrl: String, room: String) {

  private val JSON = MediaType.parse("application/json")

  def postMessage(message: String): Unit = {
    if(!webHookUrl.isEmpty && !room.isEmpty) {
      val body = RequestBody.create(JSON, "{\"room\":\"" + room + "\", \"message\":\"" + message + "\"}")
      val request = new Request.Builder().url(webHookUrl).post(body).build
      val response = client.newCall(request).execute
      if (!response.body.string.contains("OK")) {
        System.err.println("Fail to post to Hubot. body:" + response.body.string)
      }
    }
  }
}