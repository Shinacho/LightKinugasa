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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kinugasa.game.GameLog;
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.game.annotation.Nullable;
import kinugasa.game.event.exception.EventScriptException;
import kinugasa.game.event.exception.EventScriptFormatException;
import kinugasa.game.event.exception.EventScriptNameException;
import kinugasa.game.event.exception.EventScriptParamException;
import kinugasa.game.event.exception.EventScriptRuntimeException;
import kinugasa.game.system.GameSystem;
import kinugasa.game.system.UniversalValue;
import kinugasa.util.StringUtil;

/**
 * ScriptLine.<br>
 *
 * @vesion 1.0.0 - 2025/07/30_0:33:23<br>
 * @author Shinacho.<br>
 */
public class ScriptLine {

	static class Value {

		@Nullable
		public final ScriptCall scriptCall;
		@Nullable
		public final Method m;
		@Nullable
		public List<String> vName = null;
		public boolean ifNot = false;

		public Value(ScriptCall sc) {
			this.scriptCall = sc;
			this.m = null;
		}

		public Value(Method m, List<String> paramName) {
			this.scriptCall = null;
			this.m = m;
			this.vName = paramName;
		}

		@Override
		public String toString() {
			if (scriptCall != null) {
				return scriptCall.toString();
			}
			return m.getName() + "(" + String.join(",", vName) + ")";
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 89 * hash + Objects.hashCode(this.scriptCall);
			hash = 89 * hash + Objects.hashCode(this.m);
			hash = 89 * hash + Objects.hashCode(this.vName);
			hash = 89 * hash + (this.ifNot ? 1 : 0);
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Value other = (Value) obj;
			if (this.ifNot != other.ifNot) {
				return false;
			}
			if (!Objects.equals(this.scriptCall, other.scriptCall)) {
				return false;
			}
			if (!Objects.equals(this.m, other.m)) {
				return false;
			}
			return Objects.equals(this.vName, other.vName);
		}

	}

	private final ScriptBlock block;
	private final String origin;
	private List<Value> data;//getFieldMapSystem(), get("001"), load();
	private List<List<List<Value>>> ifBlock;// and / or / hoge().piyo()=data

	ScriptLine(ScriptBlock block, String line, List<List<String>> ifStack) {
		this.block = block;
		this.origin = line;
		this.data = new ArrayList<>();
		this.ifBlock = new ArrayList<>();
		parse(line, ifStack);
	}

	private void parse(String line, List<List<String>> ifStack) throws EventScriptException {
		//ifStackの処理
		{
			Class<?> o = ScriptAccessObject.class;
			for (var and : ifStack) {
				List<List<Value>> val1 = new ArrayList<>();
				for (var or : and) {
					List<Value> val2 = new ArrayList<>();
					val1.add(val2);
					for (var v : StringUtil.safeSplit(or, ".")) {
						boolean ifNot = v.startsWith("!");
						if (ifNot) {
							v = v.substring(1);
						}
						if (v.endsWith("()")) {
							String methodName = v.replaceAll("[()]", "").trim();
							Method m = getMethod(o, methodName);
							Value value = new Value(m, List.of());
							value.ifNot = ifNot;
							val2.add(value);
							o = m.getReturnType();
						} else {
							String methodName = v.split("[(]")[0];
							String[] paramNames = StringUtil.safeSplit(v.substring(v.indexOf("(") + 1, v.lastIndexOf(")")), ",");
							int paramCount = paramNames.length;
							Method m = getMethod(o, methodName, Collections.nCopies(paramCount, UniversalValue.class));
							Value value = new Value(m, Arrays.asList(paramNames).stream().map(p -> p.trim()).toList());
							value.ifNot = ifNot;
							val2.add(value);
							o = m.getReturnType();
						}
					}
					//.で切った最後のメソッドの戻り値がbooleanでない場合は例外
					if (o != boolean.class) {
						throw new EventScriptFormatException("SL : if content is not boolean method : " + or);
					}
					o = ScriptAccessObject.class;
				}
				this.ifBlock.add(val1);
			}
		}
		//ファイルモード
		if (line.trim().startsWith("@")) {
			data = List.of(new Value(new ScriptCall(line)));
			return;
		}
		//Methodモード
		{
			Class<?> o = ScriptAccessObject.class;
			for (var v : StringUtil.safeSplit(line, ".")) {
				if (o == Void.class) {
					throw new EventScriptNameException("SL : void method : " + line);
				}
				if (v.endsWith("()")) {
					//no param
					String methodName = v.replaceAll("[()]", "").trim();
					Method m = getMethod(o, methodName);
					data.add(new Value(m, List.of()));
					o = m.getReturnType();
				} else {
					String methodName = v.split("[(]")[0];
					String[] paramNames = StringUtil.safeSplit(v.substring(v.indexOf("(") + 1, v.lastIndexOf(")")), ",");
					int paramCount = paramNames.length;
					Method m = getMethod(o, methodName, Collections.nCopies(paramCount, UniversalValue.class));
					data.add(new Value(m, Arrays.asList(paramNames)));
					o = m.getReturnType();
				}
			}
		}
	}

	private Method getMethod(Class<?> c, String name) throws EventScriptNameException {
		try {
			return c.getMethod(name);
		} catch (NoSuchMethodException | SecurityException ex) {
			throw new EventScriptNameException("SL : missing Method : " + c.getName() + "#" + name);
		}
	}

	private Method getMethod(Class<?> s, String name, List<Class<?>> t) throws EventScriptNameException {
		try {
			if (t.isEmpty()) {
				return getMethod(s, name);
			}
			Class[] c = new Class[t.size()];

			for (int i = 0; i < t.size(); i++) {
				c[i] = t.get(i);
			}
			return s.getMethod(name, c);
		} catch (NoSuchMethodException | SecurityException ex) {
			throw new EventScriptNameException("SL : missing Method : " + s.getName() + "#" + name + "(" + t + ")");
		}
	}

	public class Result {

		public final ScriptResultType result;
		public final Object resultObject;
		public final boolean pause;

		public Result(ScriptResultType result, Object resultObject) {
			this.result = result;
			this.resultObject = resultObject;
			this.pause = false;
		}

		public Result(ScriptResultType result, Object resultObject, boolean pause) {
			this.result = result;
			this.resultObject = resultObject;
			this.pause = pause;
		}

	}

	//paramMapはdefault適用済み。値は"がついていない。
	public Result exec(ScriptArgs args) throws EventScriptException {
		//IF BLOCK判定
		if (!ifBlock.isEmpty()) {
			Object o = ScriptAccessObject.getInstance();
			boolean and = true;
			for (var v : ifBlock) {
				boolean or = false;
				for (var vv : v) {
					for (var vvv : vv) {
						o = invoke(o, vvv, args);
					}
					if (o.getClass() != Boolean.class) {
						throw new EventScriptRuntimeException("SL : IF is not boolean : " + this);
					}
					boolean res = Boolean.parseBoolean(o.toString());
					or |= res;
					o = ScriptAccessObject.getInstance();
					if (or) {
						break;
					}
				}
				and &= or;
				if (!and) {
					break;
				}
			}
			if (!and) {
				if (GameSystem.isDebugMode()) {
					GameLog.print("SL script misfire : " + this);
				}
				return new Result(ScriptResultType.MISFIRE, null);
			}
		}

		//ファイルモード
		if (data.get(0).scriptCall != null) {
			ScriptCall call = data.get(0).scriptCall;

			//マージ済みARGSの作成
			List<UniversalValue> merged = new ArrayList<>();

			//argsのキーに同じ名前があったらそのValueを使う
			for (var v : call.getParam()) {
				if (args.getMap().containsKey(v.value())) {
					merged.add(args.getMap().get(v.value()));
				} else {
					merged.add(v);
				}
			}

			ScriptArgs newArgs = block.createArgs(merged);

			List<ScriptLine> callCmds = call.getBlock().getCmds();
			Result r = null;
			for (var v : callCmds) {
				r = v.exec(newArgs);
			}
			return r;
		}
		//SAOモード
		Object o = ScriptAccessObject.getInstance();
		for (var v : data) {
			if (o.getClass() == Void.class) {
				throw new EventScriptRuntimeException("SL : void method : " + v);
			}
			if (o.getClass() == ScriptResultType.class) {
				return new Result((ScriptResultType) o, o);
			}
			if (v.vName.isEmpty()) {
				o = invoke(o, v, args);
				continue;
			}
			o = invoke(o, v, args);
		}
		return new Result(ScriptResultType.CONTINUE, o);

	}

	private Object invoke(Object o, Value v, ScriptArgs args) {
		if (GameSystem.isDebugMode()) {
			GameLog.print("SL : invoke : " + o.getClass().getName() + "#" + v.m.getName() + "(" + v.vName + ")");
		}
		try {
			if (v.vName.isEmpty()) {
				return v.m.invoke(o);
			}
			UniversalValue[] p = new UniversalValue[v.m.getParameterCount()];
			if (p.length != v.vName.size()) {
				throw new EventScriptParamException("SL : param missmatch : expect=" + v.m.getParameterCount() + " / actual=" + v.vName);
			}
			for (int i = 0; i < p.length; i++) {
				String val = v.vName.get(i);
				if (val.startsWith("\"")) {
					val = val.replaceAll("\"", "").trim();
					p[i] = new UniversalValue(val);
				} else {
					p[i] = new UniversalValue(args.getMap().get(val).trim().value().replaceAll("\"", ""));
				}
			}

			return v.m.invoke(o, (Object[]) p);
		} catch (InvocationTargetException ex) {
			ex.getCause().printStackTrace();
			throw new EventScriptRuntimeException("SL : RuntimeException : " + this + " / " + ex);
		} catch (IllegalAccessException | IllegalArgumentException ex) {
			throw new EventScriptRuntimeException("SL : RuntimeException : " + this + " / " + ex);
		}
	}

	public String toStringIF() {
		if (ifBlock.isEmpty()) {
			return "";
		}
		List<List<String>> val = new ArrayList<>();
		for (var v : ifBlock) {
			List<String> val2 = new ArrayList<>();
			val.add(val2);
			for (var vv : v) {
				val2.add(String.join(".", vv.stream().map(p -> (p.ifNot ? "!" : "") + p.m.getName() + "(" + String.join(",", p.vName) + ")").toList()));
			}
		}
		List<String> s = val.stream().map(p -> String.join(" OR ", p)).map(p -> "(" + p + ")").toList();
		return "IF( " + String.join(" AND ", s) + " ) : ";
	}

	@NotNewInstance
	List<Value> getData() {
		return data;
	}

	List<List<List<Value>>> getIfBlock() {
		return ifBlock;
	}

	@Override
	public String toString() {
		return toStringIF() + origin;
	}

}
