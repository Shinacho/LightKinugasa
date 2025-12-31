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

import kinugasa.util.StringUtil;

/**
 *
 * @vesion 1.0.0 - 2025/01/10_21:41:24<br>
 * @author Shinacho<br>
 */
public class StatusValueMul extends StatusValue<StatusKey> {

	private float value;

	public StatusValueMul(StatusKey key, float value) {
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

	public StatusValueMul set(float v) {
		this.value = v;
		return this;
	}

	public StatusValueMul set(StatusValueMul v) {
		return set(v.value);
	}

	public StatusValueMul add(float v) {
		this.value += v;
		return this;
	}

	public StatusValueMul add(StatusValueMul v) {
		return add(v.value);
	}

	public StatusValueMul mul(float v) {
		this.value *= v;
		return this;
	}

	public StatusValueMul mul(StatusValueMul v) {
		return mul(v.value);
	}

	public StatusValueMul toZero() {
		return set(0f);
	}

	public StatusValueMul to1() {
		return set(1f);
	}

	@Override
	public String toString() {
		return StringUtil.toPercent(value) + "%";
	}

	@Override
	public StatusValueMul clone() {
		return (StatusValueMul) super.clone();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 37 * hash + Float.floatToIntBits(this.value);
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
		final StatusValueMul other = (StatusValueMul) obj;
		return Float.floatToIntBits(this.value) == Float.floatToIntBits(other.value);
	}

}
