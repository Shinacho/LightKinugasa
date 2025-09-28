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


package kinugasa.resource.text;

import kinugasa.game.annotation.Immutable;
import kinugasa.system.UniversalValue;
import kinugasa.resource.ID;

/**
 * XMLにおける"属性"を表すクラスです.
 * <br>
 * 属性は要素に0個以上設定できるキーと値のペアです。<br>
 * たとえば、&lt;hoge fuga=piyo&gt;では、 fugaという名前でpiyoという値の属性が構築されます。<br>
 * <br>
 *
 * @version 1.0.0 - 2013/03/15_7:10:56.<br>
 * @version 2.0.0 - 2024/08/16_20:36.<br>
 * @author Shinacho (
 * <a href="mailto:d0211@live.jp">d0211@live.jp</a>&nbsp;).<br>
 * <br>
 */
@Immutable
public final class XMLAttribute extends UniversalValue implements ID {

	/**
	 * この属性の名前です.
	 */
	private String name;

	/**
	 * 名前と値を指定して、新しい属性を作成します.
	 *
	 * @param name
	 * @param value
	 */
	public XMLAttribute(String name, String value) {
		super(value);
		this.name = name;
	}

	@Override
	public String getId() {
		return name;
	}

	@Override
	public String toString() {
		return name + "=" + value();
	}
}
