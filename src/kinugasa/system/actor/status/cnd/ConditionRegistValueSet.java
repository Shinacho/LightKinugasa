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
package kinugasa.system.actor.status.cnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kinugasa.game.annotation.VariableResult;
import kinugasa.object.CloneableObject;
import kinugasa.system.actor.status.StatusValueChance;
import kinugasa.system.actor.status.StatusValueMul;
import kinugasa.util.KRandom;

/**
 * ConditionRegistKey.<br>
 *
 * @vesion 1.0.0 - 2025/10/05_16:16:14<br>
 * @author Shinacho.<br>
 */
public class ConditionRegistValueSet extends CloneableObject {

	private Map<Condition, Float> map = new HashMap<>();

	public ConditionRegistValueSet() {
	}

	public ConditionRegistValueSet add(ConditionRegistValueSet v) {
		for (var vv : v.map.entrySet()) {
			this.add(vv.getKey(), vv.getValue());
		}
		return this;
	}

	public ConditionRegistValueSet set(Map<Condition, Float> map) {
		for (var v : map.entrySet()) {
			set(v.getKey(), v.getValue());
		}
		return this;
	}

	public ConditionRegistValueSet set(Condition c, float val) {
		map.put(c, val);
		return this;
	}

	public ConditionRegistValueSet add(Condition c, float val) {
		if (map.containsKey(c)) {
			val = map.get(c) + val;
		}
		map.put(c, val);
		return this;
	}

	public ConditionRegistValueSet mul(Condition c, float val) {
		if (map.containsKey(c)) {
			val = map.get(c) * val;
		}
		map.put(c, val);
		return this;
	}

	public ConditionRegistValueSet set(Condition c, StatusValueMul val) {
		return set(c, val.getValue());
	}

	public ConditionRegistValueSet add(Condition c, StatusValueMul val) {
		return add(c, val.getValue());
	}

	public ConditionRegistValueSet mul(Condition c, StatusValueMul val) {
		return mul(c, val.getValue());
	}

	public ConditionRegistValueSet set(Condition c, StatusValueChance val) {
		return set(c, val.getValue());
	}

	public ConditionRegistValueSet add(Condition c, StatusValueChance val) {
		return add(c, val.getValue());
	}

	public ConditionRegistValueSet mul(Condition c, StatusValueChance val) {
		return set(c, val.getValue());
	}

	public ConditionRegistValueSet remove(Condition c) {
		map.remove(c);
		return this;
	}

	public ConditionRegistValueSet toZero(Condition c) {
		return set(c, 0f);
	}

	public ConditionRegistValueSet to1(Condition c) {
		return remove(c);
	}

	public ConditionRegistValueSet clear() {
		map.clear();
		return this;
	}

	public boolean has(Condition c) {
		return map.containsKey(c);
	}

	public static final record Value(Condition cnd, float value) {

	}

	public List<Value> getAll() {
		List<Value> r = new ArrayList<>();
		for (var v : ConditionSystem.getInstance().getVisible()) {
			if (!has(v)) {
				r.add(new Value(v, 1f));
			} else {
				r.add(new Value(v, map.get(v)));
			}
		}
		for (var v : map.entrySet()) {
			if (!ConditionSystem.getInstance().getVisible().contains(v.getKey())) {
				r.add(new Value(v.getKey(), v.getValue()));
			}
		}
		return r;
	}

	public Value get(Condition c) {
		if (map.containsKey(c)) {
			return new Value(c, map.get(c));
		}
		return new Value(c, 1f);
	}

	public float getValue(Condition c) {
		if (map.containsKey(c)) {
			float val = map.get(c);
			if (val == 1f) {
				remove(c);
			}
			return val;
		}
		return 1f;
	}

	@VariableResult
	public boolean test(Condition c) {
		return KRandom.percent(getValue(c));
	}

	@VariableResult
	public boolean test(Condition c, float baseChance) {
		return KRandom.percent(getValue(c) * baseChance);
	}

	@VariableResult
	public boolean test(Condition c, StatusValueMul base) {
		return test(c, base.getValue());
	}

	@VariableResult
	public boolean test(Condition c, StatusValueChance base) {
		return test(c, base.getValue());
	}

	@VariableResult
	public boolean testAndExec(Condition c, Runnable r1) {
		if (test(c)) {
			r1.run();
			return true;
		}
		return false;
	}

	@VariableResult
	public boolean testAndExec(Condition c, float baseChance, Runnable r1) {
		if (test(c, baseChance)) {
			r1.run();
			return true;
		}
		return false;
	}

	@VariableResult
	public boolean testAndExec(Condition c, StatusValueMul base, Runnable r1) {
		if (test(c, base)) {
			r1.run();
			return true;
		}
		return false;
	}

	@VariableResult
	public boolean testAndExec(Condition c, StatusValueChance base, Runnable r1) {
		if (test(c, base)) {
			r1.run();
			return true;
		}
		return false;
	}

	@VariableResult
	public boolean testAndExecOrElse(Condition c, Runnable r1, Runnable r2) {
		if (test(c)) {
			r1.run();
			return true;
		}
		r2.run();
		return false;
	}

	@VariableResult
	public boolean testAndExecOrElse(Condition c, float baseChance, Runnable r1, Runnable r2) {
		if (test(c, baseChance)) {
			r1.run();
			return true;
		}
		r2.run();
		return false;
	}

	@VariableResult
	public boolean testAndExecOrElse(Condition c, StatusValueMul base, Runnable r1, Runnable r2) {
		if (test(c, base)) {
			r1.run();
			return true;
		}
		r2.run();
		return false;
	}

	@VariableResult
	public boolean testAndExecOrElse(Condition c, StatusValueChance base, Runnable r1, Runnable r2) {
		if (test(c, base)) {
			r1.run();
			return true;
		}
		r2.run();
		return false;
	}

	@Override
	public ConditionRegistValueSet clone() {
		var r = (ConditionRegistValueSet) super.clone();
		r.map = new HashMap<>();
		for (var v : this.map.entrySet()) {
			r.map.put(v.getKey(), v.getValue());
		}
		return r;
	}

	@Override
	public String toString() {
		return "ConditionRegist{" + "map=" + map + '}';
	}

}
