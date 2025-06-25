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


package kinugasa.game.field4;

import kinugasa.game.ui.FontModel;
import kinugasa.game.ui.SimpleTextLabelModel;
import kinugasa.game.ui.TextLabelSprite;

/**
 *
 * @vesion 1.0.0 - 2023/11/09_19:39:10<br>
 * @author Shinacho<br>
 */
public class MiniMapLabel {

	private D2Idx idx;
	private String value;

	public MiniMapLabel(D2Idx idx, String value) {
		this.idx = idx;
		this.value = value;
	}

	public int getX() {
		return idx.x;
	}

	public int getY() {
		return idx.y;
	}

	public TextLabelSprite getSprite() {
		return new TextLabelSprite(value, new SimpleTextLabelModel(FontModel.DEFAULT.clone()), 0, 0).trimWSize().trimHSize();
	}

	@Override
	public String toString() {
		return "MiniMapLabel{" + "idx=" + idx + ", value=" + value + '}';
	}

}
