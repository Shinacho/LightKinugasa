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
package kinugasa.game.field4;

import kinugasa.object.IDdCloneableObject;
import kinugasa.object.ID;

/**
 * ノードが有効であるかを判定するクラスです.
 * <br>
 * このクラスは、”アイテムを所持していないと通過できない”などの ノードの判定機能を提供します。<br>
 * 作成したNodeAccepterは自動的にNodeAccepterStorageに追加されます。<br>
 * <br>
 *
 * @version 1.0.0 - 2013/04/29_12:30:52<br>
 * @author Shinacho<br>
 */
public abstract class NodeAccepter extends IDdCloneableObject {

	/**
	 * 新しいNodeAccepterを作成します.
	 *
	 * @param name 一意的な名前を指定します。<br>
	 */
	public NodeAccepter(String id) {
		super(id);
		putThis();
	}

	/**
	 * NodeAccepterStorageにthisインスタンスを追加します.
	 */
	private void putThis() {
		NodeAccepterStorage.getInstance().put(this);
	}

	/**
	 * このNodeAccepterを持つノードが使用可能であるかを判定します.
	 *
	 * @return プレイヤーがこのノードを使用できるときにtrueを返します。<br>
	 */
	public abstract boolean accept();

	@Override
	public NodeAccepter clone() {
		return (NodeAccepter) super.clone();
	}

	@Override
	public String toString() {
		return "NodeAccepter{" + "name=" + getId() + '}';
	}

}
