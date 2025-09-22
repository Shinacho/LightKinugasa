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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kinugasa.game.annotation.Immutable;
import kinugasa.game.annotation.NewInstance;
import kinugasa.game.system.UniversalValue;

/**
 * ScriptArgs.<br>
 *
 * @vesion 1.0.0 - 2025/08/10_0:41:34<br>
 * @author Shinacho.<br>
 */
@Immutable
public class ScriptArgs {

	private final Map<String, UniversalValue> paramMap;
	private final List<UniversalValue> paramOrigin;

	public ScriptArgs(Map<String, UniversalValue> paramMap, List<UniversalValue> paramOrigin) {
		this.paramMap = paramMap;
		this.paramOrigin = paramOrigin;
	}

	@NewInstance
	public Map<String, UniversalValue> getMap() {
		return new HashMap<>(paramMap);
	}

	@NewInstance
	public List<UniversalValue> getOrigin() {
		return new ArrayList<>(paramOrigin);
	}

	@Override
	public String toString() {
		return "ScriptActualParam{" + "map=" + paramMap + ", origin=" + paramOrigin + '}';
	}

}
