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
import kinugasa.game.I18NText;
import kinugasa.game.annotation.NeedSuperCallWhenOverride;
import kinugasa.game.annotation.NotNull;
import kinugasa.script.ScriptAccessMethod;
import kinugasa.script.ScriptAccessObject;
import kinugasa.system.UniversalValue;
import kinugasa.system.actor.Actor;
import kinugasa.system.actor.status.cnd.ConditionSystem;

/**
 * ActorUpdateScriptAccessObjectはターンの開始などで起動する、おもにEffectを追加するためのSAOです.<br>
 *
 * @vesion 1.0.0 - 2025/10/04_1:41:50<br>
 * @author Shinacho.<br>
 */
public class StatusUpdateSAO extends ScriptAccessObject {

	@NotNull
	private Actor tgt;
	private boolean canExec = true;
	private List<I18NText> msg = new ArrayList<>();

	public StatusUpdateSAO() {
	}

	public void setTgt(Actor tgt) {
		this.tgt = tgt;
	}

	@NeedSuperCallWhenOverride
	protected void checkTgt() {
		if (tgt == null) {
			throw new IllegalStateException("AU_SAO : tgt is null");
		}
	}

	public boolean isCanExec() {
		return canExec;
	}

	public List<I18NText> getMsg() {
		return msg;
	}

	//COMMON
	@ScriptAccessMethod
	public void ok() {
		canExec = true;
	}

	@ScriptAccessMethod
	public void ng() {
		canExec = false;
	}

	@ScriptAccessMethod
	public Actor getTgt() {
		checkTgt();
		return tgt;
	}

	@ScriptAccessMethod
	public StatusUpdateSAO addMsg(UniversalValue i18n) {
		this.msg.add(i18n.asI18N());
		return this;
	}

	@ScriptAccessMethod
	public StatusUpdateSAO br() {
		this.msg.add(I18NText.BR);
		return this;
	}

	@ScriptAccessMethod
	public StatusUpdateSAO indent(UniversalValue v) {
		this.msg.add(I18NText.indent(v.asInt()));
		return this;
	}

	protected boolean addEffect(StatusEffect<?> e) {
		checkTgt();
		if (getTgt().getStatus().getCurrentEffect().has(e)) {
			return false;
		}
		getTgt().getStatus().getCurrentEffect().add(e);
		return true;
	}

	protected boolean removeEffect(StatusEffect<?> e) {
		checkTgt();
		if (!getTgt().getStatus().getCurrentEffect().has(e)) {
			return false;
		}
		getTgt().getStatus().getCurrentEffect().remove(e);
		return true;
	}

	@ScriptAccessMethod
	public boolean hasCondition(UniversalValue id) {
		checkTgt();
		return getTgt().getStatus().getCurrentEffect().hasCondition(id.asCondition());
	}

	//CONDITION
	@ScriptAccessMethod
	public void addConditionNow(UniversalValue key) {
		checkTgt();
		getTgt().getStatus().getCurrentEffect().add(ConditionSystem.getInstance().of(key.getId()).createEffect(tgt));
	}

	@ScriptAccessMethod
	public void addConditionNow(UniversalValue key, UniversalValue mode, UniversalValue timeInt) {
		checkTgt();
		StatusEffect.ExpireMode m = mode.of(StatusEffect.ExpireMode.class);
		int time = timeInt.asInt();
		getTgt().getStatus().getCurrentEffect().add(ConditionSystem.getInstance().of(key.getId()).createEffect(tgt, m, time));
	}

}
