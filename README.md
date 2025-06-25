# LIGHT KINUGASA GAME ENGINE
これは私のゲームエンジン、通称「軽衣笠」の使い方をカンタンに書いたものです。

まずは軽衣笠の由来について少し説明しましょう。
これはもともと、2011年に当時20歳の私が作り始めた「衣笠ゲームフレームワーク」でした。
私は根っからの「天外魔境２」ファンであり、そのようなゲームを自作したいと思って、自分でフレームワークとゲーム本体を作ることにしたのです。
私は毎日想像を絶する学習・設計・製造を続け、初心者の状態から50回以上の作り直しを経て、衣笠を何とか稼働できるレベルまで仕上げました。私は本職がSEですが、ゲーム開発会社の社員ではないので、これらは独学で作ったものです。
しかし、私のゲームに必要な要素を実装していった衣笠は、まるでサイコガンダムのように巨大化し（ゲームフレームワークがデカすぎる！）、今では約7万ステップを超え、もはや汎用ゲームエンジンとゲーム本体を切り分けるという当初の設計思想の達成が難しくなっていました。
そこで、私のゲーム用に書いたロジックをバッサリカットして、汎用的な処理だけをのせているのが、この「軽衣笠」です。

ちなみにゲーム本体は今でも開発を続けており、これを書いている今はストーリーを全部書ききったので、まずは原作小説を出版して商標を取るのを目標に動いています。2030年までにゲーム本体が完成するといいなぁ。

## それで、これは何？
フレーム数ベースで動く2Dゲームを作るためのフレームワークです。
元はRPG用ですが、「軽」ではかなりの機能をオミットしているので、シューティングゲームくらいが作れると思います。

### 特徴
* JavaSEしか使ってません。面倒なライセンス管理ナシ！使い方もawt/Swing準拠で簡単！
* MITライセンスで商用非商用問わず自由に使えます。改変も可能です。ただしもとのコピーライトは消さないでね。

### 環境
このフレームワークはJava17でコンパイルしています。17以上で使ってください。
単にjarをあなたのプロジェクトに追加すれば、おそらく使えるでしょう。[モジュールシステムに対応しています。](https://github.com/Shinacho/LightKinugasa/blob/master/src/module-info.java)

### 機能
* FPSを指定してゲームを実行でき、ループ処理を記載せずとも動きます
* キーボード、マウス、USBゲームコントローラー（Windows 64Bitのみ）が使えます
* テキスト、画像、メッセージウインドウなどを表示できます
* サウンドの再生ができます
* フィールドマップ（CSVから構築するスクロール可能なイメージ）を構築して表示できます。
* I18N機能を実装しており、iniファイル等から翻訳テキストを取得できます。

## 基本構造
衣笠は、[GameManager](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/game/GameManager.java)を中心に動きます。
GameManagerを継承したクラスにMainを作って、そこをエントリポイントとして動きます。
GameManagerには、初期化・終了処理と、自動的にループで呼び出される更新・描画処理の4つがあり、これを実装することであなたのゲームを動かします。
具体例は[サンプル実装](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/game/sample/SampleMain.java)を見てください。

## GameManagerの解説
GameManagerを継承したクラスの解説をしましょう。
この継承クラスに必要なのは、最低6つのメソッドです。
1. main：通常のメインメソッドですが、そのクラスをインスタンス化してgameStart()をコールしてください。また、このフレームワークにはゲームの二重起動を防止する仕掛けが入っているので、それを無効化するために、開発中は最初にロックファイル削除を実行しておくとよいでしょう。具体例は上記のサンプルを見てください。
2. コンストラクタ：コンストラクタではsuper(GameOption)を実行します。[GameOption](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/game/GameOption.java)はnewしてもいいですし、defaultOption()メソッドを使ってiniファイルから読むこともできます。そのiniファイルはこのプロジェクトのルートに入っているでしょう。また、GameOptionのインスタンスに対して上書きで設定を追加することもできます。ゲームパッドを使いたい場合はここでsetUseGamePad(true)を実行してください。
3. startUpの実装：このメソッドはgameStartが実行されると1回だけ実行されるものです。この時点ではウインドウが表示されていません。なので、例えばBGMのロード再生といった初期化処理を書きます。ウインドウが表示されていないので、ウインドウのサイズなどをとれない点に注意してください。
4. disposeの実装：このメソッドは、gameExitが実行されると1回だけ実行されるものです。gameExitは明示的に実行してもいいですし、デフォルトではウインドウが閉じられると実行されます。したがって、ここにはファイルを解放したりセーブする処理を記述します。
5. updateの実装：このメソッドは、GameOptionで指定したFPSの周期でループ実行されます。引数は[GameTimeManager](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/game/GameTimeManager.java)通称gtmと、[InputState](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/game/input/InputState.java)通称isがあります。gtmからはFPSや総経過時間を取得でき、isではキーやマウス、コントローラーの入力を検知できます。
6. drawの実装：このメソッドは、GameOptionで指定したFPSの周期でループ実行されます。引数は[GraphicsContext](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/game/GraphicsContext.java)で、ゲーム内オブジェクトを描画する処理を記述します。drawの順序に注意してください。あとに書いたものが上に表示されます。上書きされるということです。下に書いたものは見えなくなります。

常に、update→drawの順で実行されます。これはシングルスレッドで実行されています。そのため、同一のリストを参照してどちらの処理も実行することができます。drawの中で更新を行ってもほとんどの場合問題はないですが、役割をわかりやすくするためにオブジェクトの更新処理と描画処理を分けて記述するべきでしょう。一部のアニメーションの自動再生などは、drawの中で更新をしている場合があります。

## 描画系の機能
ここでは、あなたのゲーム制作に役立つ機能を紹介しましょう。

### Sprite
[Sprite](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/object/Sprite.java)は画面に描画する物体の個体です。
様々な派生クラスがあり、どれも共通して描画が可能です。

### ImageSprite
[ImageSprite](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/object/ImageSprite.java)は画像を表示するためのSpriteの実装です。画像はKImageを使います。

### KImage
[KImage](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/graphics/KImage.java)は編集可能な画像の個体です。しかし編集は重いので、ゲーム中にあまり実施すべきではないでしょう。ロード時等に行うとよいと思います。KImageはGraphics2Dを使って何かを描画したり、ファイルから読み込むことができます。推奨のファイル形式はpngで、透過画像も使えます。

### AnimationSprite
[AnimationSprite](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/object/AnimationSprite.java)はImageSpriteの拡張で、複数の画像をアニメーション再生できるSpriteです。[SpriteSheet](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/graphics/SpriteSheet.java)を使って、1枚の画像から特定のサイズで切り出してアニメーションを作ることができます。もちろん、KImageの集合から作ってもよいです。

### TimeCounter
[TimeCounter](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/util/TimeCounter.java)は一定周期で何かを発動したいときに使うカウンタです。例えばアニメーションの再生速度を定義するのに使います。一番よく使うのは[FrameTimeCounter](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/util/FrameTimeCounter.java)でしょう。これは単純に何フレームおきにそのイベントを起こすかを定義するものです。例えばアニメーション再生速度に12を指定したら、12フレームおきに次の画像に切り替わる、ということです。

### KVector
[KVector](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/object/KVector.java)は角度と移動速度を持つクラスです。SpriteのサブクラスであるBasicSpriteが持っています。BasicSpriteのmoveを実行すると、このKVectorに基づいて移動します。simulateMove機能を使って、移動後の座標を計算して、移動範囲を制限することができます。その具体例はサンプル実装を見てください。

### FadeEffect、FlashEffect
[Effect](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/object/Effect.java)のサブクラスであるこれらは、画面全体に効果を及ぼすためのスプライトです。たとえば暗転処理や点滅処理にこれを使えます。

### TextLabelSprite
[TextLabelSprite](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/game/ui/TextLabelSprite.java)は1行のテキストを表示するためのSpriteです。TextLabelModelやFontModelを使用してフォントなどを指定できます。その具体例はサンプル実装を見てください。

### MessageWindow
[MessageWindow](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/game/ui/MessageWindow.java)は複数行にわたるテキストを表示するためのスプライトです。さらに、1文字ずつ表示したり、その速度を制御したり、次のテキストがあることを示す記号を表示する機能もあります。メッセージウインドウの見た目はデフォルトではサンプル実装のような見た目ですが、MessageWindowModelを使って変更することができます。

### Dialog
[Dialog](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/game/ui/Dialog.java)はJOptionPaneを使って簡易なダイアログを出すものです。

### Action
[UIAction](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/game/ui/UIAction.java)を持つ、[ActionTextSprite](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/game/ui/ActionTextSprite.java)などのクラスは、そのテキストに紐づく何らかの動作を登録できます。これは例えばそのスプライトをクリックしたときに動かす処理などを登録できるものです。


## データ系
### Storage
[Storage](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/resource/Storage.java)はIDに対して何らかのオブジェクトをメモリ上に置くためのマップです。HashMapのようなものです。いろいろな場所で使います。

### File
[ファイル系のクラス](https://github.com/Shinacho/LightKinugasa/tree/master/src/kinugasa/resource/text)は、テキスト、CSV、ini、XMLが使えます。これらはクラスが分かれており、共通して、ファイルパスを送れば開けます。開いた後は、load()をしてください。不要になったら、dispose()で解放できます。これらはメモリをたくさん使うゲームのために（Javaでは若干不利ではありますが）ロードタイミングと解放タイミングを指定できるようにしているものです。一部のファイルは保存もできます。作りかけで保存機能がまだできていないものがあります。

### 例外について
衣笠フレームワークでは、例外はほぼすべてRuntime例外です。これは、主にtry-catchを書くのが面倒という理由でそうなっています。
そもそもゲームでは例外＝バグで排除すべきものなので、あまり例外を使わないでしょ、という設計思想となっています。
例えばリソースのpngファイルがなくて画像を表示できないなどは、明確なバグであり、排除されているべきなので、すべてRuntimeで差し支えない、と私は思っています。


## 入力系
入力検知は[InputState]
(https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/game/input/InputState.java)から行えます。
入力状態は、現在のフレームと前のフレームの状態があります。前のフレームでも押されていて、今のフレームでも押されている場合は、CONTINUEと呼ばれます。前のフレームで押されておらず、今のフレームで押されているのはSINGLEと呼ばれます。1回だけ検知したい場合はSINGLE入力を調べ、押し続けて何かを実行する場合はCONTINUEで検知します。いずれも、InputStateから調べることを推奨しますが、InputStateから取得した下記のような各クラスから直接調べることもできるでしょう。入力状態は毎フレーム新規に作成されるインスタンスであり、その各フレームでの状態はfinalな値です。

### キーボード
キーボードの入力検知は[KeyState](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/game/input/KeyState.java)から行います。KyeStateにKeysでキーを指定すると、そのキーの状態を判定できます。

### マウス
マウスの入力検知は、[MouseState](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/game/input/MouseState.java)から行います。ボタンの状態を持っていますので、そこから判定してください。

### ゲームパッド（USBコントローラー）
USBコントローラの検知は、[GamePadState](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/game/input/GamePadState.java)から行います。
これにはボタンの状態と、スティックやトリガーの位置が格納されています。スティックはPoint2D.Floatで、-1,0f～中心が0,0、～1.0fの座標で表されます。これはBasicSpriteが持っているControllableのmoveメソッドに渡すと、そのまま移動に使えます。
Gamepadについてもう少し解説しておきましょう。まず、コントローラのレイアウトはXBOXコントローラです。ボタンの数や名前はXBOXコントローラ準拠です。
そして使用できるコントローラも、XBOX準拠です、具体的には、DirectXのXInputをJNIで接続して使っています。なので、例えばPS5のコントローラなどはそのままでは使えません。プレイヤーがSteamにあるDSXといったソフトを使えば、PS5コントローラも使えるでしょう。
GameOptionでゲームパッドを使うと指示すると、GamePadConnectionクラスがdllをロードします。dllがWindows64bit用でしかコンパイルしていないので、それ以外の環境では起動が失敗するでしょう。ゲームパッドを使用しない場合はGameOption#setUseGamePad(false)を実行すると、ほんの少しだけパフォーマンスが改善します。これはデフォルトの設定です。GamePadの入力を調べる時間が不要だからです。

## サウンド
[Sound](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/resource/sound/Sound.java)クラスを使ってサウンドを作ります。対応しているのは、wav形式とoggです。wavは実はいろいろな種類があるので、再生できない場合があるかもしれません。
サウンドをインスタンス化したら、load()してください。このフレームワークでは、サウンドはロードするとそのすべてがメモリに置かれます。すなわちロードはかなり重い処理です。これはサウンドを完璧にループ再生するためにこうなっているものです。setLoopPointからループ位置を指定できます。サウンドのタイプはSEなのかBGMなのかを示すもので、検索などで使えそうなのでつけているものですが、適当に設定しても特に問題はないです。[SoundSotorage](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/resource/sound/SoundStorage.java)に登録しておくと、一括で停止したりでき、インスタンスも使いまわせます。
サウンドのボリュームの設定ははsetMasterGainから行ってください。0.0fがミュート、1fが通常の音量です。より高い値も指定できます。
ポーズとは再生を一時停止することです、再生位置は保存され、再度再生を開始するとそこから開始します。
停止とは、再生を完全に停止して再生位置をゼロに戻すことです、再度再生を開始すると、サウンドの最初から再生されます。停止してもメモリから解放されません。メモリから解放するにはdispose()を実行してください。
サウンドの位置はフレームという単位です。これは44.1KHzのサウンドなら1秒に44100フレームあるということです。
フェードアウトを設定することができます。フェードアウトを設定したサウンドは、updateを毎フレーム実行しなければ、それが反映されません。GameManagerを継承したあなたのゲームのメインクラスで、updateを実行してください。再生されていないサウンドのupdateは何も実行しないので、サウンドはすべてSoundStorageに登録して、SoundStorageに登録されているすべてをupdateするとよいでしょう。
サンプル実装では、自作の「昔した約束」という楽曲を入れています。これはシンプルながらうまくできたと思っている一曲です。オリジナルのメロディは、たしか2012年くらいに作成したもので、それをアレンジしたものです。
この楽曲は当然再生できるので、もしあなたの楽曲が再生できないときはこの楽曲の属性を確認してみてください。


## I18N
I18Nとは国際化のことで、簡単に言えばゲーム内のテキストを翻訳することです。
[I18N](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/game/I18N.java)クラスから、キーに基づく翻訳結果を取得できます。これはデフォルトではiniファイルを使用するようになっており、GameOptionで言語を指定してiniを指示しますが、それ以外の方法で翻訳を得ることができます。具体的にはGameOptionでは翻訳を指定せず、I18Nのinitを明示的に呼び出し、その引数で翻訳データの入手方法を示したコールバック関数を送ります。これを使えば例えばDBから読むこともできるでしょう。テキスト中の!0～!nは引数として変換することができます。それにはi18N#get(String, Object...)を使います。たとえば「!0は!1を手に入れた！」に名前とアイテム名を送れば、それが反映された「卍丸はそば団子を手に入れた！」になります。

I18Nのより簡単な利用方法は、[I18NText](https://github.com/Shinacho/LightKinugasa/blob/master/src/kinugasa/game/I18NText.java)を使うことです。これは単純にキーを送れば、現在のI18Nの設定を用いて翻訳をtoString()で入手できるものです。


## field4
field4とは、正方形のタイルを並べてフィールドマップを形成するシステムのことです。
このシステムはかなり大きく、複雑で、イマイチな動作をする部分もまだあります。

そのため、すぐには解説を書ききれないので、そのうち改めて書くことにします。




 
