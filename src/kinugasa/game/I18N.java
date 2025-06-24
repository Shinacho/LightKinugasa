/*
 * Copyright (C) 2023 Shinacho
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
