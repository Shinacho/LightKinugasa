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
package kinugasa.util;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kinugasa.game.annotation.Nullable;
import kinugasa.game.annotation.Singleton;
import kinugasa.object.KVector;
import kinugasa.object.Sprite;
import kinugasa.resource.Storage;
import kinugasa.resource.ID;

/**
 * 乱数とダイスのエミュレートを提供します.
 * <br>
 * 乱数ジェネレータを利用する方法は2つあります。<br>
 * 1つは、シードを指定してから、乱数生成機能を使用する方法です。<br>
 * シードはリプレイの保存などに使用するため、そのようなゲームデザインの場合には、 適切なタイミングでシードを初期化したり取得する必要があります。<br>
 * 2つめの方法は、シードを指定しないで乱数生成機能を使用することです。<br>
 * この場合には、最初に乱数生成機能(シードの取得(getSeed)を含む)を使用した時点で、適当なシードが適用されます。<br>
 * <br>
 *
 * @version 1.0.0 - 2013/01/11_17:26:09<br>
 * @author Shinacho<br>
 */
@Singleton

public final class KRandom {

	private static final KRandom INSTANCE = new KRandom();

	public static KRandom getInstance() {
		return INSTANCE;
	}

	/**
	 * インスタンス化できません.
	 */
	private KRandom() {
	}
	/**
	 * 現在使用中のシードです.
	 */
	private long seed;
	/**
	 * 乱数ジェネレータです.
	 */
	private java.util.Random random;

	/**
	 * シードを適当な数値で初期化します.
	 *
	 * @return INSTANCE.適用されたシードを返します。<br>
	 */
	public static long initSeed() {
		return KRandom.initSeed(System.nanoTime());
	}

	/**
	 * 指定されたシードを使用してジェネレータを初期化します.
	 *
	 * @param seed 設定するシードを指定します。<br>
	 *
	 * @return INSTANCE.適用されたシードを返します。<br>
	 */
	public static long initSeed(long seed) {
		INSTANCE.random = new java.util.Random(INSTANCE.seed = seed);
		return INSTANCE.seed;
	}

	/**
	 * 使用中のシードを返します.
	 *
	 * ジェネレータが初期化されていない場合は、適当なシードを使用して初期化されます。<br>
	 *
	 * @return INSTANCE.使用中のシードを返します。<br>
	 */
	public static long getSeed() {
		if (INSTANCE.random == null) {
			initSeed();
		}
		return INSTANCE.seed;
	}

	/**
	 * 0から1までのランダムなfloat値を返します.
	 *
	 * 結果には、0は含まれますが、1は含まれません。<br>
	 *
	 * @return INSTANCE.0から1までのランダムなfloatを返します。<br>
	 */
	public static float randomFloat() {
		if (INSTANCE.random == null) {
			initSeed();
		}
		return INSTANCE.random.nextFloat();
	}

	/**
	 * 0からmaxValueまでのランダムなfloat値を返します.
	 *
	 * 結果には、0は含まれますが、maxValueは含まれません。<br>
	 *
	 * @param maxValue 乱数の最大値を指定します。<br>
	 *
	 * @return INSTANCE.0からmaxValueまでのランダムなfloatを返します。<br>
	 */
	public static float randomFloat(float maxValue) {
		if (INSTANCE.random == null) {
			initSeed();
		}
		return KRandom.randomFloat() * maxValue;
	}

	/**
	 * ランダムなint値を返します。
	 *
	 * @return INSTANCE.Intの全ての範囲のランダムな値を返します。<br>
	 */
	public static int randomInt() {
		if (INSTANCE.random == null) {
			initSeed();
		}
		return INSTANCE.random.nextInt();
	}

	/**
	 * 0からmaxValueまでのランダムなint値を返します.
	 *
	 * 結果には、0は含まれますが、maxValueは含まれません。<br>
	 *
	 * @param maxValue 乱数の最大値を指定します。0以下の場合には0に切り詰められます。<br>
	 *
	 * @return INSTANCE.0からmaxValueまでのランダムなintを返します。<br>
	 */
	public static int randomAbsInt(int maxValue) {
		if (INSTANCE.random == null) {
			initSeed();
		}
		return INSTANCE.random.nextInt(maxValue < 0 ? 0 : maxValue);
	}

	/**
	 * ランダムな整数のint値を返します.
	 *
	 * @return INSTANCE.整数のIntのランダムな値を返します。0を含みます。<br>
	 */
	public static int randomAbsInt() {
		return Math.abs(randomInt());
	}

	/**
	 * FROMからTOまでのランダムなintを返します.
	 *
	 * @param from 開始数値.
	 * @param to 終了数値.
	 * @return INSTANCE.FROM?TOまでのランダムな値。TOは含まない。
	 */
	public static int randomAbsInt(int from, int to) {
		return from + randomAbsInt(to - from);
	}

	/**
	 * ランダムなbooleanを返します.
	 *
	 * @return INSTANCE.true又はfalseを返します。<br>
	 */
	public static boolean randomBool() {
		if (INSTANCE.random == null) {
			initSeed();
		}
		return INSTANCE.random.nextBoolean();
	}

	/**
	 * 確率pを判定します.
	 *
	 * @param p 発生する確率を0から1のfloatとして指定します。<br>
	 *
	 * @return INSTANCE.p*100(%)の確率でtrueを返します。
	 * ただしpが0以下の場合はfalse、pが1以上の場合はtrueを返します。<br>
	 */
	public static boolean percent(float p) {
		if (p <= 0f) {
			return false;
		}
		if (p >= 1f) {
			return true;
		}
		return KRandom.randomFloat() < p;
	}

	// spread%上下した値を返します。
	public static float spread(float num, float spread) {
		if (spread >= 1) {
			return num * spread;
		}
		if (spread <= 0) {
			return 0;
		}
		float from = num * (1 - randomFloat(spread));
		float to = num * (1 + randomFloat(spread));
		if (randomBool()) {
			return from;
		}
		return to;
	}

	public static int spread(int n, float s) {
		int max = (int) (n + (n * s));
		int min = (int) (n - (n * s));
		return KRandom.randomAbsInt(min, max);
	}

	public static <V extends ID> V randomChoice(Storage<V> v) {
		return KRandom.randomChoice(v.asList());
	}

	public static <K, V> V randomChoice(Map<K, V> map) {
		return KRandom.randomChoice(new ArrayList<>(map.values()));
	}

	@Nullable
	public static <T> T randomChoice(List<T> t) {
		if (t.isEmpty()) {
			return null;
		}
		return t.get(randomAbsInt(t.size()));
	}

	public static <T> T randomChoice(T... t) {
		return t[randomAbsInt(t.length)];
	}

	public static <T extends Enum<T>> T randomChoice(Class<T> t) {
		return KRandom.randomChoice(List.of(t.getEnumConstants()));
	}

	public static Point2D.Float randomLocation(Rectangle2D r) {
		return KRandom.randomLocation(r, 1, 1);
	}

	public static Point2D.Float randomLocation(Rectangle2D r, float w, float h) {
		float x = (float) (r.getX() + randomAbsInt((int) (r.getWidth() - w)));
		float y = (float) (r.getY() + randomAbsInt((int) (r.getHeight() - h)));
		return new Point2D.Float(x, y);
	}

	public static Point2D.Float randomLocation(Point2D.Float center, float r) {
		Point2D.Float p = (Point2D.Float) center.clone();
		KVector v = new KVector(randomFloat(360), randomFloat(r));
		p.x += v.getLocation().x;
		p.y += v.getLocation().y;
		return p;
	}

	public static Point2D.Float randomLocation(Sprite s, float r) {
		return KRandom.randomLocation(s.getCenter(), r);
	}

	/**
	 * レガシーなダイスロールをエミュレートするメソッドです.
	 *
	 * @param num ダイスを振る回数を指定します。この引数が0の場合、0を返します。<br>
	 * @param sided ダイスの面数を指定します。<br>
	 *
	 * @return INSTANCE.[num D sided] を返します。<br>
	 */
	public static int dice(int num, int sided) {
		return KRandom.randomAbsInt(sided * num - num + 1) + (num);
	}

	/**
	 * 3面ダイスをnum回振った合計値を返します.
	 *
	 * @param num ダイスを振る回数を指定します。<br>
	 *
	 * @return INSTANCE.[num D 3]を返します。<br>
	 */
	public static int d3(int num) {
		return KRandom.dice(num, 3);
	}

	/**
	 * 4面ダイスをnum回振った合計値を返します.
	 *
	 * @param num ダイスを振る回数を指定します。<br>
	 *
	 * @return INSTANCE.[num D 4]を返します。<br>
	 */
	public static int d4(int num) {
		return KRandom.dice(num, 4);
	}

	/**
	 * 6面ダイスをnum回振った合計値を返します.
	 *
	 * @param num ダイスを振る回数を指定します。<br>
	 *
	 * @return INSTANCE.[num D 6]を返します。<br>
	 */
	public static int d6(int num) {
		return KRandom.dice(num, 6);
	}

	/**
	 * 8面ダイスをnum回振った合計値を返します.
	 *
	 * @param num ダイスを振る回数を指定します。<br>
	 *
	 * @return INSTANCE.[num D 8]を返します。<br>
	 */
	public static int d8(int num) {
		return KRandom.dice(num, 8);
	}

	/**
	 * 10面ダイスをnum回振った合計値を返します.
	 *
	 * @param num ダイスを振る回数を指定します。<br>
	 *
	 * @return INSTANCE.[num D 10]を返します。<br>
	 */
	public static int d10(int num) {
		return KRandom.dice(num, 10);
	}

	/**
	 * 12面ダイスをnum回振った合計値を返します.
	 *
	 * @param num ダイスを振る回数を指定します。<br>
	 *
	 * @return INSTANCE.[num D 12]を返します。<br>
	 */
	public static int d12(int num) {
		return KRandom.dice(num, 12);
	}

	/**
	 * 20面ダイスをnum回振った合計値を返します.
	 *
	 * @param num ダイスを振る回数を指定します。<br>
	 *
	 * @return INSTANCE.[num D 20]を返します。<br>
	 */
	public static int d20(int num) {
		return KRandom.dice(num, 20);
	}

	/**
	 * 24面ダイスをnum回振った合計値を返します.
	 *
	 * @param num ダイスを振る回数を指定します。<br>
	 *
	 * @return INSTANCE.[num D 24]を返します。<br>
	 */
	public static int d24(int num) {
		return KRandom.dice(num, 24);
	}

	/**
	 * 30面ダイスをnum回振った合計値を返します.
	 *
	 * @param num ダイスを振る回数を指定します。<br>
	 *
	 * @return INSTANCE.[num D 30]を返します。<br>
	 */
	public static int d30(int num) {
		return KRandom.dice(num, 30);
	}

	/**
	 * 50面ダイスをnum回振った合計値を返します.
	 *
	 * @param num ダイスを振る回数を指定します。<br>
	 *
	 * @return INSTANCE.[num D 50]を返します。<br>
	 */
	public static int d50(int num) {
		return KRandom.dice(num, 50);
	}

	/**
	 * 100面ダイスをnum回振った合計値を返します.
	 *
	 * @param num ダイスを振る回数を指定します。<br>
	 *
	 * @return INSTANCE.[num D 100]を返します。<br>
	 */
	public static int d100(int num) {
		return KRandom.dice(num, 100);
	}
}
