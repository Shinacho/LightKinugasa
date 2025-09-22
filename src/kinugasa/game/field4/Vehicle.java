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

import java.util.Arrays;
import java.util.EnumSet;
import static kinugasa.game.field4.MapChipAttribute.*;

/**
 * Vehicle.<br>
 *
 * @vesion 1.0.0 - 2025/07/21_10:12:33<br>
 * @author Shinacho.<br>
 */
public enum Vehicle {
	WALK(2.25f, VOID, BED, CHAIR, PAVED_ROAD, STONE_FLOOR, WOODEN_FLOOR, STONE_BRIDGE, WOODEN_BRIDGE, WOODEN_STEPS, STONE_STEPS, MAGIC_SQUARE, WASTELAND, FIELD, RUINS, MOUNTAIN_HUT, PORT, ROAD, BRIDGE, HILLS, JUNGLE, FOREST, BUSH, SANDY_BEACH, DESERT, HUT, VILLAGE, TOWN, CITY, GRASSLAND, PLAIN),
	SHIP(2f, VOID, BRIDGE, SHALLOW_WATER, SEA),;
	private float speed;
	private EnumSet<MapChipAttribute> canStep;

	private Vehicle(float speed, MapChipAttribute... c) {
		this.speed = speed;
		this.canStep = EnumSet.noneOf(MapChipAttribute.class);
		canStep.addAll(Arrays.asList(c));
	}

	public float getSpeed() {
		return speed;
	}

	public boolean canStep(MapChipAttribute c) {
		return canStep.contains(c);
	}

	public boolean canStep(LayeredTile c) {
		return c.getAttr().stream().allMatch(p -> canStep(p));
	}

}
