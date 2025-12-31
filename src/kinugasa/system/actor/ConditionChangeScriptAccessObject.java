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
package kinugasa.system.actor;

import kinugasa.system.actor.status.StatusEffect;
import kinugasa.system.actor.status.StatusUpdateSAO;
import kinugasa.script.ScriptAccessMethod;

/**
 * ConditionChangeScriptAccessObject.<br>
 *
 * @vesion 1.0.0 - 2025/10/03_22:47:06<br>
 * @author Shinacho.<br>
 */
public class ConditionChangeScriptAccessObject extends StatusUpdateSAO {

	//PARAM----------------------
	private StatusEffect<?> tgtCnd;
	//
	private boolean requireAdd;

	public ConditionChangeScriptAccessObject() {
	}

	public ConditionChangeScriptAccessObject setRequireAdd(boolean requireAdd) {
		this.requireAdd = requireAdd;
		return this;
	}

	public boolean isRequireAdd() {
		return requireAdd;
	}

	public ConditionChangeScriptAccessObject setTgtCnd(StatusEffect<?> tgtCnd) {
		this.tgtCnd = tgtCnd;
		return this;
	}

	@Override
	protected void checkTgt() {
		super.checkTgt();
		if (tgtCnd == null) {
			throw new IllegalStateException("CC_SAO : tgtCND is null");
		}

	}

	//------------------------------------------------------------------SAM-------------------------------------------------------
	//COMMON
	@ScriptAccessMethod
	public StatusEffect<?> getTgtCnd() {
		checkTgt();
		return tgtCnd;
	}

}
