/*
 * Copyright (C) 2025 Shinacho
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
package kinugasa.system;

import java.util.Objects;
import kinugasa.game.annotation.CopyCtor;
import kinugasa.resource.ID;

/**
 *
 * @vesion 1.0.0 - 2025/02/09_17:51:20<br>
 * @author Shinacho<br>
 */
public class KeyAndValue<K extends ID, V> implements ID {

	private K key;
	private V value;

	@CopyCtor
	public KeyAndValue(KeyAndValue<K, V> v) {
		this(v.key, v.value);
	}

	public KeyAndValue(K key, V value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public String getId() {
		return key.getId();
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return key + "=" + value;
	}

	@Override
	public int hashCode() {
		int hash = 7;
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
		final KeyAndValue<?, ?> other = (KeyAndValue<?, ?>) obj;
		return Objects.equals(this.key, other.key);
	}

}
