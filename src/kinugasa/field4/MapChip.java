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
package kinugasa.field4;

import kinugasa.graphics.KImage;
import kinugasa.resource.ID;

/**
 * MapChip.<br>
 *
 * @vesion 1.0.0 - 2025/07/19_7:56:15<br>
 * @author Shinacho.<br>
 */
public class MapChip implements ID {

	private final String id;
	private MapChipAttribute attr;
	private KImage image;

	protected MapChip(String id, MapChipAttribute attr, KImage image) {
		this.id = id;
		this.attr = attr;
		this.image = image;
	}

	@Override
	public String getId() {
		return id;
	}

	public MapChipAttribute getAttr() {
		return attr;
	}

	public KImage getImage() {
		return image;
	}

	public void free() {
		image = null;
	}

	@Override
	public String toString() {
		return "MapChip{" + "id=" + id + ", attr=" + attr + '}';
	}

}
