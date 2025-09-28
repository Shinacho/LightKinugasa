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
package kinugasa.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import kinugasa.game.I18NText;

/**
 * 文字列操作ユーティリティです.
 * <br>
 *
 * <br>
 *
 * @version 1.0.0 - 2012/06/16_15:23:39.<br>
 * @author Shinacho<br>
 */
public final class StringUtil {

	/**
	 * ユーティリティクラスです.
	 */
	private StringUtil() {
	}

	public static String repeat(String v, int n) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < n; i++) {
			s.append(v);
		}
		return s.toString();
	}

	public static String toPercent(float v) {
		String r = Float.toString(v * 100);
		return r.length() <= 4
				? r.length() <= 2 && !r.contains(".") ? r + ".0" : r
				: r.substring(0, 4);
	}

	public static boolean is半角(char c) {
		// Unicodeブロックをチェック
		Character.UnicodeBlock block = Character.UnicodeBlock.of(c);

		// 半角カタカナや基本ラテン文字などを半角と判定
		if (block == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| block == Character.UnicodeBlock.BASIC_LATIN
				|| block == Character.UnicodeBlock.LATIN_1_SUPPLEMENT
				|| block == Character.UnicodeBlock.KATAKANA) {
			return c < 0xFF;
		}

		// 上記以外のブロックに含まれる文字は全角と判定
		return false;
	}

	public static String spaceOf(String v) {
		if (v.isEmpty()) {
			return v;
		}
		StringBuilder sb = new StringBuilder();
		for (char c : v.toCharArray()) {
			sb.append(is半角(c) ? " " : "　");
		}
		return sb.toString();
	}

	/**
	 * 文字列をlengthの長さになるよう右詰し、空いたスペースに" "を挿入します.
	 *
	 * @param msg 対象文字列.<br>
	 * @param length 操作後の全体の長さ.<br>
	 *
	 * @return 右詰された文字列.<br>
	 */
	public static String toRight(String msg, int length) {
		String res = "";
		for (int i = 0; i < length - msg.length(); i++) {
			res += " ";
		}
		res += msg;
		return res;
	}

	public static String zeroUme(int msg, int length) {
		return zeroUme(msg + "", length);
	}

	/**
	 * 文字列をlengthの長さになるよう右詰し、空いたスペースに"0"を挿入します.
	 *
	 * @param msg 対象文字列.<br>
	 * @param length 操作後の全体の長さ.<br>
	 *
	 * @return 右詰された文字列.<br>
	 */
	public static String zeroUme(String msg, int length) {
		if (msg.length() >= length) {
			return msg;
		}
		String res = "";
		for (int i = 0; i < length - msg.length(); i++) {
			res += "0";
		}
		res += msg;
		return res;
	}

	/**
	 * 指定された文字列からファイル名を抽出します. たとえば/hoge/piyo/fuga/a.cのときa.cを返します。<br>
	 * 文字列の終端が"/"である場合はその文字列自体を返します。<br>
	 * 文字列内に"/"が存在しない場合もその文字列自体を返します。<br>
	 *
	 * @param path ファイル名を抽出するパスを送信します。<br>
	 *
	 * @return パス中からファイル名を抽出して返します。<br>
	 */
	public static String fileName(String path) {
		return path.endsWith("/") ? path : path.substring(path.lastIndexOf('/') + 1, path.length());
	}

	public static int[] parseIntCSV(String value)
			throws NumberFormatException {
		String[] values = value.split(",");
		int[] result = new int[values.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Integer.parseInt(values[i]);
		}
		return result;
	}

	public static int[] parseIntCSV(String value, String separator)
			throws NumberFormatException {
		if (!value.contains(separator)) {
			return new int[]{Integer.parseInt(value)};
		}
		String[] values = value.split(separator);
		int[] result = new int[values.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Integer.parseInt(values[i]);
		}
		return result;
	}

	public static boolean isDigit(String val) {
		boolean dg = true;
		for (char ch : val.toCharArray()) {
			dg &= (ch <= '9' & ch >= '0');
		}
		return dg;
	}

	public static String[] safeSplit(String val, String sep) {
		if (val == null || val.isEmpty()) {
			return new String[]{};
		}
		String containsSep = sep;
		String splitSep = sep;
		if ("||".equals(sep)) {
			splitSep = "[|][|]";
		} else if (".".equals(sep) || "(".equals(sep) || ")".equals(sep) || "|".equals(sep)) {
			splitSep = "[" + splitSep + "]";
		}
		return val.contains(containsSep) ? val.split(splitSep) : new String[]{val};
	}

	public static String[] safeSplit(I18NText val, String sep) {
		return safeSplit(val.toString(), sep);
	}

	public static int count(String v, char c) {
		return (int) v.chars().filter(p -> (char) p == c).count();
	}

	public static int count(String v, String tgt) {
		if (!v.contains(tgt)) {
			return 0;
		}
		return safeSplit(v, tgt).length - 1;
	}

	//escape
	public static String[] safeSplitBlock(String v, char sep, char blockCh, boolean trim) {
		List<String> r = new ArrayList<>();

		//ブロック文字が偶数個であることを確認
		StringBuilder sb = new StringBuilder();
		boolean start = false;
		boolean inValue = false;
		boolean escape = false;
		for (char c : v.toCharArray()) {
			if (c == '\\') {
				if (escape) {
					sb.append(c);
					escape = false;
					continue;
				}
				//エスケープ。次の文字はブロック区切りではなく、追加する必要がある。
				escape = true;
				continue;
			}
			if (c == blockCh) {
				if (escape) {
					sb.append(c);
					escape = false;
					continue;
				}
				if (!start) {
					sb.append(c);
					start = true;
					inValue = true;
					continue;
				}
				if (start) {
					sb.append(c);
					start = false;
					inValue = false;
					continue;
				}
			}
			if (c == sep) {
				if (inValue) {
					//無視して追加
					sb.append(c);
					continue;
				} else {
					//区切り文字なので次へ
					r.add(trim ? sb.toString().trim() : sb.toString());
					sb = new StringBuilder();
					continue;
				}
			}
			sb.append(c);

		}
		if (!sb.isEmpty()) {
			r.add(trim ? sb.toString().trim() : sb.toString());
		}

		return r.toArray(String[]::new);
	}

}
