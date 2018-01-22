# クイックスタート

この章では、Ratpackアプリケーションをどうやって動かし、使ってみればよいのかを説明します。

## Groovyスクリプトの使用

Ratpackアプリケーションは単一のGroovyスクリプトとして実装できます。
これはRatpackとGroovyを体験する便利な方法でしょう。

初めに、[Groovyをインストールします](http://groovy-lang.org/install.html)。

以下の内容の`ratpack.groovy`ファイルを作成します。
 
```language-groovy hello-world-grab
@Grapes([
  @Grab('io.ratpack:ratpack-groovy:@ratpack-version@'),
  @Grab('org.slf4j:slf4j-simple:@slf4j-version@')
])
import static ratpack.groovy.Groovy.ratpack

ratpack {
    handlers {
        get {
            render "Hello World!"
        }
        get(":name") {
            render "Hello $pathTokens.name!"
        }
    }
}
``` 

これで、下記のコマンドを入力しアプリケーションを起動できます。

```language-bash
groovy ratpack.groovy
```

サーバーには`http://localhost:5050/`からアクセスできます。

[`handlers()` メソッド](https://ratpack.io/manual/current/api/ratpack/groovy/Groovy.Ratpack.html#handlers-groovy.lang.Closure-) は [`GroovyChain`](api/ratpack/groovy/handling/GroovyChain.html) オブジェクトに委譲されるクロージャ―を引数とします。
「Groovy ハンドラー・チェーン DSL」を使って、レスポンス処理の戦略を組み立てることができます。

ファイルへの変更は開発中の間、その場で反映されます。
ファイルを変更すると、次のリクエストにはその変更が有効になっています。

## Gradleプラグインを使う

[Gradleビルドシステム](http:///www.gradle.org)を使ってRatpackアプリケーションをビルドすることが推奨されています。
Gradleの使用は必須ではありません。どんなビルドシステムでも使うことができます。

> 以下の説明はGradleがインストールされていることを前提としています。
> インストールの説明には[Gradle User Guide](https://docs.gradle.org/current/userguide/installation.html)を参照してください。

Ratpackプロジェクトは2つのGradleプラグインを提供しています:

1. [io.ratpack.ratpack-java](http://plugins.gradle.org/plugin/io.ratpack.ratpack-java) - Javaで実装されるRatpackアプリケーション用
2. [io.ratpack.ratpack-groovy](http://plugins.gradle.org/plugin/io.ratpack.ratpack-groovy)  - [Groovy](http://groovy-lang.org)で実装されるRatpackアプリケーション用
 
> Gradleビルドのサポートの詳細については、[Gradleの章](gradle.html)を参照してください。

### Gradle Javaプラグインを使う

次の内容の`build.gradle`ファイルを作成します:

```language-groovy gradle
buildscript {
  repositories {
    jcenter()
  }
  dependencies {
    classpath "io.ratpack:ratpack-gradle:@ratpack-version@"
  }
}

apply plugin: "io.ratpack.ratpack-java"
apply plugin: "idea"

repositories {
  jcenter()
}

dependencies {
  runtime "org.slf4j:slf4j-simple:@slf4j-version@"
}

mainClassName = "my.app.Main"
```

次の内容の`src/main/java/my/app/Main.java`ファイルを作成します:

```language-java hello-world
package my.app;

import ratpack.server.RatpackServer;

public class Main {
  public static void main(String... args) throws Exception {
    RatpackServer.start(server -> server 
      .handlers(chain -> chain
        .get(ctx -> ctx.render("Hello World!"))
        .get(":name", ctx -> ctx.render("Hello " + ctx.getPathTokens().get("name") + "!"))     
      )
    );
  }
}
```

これで、Gradleで`run`コマンドを実行するか(すなわち、コマンドラインで`gradle run`を実行)、
IDEにプロジェクトをインポートして、`my.app.Main`クラスを実行することで、アプリケーションを開始できます。

実行すると、サーバーに`http://localhost:5050/`でアクセスできます。

[`handlers()` メソッド](https://ratpack.io/manual/current/api/ratpack/server/RatpackServerSpec.html#handlers-ratpack.func.Action-)は[`Chain`](https://ratpack.io/manual/current/api/ratpack/handling/Chain.html)オブジェクトを受け取る関数を引数にとります。
「ハンドラー・チェーン API」を、レスポンス処理の戦略を組み立てるために使用できます。

Ratpack Gradleプラグインは、[Gradleの継続的ビルド機能](https://docs.gradle.org/current/userguide/continuous_build.html)をサポートしています。
これを使用することで、ソースコードへの変更を自動的に動作しているアプリケーションに適用することができます。

RatpackとGroovyに関するさらなる情報には、[Gradle](gradle.html)の章を参照してください。

### Gradle Groovyプラグインを使う

次の内容の`build.gradle`ファイルを作成します:

```language-groovy gradle
buildscript {
  repositories {
    jcenter()
  }
  dependencies {
    classpath "io.ratpack:ratpack-gradle:@ratpack-version@"
  }
}

apply plugin: "io.ratpack.ratpack-groovy"
apply plugin: "idea"

repositories {
  jcenter()
}

dependencies {
  runtime "org.slf4j:slf4j-simple:@slf4j-version@"
}
```

次の内容の`src/ratpack/ratpack.groovy`ファイルを作成します:

```language-groovy hello-world
import static ratpack.groovy.Groovy.ratpack

ratpack {
    handlers {
        get {
            render "Hello World!"
        }
        get(":name") {
            render "Hello $pathTokens.name!"
        }
    }
}
```

これで、Gradleで`run`コマンドを実行するか(すなわち、コマンドラインで`gradle run`を実行)、
IDEにプロジェクトをインポートして、[`ratpack.groovy.GroovyRatpackMain`](https://ratpack.io/manual/current/api/ratpack/groovy/GroovyRatpackMain.html)クラスを実行することで、アプリケーションを開始できます。

実行すると、サーバーに`http://localhost:5050/`でアクセスできます。

[`handlers()` メソッド](https://ratpack.io/manual/current/api/ratpack/groovy/Groovy.Ratpack.html#handlers-groovy.lang.Closure-)は、[`GroovyChain`](api/ratpack/groovy/handling/GroovyChain.html)オブジェクトに委譲されるクロージャ―を引数にとります。
「Groovy ハンドラー・チェーン DSL」を、レスポンス操作の戦略を組み立てるために使用できます。

Ratpack Gradleプラグインは、[Gradleの継続的ビルド機能](https://docs.gradle.org/current/userguide/continuous_build.html)をサポートしています。
これを使用することで、ソースコードへの変更を自動的に動作しているアプリケーションに適用することができます。

RatpackとGroovyに関するさらなる情報には、[Groovy](groovy.html)の章を参照してください。

RatpackとGroovyに関するさらなる情報には、[Gradle](gradle.html)の章を参照してください。

## Using Lazybones project templates

[Lazybones](https://github.com/pledbrook/lazybones) is a command line tool that allows you to generate a project structure for any framework based on pre-defined templates.

Ratpack's Lazybones templates can be found on [Bintray](https://bintray.com) in the [ratpack/lazybones repository](https://bintray.com/ratpack/lazybones).
Templates are published with each Ratpack release and template versions are aligned with Ratpack release versions.

See the [Lazybones documentation](https://github.com/pledbrook/lazybones#running-it) for help with installing Lazybones.

Lazybones commands are in the format...

```language-bash
lazybones create <ratpack template> <ratpack version> <app name>
```

With Lazybones installed, creating a new Ratpack application is as easy as…

```language-bash
lazybones create ratpack my-ratpack-app
cd my-ratpack-app
./gradlew run
```

This will use the latest available version of Ratpack.
If a specific version is required…

```language-bash
lazybones create ratpack x.x.x my-ratpack-app
cd my-ratpack-app
./gradlew run
```

Where `x.x.x` is a valid template version.
