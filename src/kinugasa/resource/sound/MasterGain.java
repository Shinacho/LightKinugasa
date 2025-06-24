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
package kinugasa.resource.sound;

import kinugasa.game.Immutable;

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
