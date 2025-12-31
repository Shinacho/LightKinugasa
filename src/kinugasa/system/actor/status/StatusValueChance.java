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

import java.util.function.Consumer;
import kinugasa.game.VisibleNameSupport;
import kinugasa.game.annotation.VariableResult;
import kinugasa.util.KRandom;
import kinugasa.util.StringUtil;

/**
 *
 * @vesion 1.0.0 - 2025/01/10_20:52:20<br>
 * @author Shinacho<br>
 */
public class StatusValueChance extends StatusValue<StatusKey> {

	private float value;

	public StatusValueChance(StatusKey key, float p) {
		super(key);
		this.value = p;
	}

	public float getValue() {
		return value;
	}

	public boolean isActive() {
		return value > 0f;
	}

	public boolean isNotActive() {
		return value <= 0f;
	}

	@VariableResult
	public boolean test() {
		return KRandom.percent(value);
	}

	@VariableResult
	public void testAndExec(Runnable r) {
		if (test()) {
			r.run();
		}
	}

	@VariableResult
	public void testAndExecOrElse(Runnable r1, Runnable r2) {
		if (test()) {
			r1.run();
		} else {
			r2.run();
		}
	}

	public StatusValueChance set(float p) {
		value = p;
		return this;
	}

	public StatusValueChance set(StatusValueChance c) {
		return set(c.value);
	}

	public StatusValueChance add(float p) {
		value += p;
		return this;
	}

	public StatusValueChance add(StatusValueChance c) {
		return add(c.value);
	}

	public StatusValueChance mul(float c) {
		value *= c;
		return this;
	}

	public StatusValueChance mul(StatusValueMul c) {
		return mul(c.getValue());
	}

	public StatusValueChance toZero() {
		return set(0f);
	}

	public StatusValueChance to1() {
		return set(1f);
	}

	@Override
	public String toString() {
		return StringUtil.toPercent(value) + "%";
	}

	@Override
	public StatusValueChance clone() {
		return (StatusValueChance) super.clone();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + Float.floatToIntBits(this.value);
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
		final StatusValueChance other = (StatusValueChance) obj;
		return Float.floatToIntBits(this.value) == Float.floatToIntBits(other.value);
	}

}
