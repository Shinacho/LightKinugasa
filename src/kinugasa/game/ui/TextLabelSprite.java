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
package kinugasa.game.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import kinugasa.graphics.GraphicsContext;
import kinugasa.game.annotation.NoLoopCall;
import kinugasa.graphics.ARGBColor;
import kinugasa.graphics.ImageUtil;
import kinugasa.graphics.RenderingQuality;
import kinugasa.object.Sprite;

/**
 * 1行テキスト表示用のスプライトです.
 * <br>
 *
 * @version 1.0.0 - 2015/03/24<br>
 * @author Shinacho<br>
 * <br>
 */
public class TextLabelSprite extends Sprite {

	private String text;
	private TextLabelModel labelModel;

	public TextLabelSprite(CharSequence text, TextLabelModel labelModel, float x, float y) {
		super(x, y, 1, 1);
		this.text = text.toString();
		this.labelModel = labelModel;
	}

	public TextLabelSprite(String text, TextLabelModel labelModel, float x, float y, float w, float h) {
		super(x, y, w, h);
		this.text = text;
		this.labelModel = labelModel;
	}

	public TextLabelSprite trim() {
		return trimWSize().trimHSize();
	}

	@NoLoopCall("its heavy")
	public TextLabelSprite trimWSize() {
		BufferedImage image = ImageUtil.newImage(2048, labelModel.getFontSize());
		Graphics2D g = ImageUtil.createGraphics2D(image, RenderingQuality.SPEED);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int) getWidth(), (int) getHeight());
		g.setColor(Color.WHITE);
		g.setFont(labelModel.getFontConfig().getFont());
		g.drawString(text, 0, labelModel.getFontSize());
		g.dispose();

		//テキストサイズの探索
		int[][] pix = ImageUtil.getPixel2D(image);
		for (int y = 0; y < pix.length; y++) {
			for (int x = pix[y].length - 1; x >= 0; x--) {
				for (int yy = 0; yy < pix.length; yy++) {
					if (ARGBColor.getRed(pix[yy][x]) == 255) {
						setWidth(x);
						return this;
					}
				}
			}
		}
		setWidth(0);
		return this;
	}

	@NoLoopCall("its heavy")
	public TextLabelSprite trimHSize() {
		BufferedImage image = ImageUtil.newImage(2048, labelModel.getFontSize() * 2);
		Graphics2D g = ImageUtil.createGraphics2D(image, RenderingQuality.SPEED);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int) getWidth(), (int) getHeight());
		g.setColor(Color.WHITE);
		g.setFont(labelModel.getFontConfig().getFont());
		g.drawString(text, 0, labelModel.getFontSize());
		g.dispose();

		//テキストサイズの探索
		int[][] pix = ImageUtil.getPixel2D(image);
		for (int y = pix.length - 1; y >= 0; y--) {
			for (int x = 0; x < pix[y].length; x++) {
				if (ARGBColor.getRed(pix[y][x]) == 255) {
					setHeight(y);
					return this;
				}
			}
		}
		setHeight(0);
		return this;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public TextLabelModel getLabelModel() {
		return labelModel;
	}

	public void setLabelModel(TextLabelModel labelModel) {
		this.labelModel = labelModel;
	}

	@Override
	public void draw(GraphicsContext g) {
		if (isVisible() & isExist()) {

			labelModel.draw(g, this);
		}
	}

	public void draw(GraphicsContext g, TextLabelModel model) {
		if (isVisible() & isExist()) {
			model.draw(g, this);
		}
	}

	@Override
	public TextLabelSprite clone() {
		var r = (TextLabelSprite) super.clone();
		r.labelModel = this.labelModel.clone();
		return r;
	}

}
