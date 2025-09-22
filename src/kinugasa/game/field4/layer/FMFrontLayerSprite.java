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

import java.util.Arrays;
import java.util.List;
import kinugasa.graphics.Animation;
import kinugasa.graphics.GraphicsContext;
import kinugasa.graphics.KImage;
import kinugasa.object.AnimationSprite;
import kinugasa.object.KVector;
import kinugasa.util.TimeCounter;

/**
 * FMFrontLayerSprite.<br>
 *
 * @vesion 1.0.0 - 2025/07/22_19:37:03<br>
 * @author Shinacho.<br>
 */
public class FMFrontLayerSprite extends AnimationSprite {

	private float drawSize;

	public FMFrontLayerSprite(float tp, float drawSize, KVector v, TimeCounter tc, KImage... images) {
		this(tp, drawSize, v, tc, Arrays.asList(images));
	}

	public FMFrontLayerSprite(float tp, float drawSize, KVector v, TimeCounter tc, List<KImage> images) {
		super(0, 0, 0, 0);
		super.setVector(v);
		this.drawSize = drawSize;

		Animation a = new Animation(tc, images.stream().map(p -> p.mulAlpha(tp)).toList());
		a.setRepeat(true);
		setAnimation(a);
		setAutoImageUpdate(true);
		float w = a.getCurrentImage().getWidth() * drawSize;
		float h = a.getCurrentImage().getHeight() * drawSize;
		setSize(w * 2, h * 2);
	}

	public void free() {
		getAnimation().setImages(List.of());
		setAnimation(null);
	}

	@Override
	public void draw(GraphicsContext g) {
		if (!isVisible() || !isExist()) {
			return;
		}
		int x = (int) getX();
		int y = (int) getY();
		int w = (int) ((int) getWidth() * drawSize);
		int h = (int) ((int) getHeight() * drawSize);

		while (x > 0) {
			x -= w;
		}
		while (y > 0) {
			y -= h;
		}
		KImage image = getAnimation().getCurrentImage();
		g.drawImage(image, x, y, w, h);
		g.drawImage(image, x + w, y, w, h);
		g.drawImage(image, x, y + h, w, h);
		g.drawImage(image, x + w, y + h, w, h);
		super.move();
		super.update();
	}

}
