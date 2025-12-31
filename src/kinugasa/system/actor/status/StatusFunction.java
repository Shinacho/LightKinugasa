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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import kinugasa.script.ScriptAccessMethod;
import kinugasa.system.UniversalValue;
import kinugasa.system.actor.Actor;
import kinugasa.system.actor.EqipAttr;
import kinugasa.system.actor.MagicAptitude;
import kinugasa.system.actor.status.cnd.Condition;
import kinugasa.system.actor.status.attr.AttributeKey;

/**
 * StatusFunction.<br>
 *
 * @vesion 1.0.0 - 2025/10/16_20:13:07<br>
 * @author Shinacho.<br>
 */
public class StatusFunction {

	private final Actor tgt;
	private final Status.Value value;
	private final List<Consumer<Status.Value>> list = new ArrayList<>();
	private final List<Consumer<Actor>> effectList = new ArrayList<>();

	private StatusFunction(Actor tgt, Status.Value value) {
		this.tgt = tgt;
		this.value = value;
	}

	public static StatusFunction base(Actor a) {
		return new StatusFunction(a, a.getStatus().getBase());
	}

	public static StatusFunction effect(Actor a, Status.Value val) {
		return new StatusFunction(a, val);
	}

	private void keyIsInt(StatusKey k) throws IllegalArgumentException {
		if (k.getType() != StatusKey.Type.INT) {
			throw new IllegalArgumentException("StatusFunction : status type missmatch : " + k);
		}
	}

	private void keyIsIntOrFloat(StatusKey k) throws IllegalArgumentException {
		if (k.getType() == StatusKey.Type.ENUM) {
			throw new IllegalArgumentException("StatusFunction : status type missmatch : " + k);
		}
	}

	private void keyIsEnum(StatusKey k, Class<?> e) throws IllegalArgumentException {
		if (k.getType() != StatusKey.Type.ENUM) {
			throw new IllegalArgumentException("StatusFunction : status type missmatch : " + k);
		}
		if (k == StatusKey.MGA) {
			if (e != MagicAptitude.class) {
				throw new IllegalArgumentException("StatusFunction : status type missmatch : " + k);
			}
		}
		if (k == StatusKey.EQA) {
			if (e != EqipAttr.class) {
				throw new IllegalArgumentException("StatusFunction : status type missmatch : " + k);
			}
		}
	}

	//------------------------------STATUS--------------------------------
	public StatusFunction addStatus(StatusKey sk, int val) throws IllegalArgumentException {
		keyIsIntOrFloat(sk);
		if (sk.getType() == StatusKey.Type.INT) {
			list.add((s) -> ((StatusValueInt) s.statusValueSet.of(sk)).addValue(val));
		}
		if (sk.getType() == StatusKey.Type.MUL) {
			list.add((s) -> ((StatusValueMul) s.statusValueSet.of(sk)).add(val));
		}
		if (sk.getType() == StatusKey.Type.CHANCE) {
			list.add((s) -> ((StatusValueChance) s.statusValueSet.of(sk)).add(val));
		}
		return this;
	}

	public StatusFunction addStatus(StatusKey sk, float val) throws IllegalArgumentException {
		keyIsIntOrFloat(sk);
		if (sk.getType() == StatusKey.Type.INT) {
			list.add((s) -> ((StatusValueInt) s.statusValueSet.of(sk)).addValue(val));
		}
		if (sk.getType() == StatusKey.Type.MUL) {
			list.add((s) -> ((StatusValueMul) s.statusValueSet.of(sk)).add(val));
		}
		if (sk.getType() == StatusKey.Type.CHANCE) {
			list.add((s) -> ((StatusValueChance) s.statusValueSet.of(sk)).add(val));
		}
		return this;
	}

	public StatusFunction addStatus(StatusKey sk, UniversalValue val) throws IllegalArgumentException {
		keyIsIntOrFloat(sk);
		if (sk.getType() == StatusKey.Type.INT) {
			list.add((s) -> ((StatusValueInt) s.statusValueSet.of(sk)).addValue(val.asInt()));
		}
		if (sk.getType() == StatusKey.Type.MUL) {
			list.add((s) -> ((StatusValueMul) s.statusValueSet.of(sk)).add(val.asFloat()));
		}
		if (sk.getType() == StatusKey.Type.CHANCE) {
			list.add((s) -> ((StatusValueChance) s.statusValueSet.of(sk)).add(val.asFloat()));
		}
		return this;
	}

	@ScriptAccessMethod
	public StatusFunction addStatus(UniversalValue key, UniversalValue val) throws IllegalArgumentException {
		StatusKey sk = key.asStatusKey();
		keyIsIntOrFloat(sk);
		if (sk.getType() == StatusKey.Type.INT) {
			list.add((s) -> ((StatusValueInt) s.statusValueSet.of(sk)).addValue(val.asInt()));
		}
		if (sk.getType() == StatusKey.Type.MUL) {
			list.add((s) -> ((StatusValueMul) s.statusValueSet.of(sk)).add(val.asFloat()));
		}
		if (sk.getType() == StatusKey.Type.CHANCE) {
			list.add((s) -> ((StatusValueChance) s.statusValueSet.of(sk)).add(val.asFloat()));
		}
		return this;
	}

	public StatusFunction mulStatus(StatusKey sk, float val) throws IllegalArgumentException {
		keyIsIntOrFloat(sk);
		if (sk.getType() == StatusKey.Type.INT) {
			list.add((s) -> ((StatusValueInt) s.statusValueSet.of(sk)).mulValue(val));
		}
		if (sk.getType() == StatusKey.Type.MUL) {
			list.add((s) -> ((StatusValueMul) s.statusValueSet.of(sk)).mul(val));
		}
		if (sk.getType() == StatusKey.Type.CHANCE) {
			list.add((s) -> ((StatusValueChance) s.statusValueSet.of(sk)).mul(val));
		}
		return this;
	}

	public StatusFunction mulStatus(StatusKey sk, UniversalValue val) throws IllegalArgumentException {
		keyIsIntOrFloat(sk);
		if (sk.getType() == StatusKey.Type.INT) {
			list.add((s) -> ((StatusValueInt) s.statusValueSet.of(sk)).mulValue(val.asFloat()));
		}
		if (sk.getType() == StatusKey.Type.MUL) {
			list.add((s) -> ((StatusValueMul) s.statusValueSet.of(sk)).mul(val.asFloat()));
		}
		if (sk.getType() == StatusKey.Type.CHANCE) {
			list.add((s) -> ((StatusValueChance) s.statusValueSet.of(sk)).mul(val.asFloat()));
		}
		return this;
	}

	@ScriptAccessMethod
	public StatusFunction mulStatus(UniversalValue key, UniversalValue val) throws IllegalArgumentException {
		StatusKey sk = key.asStatusKey();
		keyIsIntOrFloat(sk);
		if (sk.getType() == StatusKey.Type.INT) {
			list.add((s) -> ((StatusValueInt) s.statusValueSet.of(sk)).mulValue(val.asFloat()));
		}
		if (sk.getType() == StatusKey.Type.MUL) {
			list.add((s) -> ((StatusValueMul) s.statusValueSet.of(sk)).mul(val.asFloat()));
		}
		if (sk.getType() == StatusKey.Type.CHANCE) {
			list.add((s) -> ((StatusValueChance) s.statusValueSet.of(sk)).mul(val.asFloat()));
		}
		return this;
	}

	public StatusFunction setStatus(StatusKey sk, int val) throws IllegalArgumentException {
		keyIsIntOrFloat(sk);
		if (sk.getType() == StatusKey.Type.INT) {
			list.add((s) -> ((StatusValueInt) s.statusValueSet.of(sk)).setValue(val));
		}
		if (sk.getType() == StatusKey.Type.MUL) {
			list.add((s) -> ((StatusValueMul) s.statusValueSet.of(sk)).set(val));
		}
		if (sk.getType() == StatusKey.Type.CHANCE) {
			list.add((s) -> ((StatusValueChance) s.statusValueSet.of(sk)).set(val));
		}
		return this;
	}

	public StatusFunction setStatus(StatusKey sk, float val) throws IllegalArgumentException {
		keyIsIntOrFloat(sk);
		if (sk.getType() == StatusKey.Type.INT) {
			list.add((s) -> ((StatusValueInt) s.statusValueSet.of(sk)).setValue((int) val));
		}
		if (sk.getType() == StatusKey.Type.MUL) {
			list.add((s) -> ((StatusValueMul) s.statusValueSet.of(sk)).set(val));
		}
		if (sk.getType() == StatusKey.Type.CHANCE) {
			list.add((s) -> ((StatusValueChance) s.statusValueSet.of(sk)).set(val));
		}
		return this;
	}

	public StatusFunction setStatus(StatusKey sk, UniversalValue val) throws IllegalArgumentException {
		keyIsIntOrFloat(sk);
		if (sk.getType() == StatusKey.Type.INT) {
			list.add((s) -> ((StatusValueInt) s.statusValueSet.of(sk)).setValue(val.asInt()));
		}
		if (sk.getType() == StatusKey.Type.MUL) {
			list.add((s) -> ((StatusValueMul) s.statusValueSet.of(sk)).set(val.asFloat()));
		}
		if (sk.getType() == StatusKey.Type.CHANCE) {
			list.add((s) -> ((StatusValueChance) s.statusValueSet.of(sk)).set(val.asFloat()));
		}
		if (sk == StatusKey.MGA) {
			list.add((s) -> ((StatusValueEnum<MagicAptitude>) s.statusValueSet.of(sk)).set(val.asMagicAptitude()));
		}
		if (sk == StatusKey.EQA) {
			list.add((s) -> ((StatusValueEnum<EqipAttr>) s.statusValueSet.of(sk)).set(val.asEqipAttr()));
		}
		return this;
	}

	@ScriptAccessMethod
	public StatusFunction setStatus(UniversalValue key, UniversalValue val) throws IllegalArgumentException {
		StatusKey sk = key.asStatusKey();
		keyIsIntOrFloat(sk);
		if (sk.getType() == StatusKey.Type.INT) {
			list.add((s) -> ((StatusValueInt) s.statusValueSet.of(sk)).setValue(val.asInt()));
		}
		if (sk.getType() == StatusKey.Type.MUL) {
			list.add((s) -> ((StatusValueMul) s.statusValueSet.of(sk)).set(val.asFloat()));
		}
		if (sk.getType() == StatusKey.Type.CHANCE) {
			list.add((s) -> ((StatusValueChance) s.statusValueSet.of(sk)).set(val.asFloat()));
		}
		if (sk == StatusKey.MGA) {
			list.add((s) -> ((StatusValueEnum<MagicAptitude>) s.statusValueSet.of(sk)).set(val.asMagicAptitude()));
		}
		if (sk == StatusKey.EQA) {
			list.add((s) -> ((StatusValueEnum<EqipAttr>) s.statusValueSet.of(sk)).set(val.asEqipAttr()));
		}
		return this;
	}

	public StatusFunction setMGA(MagicAptitude a) throws IllegalArgumentException {
		list.add((s) -> s.statusValueSet.MGA.set(a));
		return this;
	}

	@ScriptAccessMethod
	public StatusFunction setMGA(UniversalValue a) throws IllegalArgumentException {
		list.add((s) -> s.statusValueSet.MGA.set(a.asMagicAptitude()));
		return this;
	}

	public StatusFunction setEQA(EqipAttr e) throws IllegalArgumentException {
		list.add((s) -> s.statusValueSet.EQA.set(e));
		return this;
	}

	@ScriptAccessMethod
	public StatusFunction setEQA(UniversalValue e) throws IllegalArgumentException {
		list.add((s) -> s.statusValueSet.EQA.set(e.asEqipAttr()));
		return this;
	}

	//体力がベース最大値の5%上がる
	public StatusFunction addStatusToBaseMaxOf(StatusKey tgt, StatusKey k, float mul) throws IllegalArgumentException {
		keyIsIntOrFloat(tgt);
		keyIsInt(k);
		int v = (int) (((StatusValueInt) this.tgt.getStatus().getBase().statusValueSet.of(k)).getPersonalLimit().getMax() * mul);
		return addStatus(tgt, v);
	}

	public StatusFunction setStatusToBaseMaxOf(StatusKey tgt, StatusKey k, float mul) throws IllegalArgumentException {
		keyIsIntOrFloat(tgt);
		keyIsInt(k);
		int v = (int) (((StatusValueInt) this.tgt.getStatus().getBase().statusValueSet.of(k)).getPersonalLimit().getMax() * mul);
		return setStatus(tgt, v);
	}

	@ScriptAccessMethod
	public StatusFunction addStatusToBaseMaxOf(UniversalValue tgt, UniversalValue k, UniversalValue mul) throws IllegalArgumentException {
		return addStatusToBaseMaxOf(tgt.asStatusKey(), k.asStatusKey(), mul.asFloat());
	}

	@ScriptAccessMethod
	public StatusFunction setStatusToBaseMaxOf(UniversalValue tgt, UniversalValue k, UniversalValue mul) throws IllegalArgumentException {
		return setStatusToBaseMaxOf(tgt.asStatusKey(), k.asStatusKey(), mul.asFloat());
	}

	//---------------------------------------------ATTR_IN---------------------------------------------
	public StatusFunction addAttrIn(AttributeKey ak, float val) {
		list.add((s) -> s.attrIn.of(ak).add(val));
		return this;
	}

	@ScriptAccessMethod
	public StatusFunction addAttrIn(UniversalValue ak, UniversalValue val) {
		list.add((s) -> s.attrIn.of(ak.asAttributeKey()).add(val.asFloat()));
		return this;
	}

	public StatusFunction mulAttrIn(AttributeKey ak, float val) {
		list.add((s) -> s.attrIn.of(ak).mul(val));
		return this;
	}

	@ScriptAccessMethod
	public StatusFunction mulAttrIn(UniversalValue ak, UniversalValue val) {
		list.add((s) -> s.attrIn.of(ak.asAttributeKey()).mul(val.asFloat()));
		return this;
	}

	public StatusFunction setAttrIn(AttributeKey ak, float val) {
		list.add((s) -> s.attrIn.of(ak).set(val));
		return this;
	}

	@ScriptAccessMethod
	public StatusFunction setAttrIn(UniversalValue ak, UniversalValue val) {
		list.add((s) -> s.attrIn.of(ak.asAttributeKey()).set(val.asFloat()));
		return this;
	}

	//---------------------------------------------ATTR_OUT---------------------------------------------
	public StatusFunction addAttrOut(AttributeKey ak, float val) {
		list.add((s) -> s.attrOut.of(ak).add(val));
		return this;
	}

	@ScriptAccessMethod
	public StatusFunction addAttrOut(UniversalValue ak, UniversalValue val) {
		list.add((s) -> s.attrOut.of(ak.asAttributeKey()).add(val.asFloat()));
		return this;
	}

	public StatusFunction mulAttrOut(AttributeKey ak, float val) {
		list.add((s) -> s.attrOut.of(ak).mul(val));
		return this;
	}

	@ScriptAccessMethod
	public StatusFunction mulAttrOut(UniversalValue ak, UniversalValue val) {
		list.add((s) -> s.attrOut.of(ak.asAttributeKey()).mul(val.asFloat()));
		return this;
	}

	public StatusFunction setAttrOut(AttributeKey ak, float val) {
		list.add((s) -> s.attrOut.of(ak).set(val));
		return this;
	}

	@ScriptAccessMethod
	public StatusFunction setAttrOut(UniversalValue ak, UniversalValue val) {
		list.add((s) -> s.attrOut.of(ak.asAttributeKey()).set(val.asFloat()));
		return this;
	}

	//---------------------------------------------CND_REGIST---------------------------------------------
	public StatusFunction addCndRegist(Condition c, float val) {
		list.add((s) -> s.cndRegist.add(c, val));
		return this;
	}

	@ScriptAccessMethod
	public StatusFunction addCndRegist(UniversalValue c, UniversalValue val) {
		list.add((s) -> s.cndRegist.add(c.asCondition(), val.asFloat()));
		return this;
	}

	public StatusFunction mulCndRegist(Condition c, float val) {
		list.add((s) -> s.cndRegist.mul(c, val));
		return this;
	}

	@ScriptAccessMethod
	public StatusFunction mulCndRegist(UniversalValue c, UniversalValue val) {
		list.add((s) -> s.cndRegist.mul(c.asCondition(), val.asFloat()));
		return this;
	}

	public StatusFunction setCndRegist(Condition c, float val) {
		list.add((s) -> s.cndRegist.set(c, val));
		return this;
	}

	@ScriptAccessMethod
	public StatusFunction setCndRegist(UniversalValue c, UniversalValue val) {
		list.add((s) -> s.cndRegist.set(c.asCondition(), val.asFloat()));
		return this;
	}

	//---------------------------------------------EFFECT---------------------------------------------
	public StatusFunction addEffect(StatusEffect<?> e) {
		effectList.add((a) -> a.getStatus().getCurrentEffect().add(e));
		return this;
	}

	public StatusFunction addCnd(Condition c) {
		effectList.add((a) -> a.getStatus().getCurrentEffect().add(c.createEffect(a)));
		return this;
	}

	public StatusFunction addCnd(Condition c, StatusEffect.ExpireMode mode, int time) {
		effectList.add((a) -> a.getStatus().getCurrentEffect().add(c.createEffect(a, mode, time)));
		return this;
	}

	@ScriptAccessMethod
	public StatusFunction addCnd(UniversalValue c) {
		return addCnd(c.asCondition());
	}

	@ScriptAccessMethod
	public StatusFunction addCnd(UniversalValue c, UniversalValue mode, UniversalValue time) {
		return addCnd(c.asCondition(), mode.of(StatusEffect.ExpireMode.class), time.asInt());
	}

	public StatusFunction removeEffect(StatusEffect<?> e) {
		effectList.add((a) -> a.getStatus().getCurrentEffect().remove(e));
		return this;
	}

	public StatusFunction removeCnd(Condition c) {
		effectList.add((a) -> a.getStatus().getCurrentEffect().remove(c));
		return this;
	}

	@ScriptAccessMethod
	public StatusFunction removeCnd(UniversalValue c) {
		return removeCnd(c.asCondition());
	}

	public void commit() {
		list.forEach(p -> p.accept(value));
	}

	@Override
	public String toString() {
		return "StatusFunction{" + "list=" + list + ", effectList=" + effectList + '}';
	}
}
