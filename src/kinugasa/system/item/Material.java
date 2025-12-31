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

import java.util.Objects;
import kinugasa.game.VisibleNameIDInjector;
import kinugasa.game.annotation.Immutable;
import kinugasa.resource.ID;

/**
 * Material.<br>
 *
 * @vesion 1.0.0 - 2025/10/02_12:09:05<br>
 * @author Shinacho.<br>
 */
@Immutable
public class Material implements ID, VisibleNameIDInjector<Material> {

	private final String id;
	private final int price;

	public Material(String id, int price) {
		this.id = id;
		this.price = price;
	}

	@Override
	public String getId() {
		return id;
	}

	public int getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return "Material{" + "id=" + id + ", price=" + price + ", name=" + getVisibleName() + '}';
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 73 * hash + Objects.hashCode(this.id);
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
		final Material other = (Material) obj;
		return Objects.equals(this.id, other.id);
	}

}
