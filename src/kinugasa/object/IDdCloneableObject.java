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
package kinugasa.object;

import kinugasa.resource.ID;
import java.util.Objects;

/**
 * クローニング可能な特定のアルゴリズムや状態をカプセル化するための抽象クラスです.
 * <br>
 * 全てのモデルの実装は、cloneメソッドを適切にオーバーライドする必要があります。<br>
 * <br>
 * ほとんどのモデルでは、モデルが持つ要素によって、他のインスタンスとの比較が出来ることが期待されます。<br>
 * quealsおよびhashCodeを適切にオーバーライドする必要があります。<br>
 * <br>
 *
 * @version 1.0.0 - 2012/07/14_16:58:06.<br>
 * @version 2.0.0 - 2013/01/11_17:10:31.<br>
 * @author Shinacho.<br>
 */
public abstract class IDdCloneableObject extends CloneableObject implements ID {

	private String id;

	@Override
	public String getId() {
		return id;
	}

	/**
	 * 新しいモデルを作成します.
	 */
	public IDdCloneableObject(String id) {
		this.id = id;
	}

	/**
	 * 新しいモデルを作成します.
	 */
	public <E extends Enum<E>> IDdCloneableObject(E id) {
		this.id = id.toString();
	}

	@Override
	public String toString() {
		return id;
	}

	/**
	 * このモデルのクローンを返します. クローンはObjectクラスの機能を使って行われます。<br>
	 * 全てのモデルの実装は、cloneメソッドを適切にオーバーライドする必要があります。<br>
	 *
	 * @return このモデルと同じクラスの新しいインスタンスを返します。<br>
	 */
	@Override
	public IDdCloneableObject clone() {
		IDdCloneableObject r = (IDdCloneableObject) super.clone();
		r.id = this.id;
		return r;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 43 * hash + Objects.hashCode(this.id);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final IDdCloneableObject other = (IDdCloneableObject) obj;
		return Objects.equals(this.id, other.id);
	}

}
