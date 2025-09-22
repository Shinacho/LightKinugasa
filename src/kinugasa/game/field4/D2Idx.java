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

import java.awt.geom.Point2D;
import kinugasa.game.annotation.Immutable;
import kinugasa.game.annotation.NewInstance;
import kinugasa.object.FourDirection;
import static kinugasa.object.FourDirection.EAST;
import static kinugasa.object.FourDirection.WEST;
import kinugasa.object.IDdCloneableObject;

/**
 * D2Idx.<br>
 *
 * @vesion 1.0.0 - 2025/07/19_8:00:58<br>
 * @author Shinacho.<br>
 */
@Immutable
public class D2Idx extends IDdCloneableObject {

	public final int x;
	public final int y;

	public D2Idx(int x, int y) {
		super(x + "," + y);
		this.x = x;
		this.y = y;
	}

	@Override
	public D2Idx clone() {
		return (D2Idx) super.clone();
	}

	@NewInstance
	public D2Idx add(D2Idx d) {
		return new D2Idx(x + d.x, y + d.y);
	}

	@NewInstance
	public D2Idx addX(int i) {
		return new D2Idx(this.x + i, y);
	}

	@NewInstance
	public D2Idx addY(int i) {
		return new D2Idx(this.x, y + i);
	}

	@Override
	public String toString() {
		return getId();
	}

	@NewInstance
	public D2Idx distance(D2Idx i) {
		return new D2Idx(
				this.x - i.x,
				this.y - i.y);
	}

	@NewInstance
	public Point2D.Float asPoint() {
		return new Point2D.Float(x, y);
	}

	@NewInstance
	public D2Idx add(FourDirection dir, int d) {
		int x = this.x;
		int y = this.y;

		x += switch (dir) {
			case EAST ->
				d;
			case WEST ->
				-d;
			default ->
				0;
		};
		y += switch (dir) {
			case SOUTH ->
				d;
			case NORTH ->
				-d;
			default ->
				0;
		};
		return new D2Idx(x, y);

	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 41 * hash + this.x;
		hash = 41 * hash + this.y;
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
		final D2Idx other = (D2Idx) obj;
		if (this.x != other.x) {
			return false;
		}
		return this.y == other.y;
	}

}
