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

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.function.Supplier;
import kinugasa.game.Immutable;
import kinugasa.game.NotNull;
import kinugasa.game.Nullable;

/**
 *
 * @vesion 1.0.0 - 2024/04/26_20:12:43<br>
 * @author Shinacho<br>
 */
@Immutable
public final class UIDValue implements UIDSupport {

	private UIDKey key;
	private Reference<Object> value;

	private UIDValue() {
	}

	public static UIDValue weakOf(String key, Object value) {
		UIDValue v = new UIDValue();
		v.key = UIDKey.of(key);
		v.value = new WeakReference<>(value);
		UIDSystem.getInstance().add(v);
		return v;
	}

	public static UIDValue softOf(String key, Object value) {
		UIDValue v = new UIDValue();
		v.key = UIDKey.of(key);
		v.value = new SoftReference<>(value);
		UIDSystem.getInstance().add(v);
		return v;
	}

	public static UIDValue weakOf(UIDKey key, Object value) {
		UIDValue v = new UIDValue();
		v.key = key;
		v.value = new WeakReference<>(value);
		UIDSystem.getInstance().add(v);
		return v;
	}

	public static UIDValue softOf(UIDKey key, Object value) {
		UIDValue v = new UIDValue();
		v.key = key;
		v.value = new SoftReference<>(value);
		UIDSystem.getInstance().add(v);
		return v;
	}

	@Override
	public String getId() {
		return key.getId();
	}

	public UIDKey getKey() {
		return key;
	}

	public boolean isSoft() {
		return value instanceof SoftReference<?>;
	}

	public boolean isWeak() {
		return value instanceof WeakReference<?>;
	}

	void setKey(UIDKey k) {
		this.key = k;
	}

	public boolean has() {
		return value.get() != null;
	}

	@NotNull
	public <T extends UIDSupport> T as(Class<T> e) throws ClassCastException, UIDReferenceNullException {
		Object v = value.get();
		if (v == null) {
			UIDSystem.getInstance().remove(key);
			throw new UIDReferenceNullException("uid : reference is removed");
		}
		try {
			return e.cast(v);
		} catch (ClassCastException ex) {
			throw ex;
		}
	}

	public boolean is(Class<? extends UIDSupport> c) {
		if (!has()) {
			return false;
		}
		try {
			as(c);
			return true;
		} catch (ClassCastException e) {
		}
		return false;
	}

	@Nullable
	public <T> T asTorNull(Class<T> e) {
		Object v = value.get();
		if (v == null) {
			UIDSystem.getInstance().remove(key);
			return null;
		}
		try {
			return e.cast(v);
		} catch (ClassCastException ex) {
			return null;
		}
	}

	public <T> T orElse(Class<T> e, Supplier<? extends T> r) {
		Object v = value.get();
		if (v == null) {
			UIDSystem.getInstance().remove(key);
			T t = r.get();
			UIDSystem.getInstance().add(weakOf(key, t));
			return t;
		}
		try {
			return e.cast(v);
		} catch (ClassCastException ex) {
			return r.get();
		}
	}

	@Override
	public String toString() {
		return "UIDValue{" + "key=" + key + ", value=" + value.get() == null ? "null" : value.get().toString() + '}';
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + Objects.hashCode(this.key);
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
		final UIDValue other = (UIDValue) obj;
		return Objects.equals(this.key, other.key);
	}

	@Override
	public UIDKey getUIDKey() {
		return key;
	}

	@Override
	public UIDValue getUIDValue() {
		return this;
	}

}
