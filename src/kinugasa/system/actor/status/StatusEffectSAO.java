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

import kinugasa.script.ScriptAccessMethod;

/**
 * StatusEffectSAO.<br>
 *
 * @vesion 1.0.0 - 2025/10/08_0:21:26<br>
 * @author Shinacho.<br>
 */
public class StatusEffectSAO extends StatusUpdateSAO {

	public StatusEffectSAO() {
	}

	private Status.Value value;
	private StatusFunction base, effect;

	public void setValue(Status.Value value) {
		this.value = value;
	}

	@Override
	protected void checkTgt() {
		super.checkTgt();
		if (value == null) {
			throw new IllegalStateException("AU_SAO : tgt is null");
		}
	}

	@ScriptAccessMethod
	public StatusFunction base() {
		if (base == null) {
			base = StatusFunction.base(getTgt());
		}
		return base;
	}

	@ScriptAccessMethod
	public StatusFunction effect() {
		if (effect == null) {
			effect = StatusFunction.effect(getTgt(), value);
		}
		return effect;
	}

}
