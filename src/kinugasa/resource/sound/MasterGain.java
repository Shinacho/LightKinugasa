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

import kinugasa.game.annotation.Immutable;

/**
 *
 * @vesion 1.0.0 - 2024/08/16_20:42:03<br>
 * @author Shinacho<br>
 */
@Immutable
public final class MasterGain {

	private final float value;

	public MasterGain() {
		this(1f);
	}

	public MasterGain(float value) {
		this.value = value;
	}

	public float value() {
		return value;
	}

	@Override
	public String toString() {
		return "MasterGain{" + "value=" + value + '}';
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 29 * hash + Float.floatToIntBits(this.value);
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
		final MasterGain other = (MasterGain) obj;
		return Float.floatToIntBits(this.value) == Float.floatToIntBits(other.value);
	}

}
