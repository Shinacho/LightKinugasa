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

import java.util.Objects;
import kinugasa.game.UnneedTranslate;

/**
 *
 * @vesion 1.0.0 - 2024/04/28_1:02:33<br>
 * @author Shinacho<br>
 */
public abstract class UIDCloneableObject extends CloneableObject implements UIDSupport {

	private UIDKey key;
	private UIDValue uidv;

	@UnneedTranslate
	public enum Type {
		WEAK,
		SOFT,;
	}

	public UIDCloneableObject(ID id, Type t) {
		this.key = UIDKey.of(id);
		switch (t) {
			case SOFT ->
				uidv = createSoftUIDValue();
			case WEAK ->
				uidv = createWeakUIDValue();
			default ->
				throw new AssertionError("undefined type : " + this);
		}

	}

	public <E extends Enum<E>> UIDCloneableObject(E id, Type t) {
		this.key = UIDKey.of(id);
		switch (t) {
			case SOFT ->
				uidv = createSoftUIDValue();
			case WEAK ->
				uidv = createWeakUIDValue();
			default ->
				throw new AssertionError("undefined type : " + this);
		}

	}

	public UIDCloneableObject(UIDKey id, Type t) {
		this.key = id;
		switch (t) {
			case SOFT ->
				uidv = createSoftUIDValue();
			case WEAK ->
				uidv = createWeakUIDValue();
			default ->
				throw new AssertionError("undefined type : " + this);
		}

	}

	public UIDCloneableObject(String id, Type t) {
		this.key = UIDKey.of(id);
		switch (t) {
			case SOFT ->
				uidv = createSoftUIDValue();
			case WEAK ->
				uidv = createWeakUIDValue();
			default ->
				throw new AssertionError("undefined type : " + this);
		}

	}

	public final void changeCacheTypeTo(Type t) {
		switch (t) {
			case SOFT ->
				uidv = createSoftUIDValue();
			case WEAK ->
				uidv = createWeakUIDValue();
			default ->
				throw new AssertionError("undefined type : " + this);
		}
	}

	protected UIDCloneableObject() {
	}

	protected void set(UIDKey key, Type t) {
		this.key = key;
		switch (t) {
			case SOFT ->
				uidv = createSoftUIDValue();
			case WEAK ->
				uidv = createWeakUIDValue();
			default ->
				throw new AssertionError("undefined type : " + this);
		}
	}

	@Override
	public final UIDKey getUIDKey() {
		return key;
	}

	@Override
	public final UIDValue getUIDValue() {
		return uidv;
	}

	@Override
	public UIDCloneableObject clone() {
		UIDCloneableObject r = (UIDCloneableObject) super.clone();
		r.key = UIDKey.of(this.key.getId() + "C");
		r.uidv.setKey(r.key);
		return r;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 59 * hash + Objects.hashCode(this.key);
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
		final UIDCloneableObject other = (UIDCloneableObject) obj;
		return true;
	}

}
