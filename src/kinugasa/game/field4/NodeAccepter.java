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
