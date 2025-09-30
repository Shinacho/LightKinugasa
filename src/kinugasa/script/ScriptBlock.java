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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import kinugasa.game.GameLog;
import static kinugasa.script.ScriptResultType.PAUSE;
import kinugasa.script.exception.ScriptSyntaxException;
import kinugasa.system.GameSystem;
import kinugasa.system.UniversalValue;
import kinugasa.resource.text.DataFile;
import kinugasa.util.StringUtil;

/**
 * ScriptBlock.<br>
 *
 * @vesion 1.0.0 - 2025/07/30_0:38:52<br>
 * @author Shinacho.<br>
 */
public class ScriptBlock {

	private final ScriptBlockType type;
	private final ScriptFile script;
	private final List<ScriptLine> lines;
	private ScriptAccessObject sao;
	private int currentIdx = 0;

	ScriptBlock(ScriptBlockType type, ScriptAccessObject sao, ScriptFile script, List<DataFile.Element> e) throws ScriptSyntaxException {
		this.type = type;
		this.script = script;
		this.lines = new ArrayList<>();
		this.sao = sao;
		parse(e);
	}

	private void parse(List<DataFile.Element> e) throws ScriptSyntaxException {
		if (e == null || e.isEmpty()) {
			return;
		}
		if (this.sao == null) {
			throw new ScriptSyntaxException("SB : SAO is null : " + type);
		}
		//IF - ENDIFのチェック
		{
			for (var v : e) {
				if (v.key.value().trim().toUpperCase().replaceAll(" ", "").startsWith("IF")) {
					if (!v.key.value().trim().toUpperCase().replaceAll(" ", "").startsWith("IF(")) {
						throw new ScriptSyntaxException("SB : IF '(' not found : " + this);
					}
				}
			}
			int ifCount = e.stream().filter(p -> p.key.value().trim().toUpperCase().replaceAll(" ", "").startsWith("IF(")).toList().size();
			int endIfCount = e.stream().filter(p -> p.key.value().trim().toUpperCase().replaceAll(" ", "").endsWith("ENDIF")).toList().size();
			if (ifCount != endIfCount) {
				throw new ScriptSyntaxException("SB : IF-ENDIF missmatch : " + this);
			}
		}

		//IF-STACKの生成とブロック内値の追加
		LinkedList<List<String>> ifStack = new LinkedList<>();//and/or [hoge().piyo()]
		for (var v : e) {
			String line = v.key.value().trim();
			if (line.endsWith(";")) {
				line = line.substring(0, line.length() - 1);
			}
			if (line.toUpperCase().replaceAll(" ", "").startsWith("IF(")) {
				//IFの内部を取得
				String ifInner = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")")).trim();

				if (ifInner.contains("&&")) {
					throw new ScriptSyntaxException("SB : cant use [&&], use nest if block : " + this);
				}
				ifStack.add(Arrays.asList(StringUtil.safeSplit(ifInner, "||")).stream().map(p -> p.trim()).toList());
				continue;
			}
			if (line.toUpperCase().replaceAll(" ", "").startsWith("ENDIF")) {
				ifStack.removeLast();
				continue;
			}
			line = v.key.value().trim();
			lines.add(new ScriptLine(this.sao, line, ifStack));
		}

		checkOpenMW();

	}

	public boolean isEmpty() {
		return lines == null || lines.isEmpty();
	}

	private void checkOpenMW() throws ScriptSyntaxException {
		//openMessageWindow - closeのチェック

		boolean open = lines.stream().flatMap(p -> p.getData().stream()).filter(p -> p.m.getName().contains("openMessageWindow")).count() > 0;
		boolean close = lines.stream().flatMap(p -> p.getData().stream()).filter(p -> p.m.getName().contains("closeMessageWindow")).count() > 0;

		if (open && !close || !open && close) {
			throw new ScriptSyntaxException("SB : openMW-closeMW missMatch " + this.script.getName() + "." + this.type);
		}

	}

	public ScriptResult exec() {
		return exec(List.of());
	}

	public ScriptResult exec(List<UniversalValue> args) {
		ScriptResult res = new ScriptResult(this);
		if (lines == null || lines.isEmpty()) {
			if (GameSystem.isDebugMode()) {
				GameLog.print("SB : EventScript [" + script.getName() + "].[" + type + "] is start : param=" + args);
				GameLog.print("SB : EventScript [" + script.getName() + "].[" + type + "] is end [empty] : param=" + args);
			}
			return res.add(ScriptResultType.END);
		}
		if (currentIdx >= lines.size()) {
			if (GameSystem.isDebugMode()) {
				GameLog.print("SB : EventScript [" + script.getName() + "].[" + type + "] is start : param=" + args);
				GameLog.print("SB : EventScript [" + script.getName() + "].[" + type + "] is end [current > content] : param=" + args);
			}
			return res.add(ScriptResultType.END);
		}

		if (GameSystem.isDebugMode()) {
			GameLog.print("SB : EventScript [" + script.getName() + "].[" + type + "] is start : param=" + args);
			GameLog.addIndent();
		}

		//名前-値のMap作成
		Map<String, UniversalValue> argsValMap = new HashMap<>();
		if (script.getParam() != null) {
			if (args.size() != script.getParam().size()) {
				throw new ScriptSyntaxException("SB : param count missmatch : expect=" + script.getParam() + " / actual=" + args);
			}
			for (int i = 0; i < script.getParam().size(); i++) {
				String name = script.getParam().get(i);
				UniversalValue value = args.get(i);
				argsValMap.put(name, value);
			}
		}

		//実行
		ScriptSystem.getInstance().setCurrentExecFile(script);
		ScriptSystem.getInstance().setCurrentExecBlock(this);
		ScriptSystem.getInstance().setCurrentArgs(args);
		ScriptSystem.getInstance().setCurrentArgsValMap(argsValMap);

		ScriptResult.Value last = null;
		ScriptSystem.getInstance().setLast(last);

		for (int i = currentIdx; i < lines.size(); i++, currentIdx = i) {
			ScriptSystem.getInstance().setCurrentBlockIdx(currentIdx);
			var v = lines.get(i);
			last = v.exec(argsValMap);
			res.add(last);
			ScriptSystem.getInstance().setLast(last);

			switch (last.type()) {
				case CONTINUE -> {
					//pauseModeの判定
					if (ScriptSystem.getInstance().isPauseMode() && last.type() != ScriptResultType.MISSFIRE) {
						if (GameSystem.isDebugMode()) {
							GameLog.removeIndent();
						}
						return res;
					}
					//マニュアルIDXモードの判定
					if (ScriptSystem.getInstance().isManualIdxMode()) {
						if (GameSystem.isDebugMode()) {
							GameLog.removeIndent();
						}
						return res;
					}
					//それ以外は次の処理へ
				}
				case MISSFIRE -> {
					//次の処理へ
				}
				case PAUSE -> {
					if (GameSystem.isDebugMode()) {
						GameLog.removeIndent();
					}
					return res;
				}
				case END -> {
					if (GameSystem.isDebugMode()) {
						GameLog.removeIndent();
					}
					currentIdx = 0;
					return res;
				}
				default ->
					throw new AssertionError(last.type().name());

			}
		}
		if (GameSystem.isDebugMode()) {
			GameLog.removeIndent();
			GameLog.print("SB : EventScript [" + script.getName() + "].[" + type + "] is ALL end : result=" + last);
		}
		ScriptSystem.getInstance().unsetScript();
		return res;
	}

	public void resetIdx() {
		currentIdx = 0;
	}

	public void setCurrentIdx(int currentIdx) {
		this.currentIdx = currentIdx;
	}

	public boolean hasNext() {
		return currentIdx < lines.size();
	}

	public void next() {
		currentIdx++;
	}

	public List<ScriptLine> getCmds() {
		return lines;
	}

	public ScriptBlockType getType() {
		return type;
	}

	public ScriptFile getScriptFile() {
		return script;
	}

	@Override
	public String toString() {
		return "ScriptBlock{" + "type=" + type + ", cmds=" + lines.size() + ", currentIdx=" + currentIdx + '}';
	}

}
