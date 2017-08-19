# Twisearch Slackbot
twitterのキーワードを検索して、新しいつぶやきが見つかるたびに、Slackに投稿します。
Streaming APIを利用せず、REST Search APIを利用しています。

# ビルド方法
Java(Java8以上)とsbtをインストールの上、

```sh
$ sbt
> assembly
```
これでtarget/scala-2.11ディレクトリの中に、twisearch_slackbot-assembly-X.X.jarがビルドされます。

# ビルド済みバイナリ

なおビルド済みの twisearch_slackbot-assembly-X.X.jar もこのリポジトリのルートフォルダに置いてあります。

# 使い方
twisearch_slackbot-assembly-X.X.jarと同じディレクトリに、
application_template.confを正しく編集して、
application.confというファイル名で保存ください。

```properties
app {
  slackWebHookUrls = ["https://hooks.slack.com/services/hoge/fuga/hege"]
  intervalSec = 60
  keyword = "\"test\"OR\"テスト\"OR\"hoge\"OR\"fuga\""
  ignoreScreenNames = ["sifue_4466"]
  isSendRetweet = false
  messageFormat = "https://twitter.com/%1$s/status/%2$s"
  consumerKey = "consumerKey"
  consumerSecret = "consumerSecret"
  accessToken = "accessToken"
  accessTokenSecret = "accessTokenSecret"
  hubotWebHookUrl = ""
  hubotWebHookRoom = ""
}
```

https://api.slack.com/slack-apps にて Twisearch SlackbotのアプリはSlackでAppを作成して、WebHook Incomeを許可し、そこでWebHook URLを特定のチャンネルに対して作成してそれを利用して下さい。
https://dev.twitter.com/ より Twitter のアプリケーションを作成して、consumerKey、consumerSecret を取得、accessToken、accessTokenSecret は生成して取得してください。
これらを設定します。


設定の後、

```sh
$java -jar twisearch_slackbot-assembly-X.X.jar
```

で実行することができます。

