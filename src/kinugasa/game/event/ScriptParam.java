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

import kinugasa.game.annotation.Nullable;
import kinugasa.game.system.UniversalValue;

/**
 * ScriptParam.<br>
 *
 * @vesion 1.0.0 - 2025/08/09_23:55:21<br>
 * @author Shinacho.<br>
 */
public class ScriptParam {

	private final String name;
	@Nullable
	private UniversalValue defaultValue = null;

	public ScriptParam(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Param{" + "name=" + name + ", defaultValue=" + defaultValue + '}';
	}

	public String getName() {
		return name;
	}

	public UniversalValue getDefaultValue() {
		return defaultValue;
	}

	public boolean isOptional() {
		return defaultValue != null && !defaultValue.isEmpty();
	}

	void setDefaultValue(UniversalValue defaultValue) {
		this.defaultValue = defaultValue;
	}

}
