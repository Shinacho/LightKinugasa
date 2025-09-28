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


package kinugasa.ui;

import java.awt.Color;
import java.awt.Font;
import kinugasa.object.IDdCloneableObject;

/**
 * .
 * <br>
 *
 * @version 1.0.0 - 2015/03/29<br>
 * @author Shinacho<br>
 * <br>
 */
public class FontModel extends IDdCloneableObject {

	public static final FontModel DEFAULT = new FontModel("DEFAULT", Color.WHITE, new Font("MONOSPACED", Font.PLAIN, 12));

	public static FontModel defaultClone() {
		return DEFAULT.clone();
	}
	private Color color;
	private Font font;

	public FontModel(String id, Color color, Font font) {
		super(id);
		this.color = color;
		this.font = font;
		addThis();
	}

	private void addThis() {
		FontModelStorage.getInstance().add(this);
	}

	public Color getColor() {
		return color;
	}

	public FontModel setColor(Color color) {
		this.color = color;
		return this;
	}

	public Font getFont() {
		return font;
	}

	public FontModel setFont(Font font) {
		this.font = font;
		return this;
	}

	public FontModel setFontSize(float size) {
		font = font.deriveFont(size);
		return this;
	}

	public FontModel setFontStyle(int style) {
		font = font.deriveFont(style);
		return this;
	}

	public void addAlpha(int val) {
		int newVal = color.getAlpha() + val;
		newVal = newVal > 255 ? 255 : newVal < 0 ? 0 : newVal;
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), newVal);
	}

	@Override
	public FontModel clone() {
		return (FontModel) super.clone();
	}

}
