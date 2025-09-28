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
package kinugasa.script;

import java.util.ArrayList;
import java.util.List;
import kinugasa.game.annotation.Nullable;

/**
 * ScriptResult.<br>
 *
 * @vesion 1.0.0 - 2025/09/27_22:30:36<br>
 * @author Shinacho.<br>
 */
public class ScriptResult {

	public static record Value(Object lastResultObject, @Nullable ScriptResultType type) {

	}
	@Nullable
	private ScriptBlock block;
	private final List<Value> results = new ArrayList<>();

	public ScriptResult(ScriptBlock block) {
		this.block = block;
	}

	ScriptResult add(Object result) {
		if(result instanceof Value v){
			results.add(v);
			return this;
		}
		Object lro = result;
		ScriptResultType t = result instanceof ScriptResultType ? (ScriptResultType) result : ScriptResultType.CONTINUE;
		this.results.add(new Value(lro, t));
		return this;
	}

	public Value getLast() {
		return results.isEmpty() ? null : results.get(results.size() - 1);
	}

	public ScriptBlock getBlock() {
		return block;
	}

	public ScriptResult free() {
		block.getScriptFile().free();
		return this;
	}

	@Override
	public String toString() {
		return "ScriptResult{" + "results=" + results + '}';
	}

}
