# はじめに

Ratpackは高速、効率的、拡張可能かつテスト可能なHTTPアプリケーションを作るためのJavaフレームワークです。
高パフォーマンスなイベント駆動型ネットワークエンジンであるNetty上に作られています。

Ratpackは単なる実行環境です。
インストール可能なパッケージや、同梱されるビルドツール(Rails、Play、Grailsなど)はありません。
Ratpackアプリケーションを作るために、お好みのJVMビルドツールを使用できます。
Ratpackプロジェクトは特に、プラグインを通して[Gradle](http://www.gradle.org)へのサポートを提供していますが、どんなビルドツールでも使用できます。

RatpackはいくつかのJARライブラリーの集まりとして公開されています。
The `ratpack-core` library is the only strictly required library.
厳密には、`ratpack-core`だけが必要なライブラリーです。
そのほかのライブラリー`ratpack-groovy`、`ratpack-guice`、`ratpack-jackson`、`ratpack-test`などは任意です。

## 目標

ラットパックの目標は:
  
1. 高速で、スケーラブルで、効率的であること
2. 複雑になるという犠牲を払うことなく、アプリケーションを発展させられること
3. 非ブロッキングプログラミングの利点を活用し、欠点を削減すること
4. 他のツールやライブラリーと統合するとき、柔軟であり非オピオネイテッドであること
5. アプリケーションが、簡単にかつ完全にテストできるようにすること
 
Ratpackの目標は、以下のものでは **ありません** :

1. 完全に統合された、「フルスタック」なソリューションになること
2. 必要とされる可能性のある、ありとあらゆる機能を提供すること
3. 「ビジネスロジック」へのアーキテクチャーやフレームワークを提供すること

## このドキュメンテーションについて

Ratpackのドキュメンテーションはこのマニュアルと[Javadoc API リファレンス](https://ratpack.io/manual/current/api/index.html)にあります。
マニュアルは、高レベルでのトピックやコンセプトを紹介し、詳細なAPIに関する情報はJavadocへリンクしています。
情報の大部分はJavadocの中にあります。
Ratpackの中心となるコンセプトを理解した後は、マニュアルよりもJavadocのほうが役に立つでしょう。
(訳注: 日本語のJavadocは未整備です)

### Code samples

All of the code samples in the documentation are tested, and most are complete programs that you can copy/paste and run yourself (given the right classpath etc.).

Most of the samples are given as tiny embedded Ratpack applications, under test.
The following is the “Hello World” of Ratpack code samples.

```language-java
import ratpack.test.embed.EmbeddedApp;
import static org.junit.Assert.assertEquals;
 
public class Example {
  public static void main(String... args) throws Exception {
    EmbeddedApp.fromHandler(ctx -> 
      ctx.render("Hello World!")
    ).test(httpClient -> 
      assertEquals("Hello World!", httpClient.getText())
    );
  }
}
```

The `import` statements are collapsed by default for clarity.
Click them to show/hide them.

This example is a complete Ratpack application.
However, the [`EmbeddedApp`](api/ratpack/test/embed/EmbeddedApp.html) is not the entry point that is typically used for proper applications (see the [Launching chapter](launching.html) for details on the typical entry point).
`EmbeddedApp` is testing oriented.
It makes it easy to start/stop very small (or fully fledged) apps during a larger application, and provides a convenient way to make HTTP requests against the app.
It is used in the examples to keep the amount of bootstrapping to a minimum in order to focus on the API being demonstrated.

In this example we are starting a Ratpack server on an ephemeral port with default configuration that responds to all HTTP requests with the plain text string “Hello World”.
The [`test()`](api/ratpack/test/CloseableApplicationUnderTest.html#test-ratpack.func.Action-) method being used here provides a [`TestHttpClient`](api/ratpack/test/http/TestHttpClient.html) to the given function, that is configured to make requests of the server under test.
This example and all others like it are making HTTP requests to a Ratpack server.
[`EmbeddedApp`](api/ratpack/test/embed/EmbeddedApp.html) and [`TestHttpClient`](api/ratpack/test/http/TestHttpClient.html) are provided as part of Ratpack's [testing support](testing.html).

Another key testing utility that is used in many examples is [`ExecHarness`](api/ratpack/test/exec/ExecHarness.html).

```language-java
import com.google.common.io.Files;
import ratpack.test.exec.ExecHarness;
import ratpack.exec.Blocking;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class Example {
  public static void main(String... args) throws Exception {
    File tmpFile = File.createTempFile("ratpack", "test");
    Files.write("Hello World!", tmpFile, StandardCharsets.UTF_8);
    tmpFile.deleteOnExit();

    String content = ExecHarness.yieldSingle(e ->
        Blocking.get(() -> Files.toString(tmpFile, StandardCharsets.UTF_8))
    ).getValueOrThrow();

    assertEquals("Hello World!", content);
  }
}
```

Where `EmbeddedApp` supports creating an entire Ratpack application, `ExecHarness` provides just the infrastructure for Ratpack's execution model.
It is typically used to unit test asynchronous code that uses Ratpack constructs like [`Promise`](api/ratpack/exec/Promise.html) (see the [“Asynchronous & Non Blocking”](async.html) chapter for more info on the execution model).
[`ExecHarness`](api/ratpack/test/exec/ExecHarness.html) is also provided as part of Ratpack's [testing support](testing.html).

#### Java 8 スタイル

RatpackはJava 8上に作られており、Java 8を必要とします。コード例はのラムダ式やメソッド参照のようなJava 8の機能を沢山使用しています。
Javaの経験があったとしてもJava 8の文法になじみがなければ、コード例が「奇妙」に感じるかもしれません。
