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

import java.io.File;
import kinugasa.resource.ID;
import kinugasa.script.ScriptFile;
import kinugasa.system.actor.Actor;
import kinugasa.system.actor.status.StatusEffect;
import kinugasa.system.actor.status.ScriptedStatusEffect;
import kinugasa.system.actor.status.StatusEffectable;

/**
 * Condition.<br>
 *
 * @vesion 1.0.0 - 2025/10/05_22:01:06<br>
 * @author Shinacho.<br>
 */
public class Condition extends ScriptFile implements ID, StatusEffectable<Condition> {

	public Condition(File file) {
		super(file);
	}

	@Override
	public StatusEffect<Condition> createEffect(Actor tgt, StatusEffect.ExpireMode eMode, int time, StatusEffect.ManualCountMode mMode, int count) {
		return new ScriptedStatusEffect<>(this, tgt, eMode, time, mMode, count);
	}

}
