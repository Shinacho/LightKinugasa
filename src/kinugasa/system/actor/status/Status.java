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

import java.util.EnumSet;
import java.util.Objects;
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.game.annotation.UnneedTranslate;
import kinugasa.object.CloneableObject;
import kinugasa.resource.ID;
import kinugasa.resource.IDImpl;
import kinugasa.system.actor.Actor;
import kinugasa.system.actor.status.cnd.Condition;
import kinugasa.system.actor.status.attr.AttributeValueSet;
import kinugasa.system.actor.status.cnd.ConditionRegistValueSet;
import kinugasa.system.actor.status.skill.SkillSet;
import kinugasa.system.dcs.StatusAutoConditionSystem;

/**
 * Status.<br>
 *
 * @vesion 1.0.0 - 2025/08/13_14:10:59<br>
 * @author Shinacho.<br>
 */
public class Status extends CloneableObject {

	@UnneedTranslate
	public static enum Type {
		STATUS,
		ATTR_IN,
		ATTR_OUT,
		CND_REGIST,
		EFFECT,;

	}

	public class Value extends CloneableObject {

		public final StatusValueSet statusValueSet;
		public final AttributeValueSet attrIn;
		public final AttributeValueSet attrOut;
		public final ConditionRegistValueSet cndRegist;

		public Value(StatusValueSet statusValueSet, AttributeValueSet attrIn, AttributeValueSet attrOut, ConditionRegistValueSet cndRegist) {
			this.statusValueSet = statusValueSet;
			this.attrIn = attrIn;
			this.attrOut = attrOut;
			this.cndRegist = cndRegist;
		}

		public Value(Value v) {
			this(v.statusValueSet.clone(), v.attrIn.clone(), v.attrOut.clone(), v.cndRegist.clone());
		}

		public void updateAutoCnd() {
			//自動付与CNDを設定する。
			StatusAutoConditionSystem.getInstance().addAutoCnd(Status.this);
		}


	}

	private final Actor actor;
	private StatusEffectSet effectSet;
	private SkillSet skillSet;

	//
	//BASE
	private Value base;
	private Value effected;
	//

	public void updateEffectedStatus() {
		effected = new Value(base);

		//set
		for (var v : effectSet.sorted()) {
			v.setEffectedStatus(effected);
		}
	}

	public Status(Actor a) {
		this.actor = a;
		this.effectSet = new StatusEffectSet(a);
		actor.getRace().getPassive().forEach(p -> effectSet.add(p.createEffect(actor)));
		{
			var s = new StatusValueSet();
			var in = new AttributeValueSet();
			var out = new AttributeValueSet();
			var cnd = new ConditionRegistValueSet();
			base = new Value(s, in, out, cnd);
			effected = new Value(s, in, out, cnd);
		}

	}

	public Status(Actor a, Value base) {
		this.actor = a;
		this.effectSet = new StatusEffectSet(a);
		actor.getRace().getPassive().forEach(p -> effectSet.add(p.createEffect(actor)));
		{
			this.base = new Value(base);
			effected = new Value(this.base);
		}

	}

	public Actor getActor() {
		return actor;
	}

	public StatusEffectSet getCurrentEffect() {
		return effectSet;
	}

	//基本は変更するな。
	@NotNewInstance
	public Status.Value getBase() {
		return base;
	}

	@NotNewInstance
	public Status.Value getEffected() {
		return effected;
	}

	public SkillSet getSkillSet() {
		return skillSet;
	}

	@Override
	public Status clone() {
		Status r = (Status) super.clone();
		r.base = new Value(this.base);
		r.effected = new Value(this.effected);
		r.effectSet = this.effectSet.clone();
		return r;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 13 * hash + Objects.hashCode(this.actor);
		hash = 13 * hash + Objects.hashCode(this.effectSet);
		hash = 13 * hash + Objects.hashCode(this.base);
		hash = 13 * hash + Objects.hashCode(this.effected);
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
		final Status other = (Status) obj;
		if (!Objects.equals(this.actor, other.actor)) {
			return false;
		}
		if (!Objects.equals(this.effectSet, other.effectSet)) {
			return false;
		}
		if (!Objects.equals(this.base, other.base)) {
			return false;
		}
		return Objects.equals(this.effected, other.effected);
	}

}
