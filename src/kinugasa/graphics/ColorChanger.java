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


package kinugasa.graphics;

import java.awt.Color;
import kinugasa.object.CloneableObject;
import kinugasa.object.IDdCloneableObject;
import kinugasa.object.Statable;
import kinugasa.util.StringUtil;

/**
 * ARGB各要素のColorTransitionModelをまとめるクラスです.
 * <br>
 * このクラスはRGBとアルファ成分の4つのColorTransitionModelをもち、それら全てを
 * 更新したり、現在の値を使用して色を構築できます。<br>
 * <br>
 *
 * @version 1.0.0 - 2013/01/12_19:23:37<br>
 * @author Shinacho<br>
 */
public class ColorChanger extends CloneableObject implements Statable {

	/**
	 * 赤の遷移アルゴリズムです.
	 */
	private ColorTransitionModel red;
	/**
	 * 緑の遷移アルゴリズムです.
	 */
	private ColorTransitionModel green;
	/**
	 * 青の遷移アルゴリズムです.
	 */
	private ColorTransitionModel blue;
	/**
	 * アルファ成分の遷移アルゴリズムです.
	 */
	private ColorTransitionModel alpha;

	/**
	 * RGBのアルゴリズムを指定して、新しいColorChangerを作成します. このコンストラクタでは、アルファ成分は255固定となります。<br>
	 *
	 * @param red 赤の遷移アルゴリズムです。<br>
	 * @param green 緑の遷移アルゴリズムです。<br>
	 * @param blue 青の遷移アルゴリズムです。<br>
	 */
	public ColorChanger(ColorTransitionModel red,
			ColorTransitionModel green,
			ColorTransitionModel blue) {
		this(red, green, blue, ColorTransitionModel.valueOf(255));
	}

	/**
	 * RGBAのアルゴリズムを指定して、新しいColorChangerを作成します.
	 *
	 * @param red 赤の遷移アルゴリズムです。<br>
	 * @param green 緑の遷移アルゴリズムです。<br>
	 * @param blue 青の遷移アルゴリズムです。<br>
	 * @param alpha アルファ成分の遷移アルゴリズムです。<br>
	 */
	public ColorChanger(ColorTransitionModel red,
			ColorTransitionModel green,
			ColorTransitionModel blue,
			ColorTransitionModel alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	/**
	 * アルファ成分のアルゴリズムを収録します.
	 *
	 * @return アルファ成分の遷移アルゴリズムを返します。<br>
	 */
	public ColorTransitionModel getAlpha() {
		return alpha;
	}

	/**
	 * 青のアルゴリズムを収録します.
	 *
	 * @return 青の遷移アルゴリズムを返します。<br>
	 */
	public ColorTransitionModel getBlue() {
		return blue;
	}

	/**
	 * 緑のアルゴリズムを収録します.
	 *
	 * @return 緑の遷移アルゴリズムを返します。<br>
	 */
	public ColorTransitionModel getGreen() {
		return green;
	}

	/**
	 * 赤のアルゴリズムを収録します.
	 *
	 * @return 赤の遷移アルゴリズムを返します。<br>
	 */
	public ColorTransitionModel getRed() {
		return red;
	}

	/**
	 * アルファ成分のアルゴリズムを変更します.
	 *
	 * @param alpha アルファ成分の遷移アルゴリズムを指定します。<br>
	 */
	public void setAlpha(ColorTransitionModel alpha) {
		this.alpha = alpha;
	}

	/**
	 * 青のアルゴリズムを変更します.
	 *
	 * @param blue 青の遷移アルゴリズムを指定します。<br>
	 */
	public void setBlue(ColorTransitionModel blue) {
		this.blue = blue;
	}

	/**
	 * 緑のアルゴリズムを変更します.
	 *
	 * @param green 緑の遷移アルゴリズムを指定します。<br>
	 */
	public void setGreen(ColorTransitionModel green) {
		this.green = green;
	}

	/**
	 * 赤のアルゴリズムを変更します.
	 *
	 * @param red 赤の遷移アルゴリズムを指定します。<br>
	 */
	public void setRed(ColorTransitionModel red) {
		this.red = red;
	}

	@Override
	public ColorChanger clone() {
		ColorChanger result = (ColorChanger) super.clone();
		result.red = this.red.clone();
		result.green = this.green.clone();
		result.blue = this.blue.clone();
		result.alpha = this.alpha.clone();
		return result;
	}
	/**
	 * 終了しているかを判定するためのフラグです.
	 */
	private boolean ended = false;

	/**
	 * 全てのアルゴリズムを更新します. 全てのアルゴリズムが「終了状態」になったとき、このColorChangerも終了状態となります。<br>
	 */
	public void update() {
		red.update();
		green.update();
		blue.update();
		alpha.update();
		ended = red.isEnded() && green.isEnded() && blue.isEnded() && alpha.isEnded();
	}

	@Override
	public boolean isEnded() {
		return ended;
	}

	@Override
	public boolean isRunning() {
		return !ended;
	}

	/**
	 * RGBAの現在の値を使用して、色を構築します.
	 *
	 * @return 現在の値から作成された色を返します。<br>
	 */
	public Color createColor() {
		return ColorTransitionModel.createColor(red, green, blue, alpha);
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 71 * hash + (this.red != null ? this.red.hashCode() : 0);
		hash = 71 * hash + (this.green != null ? this.green.hashCode() : 0);
		hash = 71 * hash + (this.blue != null ? this.blue.hashCode() : 0);
		hash = 71 * hash + (this.alpha != null ? this.alpha.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ColorChanger other = (ColorChanger) obj;
		if (this.red != other.red && (this.red == null || !this.red.equals(other.red))) {
			return false;
		}
		if (this.green != other.green && (this.green == null || !this.green.equals(other.green))) {
			return false;
		}
		if (this.blue != other.blue && (this.blue == null || !this.blue.equals(other.blue))) {
			return false;
		}
		if (this.alpha != other.alpha && (this.alpha == null || !this.alpha.equals(other.alpha))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ColorChanger{" + "red=" + red + ", green=" + green + ", blue=" + blue + ", alpha=" + alpha + '}';
	}
}
