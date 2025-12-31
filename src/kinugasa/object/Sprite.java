/*
  * MIT License
  *
  * Copyright (c) 2025 しなちょ
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
 */
package kinugasa.object;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import kinugasa.object.moveEvent.BasicMoving;
import kinugasa.game.GameManager;
import kinugasa.game.GameWindow;
import kinugasa.game.annotation.NewInstance;
import kinugasa.game.annotation.RequiresReturnTypeChange;
import kinugasa.game.annotation.Virtual;
import kinugasa.game.GraphicsContext;
import kinugasa.graphics.ImageUtil;
import kinugasa.graphics.KImage;
import kinugasa.graphics.RenderingQuality;

/**
 * ゲームに表示される自機やキャラクタの基底クラスです.
 * <br>
 * スプライトのすべてのサブクラスでは、クローンを適切にオーバーライドする必要があります。<br>
 * <br>
 *
 * @version 3.0 : 2012/01/20_18:30-<br>
 * @version 4.0 : 2012/02/06_21:00-<br>
 * @version 5.0 : 2012/02/18_18:55-21:45<br>
 * @version 6.0.0 - 2012/06/02_16:46:58.<br>
 * @version 6.14.0 - 2012/06/12_20:17.<br>
 * @version 6.18.0 - 2012/06/16_02:26.<br>
 * @version 6.20.0 - 2012/06/18_21:53.<br>
 * @version 6.23.0 - 2012/07/01_00:48.<br>
 * @version 6.28.0 - 2012/07/07_00:37.<br>
 * @version 6.40.0 - 2012/07/07_17:24.<br>
 * @version 6.40.2 - 2012/07/07_22:45.<br>
 * @version 7.0.0 - 2012/07/14_16:41:11.<br>
 * @version 7.0.1 - 2012/07/21_23:42.<br>
 * @version 7.1.1 - 2012/09/23_01:58.<br>
 * @version 7.1.3 - 2012/11/12_00:54.<br>
 * @version 7.5.0 - 2012/11/21_11:39.<br>
 * @version 8.0.0 - 2013/01/14_16:52:02<br>
 * @version 8.3.0 - 2013/02/10_02:42<br>
 * @version 8.4.0 - 2013/04/28_21:54<br>
 * @version 8.5.0 - 2015/01/05_21:14<br>
 * @version 8.6.0 - 2015/03/29_16:26<br>
 * @version 8.7.0 - 2023/08/26_21:50<br>
 * @version 8.8.0 - 2023/10/28_15:15<br>
 * @author Shinacho<br>
 */
public abstract class Sprite extends CloneableObject
		implements Drawable, Shapeable, Comparable<Sprite>, Updateable<Sprite>, Controllable {

	/**
	 * 領域.
	 */
	private Rectangle2D.Float bounds;
	/**
	 * 中心座標のキャッシュ.
	 */
	private Point2D.Float center = null;
	/**
	 * 相対中心座標.
	 */
	private Point2D.Float personalCenter = null;
	/**
	 * Z軸深度.
	 */
	private float z = 0f;
	/**
	 * 可視状態.
	 */
	private boolean visible = true;
	/**
	 * 生存状態.
	 */
	private boolean exist = true;

	/**
	 * 角度と速度.
	 */
	private KVector vector;
	/**
	 * 移動アルゴリズム.
	 */
	private MovingModel moving;

	/**
	 * 新しいスプライトを作成します. 全てのフィールドが初期化されます.<br>
	 */
	public Sprite() {
		this(0, 0, 0, 0);
	}

	public Sprite(Rectangle r) {
		this(r.x, r.y, r.width, r.height);
	}

	/**
	 * 位置およびサイズを指定してスプライトを作成します.
	 *
	 * @param x X座標.<br>
	 * @param y Y座標.<br>
	 * @param w 幅.<br>
	 * @param h 高さ.<br>
	 */
	public Sprite(float x, float y, float w, float h) {
		this(x, y, w, h, 0);
	}

	/**
	 * 位置およびサイズを指定してスプライトを作成します.
	 *
	 * @param x X座標.<br>
	 * @param y Y座標.<br>
	 * @param w 幅.<br>
	 * @param h 高さ.<br>
	 * @param z
	 */
	public Sprite(float x, float y, float w, float h, float z) {
		this.bounds = new Rectangle2D.Float(x, y, w, h);
		this.z = z;
		this.vector = new KVector(0, 0);
		this.moving = BasicMoving.getInstance();
		setCenter();
	}

	public Sprite(float x, float y, float w, float h, KVector vector) {
		this(x, y, w, h);
		this.vector = vector;
		this.moving = BasicMoving.getInstance();
	}

	public Sprite(float x, float y, float w, float h, KVector vector, MovingModel model) {
		this(x, y, w, h);
		this.vector = vector;
		this.moving = model;
	}

	public Sprite(float w, float h, KVector vector, MovingModel model) {
		this(0, 0, w, h);
		this.vector = vector;
		this.moving = model;
	}

	public float getAngle() {
		return vector.angle;
	}

	public void setAngle(float angle) {
		vector.angle = angle;
	}

	public float getSpeed() {
		return vector.speed;
	}

	public void setSpeed(float speed) {
		vector.speed = speed;
	}

	/**
	 * 中心座標のキャッシュを作成します.
	 */
	private void setCenter() {
		center = new Point2D.Float(bounds.x + bounds.width / 2,
				bounds.y + bounds.height / 2);
		personalCenter = new Point2D.Float(bounds.width / 2, bounds.height / 2);
	}

	/**
	 * 中心座標のキャッシュを更新します.
	 */
	protected final void updateCenter() {
		center.x = bounds.x + personalCenter.x;
		center.y = bounds.y + personalCenter.y;
	}

	/**
	 * 中心座標のキャッシュを更新します.
	 */
	protected final void updatePersonalCenter() {
		personalCenter.x = bounds.width / 2;
		personalCenter.y = bounds.height / 2;
	}

	/**
	 * スプライトを描画します. visibleまたはexistがfalseのとき、描画してはなりません.<br>
	 *
	 * @param g グラフィックスコンテキスト.<br>
	 */
	@Override
	public abstract void draw(GraphicsContext g);

	/**
	 * スプライトの様々なプロパティを更新します. このメソッドは、オーバーライドしない限り、何も行いません。<br>
	 * 移動とは別に、状態を更新する必要がある場合、オーバーライドすることで 処理を定義できます。<br>
	 */
	@Override
	@Virtual
	@RequiresReturnTypeChange
	public Sprite update() {
		return this;
	}

	@Override
	@Virtual
	public boolean isRunning() {
		return visible && exist;
	}

	@Override
	@Virtual
	public boolean isEnded() {
		return !visible || !exist;
	}

	/**
	 * このスプライトの領域を取得します. このメソッドではクローンが返されます.<br>
	 *
	 * @return スプライトの領域.<br>
	 */
	public Rectangle2D.Float getBounds() {
		return (Rectangle2D.Float) bounds.clone();
	}

	/**
	 * このスプライトの”当たり判定”の領域を返します. このメソッドはスプライトが他のスプライトを衝突しているか検査する場合に
	 * スプライトを包含する矩形と論理的な衝突状態を区別するために設けられています。<br>
	 * このメソッドはデフォルトでは、getBounds()と同じ値を返します。<br>
	 * このメソッドを使用する場合は適切にオーバーライドしてください。<br>
	 *
	 * @return スプライトの”当たり判定”の領域を返します。オーバーライドしない場合はgetBounds()を返します。<br>
	 */
	public Rectangle2D.Float getHitBounds() {
		return (Rectangle2D.Float) bounds.clone();
	}

	/**
	 * このスプライトの領域を設定します.
	 *
	 * @param bounds スプライトの領域.<br>
	 */
	public void setBounds(Rectangle2D.Float bounds) {
		this.bounds = (Rectangle2D.Float) bounds.clone();
		updatePersonalCenter();
		updateCenter();
	}

	/**
	 * このスプライトの領域を設定します.
	 *
	 * @param location 位置を指定します。<br>
	 * @param width 幅です。<br>
	 * @param height 高さです。<br>
	 */
	public void setBounds(Point2D.Float location, float width, float height) {
		setBounds(new Rectangle2D.Float(location.x, location.y, width, height));
	}

	/**
	 * このスプライトの領域を設定します.
	 *
	 * @param x X位置です。<br>
	 * @param y Y位置です。<br>
	 * @param width 幅です。<br>
	 * @param height 高さです。<br>
	 */
	public void setBounds(float x, float y, float width, float height) {
		setBounds(new Rectangle2D.Float(x, y, width, height));
	}

	//必要であれば、hitBoundsによる判定にオーバーライドできる
	@Override
	public boolean contains(Point2D point) {
		return bounds.contains(point);
	}

	public boolean contains(Sprite s) {
		return bounds.contains(s.bounds);
	}

	public boolean hit(Sprite s) {
		return hit(s.getBounds());
	}

	public boolean hit(Rectangle2D r) {
		return getBounds().intersects(r);
	}

	public boolean hitOnNextMove(Sprite s) {
		final float SPEED = 0.7f;
		Sprite c = this.clone();
		if (c.getVector().getSpeed() < 0) {
			c.getVector().setSpeed(-SPEED);
		} else {
			c.getVector().setSpeed(SPEED);
		}
		for (int i = 0; i < (int) (Math.abs(this.getVector().getSpeed()) / SPEED); i++) {
			c.move();
			if (c.hit(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * スプライトの左上の位置を取得します. このメソッドは新しいインスタンスを返します.<br>
	 *
	 * @return 左上の位置.<br>
	 */
	@NewInstance
	public Point2D.Float getLocation() {
		return new Point2D.Float(bounds.x, bounds.y);
	}

	public Point2D.Float getRightLowerLocation() {
		Point2D.Float p = getLocation();
		p.x += getWidth();
		p.y += getHeight();
		return p;
	}

	/**
	 * スプライトの左上の位置を設定します.
	 *
	 * @param location 左上の位置.<br>
	 */
	public void setLocation(Point2D.Float location) {
		setLocation(location.x, location.y);
	}

	/**
	 * スプライトと同じ座標に配置します。
	 *
	 * @param s 対象のスプライト。
	 */
	public void setLocation(Sprite s) {
		setLocation(s.getLocation());
	}

	/**
	 * スプライトの左上の位置を設定します.
	 *
	 * @param x X座標.<br>
	 * @param y Y座標.<br>
	 */
	public void setLocation(float x, float y) {
		setX(x);
		setY(y);
	}

	public void addLocation(float x, float y) {
		setLocation(getX() + x, getY() + y);
	}

	//pがcenterになるように位置を設定
	public void setLocationByCenter(Point2D.Float p) {
		float x = p.x - getWidth() / 2;
		float y = p.y - getHeight() / 2;
		setLocation(x, y);
	}

	/**
	 * スプライトの中心の座標を取得します. このメソッドではクローンが返されます.<br>
	 *
	 * @return スプライトの中心の座標.ウインドウ上での絶対座標.<Br>
	 */
	@NewInstance
	public Point2D.Float getCenter() {
		return (Point2D.Float) center.clone();
	}

	/**
	 * スプライトの中心のX座標を取得します.
	 *
	 * @return スプライトの中心のX座標。ウインドウ上での座標を返します。<br>
	 */
	public float getCenterX() {
		return center.x;
	}

	/**
	 * スプライトの中心のY座標を取得します.
	 *
	 * @return スプライトの中心のY座標。ウインドウ上での座標を返します。<br>
	 */
	public float getCenterY() {
		return center.y;
	}

	/**
	 * スプライトの中心の相対的なX座標を取得します.
	 *
	 * @return スプライトの中心のX座標。スプライトのサイズに対する中心の座標を返します。<br>
	 */
	public float getPersonalCenterX() {
		return personalCenter.x;
	}

	/**
	 * スプライトの中心の相対的なY座標を取得します.
	 *
	 * @return スプライトの中心のY座標。スプライトのサイズに対する中心の座標を返します。<br>
	 */
	public float getPersonalCenterY() {
		return personalCenter.y;
	}

	/**
	 * スプライトの中心の相対座標を取得します. 相対中心座標とはスプライトの領域の左上からの中心までの距離です.<br>
	 * このメソッドではクローンが返されます.<br>
	 *
	 * @return 中心の相対座標.<br>
	 */
	public Point2D.Float getPersonalCenter() {
		return (Point2D.Float) personalCenter.clone();
	}

	/**
	 * スプライトのサイズを取得します. サイズはint精度に丸められます.<br>
	 * このメソッドは新しいインスタンスを返します.<br>
	 *
	 * @return スプライトのサイズ.<br>
	 */
	public Dimension getSize() {
		return new Dimension((int) bounds.width, (int) bounds.height);
	}

	/**
	 * スプライトのサイズを取得します. サイズはint精度に丸められます.<br>
	 *
	 * @param size スプライトのサイズ.<br>
	 */
	public void setSize(Dimension size) {
		setSize(size.width, size.height);
	}

	/**
	 * スプライトのサイズを取得します.
	 *
	 * @param w スプライトの幅.<br>
	 * @param h スプライトの高さ.<br>
	 */
	public void setSize(float w, float h) {
		bounds.width = w;
		bounds.height = h;
		updatePersonalCenter();
		updateCenter();
	}

	/**
	 * スプライトの生存状態を取得します.
	 *
	 * @return 生存中の場合はtrueを返す.<Br>
	 */
	public boolean isExist() {
		return exist;
	}

	/**
	 * スプライトの生存状態を設定します.
	 *
	 * @param exist 生存状態.<br>
	 */
	public void setExist(boolean exist) {
		this.exist = exist;
	}

	/**
	 * スプライトの可視状態を取得します.
	 *
	 * @return スプライトの可視状態.<br>
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * スプライトの可視状態を設定します.
	 *
	 * @param visible スプライトの可視状態.<br>
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void switchVisible() {
		setVisible(!isVisible());
	}

	/**
	 * このスプライトの左上のX座標を取得します.<br>
	 *
	 * @return X座標.<br>
	 */
	public float getX() {
		return bounds.x;
	}

	/**
	 * このスプライトの左上のX座標を設定します.<br>
	 *
	 * @param x X座標.<br>
	 */
	public void setX(float x) {
		bounds.x = x;
		center.x = bounds.x + personalCenter.x;
	}

	/**
	 * このスプライトの左上のY座標を取得します.<br>
	 *
	 * @return Y座標.<br>
	 */
	public float getY() {
		return bounds.y;
	}

	/**
	 * このスプライトの左上のY座標を設定します.<br>
	 *
	 * @param y Y座標.<br>
	 */
	public void setY(float y) {
		bounds.y = y;
		center.y = bounds.y + personalCenter.y;
	}

	/**
	 * このスプライトの幅を取得します.<br>
	 *
	 * @return 幅.<br>
	 */
	public float getWidth() {
		return bounds.width;
	}

	/**
	 * このスプライトの幅を設定します.<br>
	 *
	 * @param width 幅.<br>
	 */
	public void setWidth(float width) {
		bounds.width = width;
		personalCenter.x = bounds.width / 2;
		center.x = bounds.x + personalCenter.x;
	}

	/**
	 * このスプライトの高さを取得します.<br>
	 *
	 * @return 高さ.<br>
	 */
	public float getHeight() {
		return bounds.height;
	}

	/**
	 * このスプライトの高さを設定します.<br>
	 *
	 * @param height 高さ.<br>
	 */
	public void setHeight(float height) {
		bounds.height = height;
		personalCenter.y = bounds.height / 2;
		center.y = bounds.y + personalCenter.y;
	}

	/**
	 * このスプライトのZ深度を取得します.
	 *
	 * @return 深度.<br>
	 */
	public float getZ() {
		return z;
	}

	/**
	 * このスプライトのZ深度を設定します.
	 *
	 * @param z 深度.<br>
	 */
	public void setZ(float z) {
		this.z = z;
	}

	@NewInstance
	public KImage toImage() {
		GameWindow window = GameManager.getInstance().getWindow();
		BufferedImage image = ImageUtil.newImage(window.getWidth(), window.getHeight());
		Graphics2D g2 = ImageUtil.createGraphics2D(image, RenderingQuality.NOT_USE);
		this.draw(g2);
		g2.dispose();
		return new KImage(image);
	}

	@NewInstance
	public KImage drawOn00() {
		Sprite s = clone();
		s.setLocation(0, 0);
		KImage image = new KImage((int) getWidth(), (int) getHeight());
		Graphics2D g2 = image.createGraphics2D();
		s.draw(g2);
		g2.dispose();
		return image;
	}

	/**
	 * スプライトの深度を比較し、z軸座標の昇順に並び替える機能を提供します.
	 *
	 * @param spr 比較するスプライト.<br>
	 *
	 * @return Comparableの実装に基づく値.<br>
	 */
	@Override
	public final int compareTo(Sprite spr) {
		return java.lang.Float.compare(z, spr.z);
	}

	/**
	 * オブジェクトに設定されているパラメータおよびアルゴリズムを使用して移動します.
	 */
	public void move() {
		moving.move(this);
		updateCenter();
	}

	//移動をシミュレートして、次の移動で移動後の座標を返します。
	private Point2D.Float simulateMoveLocation;

	public void commitMove() {
		if (simulateMoveLocation == null) {
			return;
		}
		setLocation(simulateMoveLocation);
	}

	public Point2D.Float simulateMove() {
		Sprite s = clone();
		s.move();
		return simulateMoveLocation = s.getLocation();
	}

	public Point2D.Float simulateMoveCenterLocation() {
		Sprite s = clone();
		s.move();
		return s.getCenter();
	}

	public Point2D.Float simulateMove(KVector v) {
		Sprite s = asEmptySprite();
		s.setVector(v);
		s.move();
		return s.getLocation();
	}

	public Point2D.Float simulateMoveCenterLocation(KVector v) {
		Sprite s = asEmptySprite();
		s.setVector(v);
		s.move();
		return s.getCenter();
	}

	@NewInstance
	public EmptySprite asEmptySprite() {
		return new EmptySprite(getLocation(), getSize());
	}

	/**
	 * 指定のアルゴリズムを使用して移動します.
	 *
	 * @param m 移動方法.<br>
	 */
	public void move(MovingModel m) {
		m.move(this);
		updateCenter();
	}

	@Override
	public boolean move(float xValue, float yValue, Shape s) {
		float x = getX() + xValue * vector.speed;
		float y = getY() - yValue * vector.speed;
		if (s == null) {
			setX(x);
			setY(y);
			updateCenter();
			return true;
		}
		if (s.contains(new Point2D.Float(x + getPersonalCenterX(), y + getPersonalCenterY()))) {
			setX(x);
			setY(y);
			updateCenter();
			return true;
		}
		return false;
	}

	@Override
	public boolean move(Point2D.Float p, Shape s) {
		return move(p.x, p.y, s);
	}

	/**
	 * このスプライトが現在の設定で次に移動した時の中心の座標を返します.
	 * <br>
	 * このメソッドは、移動モデルによる移動手段を考慮しません。<br>
	 *
	 * @return 次の中心座標.<br>
	 */
	public Point2D.Float getNextCenter() {
		Point2D.Float p = (Point2D.Float) getCenter().clone();
		p.x += vector.getX();
		p.y += vector.getY();
		return p;
	}

	/**
	 * このスプライトが現在の設定で次に移動した時の左上の座標を返します.
	 * <br>
	 * このメソッドは、移動モデルによる移動手段を考慮しません。<br>
	 *
	 * @return 次の座標.<br>
	 */
	public Point2D.Float getNextLocation() {
		Point2D.Float p = getLocation();
		p.x += vector.getX();
		p.y += vector.getY();
		return p;
	}

	public KVector getVector() {
		return vector;
	}

	public void setVector(KVector vector) {
		this.vector = vector;
	}

	/**
	 * 移動モデルを取得します.
	 *
	 * @return 移動モデル.<br>
	 */
	public MovingModel getMovingModel() {
		return moving;
	}

	/**
	 * このスプライトの移動イベントのうち、指定したクラスのイベントを返します.
	 * このメソッドでは、このスプライトの移動イベントがMovingEventである場合には
	 * その内部を検索して移動イベントの実装を返します。<br>MovingEventを取得するには、
	 * 引数にMovingEventのクラスを指定します。<br>
	 *
	 * @param model 検索するモデルのクラス。<br>
	 *
	 * @return 指定したクラスのイベントが含まれている場合にそのインスタンスを返す。存在しない場合はnullを返す。<br>
	 */
	public MovingModel getMovingModel(Class<? extends MovingModel> model) {
		return (model.isInstance(moving)) ? moving : null;
	}

	/**
	 * 移動モデルを設定します.
	 *
	 * @param movingModel 移動モデル.<br>
	 */
	public void setMovingModel(MovingModel movingModel) {
		this.moving = movingModel;
	}

	/**
	 * このスプライトの複製を作成します. このメソッドでは、全てのフィールドをクローニングします.<br>
	 * このメソッドはサブクラスで適切にオーバーライドしてください.<br>
	 *
	 * @return このスプライトと同じ設定の新しいインスタンス.<br>
	 */
	@Override
	public Sprite clone() {
		Sprite s = (Sprite) super.clone();
		s.bounds = (Rectangle2D.Float) this.bounds.clone();
		s.center = (Point2D.Float) this.center.clone();
		s.personalCenter = (Point2D.Float) this.personalCenter.clone();
		s.vector = this.vector.clone();
		s.moving = this.moving.clone();
		return s;
	}

	/**
	 * スプライトの文字列表記を取得します.
	 * 文字列にはスプライトのフィールド情報が含まれています.これらの値はすべてアクセサを通して取得可能です.<br>
	 *
	 * @return スプライトの情報.<br>
	 */
	@Override
	public String toString() {
		return "Sprite location=[" + getX() + "," + getY() + "] size=["
				+ getWidth() + "," + getHeight() + "] " + "center=["
				+ getCenterX() + "," + getCenterY() + "] personalCenter=["
				+ getPersonalCenterX() + "," + getPersonalCenterY() + "] visible=["
				+ isVisible() + "] exist=[" + isExist() + "] vector=[" + getVector() + "] "
				+ "z=[" + getZ() + "]";
	}
}
