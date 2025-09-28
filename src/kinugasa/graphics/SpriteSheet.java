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

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;
import kinugasa.game.annotation.NewInstance;

/**
 * 1つの画像リソースを切り出して、複数の画像インスタンスを構築するためのビルダです.
 * <br>
 * 同一のアルゴリズムで複数のスプライトシートを構築する場合はSpriteSheetCutterを使用してください。<br>
 * <br>
 *
 * @version 1.0.0 - 2013/01/13_13:00:09<br>
 * @version 1.1.0 - 2015/06/19_22:39<br>
 * @author Shinacho<br>
 */
public class SpriteSheet {

	/**
	 * 画像を切り出すベースとなる画像です. この画像は変更されません。
	 */
	private KImage baseImage;
	/**
	 * 切り出した画像が追加されるリストです.
	 */
	private List<KImage> subImages;

	/**
	 * 空のスプライトシートを作成します.
	 */
	public SpriteSheet() {
		subImages = new ArrayList<>();
	}

	/**
	 * 新しいスプライトシートを構築します.
	 *
	 * @param filePath ロードする画像のパスを指定します。
	 * このコンストラクタでは、ImageUtilのloadメソッドを使用して画像がロードされます。<br>
	 */
	public SpriteSheet(String filePath) {
		baseImage = new KImage(filePath);
		subImages = new ArrayList<>();
	}

	/**
	 * 新しいスプライトシートを構築します.
	 *
	 * @param baseImage ベースとなる画像を指定します。<br>
	 */
	public SpriteSheet(KImage baseImage) {
		this.baseImage = baseImage;
		subImages = new ArrayList<>();
	}

	public SpriteSheet(BufferedImage image) {
		this(new KImage(image));
	}

	public SpriteSheet set(UnaryOperator<KImage> u) {
		this.subImages = new ArrayList<>(subImages.stream().map(p -> u.apply(p)).toList());
		return this;
	}

	/**
	 * ベース画像の指定された領域を切り出して新しい画像とします.
	 *
	 * @param x X座標.<br>
	 * @param y Y座標.<br>
	 * @param width 幅.<br>
	 * @param height 高さ.<br>
	 *
	 * @return このインスタンス自体が返る.<br>
	 *
	 * @throws GraphicsException 画像の範囲外にアクセスしたときに投げられる.<br>
	 */
	public SpriteSheet cut(int x, int y, int width, int height) throws GraphicsException {
		subImages.add(baseImage.subImage(x, y, width, height));
		return this;
	}

	/**
	 * ベース画像の指定された領域を切り出して新しい画像とします.
	 *
	 * @param rectangle 領域.<br>
	 *
	 * @return このインスタンス自体が返る.<br>
	 *
	 * @throws GraphicsException 画像の範囲外にアクセスしたときに投げられる.<br>
	 */
	public SpriteSheet cut(Rectangle rectangle) throws GraphicsException {
		return cut(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}

	public SpriteSheet resizeAll(float scale) {
		subImages = new ArrayList<>(KImage.resizeAll(scale, subImages));
		return this;
	}

	/**
	 * 切り出しアルゴリズムに基づいて、このシートを切り出します.
	 *
	 * @param cutter 特定の切り出し設定アルゴリズム.<br>
	 *
	 * @return このインスタンス自体が返る.<br>
	 *
	 * @throws GraphicsException 画像の範囲外にアクセスしたときに投げられる.<br>
	 */
	public SpriteSheet cut(SpriteSheetCutter cutter) throws GraphicsException {
		addAll(cutter.cut(baseImage));
		return this;
	}

	/**
	 * 座標0,0からwidth,heightのサイズで二次元に可能な数だけ分割し、全ての部分画像をリストに追加します.
	 *
	 * <br>
	 *
	 * @param width 幅.<br>
	 * @param height 高さ.<br>
	 *
	 * @return このインスタンス自体が返る.<br>
	 *
	 * @throws GraphicsException 画像の範囲外にアクセスしたときに投げられる.<br>
	 */
	public SpriteSheet split2D(int width, int height) throws GraphicsException {
		List<List<KImage>> r = baseImage.split2D(width, height);
		for (var v : r) {
			subImages.addAll(v);
		}
		return this;
	}

	/**
	 * 座標0,yからwidth,heightのサイズでX方向に可能な数だけ画像を分割し、全ての部分画像をリストに追加します.
	 *
	 * @param y Y座標.<br>
	 * @param width 幅.<br>
	 * @param height 高さ.<br>
	 *
	 * @return このインスタンス自体が返る.<br>
	 *
	 * @throws GraphicsException 画像の範囲外にアクセスしたときに投げられる.<br>
	 */
	public SpriteSheet rows(int y, int width, int height) throws GraphicsException {
		subImages.addAll(baseImage.splitX(y, width, height));
		return this;
	}

	/**
	 * 座標x,0からwidth,heightのサイズでY方向に可能な数だけ画像を分割し、全ての部分画像をリストに追加します.
	 *
	 * @param x X座標.<br>
	 * @param width 幅.<br>
	 * @param height 高さ.<br>
	 *
	 * @return このインスタンス自体が返る.<br>
	 *
	 * @throws GraphicsException 画像の範囲外にアクセスしたときに投げられる.<br>
	 */
	public SpriteSheet columns(int x, int width, int height) throws GraphicsException {
		subImages.addAll(baseImage.splitY(x, width, height));
		return this;
	}

	public SpriteSheet listX(int w, int h) throws GraphicsException {
		subImages.addAll(baseImage.splitX(w, h));
		return this;
	}

	/**
	 * 指定された画像を追加します.
	 *
	 * @param image 画像.<br>
	 *
	 * @return このインスタンス自体が返る.<br>
	 */
	public SpriteSheet add(KImage image) {
		subImages.add(image);
		return this;
	}

	/**
	 * 指定された0個以上の画像を全てその順序でリストに追加します.
	 *
	 * @param images 画像.<br>
	 *
	 * @return このインスタンス自体が返る.<br>
	 */
	public SpriteSheet addAll(KImage... images) {
		subImages.addAll(Arrays.asList(images));
		return this;
	}

	public SpriteSheet addAll(Collection<KImage> images) {
		subImages.addAll(images);
		return this;
	}

	/**
	 * ベース画像本体をリストに追加します.
	 *
	 * @return このインスタンス自体が返る.<br>
	 */
	public SpriteSheet baseImage() {
		subImages.add(baseImage);
		return this;
	}

	@NewInstance
	public List<KImage> images() {
		return subImages.stream().filter(p -> p != null).toList();
	}

	@Override
	public String toString() {
		return "SpriteSheet{" + "baseImage=" + baseImage + ", subImages=" + subImages + '}';
	}
}
