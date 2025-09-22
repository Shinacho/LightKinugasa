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
package kinugasa.game.field4;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import kinugasa.game.annotation.NewInstance;
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.game.event.ScriptCall;

/**
 * FieldEventScriptMap.<br>
 *
 * @vesion 1.0.0 - 2025/08/02_23:58:23<br>
 * @author Shinacho.<br>
 */
public class FieldEventScriptMap {

	private Map<D2Idx, ScriptCall> scriptMap;

	public FieldEventScriptMap() {
		scriptMap = new HashMap<>();
	}

	public boolean has(D2Idx i) {
		return scriptMap.containsKey(i);
	}

	public void add(D2Idx i, ScriptCall e) {
		scriptMap.put(i, e);
	}

	public ScriptCall get(D2Idx i) {
		return scriptMap.get(i);
	}

	@NotNewInstance
	public Collection<ScriptCall> values() {
		return scriptMap.values();
	}

	void free() {
		scriptMap.values().forEach(p -> p.getScriptFile().free());
		scriptMap.clear();
	}

	@NewInstance
	public Map<D2Idx, ScriptCall> all() {
		return new HashMap<>(scriptMap);
	}

	@Override
	public String toString() {
		return "FieldEventScriptMap{" + "scriptMap=" + scriptMap + '}';
	}

}
