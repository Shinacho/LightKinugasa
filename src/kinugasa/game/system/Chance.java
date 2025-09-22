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
package kinugasa.game.system;

import kinugasa.game.annotation.Immutable;
import kinugasa.game.annotation.NewInstance;
import kinugasa.game.annotation.VariableResult;
import kinugasa.object.CloneableObject;
import kinugasa.util.Random;
import kinugasa.util.StringUtil;

/**
 *
 * @vesion 1.0.0 - 2025/01/10_20:52:20<br>
 * @author Shinacho<br>
 */
@Immutable
public class Chance extends CloneableObject {

	public static final Chance ZERO = new Chance(0f);
	public static final Chance ONE = new Chance(1f);

	private float p;

	public Chance(Chance c1, Chance c2) {
		this.p = c1.p + c2.p;
		if (p > 1f) {
			p = 1f;
		}
	}

	public Chance(float p) {
		this.p = p;
	}

	public boolean isActive() {
		return p > 0f;
	}

	public boolean isNotActive() {
		return p <= 0f;
	}

	@VariableResult
	public boolean test() {
		return Random.percent(p);
	}

	public float getValue() {
		return p;
	}

	@NewInstance
	public Chance add(Chance c) {
		return new Chance(p + c.p);
	}

	@NewInstance
	public Chance mul(float c) {
		return new Chance(p * c);
	}

	@NewInstance
	public Chance mul(MulValue c) {
		return new Chance((float) c.mul(p));
	}

	@Override
	public String toString() {
		return StringUtil.toPercent(p) + "%";
	}

	@Override
	public Chance clone() {
		return (Chance) super.clone();
	}

}
