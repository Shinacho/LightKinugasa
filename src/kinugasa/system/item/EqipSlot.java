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
package kinugasa.system.item;

import kinugasa.game.EnumUtilInjector;
import kinugasa.game.VisibleNameEnumInjector;

/**
 * EqipSlot.<br>
 *
 * @vesion 1.0.0 - 2025/10/02_21:36:18<br>
 * @author Shinacho.<br>
 */
public enum EqipSlot implements VisibleNameEnumInjector<EqipSlot>, EnumUtilInjector<EqipSlot> {
	L_ARM,
	R_ARM,
	HEAD,
	INNER,
	BODY,
	LEG,
	ACCESSORIES,;

	public static boolean has(String s) {
		for (var v : values()) {
			if (v.toString().equals(s.toUpperCase())) {
				return true;
			}
		}
		return false;
	}
}
