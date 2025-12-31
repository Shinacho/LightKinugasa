/*
 * The MIT License
 *
 * Copyright 2025 owner.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package kinugasa.xshare;

/**
 * XUtil.<br>
 *
 * @vesion 1.0.0 - 2025/12/24_19:57:23<br>
 * @author Shinacho.<br>
 */
public class XUtil {

	private XUtil() {
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
}
