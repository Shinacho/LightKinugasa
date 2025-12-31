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
package kinugasa.system.actor.status.attr;

import kinugasa.system.actor.status.StatusValue;
import kinugasa.system.actor.status.StatusValueMul;
import kinugasa.util.StringUtil;

/**
 * AttributeValue.<br>
 *
 * @vesion 1.0.0 - 2025/10/05_16:14:00<br>
 * @author Shinacho.<br>
 */
public class AttributeValue extends StatusValue<AttributeKey> {

	private float value;

	public AttributeValue(AttributeKey key, float value) {
		super(key);
		this.value = value;
	}

	public float getValue() {
		return value;
	}

	public int exec(int i) {
		return (int) (i * value);
	}

	public float exec(float i) {
		return i * value;
	}

	public AttributeValue set(float v) {
		this.value = v;
		return this;
	}

	public AttributeValue set(AttributeValue v) {
		return set(v.value);
	}

	public AttributeValue add(float v) {
		this.value += v;
		return this;
	}

	public AttributeValue add(AttributeValue v) {
		return add(v.value);
	}

	public AttributeValue mul(float v) {
		this.value *= v;
		return this;
	}

	public AttributeValue mul(StatusValueMul v) {
		return mul(v.getValue());
	}

	public AttributeValue toZero() {
		return set(0f);
	}

	public AttributeValue to1() {
		return set(1f);
	}

	@Override
	public String toString() {
		return StringUtil.toPercent(value) + "%";
	}

	@Override
	public AttributeValue clone() {
		return (AttributeValue) super.clone();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + Float.floatToIntBits(this.value);
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
		final AttributeValue other = (AttributeValue) obj;
		return Float.floatToIntBits(this.value) == Float.floatToIntBits(other.value);
	}

}
