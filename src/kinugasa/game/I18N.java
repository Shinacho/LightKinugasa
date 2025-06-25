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

import java.util.HashSet;
import java.util.Set;
import kinugasa.resource.NameNotFoundException;
import kinugasa.resource.text.IniFile;

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

	public static void fromIni(IniFile f) {
		init(new IniI18NReader(f));
	}

	public static Set<String> getNotFoundKeySet() {
		return notFoundKeySet;
	}

	public static <T extends Enum<T>> String get(T t) {
		return get(t.toString());
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
		String res = getText(key);
		for (int i = 0; i < param.length; i++) {
			Object o = param[i];
			if (o instanceof VisibleNameSupport t) {
				res = res.replaceAll("!" + i, t.getVisibleName().toString());
			} else {
				res = res.replaceAll("!" + i, o.toString());
			}
		}
		return res;
	}

}
