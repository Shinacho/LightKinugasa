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


package kinugasa.game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * スクリーンエフェクト連続体は、複数のスクリーンエフェクトを終わり次第順番に適用するためのスクリーンエフェクトの拡張です。
 *
 * @vesion 1.0.0 - 2023/07/20_12:37:25<br>
 * @author Shinacho<br>
 */
public class ScreenEffectContinuum implements ScreenEffect {

	private int idx = 0;
	private List<ScreenEffect> effects = new ArrayList<>();
	private boolean ended = false;

	public ScreenEffectContinuum() {
	}

	public ScreenEffectContinuum(ScreenEffect... v) {
		this(Arrays.asList(v));

	}

	public ScreenEffectContinuum(List<ScreenEffect> v) {
		effects.addAll(v);
	}

	public ScreenEffectContinuum add(ScreenEffect e) {
		effects.add(e);
		return this;
	}

	public List<ScreenEffect> getEffects() {
		return effects;
	}

	@Override
	public BufferedImage doIt(BufferedImage src) {
		if (ended) {
			return src;
		}
		BufferedImage res = effects.get(idx).doIt(src);
		if (effects.get(idx).isEnded()) {
			idx++;
			if (idx >= effects.size()) {
				ended = true;
			}
		}
		return res;
	}

	@Override
	public boolean isEnded() {
		return ended;
	}

	@Override
	public boolean isRunning() {
		return !ended;
	}

}
