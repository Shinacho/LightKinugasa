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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import kinugasa.game.system.GameSystemException;

/**
 *
 * @vesion 1.0.0 - 2024/04/27_12:29:25<br>
 * @author Shinacho<br>
 */
public class UIDKey implements ID {

	private static final Map<String, UIDKey> cache = new HashMap<>();
	private final String key;

	public static UIDKey of(String key) {
		if (key == null) {
			throw new GameSystemException("UIDK is null");
		}
		if (cache.containsKey(key)) {
			return cache.get(key);
		}
		var r = new UIDKey(key);
		cache.put(key, r);
		return r;
	}

	public static <E extends Enum<E>> UIDKey of(E e) {
		return of(e.toString());
	}

	public static UIDKey of(UIDKey k) {
		return of(k.getId());
	}

	public static UIDKey of(ID i) {
		return of(i.getId());
	}

	private UIDKey(String key) {
		this.key = key;
	}

	@Override
	public String getId() {
		return key;
	}

	@Override
	public String toString() {
		return key;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 47 * hash + Objects.hashCode(this.key);
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
		final UIDKey other = (UIDKey) obj;
		return Objects.equals(this.key, other.key);
	}

}
