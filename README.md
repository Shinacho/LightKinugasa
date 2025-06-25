# LIGHT KINUGASA GAME ENGINE
これは私のゲームエンジン、通称「軽衣笠」の使い方をカンタンに書いたものです。

軽衣笠の由来について少し説明しましょう。
もともとこれは2011年、当時20歳の時に私が作り始めた「衣笠ゲームフレームワーク」でした。
私は根っからの「天外魔境２」ファンであり、そのようなゲームを自作したいと思って、自分でフレームワークごと作ることにしたのです。
しかし、私のゲームに必要な要素を実装していった衣笠は、まるでサイコガンダムのように巨大化し、今では約7万ステップを超え、もはや汎用ゲームエンジンとゲーム本体を切り分けるという当初の設計思想の達成が難しくなっていました。
そこで、私のゲーム用に書いたロジックをバッサリカットして、汎用的な処理だけをのせているのが、この「軽衣笠」です。

ちなみにゲーム本体は今でも開発を続けており、これを書いている今はストーリーを全部書ききったので、まずは原作小説を出版して商標を取るのを目標に動いています。2030年までに完成するといいなぁ。

## それでこれは何？
フレーム数ベースで動く2Dゲームを作るためのフレームワークです。
シューティングゲームなどが作れると思います。

### 特徴
* JavaSEしか使ってません。面倒なライセンス管理ナシ！使い方もawt/Swing準拠で簡単！
* MITライセンスで商用非商用問わず自由に使えます。改変も可能です。ただしもとのコピーライトは消さないでね。

### 機能
* FPSを指定してゲームを実行でき、ループ処理を記載せずとも動きます
* キーボード、マウス、USBゲームコントローラー（Windows 64Bitのみ）が使えます
* テキスト、画像、メッセージウインドウなどを表示できます
* サウンドの再生ができます
* フィールドマップ（CSVから構築するスクロール可能なイメージ）を構築して表示できます。
* I18N機能を実装しており、iniファイル等から翻訳テキストを取得できます。

## 基本構造
衣笠の基本構造は、[GameManager](https://github.com/Shinacho/LightKinugasa/blob/04c037fafd924168e785ea0600ae1d168922b37e/src/kinugasa/game/GameManager.java)が中心にあります。
GameManagerを継承したクラスにMainを作って、そこをエントリポイントとして動きます。
GameManagerには、初期化・終了処理と、自動的にループで呼び出される更新・描画処理の4つがあり、これを実装することであなたのゲームを動かします。
具体例は[サンプル実装](https://github.com/Shinacho/LightKinugasa/blob/04c037fafd924168e785ea0600ae1d168922b37e/src/kinugasa/game/sample/SampleMain.java)を見てください。

## GameManagerの説明
GameManagerを継承したクラスの解説をしましょう。
この継承クラスに必要なのは、最低6つのメソッドです。
1. main：通常のメインメソッドですが、そのクラスをインスタンス化してgameStart()をコールしてください。また、このフレームワークにはゲームの二重起動を防止する装置が入っているので、それを無効化するために開発中はロックファイル削除を実行しておくとよいでしょう。具体例は上記のサンプルを見てください。
2. コンストラクタ：コンストラクタではsuper(GameOption)を実行します。[GameOption](https://github.com/Shinacho/LightKinugasa/blob/04c037fafd924168e785ea0600ae1d168922b37e/src/kinugasa/game/GameOption.java)はnewしてもいいですし、defaultOptionメソッドを使ってiniファイルから読むこともできます。そのiniファイルはこのプロジェクトのルートに入っているでしょう。また、GameOptionのインスタンスに対して上書きで設定を追加することもできます。ゲームパッドを使いたい場合はここでsetUseGamePad(true)を実行してください。
3. startUpの実装：このメソッドはgameStartが実行されると1回だけ実行されるものです。この時点ではウインドウが表示されていません。なので、例えばBGMのロード再生といった初期化処理を書きます。ウインドウが表示されていないので、ウインドウのサイズなどをとれない点に注意してください。
4. disposeの実装：このメソッドは、gameExitが実行されると1回だけ実行されるものです。gameExitは明示的に実行してもいいですし、デフォルトではウインドウが閉じられると実行されます。したがって、ここにはファイルを解放したりセーブする処理を記述します。
5. updateの実装：このメソッドは、GameOptionで指定したFPSの周期でループ実行されます。引数は[GameTimeManager](https://github.com/Shinacho/LightKinugasa/blob/04c037fafd924168e785ea0600ae1d168922b37e/src/kinugasa/game/GameTimeManager.java)通称gtmと、[InputState](https://github.com/Shinacho/LightKinugasa/blob/04c037fafd924168e785ea0600ae1d168922b37e/src/kinugasa/game/input/InputState.java)通称isがあります。gtmからはFPSや総経過時間を取得でき、isはキーやマウス、コントローラーの入力を検知できます。
6. drawの実装：このメソッドは、GameOptionで指定したFPSの周期でループ実行されます。引数は[GraphicsContext](https://github.com/Shinacho/LightKinugasa/blob/04c037fafd924168e785ea0600ae1d168922b37e/src/kinugasa/game/GraphicsContext.java)で、ゲーム内オブジェクトを描画する処理を記述します。

常に、update→drawの順で実行されます。これはシングルスレッドで実行されています。そのため、同一のリストを参照して描画することができます。drawの中で更新を行ってもほとんどの場合問題はないですが、役割をわかりやすくするためにオブジェクトの更新処理と描画処理を分けて記述するべきでしょう。

## 描画系の機能
ここでは、あなたのゲーム制作に役立つ機能を紹介しましょう。

### Sprite
[Sprite](https://github.com/Shinacho/LightKinugasa/blob/7af343ba48f6433b12ad03b10942a761595ac897/src/kinugasa/object/Sprite.java)は画面に描画する物体の個体です。
様々な派生クラスがあり、どれも描画が可能です。

### ImageSprite
[ImageSprite](https://github.com/Shinacho/LightKinugasa/blob/7af343ba48f6433b12ad03b10942a761595ac897/src/kinugasa/object/ImageSprite.java)は画像を表示するためのSpriteの実装です。画像はKImageを使います。

### KImage
[KImage](https://github.com/Shinacho/LightKinugasa/blob/7af343ba48f6433b12ad03b10942a761595ac897/src/kinugasa/graphics/KImage.java)は編集可能な画像の個体です。しかし編集は重いので、ゲーム中にあまり実施すべきではないでしょう。ロード時等に行うとよいと思います。KImageはGraphics2Dを使って何かを描画したり、ファイルから読み込むことができます。推奨のファイル形式はpngで、透過画像も使えます。

### AnimationSprite
[AnimationSprite](https://github.com/Shinacho/LightKinugasa/blob/508158febcebfb02153e30f08b8370edc903f117/src/kinugasa/object/AnimationSprite.java)はImageSpriteの拡張で、複数の画像をアニメーション再生できるSpriteです。

### TimeCounter
[TimeCounter](https://github.com/Shinacho/LightKinugasa/blob/508158febcebfb02153e30f08b8370edc903f117/src/kinugasa/util/TimeCounter.java)は一定周期で何かを発動したいときに使うカウンタです。例えばアニメーションの再生速度を定義するのに使います。一番よく使うのは[FrameTimeCounter](https://github.com/Shinacho/LightKinugasa/blob/508158febcebfb02153e30f08b8370edc903f117/src/kinugasa/util/FrameTimeCounter.java)でしょう。これは単純に何フレームおきにそのイベントを起こすかを定義するものです。

### TextLabelSprite
[TextLabelSprite](https://github.com/Shinacho/LightKinugasa/blob/508158febcebfb02153e30f08b8370edc903f117/src/kinugasa/game/ui/TextLabelSprite.java)は1行のテキストを表示するためのSpriteです。TextLabelModelやFontModelを使用してフォントなどを指定できます。その具体例はサンプル実装を見てください。

### KVector
[KVector](https://github.com/Shinacho/LightKinugasa/blob/508158febcebfb02153e30f08b8370edc903f117/src/kinugasa/object/KVector.java)は角度と移動速度を持つクラスです。SpriteのサブクラスであるBasicSpriteが持っています。BasicSpriteのmoveを実行すると、このKVectorに基づいて移動します。simulateMove機能を使って、移動後の座標を計算して、移動範囲を制限することができます。その具体例はサンプル実装を見てください。

### FadeEffect、FlashEffect
[Effect](https://github.com/Shinacho/LightKinugasa/blob/508158febcebfb02153e30f08b8370edc903f117/src/kinugasa/object/Effect.java)のサブクラスであるこれらは、画面全体に効果を及ぼすためのスプライトです。たとえば暗転処理や点滅処理にこれを使えます。

### MessageWindow
[MessageWindow](https://github.com/Shinacho/LightKinugasa/blob/508158febcebfb02153e30f08b8370edc903f117/src/kinugasa/game/ui/MessageWindow.java)は複数行にわたるテキストを表示するためのスプライトです。さらに、1文字ずつ表示したり、その速度を制御したり、次のテキストがあることを示す記号を表示する機能もあります。メッセージウインドウの見た目はデフォルトではサンプル実装のような見た目ですが、MessageWindowModelを使って変更することができます。

### Dialog
[Dialog](https://github.com/Shinacho/LightKinugasa/blob/508158febcebfb02153e30f08b8370edc903f117/src/kinugasa/game/ui/Dialog.java)はJOptionPaneを使って簡易なダイアログを出すものです。

### Action
[UIAction](https://github.com/Shinacho/LightKinugasa/blob/508158febcebfb02153e30f08b8370edc903f117/src/kinugasa/game/ui/UIAction.java)を持つ、[ActionTextSprite](https://github.com/Shinacho/LightKinugasa/blob/508158febcebfb02153e30f08b8370edc903f117/src/kinugasa/game/ui/ActionTextSprite.java)などのクラスは、そのテキストに紐づく何らかの動作を登録できます。これは例えばそのスプライトをクリックしたときに動かす処理などを登録できるものです。




## データ系
### Storage
[Storage](https://github.com/Shinacho/LightKinugasa/blob/508158febcebfb02153e30f08b8370edc903f117/src/kinugasa/resource/Storage.java)はIDに対して何らかのオブジェクトをメモリ上に置くためのマップです。HashMapのようなものです。いろいろな場所で使います。




 
