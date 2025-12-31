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
package kinugasa.system.actor.status.attr;

import java.util.EnumMap;
import java.util.Objects;
import kinugasa.object.CloneableObject;

/**
 * AttributeValueSet.<br>
 *
 * @vesion 1.0.0 - 2025/10/05_17:02:20<br>
 * @author Shinacho.<br>
 */
public class AttributeValueSet extends CloneableObject {

	private EnumMap<AttributeKey, AttributeValue> map = new EnumMap(AttributeKey.class);

	public AttributeValueSet() {
		for (var k : AttributeKey.values()) {
			map.put(k, new AttributeValue(k, 1f));
		}
	}

	public static AttributeValueSet zero() {
		var r = new AttributeValueSet();
		for (var v : r.map.values()) {
			v.set(0f);
		}
		return r;
	}

	@Override
	public AttributeValueSet clone() {
		var r = (AttributeValueSet) super.clone();
		r.map = new EnumMap(AttributeKey.class);
		r.setBy(this);
		return r;
	}

	public AttributeValue of(AttributeKey k) {
		return map.get(k);
	}

	public AttributeValueSet add(AttributeValueSet v) {
		for (var vv : v.map.entrySet()) {
			this.map.get(vv.getKey()).add(vv.getValue());
		}
		return this;
	}

	public AttributeValueSet setBy(AttributeValueSet s) {
		for (var v : s.map.values()) {
			this.map.put(v.key, v);
		}
		return this;
	}

	@Override
	public String toString() {
		return "AttributeValueSet{" + "map=" + map + '}';
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + Objects.hashCode(this.map);
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
		final AttributeValueSet other = (AttributeValueSet) obj;
		return Objects.equals(this.map, other.map);
	}

}
