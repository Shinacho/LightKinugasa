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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Result;
import kinugasa.game.GameLog;
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.game.annotation.Nullable;
import kinugasa.script.exception.ScriptNoSuchMethodException;
import kinugasa.script.exception.ScriptRuntimeException;
import kinugasa.script.exception.ScriptSyntaxException;
import kinugasa.system.GameSystem;
import kinugasa.system.UniversalValue;
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
		public final ScriptFileCall scriptFileCall;
		@Nullable
		public final Method m;
		@Nullable
		public List<UniversalValue> args = null;
		public boolean ifNot = false;

		private Value(ScriptFileCall scriptFileCall, Method m, List<UniversalValue> args) {
			this.scriptFileCall = scriptFileCall;
			this.m = m;
			this.args = args;
		}

		public static Value scriptCall(ScriptFileCall scriptFileCall) {
			return new Value(scriptFileCall, null, null);
		}

		public static Value methodCall(Method m, List<UniversalValue> args) {
			return new Value(null, m, args);
		}

		@Override
		public String toString() {
			if (scriptFileCall != null) {
				return scriptFileCall.toString();
			}
			return m.getName() + "(" + String.join(",", args.stream().map(p -> p.value()).toList()) + ")";
		}

	}

	private final Object sao;
	private final String origin;
	private final List<Value> data;//getFieldMapSystem(), get("001"), load();
	private final List<List<List<Value>>> ifBlock;// and / or / hoge().piyo()=data

	ScriptLine(ScriptAccessObject sao, String line, List<List<String>> ifStack) {
		this.sao = sao;
		this.origin = line;
		this.data = new ArrayList<>();
		this.ifBlock = new ArrayList<>();
		parse(line, ifStack);
	}

	private void parse(String line, List<List<String>> ifStack) throws ScriptSyntaxException {
		if (sao == null) {
			throw new ScriptSyntaxException("SL SAO is null");
		}

		//ifStackの処理
		{
			Class<?> o = sao.getClass();
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
							Value value = Value.methodCall(m, List.of());
							value.ifNot = ifNot;
							val2.add(value);
							o = m.getReturnType();
						} else {
							String methodName = v.split("[(]")[0];
							String[] param = StringUtil.safeSplitBlock(v.substring(v.indexOf("(") + 1, v.lastIndexOf(")")), ',', '"', true);
							int paramCount = param.length;
							Method m = getMethod(o, methodName, Collections.nCopies(paramCount, UniversalValue.class));
							Value value = Value.methodCall(m, Arrays.asList(param).stream().map(p -> p.trim()).map(p -> new UniversalValue(p)).toList());
							value.ifNot = ifNot;
							val2.add(value);
							o = m.getReturnType();
						}
					}
					//.で切った最後のメソッドの戻り値がbooleanでない場合は例外
					if (o != boolean.class) {
						throw new ScriptSyntaxException("SL : if content is not boolean method : " + or);
					}
					o = sao.getClass();
				}
				this.ifBlock.add(val1);
			}
		}
		//ファイルモード
		if (line.trim().startsWith("@")) {
			data.add(Value.scriptCall(new ScriptFileCall(line)));
			//size = 1
			return;
		}
		//Methodモード
		{
			Class<?> o = sao.getClass();
			for (var v : StringUtil.safeSplit(line, ".")) {
				if (o == Void.class) {
					throw new ScriptSyntaxException("SL : void method : " + line);
				}
				if (v.endsWith("()")) {
					//no param
					String methodName = v.replaceAll("[()]", "").trim();
					Method m = getMethod(o, methodName);
					data.add(Value.methodCall(m, List.of()));
					o = m.getReturnType();
				} else {
					String methodName = v.split("[(]")[0];
					//paramはリテラルと実行時変換文字の両方がある
					String[] param = StringUtil.safeSplitBlock(v.substring(v.indexOf("(") + 1, v.lastIndexOf(")")), ',', '"', true);
					int paramCount = param.length;
					Method m = getMethod(o, methodName, Collections.nCopies(paramCount, UniversalValue.class));
					data.add(Value.methodCall(m, Arrays.asList(param).stream().map(p -> p.trim()).map(p -> new UniversalValue(p)).toList()));
					o = m.getReturnType();
				}
			}
		}
	}

	private Method getMethod(Class<?> c, String name) throws ScriptNoSuchMethodException {
		try {
			return c.getMethod(name);
		} catch (NoSuchMethodException | SecurityException ex) {
			throw new ScriptNoSuchMethodException("SL : missing Method : " + c.getName() + "#" + name);
		}
	}

	private Method getMethod(Class<?> s, String name, List<Class<?>> t) throws ScriptNoSuchMethodException {
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
			throw new ScriptNoSuchMethodException("SL : missing Method : " + s.getName() + "#" + name + "(" + t + ")");
		}
	}

	private UniversalValue[] createArgs(Value v, Map<String, UniversalValue> args) {
		if (v.scriptFileCall != null) {
			throw new InternalError("SL :  scriptCall, but createARGS");
		}
		if (v.args == null || v.args.isEmpty()) {
			throw new InternalError("SL :  createARGS, but non param");
		}
		UniversalValue[] res = new UniversalValue[v.args.size()];

		for (int i = 0; i < res.length; i++) {
			var key = v.args.get(i).trim().value();
			if (key.startsWith("\"")) {
				//りてらる　
				key = key.substring(1);
				key = key.substring(0, key.length() - 1);
				res[i] = new UniversalValue(key);
			} else {
				//引数
				if (!args.containsKey(key)) {
					throw new ScriptSyntaxException("SL : param not found : " + key);
				}
				String val = args.get(key).value().trim();
				if (val.startsWith("\"")) {
					val = val.substring(1);
					val = val.substring(0, val.length() - 1);
				}
				res[i] = new UniversalValue(val);
			}
		}
		return res;
	}

	public ScriptResult.Value exec(Map<String, UniversalValue> argsMap) throws ScriptSyntaxException {
		//IF BLOCK判定
		if (!ifBlock.isEmpty()) {
			Object o = sao;
			boolean and = true;
			for (var v : ifBlock) {
				boolean or = false;
				for (var vv : v) {
					for (var vvv : vv) {
						o = invoke(o, vvv, createArgs(vvv, argsMap));
					}
					if (o.getClass() != Boolean.class) {
						throw new ScriptSyntaxException("SL : IF is not boolean : " + this);
					}
					boolean res = Boolean.parseBoolean(o.toString());
					or |= res;
					o = sao;
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
				return new ScriptResult.Value(ScriptResultType.MISSFIRE, ScriptResultType.MISSFIRE);
			}
		}

		//ファイルモード
		if (data.get(0).scriptFileCall != null) {
			return data.get(0).scriptFileCall.exec(argsMap).getLast();
		}
		//SAOモード
		Object o = sao;
		for (var v : data) {
			if (o.getClass() == Void.class) {
				throw new ScriptSyntaxException("SL : return type is void : " + v);
			}

			//no param
			if (v.args == null || v.args.isEmpty()) {
				o = invoke(o, v);
			} else {
				o = invoke(o, v, createArgs(v, argsMap));
			}
			//終了判定
			if (o instanceof ScriptResultType t) {
				if (t.is(ScriptResultType.END)) {
					return new ScriptResult.Value(o, ScriptResultType.END);
				}
				if (t.is(ScriptResultType.PAUSE)) {
					return new ScriptResult.Value(o, ScriptResultType.PAUSE);
				}
				//MISSFIREとCONTINUEは継続する
			}
		}
		return new ScriptResult.Value(o, ScriptResultType.CONTINUE);

	}

	private Object invoke(Object o, Value v) {
		try {
			if (v.scriptFileCall != null) {
				throw new InternalError("SL : method invoke, but value is scriptFileCall : " + v);
			}
			if (GameSystem.isDebugMode()) {
				GameLog.print("SL : invoke : " + o.getClass().getName() + "#" + v.toString() + ")");
				GameLog.addIndent();
			}
			if (v.args != null && !v.args.isEmpty()) {
				throw new InternalError("SL : invoke missmatch : " + v);
			}

			return v.m.invoke(o);

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			ex.printStackTrace();
			throw new ScriptRuntimeException(ex);
		} finally {
			if (GameSystem.isDebugMode()) {
				GameLog.removeIndent();
			}
		}

	}

	private Object invoke(Object o, Value v, UniversalValue[] args) {

		try {
			if (v.scriptFileCall != null) {
				throw new InternalError("SL : method invoke, but value is scriptFileCall : " + v);
			}
			if (GameSystem.isDebugMode()) {
				GameLog.print("SL : invoke : " + o.getClass().getName() + "#" + v.toString() + ")");
				GameLog.addIndent();
			}

			Object[] p = new Object[args.length];
			System.arraycopy(args, 0, p, 0, p.length);

			return v.m.invoke(o, p);

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			ex.printStackTrace();
			throw new ScriptRuntimeException(ex);
		} finally {
			if (GameSystem.isDebugMode()) {
				GameLog.removeIndent();
			}
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
				val2.add(String.join(".", vv.stream().map(p -> (p.ifNot ? "!" : "") + p.m.getName() + "(" + String.join(",", p.args.stream().map(q -> q.value()).toList()) + ")").toList()));
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
