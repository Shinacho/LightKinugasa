/*
 * Copyright (C) 2024 Shinacho
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
