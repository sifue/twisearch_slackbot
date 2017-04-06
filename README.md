# Twisearch Slackbot
twitterのキーワードを検索して、新しいつぶやきが見つかるたびに、Slackの特定のStreaming APIを利用せず、REST Search APIを利用しています。

# ビルド方法
javaとsbtをインストールの上、

```sh
$ sbt
> assembly
```
これでtargetディレクトリの中に、twisearch_ircbot-assembly-2.0.jarがビルドされます。

# 使い方
twisearch_ircbot-assembly-2.0.jarと同じディレクトリに、
twisearch_ircbot_template.propertiesを正しく編集して、
twisearch_ircbot.propertiesというファイル名で保存ください。

```properties
irc.address = hostname
irc.channel = #channelname
irc.nickname = twisearch_ircbot
irc.charset = UTF-8
limitCount = 3
intervalSec = 60
keyword = #MT2
messageFormat = interval:%1$s keyword:%2$s count:%3$s
noticeFormat = @%1$s %2$s %3$s
consumerKey = consumerKey
consumerSecret = consumerSecret
accessToken = acessToken
accessTokenSecret = accessTokenSecret
```

twisearch_ircbot.propertiesの内容のうち、consumerKey、consumerSecret、accessToken、accessTokenSecretはTwitter Developpers https://dev.twitter.com/ より、ログインの後、アプリケーションを作成して、これらのキーとトークンを発行してご利用ください。

なお日本語などを入れたい場合には、javaのnative2asciiコマンドを利用してascii化する必要があります。
http://symfo.web.fc2.com/js-sample/jq/sample2.html
以上のようなサイトでも変換できます。

設定の後、

```sh
$java -jar twisearch_ircbot-assembly-2.0.jar
```

で実行することができます。
またIRCのボットが入っているチャンネルにて
```
ping nickname
```
とするとWorking now.とnoticeを返します。

