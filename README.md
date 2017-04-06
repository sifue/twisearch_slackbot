# Twisearch Slackbot
twitterのキーワードを検索して、新しいつぶやきが見つかるたびに、Slackに投稿します。
Streaming APIを利用せず、REST Search APIを利用しています。

# ビルド方法
Java(java8以上)とsbtをインストールの上、

```sh
$ sbt
> assembly
```
これでtarget/scala-2.11ディレクトリの中に、twisearch_slackbot-assembly-X.X.jarがビルドされます。

# 使い方
twisearch_slackbot-assembly-X.X.jarと同じディレクトリに、
application_template.confを正しく編集して、
application.confというファイル名で保存ください。

```properties
app {
  slackWebHookUrl = "https://hooks.slack.com/services/hoge/fuga/hege"
  intervalSec = 60
  keyword = "\"test\"OR\"テスト\"OR\"hoge\"OR\"fuga\""
  messageFormat = "https://twitter.com/%1$s/status/%2$s"
  consumerKey = "consumerKey"
  consumerSecret = "consumerSecret"
  accessToken = "accessToken"
  accessTokenSecret = "accessTokenSecret"
}
```

Twisearch SlackbotのアプリはSlackでAppを作成して、WebHook Incomeを許可し、そこでWebHook URLを特定のチャンネルに対して作成してそれを利用して下さい。
application.confの内容のうち、consumerKey、consumerSecret、accessToken、accessTokenSecretはTwitter Developpers https://dev.twitter.com/ より、ログインの後、アプリケーションを作成して、これらのキーとトークンを発行してご利用ください。


設定の後、

```sh
$java -jar twisearch_slackbot-assembly-X.X.jar
```

で実行することができます。Procfile などを作成して、 Heroku や Dokku にあげてください。
