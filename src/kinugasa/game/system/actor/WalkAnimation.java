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
package kinugasa.game.system.actor;

import java.util.EnumMap;
import kinugasa.graphics.Animation;
import kinugasa.object.FourDirection;

/**
 * WalkAnimationは歩行グラフィックとも呼ばれ、4方向に対する画像です.<br>
 * updateの実行が必要です。<br>
 *
 * @vesion 1.0.0 - 2025/08/11_17:44:55<br>
 * @author Shinacho.<br>
 */
public class WalkAnimation extends Animation {

	private FourDirection currentDir;
	private final EnumMap<FourDirection, Animation> anime;

	public WalkAnimation(Animation north, Animation south, Animation east, Animation west) {
		super(south);
		currentDir = FourDirection.SOUTH;
		anime = new EnumMap<>(FourDirection.class);
		anime.put(FourDirection.NORTH, north);
		anime.put(FourDirection.SOUTH, south);
		anime.put(FourDirection.WEST, west);
		anime.put(FourDirection.EAST, east);
	}

	public void to(FourDirection d) {
		currentDir = d;
		super.setBy(anime.get(d));
	}

	public FourDirection getCurrentDir() {
		return currentDir;
	}

	@Override
	public String toString() {
		return "WalkAnimation{" + "currentDir=" + currentDir + ", anime=" + anime + '}';
	}

}
