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
package kinugasa.resource.text;

import kinugasa.game.Immutable;
import kinugasa.game.system.UniversalValue;
import kinugasa.object.ID;

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
