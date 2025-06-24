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
package kinugasa.object;

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

	/**
	 * このモデルのクローンを返します. クローンはObjectクラスの機能を使って行われます。<br>
	 * 全てのモデルの実装は、cloneメソッドを適切にオーバーライドする必要があります。<br>
	 *
	 * @return このモデルと同じクラスの新しいインスタンスを返します。<br>
	 */
	@Override
	public IDdCloneableObject clone() {
		IDdCloneableObject r = (IDdCloneableObject) super.clone();
		r.id = this.id + "C";
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
