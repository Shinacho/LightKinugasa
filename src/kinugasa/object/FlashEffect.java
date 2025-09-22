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

import java.awt.Color;
import kinugasa.graphics.GraphicsContext;
import kinugasa.graphics.GraphicsUtil;
import kinugasa.util.TimeCounter;
import kinugasa.util.TimeCounterState;

/**
 * 単色のフラッシュ効果のエフェクトの実装です.
 * <br>
 *
 * <br>
 *
 * @version 1.0.0 - 2013/01/14_20:10:48<br>
 * @author Shinacho<br>
 */
public class FlashEffect extends Effect {

	private Color color;
	private TimeCounter blinkRate;//TRUEでフラッシュ
	private TimeCounter flashTime;//TRUEで終了
	private boolean ended = false;
	private boolean running = false;

	;

	public FlashEffect(Color color, TimeCounter blinkRate, TimeCounter flashTime, float x, float y, float w, float h) {
		super(x, y, w, h);
		this.color = color;
		this.blinkRate = blinkRate;
		this.flashTime = flashTime;
	}
	private int stage = 0;

	@Override
	public void draw(GraphicsContext g) {
		if (!isVisible() || !isExist()) {
			return;
		}
		if (ended) {
			return;
		}
		running = true;
		switch (stage) {
			case 0:
				if (blinkRate.update() == TimeCounterState.ACTIVE) {
					stage = 1;
				}
				break;
			case 1:
				g.setColor(color);
				GraphicsUtil.fillRect(g, getBounds());
				if (flashTime.update() == TimeCounterState.ACTIVE) {
					stage = 2;
				}
				break;
			case 2:
				ended = true;
				setVisible(false);
				stage = 3;
				break;
			case 3:
				break;
			default:
				throw new AssertionError();
		}
	}

	@Override
	public boolean isEnded() {
		return ended;
	}

	@Override
	public void reset() {
		blinkRate.reset();
		flashTime.reset();
		ended = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public String toString() {
		return "FlashEffect{" + "color=" + color + ", blinkRate=" + blinkRate + ", flashTime=" + flashTime + ", ended=" + ended + ", running=" + running + ", stage=" + stage + '}';
	}
}
