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
