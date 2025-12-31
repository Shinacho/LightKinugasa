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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import kinugasa.game.VisibleNameIDInjector;
import kinugasa.game.annotation.Virtual;
import kinugasa.resource.ID;
import kinugasa.system.actor.status.cnd.Condition;
import kinugasa.system.item.EqipSlot;

/**
 * Race.<br>
 *
 * @vesion 1.0.0 - 2025/10/02_21:38:43<br>
 * @author Shinacho.<br>
 */
public interface Race extends ID, VisibleNameIDInjector<Race> {

	@Virtual
	public default int getItemBagSize() {
		return 16;
	}

	@Virtual
	public default int getBookBagSize() {
		return 8;
	}

	@Virtual
	public default int getScriptBagSize() {
		return 3;
	}

	@Virtual
	public default EnumSet<EqipSlot> getSlot() {
		return EnumSet.allOf(EqipSlot.class);
	}

	@Virtual
	public default List<Condition> getPassive() {
		return List.of();
	}

}
