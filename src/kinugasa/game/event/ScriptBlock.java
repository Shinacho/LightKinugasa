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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import kinugasa.game.GameLog;
import static kinugasa.game.event.ScriptResultType.PAUSE;
import kinugasa.game.event.exception.EventScriptException;
import kinugasa.game.event.exception.EventScriptFormatException;
import kinugasa.game.event.exception.EventScriptRuntimeException;
import kinugasa.game.system.GameSystem;
import kinugasa.game.system.UniversalValue;
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
	private final List<ScriptLine> cmds;
	private int currentIdx = 0;

	ScriptBlock(ScriptBlockType type, ScriptFile script, List<DataFile.Element> e) throws EventScriptException {
		this.type = type;
		this.script = script;
		this.cmds = new ArrayList<>();
		parse(e);
	}

	private void parse(List<DataFile.Element> e) throws EventScriptException {
		if (e == null) {
			return;
		}
		//IF - ENDIFのチェック
		for (var v : e) {
			if (v.original().trim().toUpperCase().replaceAll(" ", "").startsWith("IF")) {
				if (!v.original().trim().toUpperCase().replaceAll(" ", "").startsWith("IF(")) {
					throw new EventScriptFormatException("SB : if '(' not found : " + this);
				}
			}
		}
		int ifCount = e.stream().filter(p -> p.original().trim().toUpperCase().replaceAll(" ", "").startsWith("IF(")).toList().size();
		int endIfCount = e.stream().filter(p -> p.original().trim().toUpperCase().replaceAll(" ", "").endsWith("ENDIF")).toList().size();
		if (ifCount != endIfCount) {
			throw new EventScriptFormatException("SB : if-endif missmatch : " + this);
		}
		LinkedList<List<String>> ifStack = new LinkedList<>();//and/or [hoge().piyo()]
		for (var v : e) {
			String line = v.original().trim();
			if (line.endsWith(";")) {
				line = line.substring(0, line.length() - 1);
			}
			if (line.toUpperCase().replaceAll(" ", "").startsWith("IF(")) {
				//IFの内部を取得
				String ifInner = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")")).trim();

				if (ifInner.contains("&&")) {
					throw new EventScriptFormatException("SB : cant use [&&], use nest if block : " + this);
				}
				ifStack.add(Arrays.asList(StringUtil.safeSplit(ifInner, "||")).stream().map(p -> p.trim()).toList());
				continue;
			}
			if (line.toUpperCase().replaceAll(" ", "").startsWith("ENDIF")) {
				ifStack.removeLast();
				continue;
			}
			line = v.original().trim().replaceAll(" ", "").replaceAll("\t", "");
			cmds.add(new ScriptLine(this, line, ifStack));
		}

		checkOpenMW();

	}

	public class Result {

		public final List<ScriptLine.Result> cmdResult;
		public final ScriptResultType lastResult;

		public Result(List<ScriptLine.Result> cmdResult, ScriptResultType lastResult) {
			this.cmdResult = cmdResult;
			this.lastResult = lastResult;
		}

		public void free() {
			ScriptBlock.this.script.free();
		}

		public Object lastResultObject() {
			return cmdResult.get(cmdResult.size() - 1).resultObject;
		}

	}

	public ScriptArgs createArgs(List<UniversalValue> param) {
		Map<String, UniversalValue> paramMap = new HashMap<>();

		int expectParamCount = script.getParam().size();
		int noOptionalParamCount = script.getParam().stream().filter(p -> !p.isOptional()).toList().size();
		int actualParamCount = param.size();
		if (expectParamCount != noOptionalParamCount) {
			if (actualParamCount < noOptionalParamCount || actualParamCount > expectParamCount) {
				throw new EventScriptRuntimeException("SB : param size missmatch : " + this);
			}
		}

		for (kinugasa.game.event.ScriptParam v : script.getParam()) {
			paramMap.put(v.getName(), v.getDefaultValue());
		}
		for (int i = 0, j = 0; i < script.getParam().size() && j < param.size(); i++) {
			ScriptParam p = script.getParam().get(i);
			String paramName = p.getName();
			UniversalValue v = param.get(j);
			if (v != null && !v.isEmpty()) {
				paramMap.put(paramName, v);
				j++;
			}
		}
		//param nullチェック
		for (var v : paramMap.entrySet()) {
			if (v.getValue() == null || v.getValue().isEmpty()) {
				GameLog.print("WARN : ScriptParam is empty : " + paramMap);
			}
		}

		return new ScriptArgs(paramMap, param);
	}

	public Result exec() {
		return exec(List.of());
	}

	public Result exec(List<UniversalValue> param) throws EventScriptException {
		if (cmds == null || cmds.isEmpty()) {
			if (GameSystem.isDebugMode()) {
				GameLog.print("SB : EventScript [" + script.getName() + "].[" + type + "] is start : param=" + param);
				GameLog.print("SB : EventScript [" + script.getName() + "].[" + type + "] is end [empty] : param=" + param);
			}
			return new Result(List.of(), ScriptResultType.END);
		}
		ScriptArgs args = createArgs(param);
		return exec(args);
	}

	public void next() {
		currentIdx++;

	}

	private void checkOpenMW() throws EventScriptFormatException {
		//openMessageWindow - closeのチェック

		boolean open = cmds.stream().flatMap(p -> p.getData().stream()).filter(p -> p.m.getName().endsWith("openMessageWindow")).count() > 0;
		boolean close = cmds.stream().flatMap(p -> p.getData().stream()).filter(p -> p.m.getName().equals("closeMessageWindow")).count() > 0;

		if (open && !close || !open && close) {
			throw new EventScriptFormatException("SB : openMW, but close not found " + this.script.getName() + "." + this.type + ": open=" + open + " / close=" + close);
		}

	}

	public Result exec(ScriptArgs args) {
		checkOpenMW();
		if (cmds == null || cmds.isEmpty()) {
			if (GameSystem.isDebugMode()) {
				GameLog.print("SB : EventScript [" + script.getName() + "].[" + type + "] is start : param=" + args);
				GameLog.print("SB : EventScript [" + script.getName() + "].[" + type + "] is end [empty] : param=" + args);
			}
			return new Result(List.of(), ScriptResultType.END);
		}
		if (GameSystem.isDebugMode()) {
			GameLog.print("SB : EventScript [" + script.getName() + "].[" + type + "] is start : param=" + args);
			GameLog.addIndent();
		}

		//実行
		ScriptSystem.getInstance().setCurrentExecFile(script);
		ScriptSystem.getInstance().setCurrentExecBlock(this);
		ScriptSystem.getInstance().setCurrentArgs(args);

		List<ScriptLine.Result> cmdResult = new ArrayList<>();
		ScriptResultType last = null;

		for (int i = currentIdx; i < cmds.size(); i++, currentIdx = i) {
			ScriptSystem.getInstance().setCurrentBlockIdx(currentIdx);
			var v = cmds.get(i);
			ScriptLine.Result r = v.exec(args);
			cmdResult.add(r);
			last = r.result;
			if (last == ScriptResultType.RESET_SCRIPT) {
				if (GameSystem.isDebugMode()) {
					GameLog.removeIndent();
				}
				currentIdx = 0;
				return exec(args.getOrigin());
			}
			ScriptSystem.getInstance().setLastResultType(last);
			if (last == PAUSE) {
				if (GameSystem.isDebugMode()) {
					GameLog.removeIndent();
				}
				return new Result(cmdResult, last);
			}
			if (ScriptSystem.getInstance().isPauseMode() && r.result != ScriptResultType.MISFIRE) {
				if (GameSystem.isDebugMode()) {
					GameLog.removeIndent();
				}
				return new Result(cmdResult, last);
			}
			if (r.resultObject instanceof ScriptResultType t) {
				last = t;
				if (last != ScriptResultType.CONTINUE) {
					if (GameSystem.isDebugMode()) {
						GameLog.removeIndent();
						GameLog.print("SB : EventScript [" + script.getName() + "].[" + type + "] is end : result=" + last);
					}
					ScriptSystem.getInstance().setCurrentExecFile(null);
					ScriptSystem.getInstance().setCurrentExecBlock(null);
					ScriptSystem.getInstance().setCurrentArgs(null);
					return new Result(cmdResult, last);
				}
			}
		}
		if (GameSystem.isDebugMode()) {
			GameLog.removeIndent();
			GameLog.print("SB : EventScript [" + script.getName() + "].[" + type + "] is ALL end : result=" + last);
		}
		ScriptSystem.getInstance().setCurrentExecFile(null);
		ScriptSystem.getInstance().setCurrentExecBlock(null);
		ScriptSystem.getInstance().setCurrentArgs(null);
		ScriptSystem.getInstance().setCurrentBlockIdx(0);
		return new Result(cmdResult, last);
	}

	public void resetIdx() {
		currentIdx = 0;
	}

	public void setCurrentIdx(int currentIdx) {
		this.currentIdx = currentIdx;
	}

	public boolean hasNext() {
		return currentIdx < cmds.size();
	}

	public List<ScriptLine> getCmds() {
		return cmds;
	}

	public ScriptBlockType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "ScriptBlock{" + "type=" + type + ", cmds=" + cmds.size() + ", currentIdx=" + currentIdx + '}';
	}

}
