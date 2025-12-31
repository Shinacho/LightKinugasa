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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import kinugasa.game.annotation.NewInstance;

/**
 * MaterialSet.<br>
 *
 * @vesion 1.0.0 - 2025/10/02_12:14:25<br>
 * @author Shinacho.<br>
 */
public class MaterialSet implements Iterable<Map.Entry<Material, Integer>> {

	private Map<Material, Integer> map = new HashMap<>();

	public MaterialSet() {
	}

	public void add(Material m, int n) {
		if (map.containsKey(m)) {
			n += map.get(m);
			map.put(m, n);
			return;
		}
		map.put(m, n);
	}

	@NewInstance
	public MaterialSet add(MaterialSet m) {
		MaterialSet r = new MaterialSet();

		for (var v : this) {
			r.add(v.getKey(), v.getValue());
		}
		for (var v : m) {
			r.add(v.getKey(), v.getValue());
		}
		return r;
	}

	public int get(String id) {
		for (var v : this) {
			if (v.getKey().getId().equals(id)) {
				return v.getValue();
			}
		}
		return 0;
	}

	public int get(Material m) {
		return get(m.getId());
	}

	@Override
	public String toString() {
		return "MaterialSet{" + "map=" + map + '}';
	}

	@Override
	public Iterator<Map.Entry<Material, Integer>> iterator() {
		return map.entrySet().iterator();
	}

}
