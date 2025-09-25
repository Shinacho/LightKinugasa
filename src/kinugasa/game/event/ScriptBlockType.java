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
package kinugasa.game.event;

/**
 * ScriptBlockType.<br>
 *
 * @vesion 1.0.0 - 2025/08/01_12:41:12<br>
 * @author Shinacho.<br>
 */
public enum ScriptBlockType {
	MAIN,
	STEP_ON,
	STEP_DOWN,
	APPROACH,
	LEAVE,
	TOUCH,
	TALK,
	MANUAL;

	public static boolean has(String n) {
		for (var v : values()) {
			if (n.toUpperCase().equals(v.toString())) {
				return true;
			}
		}
		return false;
	}

}
