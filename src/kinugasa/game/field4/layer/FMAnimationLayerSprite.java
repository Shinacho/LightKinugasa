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
package kinugasa.game.field4.layer;

import java.util.List;
import kinugasa.game.field4.D2Idx;
import kinugasa.graphics.Animation;
import kinugasa.object.AnimationSprite;

/**
 * FMAnimationLayerSprite.<br>
 *
 * @vesion 1.0.0 - 2025/07/21_18:21:57<br>
 * @author Shinacho.<br>
 */
public class FMAnimationLayerSprite extends AnimationSprite {

	private int chipSize;
	private D2Idx idx;

	public FMAnimationLayerSprite(int chipSize, D2Idx idx, Animation a) {
		super(0, 0, 0, 0, a);
		this.chipSize = chipSize;
		this.idx = idx;
		build();
	}

	private void build() {
		int x = chipSize * idx.x;
		int y = chipSize * idx.y;
		int w = getAnimation().getCurrentImage().getWidth();
		int h = getAnimation().getCurrentImage().getHeight();
		setLocation(x, y);
		setSize(w, h);
	}

	public D2Idx getIdx() {
		return idx;
	}

	public void free() {
		getAnimation().setImages(List.of());
		setAnimation(null);
	}

}
