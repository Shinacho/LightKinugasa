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
package kinugasa.field4.layer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import kinugasa.field4.MapChipAttribute;
import kinugasa.graphics.Animation;
import kinugasa.game.GraphicsContext;
import kinugasa.graphics.KImage;
import kinugasa.object.AnimationSprite;
import kinugasa.util.FrameTimeCounter;

/**
 * FMBackLayerSprite.<br>
 *
 * @vesion 1.0.0 - 2025/07/21_21:45:41<br>
 * @author Shinacho.<br>
 */
public class FMBackLayerSprite extends AnimationSprite {

	private float drawSize;
	private MapChipAttribute attr;
	private boolean traceMove = true;

	public FMBackLayerSprite(float windowW, float windowH, MapChipAttribute attr, float drawSize, FrameTimeCounter tc, KImage... images) {
		this(windowW, windowH, attr, drawSize, tc, Arrays.asList(images));
	}

	public FMBackLayerSprite(float windowW, float windowH, MapChipAttribute attr, float drawSize, FrameTimeCounter tc, List<KImage> images) {
		super(0, 0, windowW, windowH);
		this.drawSize = drawSize;
		this.attr = attr;
		build(tc, images);
	}

	public void setTraceMove(boolean traceMove) {
		this.traceMove = traceMove;
	}

	public boolean isTraceMove() {
		return traceMove;
	}

	private void build(FrameTimeCounter tc, List<KImage> images) {
		int wNum = (int) (getWidth() / (images.get(0).getWidth()));
		int hNum = (int) (getHeight() / (images.get(0).getHeight()));

		List<KImage> animationImage = images.stream().map(p -> p.tiling(wNum, hNum)).toList();

		Animation a = new Animation(tc, animationImage);
		a.setRepeat(true);
		setAnimation(a);
		setAutoImageUpdate(true);
	}

	public MapChipAttribute getAttr() {
		return attr;
	}

	public void free() {
		getAnimation().setImages(List.of());
		setAnimation(null);
	}

	@Override
	public void draw(GraphicsContext g2) {
		if (!isVisible() || !isExist()) {
			return;
		}
		float x = getX();
		float y = getY();

		for (; x >= 0; x -= getWidth() * drawSize);
		for (; y >= 0; y -= getHeight() * drawSize);

		float ix = x;
		KImage image = getAnimation().getCurrentImage();
		float totalWidth = getWidth();
		float totalHeight = getHeight();

		for (; y < totalHeight; y += image.getHeight() * drawSize) {
			for (; x < totalWidth; x += image.getWidth() * drawSize) {
				g2.drawImage(image, (int) x, (int) y, (int) (image.getWidth() * drawSize), (int) (image.getHeight() * drawSize));
			}
			x = ix;
		}
		super.update();

	}

}
