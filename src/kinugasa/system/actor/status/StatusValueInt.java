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

import java.util.Objects;
import kinugasa.game.VisibleNameSupport;
import kinugasa.object.CloneableObject;

/**
 * IntStatusKeyAndValue.<br>
 *
 * @vesion 1.0.0 - 2025/10/05_14:46:37<br>
 * @author Shinacho.<br>
 */
public class StatusValueInt extends StatusValue<StatusKey> {

	public static class Limit extends CloneableObject {

		private int min, max;

		public Limit(int min, int max) {
			this.min = min;
			this.max = max;
		}

		public int getMax() {
			return max;
		}

		public int getMin() {
			return min;
		}

		@Override
		public Limit clone() {
			return (Limit) super.clone();
		}

		@Override
		public String toString() {
			return "Limit{" + "min=" + min + ", max=" + max + '}';
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 37 * hash + this.min;
			hash = 37 * hash + this.max;
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
			final Limit other = (Limit) obj;
			if (this.min != other.min) {
				return false;
			}
			return this.max == other.max;
		}

	}
	private final Limit keyLimit;
	private Limit personalLimit;
	private int value;
	private boolean autoTrim = true;

	public StatusValueInt(StatusKey key, Limit keyLimit, Limit personalLimit, int value) {
		super(key);
		this.keyLimit = keyLimit;
		this.personalLimit = personalLimit;
		this.value = value;
	}

	public StatusValueInt setAutoTrim(boolean autoTrim) {
		this.autoTrim = autoTrim;
		return this;
	}

	public StatusValueInt setPersonalLimitToKeyLimit() {
		this.personalLimit = keyLimit.clone();
		return this;
	}

	public Limit getKeyLimit() {
		return keyLimit;
	}

	public Limit getPersonalLimit() {
		return personalLimit;
	}

	public int getValue() {
		return value;
	}

	private int trimToKey(int v) {
		if (v > keyLimit.max) {
			v = keyLimit.max;
		}
		if (v < keyLimit.min) {
			v = keyLimit.min;
		}
		return v;
	}

	public StatusValueInt trim() {
		if (value > personalLimit.max) {
			value = personalLimit.max;
		}
		if (value < personalLimit.min) {
			value = personalLimit.min;
		}
		return this;
	}

	public StatusValueInt set(StatusValueInt v) {
		if (key != v.key) {
			throw new IllegalArgumentException("StatusValueInt : key missmatch : " + this + " / " + v);
		}
		this.personalLimit = v.personalLimit.clone();
		return setValue(v);
	}

	public StatusValueInt setValue(int v) {
		value = trimToKey(v);
		if (autoTrim) {
			trim();
		}
		return this;
	}

	public StatusValueInt setValue(StatusValueInt v) {
		return setValue(v.value);
	}

	public StatusValueInt addValue(float v) {
		return setValue((int) (value + v));
	}

	public StatusValueInt addValue(int v) {
		return setValue(value + v);
	}

	public StatusValueInt addValue(StatusValueInt v) {
		return addValue(v.value);
	}

	public StatusValueInt mulValue(float v) {
		return setValue((int) (value * v));
	}

	public StatusValueInt mulValue(StatusValueMul v) {
		return mulValue(v.getValue());
	}

	public StatusValueInt setPersonalMax(int v) {
		personalLimit.max = trimToKey(v);
		if (autoTrim) {
			trim();
		}
		return this;
	}

	public StatusValueInt addPersonalMax(int v) {
		return setPersonalMax(personalLimit.max + v);
	}

	public StatusValueInt setPersonalMin(int v) {
		personalLimit.min = trimToKey(v);
		if (autoTrim) {
			trim();
		}
		return this;
	}

	public StatusValueInt addPersonalMin(int v) {
		return setPersonalMin(personalLimit.min + v);
	}

	public StatusValueInt setPersonalMaxAndRecalc(int v) {
		float c = perPersonalMax();
		setPersonalMax(v);
		int newVal = (int) (personalLimit.max * c);
		return setValue(newVal);
	}

	public StatusValueInt addPersonalMaxAndRecalc(int v) {
		float c = perPersonalMax();
		addPersonalMax(v);
		int newVal = (int) (personalLimit.max * c);
		return setValue(newVal);
	}

	public StatusValueInt toMax() {
		return setValue(personalLimit.max);
	}

	public StatusValueInt toMin() {
		return setValue(personalLimit.min);
	}

	public StatusValueInt toKeyMax() {
		return setValue(keyLimit.max);
	}

	public StatusValueInt toKeyMin() {
		return setValue(keyLimit.min);
	}

	public StatusValue toZero() {
		value = 0;
		return this;
	}

	public float perPersonalMax() {
		return value / personalLimit.max;
	}

	public float perKeyMax() {
		return value / keyLimit.max;
	}

	public boolean isMin() {
		return value <= personalLimit.min;
	}

	public boolean isKeyMin() {
		return value <= keyLimit.min;
	}

	public boolean isMax() {
		return value >= personalLimit.max;
	}

	public boolean isKeyMax() {
		return value >= keyLimit.max;
	}

	public boolean isZero() {
		return value == 0;
	}

	public boolean isPlus() {
		return value > 0;
	}

	public boolean isMinus() {
		return value < 0;
	}

	public boolean isZeroOrMinus() {
		return value <= 0;
	}

	@Override
	public StatusValueInt clone() {
		var r = (StatusValueInt) super.clone();
		r.personalLimit = this.personalLimit.clone();
		return r;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 61 * hash + Objects.hashCode(this.keyLimit);
		hash = 61 * hash + Objects.hashCode(this.personalLimit);
		hash = 61 * hash + this.value;
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
		final StatusValueInt other = (StatusValueInt) obj;
		if (this.value != other.value) {
			return false;
		}
		if (!Objects.equals(this.keyLimit, other.keyLimit)) {
			return false;
		}
		return Objects.equals(this.personalLimit, other.personalLimit);
	}

}
