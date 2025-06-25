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

import kinugasa.game.NewInstance;
import kinugasa.game.NotNewInstance;
import kinugasa.game.NotNull;
import kinugasa.game.OneceTime;

/**
 *
 * @vesion 1.0.0 - 2024/04/27_12:27:59<br>
 * @author Shinacho<br>
 */
public interface UIDSupport extends ID {

	@Override
	public default String getId() {
		return getUIDKey().getId();
	}

	//都度newしてもいい
	@NotNull
	public UIDKey getUIDKey();

	//都度newするとキャッシュに乗らない。
	@NotNewInstance
	@NotNull
	public UIDValue getUIDValue();

	public default boolean equals(UIDSupport s) {
		if (this == s) {
			return true;
		}
		if (s == null) {
			return false;
		}
		if (getClass() != s.getClass()) {
			return false;
		}
		return getUIDKey().equals(s.getUIDKey());
	}

	@OneceTime
	@NewInstance
	public default UIDValue createWeakUIDValue() {
		return UIDValue.weakOf(getUIDKey(), this);
	}

	@OneceTime
	@NewInstance
	public default UIDValue createSoftUIDValue() {
		return UIDValue.softOf(getUIDKey(), this);
	}
}
