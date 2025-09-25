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
package kinugasa.game.field4;

import java.util.List;

/**
 * LayeredTile.<br>
 *
 * @vesion 1.0.0 - 2025/07/21_10:15:13<br>
 * @author Shinacho.<br>
 */
public class LayeredTile {

	private D2Idx idx;
	private List<MapChipAttribute> attr;

	public LayeredTile(D2Idx idx, List<MapChipAttribute> attr) {
		this.idx = idx;
		this.attr = attr;
	}

	public D2Idx getIdx() {
		return idx;
	}

	public List<MapChipAttribute> getAttr() {
		return attr;
	}

	public boolean has(MapChipAttribute a) {
		return attr.contains(a);
	}
	
	public boolean canStep(Vehicle v){
		return v.canStep(this);
	}

	@Override
	public String toString() {
		return idx + " = " + attr;
	}

}
