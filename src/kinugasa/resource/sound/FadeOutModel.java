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


package kinugasa.resource.sound;

import java.util.Objects;
import kinugasa.object.CloneableObject;
import kinugasa.resource.Updateable;

/**
 * これが設定された瞬間から適用される、音量の調整スライダー機能です。 音量がになったとき、サウンドは停止されキューから削除されます。
 *
 * @vesion 1.0.0 - 2024/08/16_23:15:27<br>
 * @author Shinacho<br>
 */
public class FadeOutModel extends CloneableObject implements Updateable<FadeOutResult> {

	private float sub;
	private float current = 1f;
	private float initial = -1;
	private FadeOutResult lastUpdateResult = FadeOutResult.INPROGRESS;

	public FadeOutModel(int frame) {
		if (frame == 0) {
			throw new IllegalArgumentException("fade out frame is 0");
		}
		sub = 1f / frame;
	}

	void set(Sound s) {
		if (initial < 0) {
			initial = s.getMasterGain().value();
		}
		s.setMasterGain(new MasterGain(initial * current));
	}

	public float getCurrent() {
		return current;
	}

	public float getInitial() {
		return initial;
	}

	public FadeOutResult getLastUpdateResult() {
		return lastUpdateResult;
	}

	public float getSub() {
		return sub;
	}

	@Override
	public FadeOutResult update() {
		current -= sub;
		if (current <= 0) {
			current = 0;
			return lastUpdateResult = FadeOutResult.END;
		}
		return lastUpdateResult = FadeOutResult.INPROGRESS;
	}

	@Override
	public boolean isRunning() {
		return lastUpdateResult == FadeOutResult.INPROGRESS;
	}

	@Override
	public boolean isEnded() {
		return lastUpdateResult == FadeOutResult.END;
	}

	@Override
	public FadeOutModel clone() {
		return (FadeOutModel) super.clone();
	}

	@Override
	public String toString() {
		return "VolumeFadeOutModel{" + "sub=" + sub + ", current=" + current + ", initial=" + initial + ", lastUpdateResult=" + lastUpdateResult + '}';
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 83 * hash + Float.floatToIntBits(this.sub);
		hash = 83 * hash + Float.floatToIntBits(this.current);
		hash = 83 * hash + Float.floatToIntBits(this.initial);
		hash = 83 * hash + Objects.hashCode(this.lastUpdateResult);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final FadeOutModel other = (FadeOutModel) obj;
		if (Float.floatToIntBits(this.sub) != Float.floatToIntBits(other.sub)) {
			return false;
		}
		if (Float.floatToIntBits(this.current) != Float.floatToIntBits(other.current)) {
			return false;
		}
		if (Float.floatToIntBits(this.initial) != Float.floatToIntBits(other.initial)) {
			return false;
		}
		return this.lastUpdateResult == other.lastUpdateResult;
	}

}
