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
package kinugasa.system.actor.status;

import kinugasa.game.VisibleNameSupport;

/**
 * EnumValue.<br>
 *
 * @vesion 1.0.0 - 2025/10/05_13:51:39<br>
 * @author Shinacho.<br>
 */
public class StatusValueEnum<V extends Enum<V>> extends StatusValue<StatusKey> {

	private V value;

	public StatusValueEnum(StatusKey key, V value) {
		super(key);
		this.value = value;
	}

	public boolean is(V e) {
		return value == e;
	}

	public V getValue() {
		return value;
	}

	public void set(V value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public StatusValueEnum< V> clone() {
		return (StatusValueEnum<V>) super.clone();
	}

}
