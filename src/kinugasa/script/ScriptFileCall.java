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
import java.util.Map;
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.script.exception.ScriptSyntaxException;
import kinugasa.system.UniversalValue;
import kinugasa.util.StringUtil;

/**
 * ScriptCallは@Hoge(piyo).BLOCKの構文で記載されるスクリプトのファイル呼び出しです.<br>
 *
 * @vesion 1.0.0 - 2025/08/09_22:02:03<br>
 * @author Shinacho.<br>
 */
public class ScriptFileCall {

	private final String original;
	private String scriptName;
	private List<UniversalValue> args;
	private ScriptBlockType blockType;

	public ScriptFileCall(String original) {
		this.original = original;
		parse(original);
	}

	public ScriptFileCall(String original, String scriptName, List<UniversalValue> args, ScriptBlockType blockType) {
		this.original = original;
		this.scriptName = scriptName;
		this.args = args;
		this.blockType = blockType;
	}

	private void parse(String line) {
		//@以降を解析して実行する。#はコメントとする
		line = line.trim();
		if (!line.startsWith("@")) {
			throw new ScriptSyntaxException("SFC : [" + line + "] is not file call");
		}
		if (line.endsWith(";")) {
			line = line.substring(0, line.length() - 1);
		}

		this.scriptName = line.substring(1, line.indexOf("("));

		//param切り出し
		String[] paramNames = StringUtil.safeSplitBlock(line.substring(line.indexOf("(") + 1, line.lastIndexOf(")")), ',', '"', true);
		this.args = new ArrayList<>();
		for (var v : paramNames) {
			// "a" , b
			this.args.add(new UniversalValue(v.trim()));
		}

		//実行ブロック切り出し
		if (line.contains(".")) {
			this.blockType = ScriptBlockType.valueOf(line.substring(line.lastIndexOf(".") + 1).trim());
		} else {
			this.blockType = ScriptBlockType.MAIN;
		}

	}

	public ScriptBlock getBlock() {
		return ScriptSystem.getInstance().of(scriptName).getBlockOf(blockType);
	}

	public String getOriginal() {
		return original;
	}

	public String getScriptName() {
		return scriptName;
	}

	@NotNewInstance
	public List<UniversalValue> getParam() {
		return args;
	}

	public ScriptBlockType getBlockType() {
		return blockType;
	}

	public ScriptResult exec() {
		return exec(Map.of());
	}

	public ScriptResult exec(Map<String, UniversalValue> argsMap) {
		return exec(this.blockType, argsMap);
	}

	public ScriptResult exec(ScriptBlockType type) {
		return exec(type, Map.of());
	}

	public ScriptResult exec(ScriptBlockType type, Map<String, UniversalValue> argsMap) {
		//args再計算
		List<UniversalValue> newArgs = new ArrayList<>();
		for (var v : this.args) {
			if (v.startWith("\"")) {
				newArgs.add(v);
				continue;
			}
			String newValue = argsMap.get(v.value()).value();
			newArgs.add(new UniversalValue("\"" + newValue + "\""));
		}

		return getScriptFile().load().getBlockOf(type).exec(newArgs).free();
	}

	public ScriptFile getScriptFile() {
		return ScriptSystem.getInstance().of(scriptName);
	}

	@Override
	public String toString() {
		return this.original;
	}

}
