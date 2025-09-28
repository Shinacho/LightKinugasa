/*
  * MIT License
  *
  * Copyright (c) 2025 しなちょ
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
 */
package kinugasa.game;

import java.io.File;
import kinugasa.game.annotation.OneceTime;
import kinugasa.game.annotation.NoLoopCall;
import java.util.HashSet;
import java.util.Set;
import kinugasa.script.ScriptFileCall;
import kinugasa.script.ScriptSystem;

/**
 *
 * @vesion 1.0.0 - 2021/08/17_6:55:53<br>
 * @author Shinacho<br>
 */
public class I18N {

	private static I18NReader reader;
	private static Set<String> notFoundKeySet = new HashSet<>();
	private static Set<String> nullValueKeySet = new HashSet<>();

	@OneceTime
	public static void init(I18NReader reader) {
		I18N.reader = reader;
	}

	public static void fromIni(File f) {
		init(new IniI18NReader(f));
	}

	public static Set<String> getNotFoundKeySet() {
		return notFoundKeySet;
	}

	public static <T extends Enum<T>> String get(T t) {
		return get(t.toString());
	}

	public static I18NReader getReader() {
		return reader;
	}

	private static String getText(String key) {
		if (notFoundKeySet.contains(key)) {
			return "";
		}
		if (nullValueKeySet.contains(key)) {
			return "";
		}
		if (key == null || key.isEmpty()) {
			notFoundKeySet.add(key);
			nullValueKeySet.add(key);
			GameLog.print("!> WARNING : I18N value is empty : " + key);
			return "";
		}

		String res = reader.getValue(key);
		if (res == null || "".equals(res)) {
			nullValueKeySet.add(key);
			GameLog.print("!> WARNING : I18N value is empty : " + key);
			return "";
		}
		return res;
	}

	@NoLoopCall
	public static String get(String key) {
		return getText(key);
	}

	@NoLoopCall
	public static String get(String key, Object... param) {
		//ID = ${pc0()}のアイテム${itemOf("I0001").getVisibleName()}の${0}と${1}

		String val = get(key);
		char[] val2 = val.toCharArray();
		//$nと${}の処理
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < val2.length; i++) {
			char c = val2[i];
			if (c == '$') {
				int start = i + 2, end = start + 1;
				for (; end < val2.length; end++) {
					if (val2[end] == '}') {
						break;
					}
				}
				String inner = val.substring(start, end);

				if (end >= val.length()) {
					throw new IllegalArgumentException("I18N instantCall is not closed : " + key);
				}

				if (inner.matches("[0-9]*")) {
					//paramモード
					int pidx = Integer.parseInt(inner);
					String p = pidx < param.length
							? param[pidx].toString()
							: "[?]";
					sb.append(p);
					i = end;
				} else {
					//callモード
					var r = ScriptSystem.getInstance().instantCall(inner);
					sb.append(r.lastResultObject().toString());
					i = end;
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
