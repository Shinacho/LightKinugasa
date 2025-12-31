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

import kinugasa.resource.ID;
import kinugasa.system.actor.Actor;

/**
 * ActorEffectable.<br>
 *
 * @vesion 1.0.0 - 2025/10/05_22:08:12<br>
 * @author Shinacho.<br>
 */
public interface StatusEffectable<T extends ID> {

	public StatusEffect<T> createEffect(Actor tgt, StatusEffect.ExpireMode eMode, int time, StatusEffect.ManualCountMode mMode, int count);

	public default StatusEffect<T> createEffect(Actor tgt) {
		return createEffect(tgt, StatusEffect.ExpireMode.NON, StatusEffect.TIME_NOT_USER);
	}

	public default StatusEffect<T> createEffect(Actor tgt, StatusEffect.ExpireMode mode, int time) {
		return createEffect(tgt, mode, time, StatusEffect.ManualCountMode.NON, StatusEffect.TIME_NOT_USER);
	}

	public default StatusEffect<T> createEffect(Actor tgt, StatusEffect.ManualCountMode mode, int count) {
		return createEffect(tgt, StatusEffect.ExpireMode.NON, StatusEffect.TIME_NOT_USER, mode, count);
	}

}
