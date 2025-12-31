/*
 * Copyright (C) 2025 Shinacho
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
package kinugasa.system.item;

import kinugasa.game.annotation.Immutable;

/**
 * ItemUpgrade.<br>
 *
 * @vesion 1.0.0 - 2025/10/02_12:29:01<br>
 * @author Shinacho.<br>
 */
@Immutable
public class ItemUpgrade {

	private int idx;
	private MaterialSet needMaterial;
	private int price;

	public ItemUpgrade(int idx, MaterialSet needMaterial, int price) {
		this.idx = idx;
		this.needMaterial = needMaterial;
		this.price = price;
	}

	public int getIdx() {
		return idx;
	}

	public MaterialSet getNeedMaterial() {
		return needMaterial;
	}

	public int getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return "ItemUpgrade{" + "idx=" + idx + ", needMaterial=" + needMaterial + ", price=" + price + '}';
	}

}
