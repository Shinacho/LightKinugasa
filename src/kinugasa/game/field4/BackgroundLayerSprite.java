/*
 * Copyright (C) 2023 Shinacho
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

import java.awt.image.BufferedImage;
import java.util.List;
import kinugasa.game.GraphicsContext;
import kinugasa.graphics.Animation;
import kinugasa.graphics.KImage;
import kinugasa.object.AnimationSprite;
import kinugasa.resource.Disposable;
import kinugasa.util.TimeCounter;

/**
 * 背景用アニメーションを表示するレイヤです.
 * <br>
 * 背景レイヤは移動判定に使用されません。<br>
 * <br>
 * 背景は1つ以上の画像をタイリングして構築されます。ロードすると、あらかじめ設定されている 画像をタイリングした描画用アニメーションが設定されます。<br>
 * <br>
 *
 * @version 1.0.0 - 2013/05/02_23:03:55<br>
 * @author Shinacho<br>
 */
public class BackgroundLayerSprite extends AnimationSprite implements Disposable {

	private float mg;

	public BackgroundLayerSprite(float w, float h, float mg) {
		super(0, 0, w, h);
		this.mg = mg;
	}

	public void build(TimeCounter tc, List<BufferedImage> images) {
		//画面サイズまでタイリングした画像を生成
		KImage[] images2 = new KImage[images.size()];
		for (int i = 0; i < images2.length; i++) {
			images2[i] = new KImage(images.get(i));
			images2[i] = images2[i].tiling(
					(int) (getWidth() / images.get(i).getWidth()),
					(int) (getHeight() / images.get(i).getHeight()),
					(int) (images.get(i).getWidth()),
					(int) (images.get(i).getHeight()));
			images2[i] = images2[i].resizeTo(mg);
		}

		setAnimation(new Animation(tc, images2));
	}

	@Override
	public void draw(GraphicsContext g) {
		if (!isVisible() || !isExist()) {
			return;
		}
		float x = getX();
		float y = getY();
		for (; x >= 0; x -= getWidth());
		for (; y >= 0; y -= getHeight());

		float ix = x;
		BufferedImage image = getAnimation().getCurrentImage().asBufferedImage();
		float totalWidth = getWidth();
		float totalHeight = getHeight();

		for (; y < totalHeight; y += image.getHeight()) {
			for (; x < totalWidth; x += image.getWidth()) {
				g.drawImage(image, (int) x, (int) y);
			}
			x = ix;
		}
		super.update();
	}

	@Override
	public void dispose() {
		setAnimation(null);
	}
}
