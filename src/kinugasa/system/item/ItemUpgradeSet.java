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

import java.util.ArrayList;
import java.util.List;

/**
 * ItemUpgradeSet.<br>
 *
 * @vesion 1.0.0 - 2025/10/02_21:33:53<br>
 * @author Shinacho.<br>
 */
public class ItemUpgradeSet {

	private int current = 0;
	private List<ItemUpgrade> list = new ArrayList<>();

	public ItemUpgradeSet() {
	}

	public ItemUpgradeSet(int c) {
		this.current = c;
	}

	public void add(ItemUpgrade i) {
		list.add(i);
	}

	public ItemUpgrade next() {
		if (!hasNext()) {
			return null;
		}
		current++;
		return list.get(current);
	}

	public boolean hasNext() {
		return current < list.size() - 1;
	}

	@Override
	public String toString() {
		return "ItemUpgradeSet{" + "current=" + current + ", list=" + list + '}';
	}

}
