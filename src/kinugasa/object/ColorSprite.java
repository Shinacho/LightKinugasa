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
package kinugasa.object;

import java.awt.Color;
import java.awt.Rectangle;
import kinugasa.game.GraphicsContext;

/**
 * ColorSprite.<br>
 *
 * @vesion 1.0.0 - 2025/09/30_23:31:37<br>
 * @author Shinacho.<br>
 */
public class ColorSprite extends Sprite {

	private Color color = Color.BLACK;

	public ColorSprite() {
	}

	public ColorSprite(Rectangle r) {
		super(r);
	}

	public ColorSprite(Color color, Rectangle r) {
		super(r);
		this.color = color;
	}

	public ColorSprite(float x, float y, float w, float h) {
		super(x, y, w, h);
	}

	public ColorSprite(float x, float y, float w, float h, float z) {
		super(x, y, w, h, z);
	}

	public ColorSprite(float x, float y, float w, float h, KVector vector) {
		super(x, y, w, h, vector);
	}

	public ColorSprite(float x, float y, float w, float h, KVector vector, MovingModel model) {
		super(x, y, w, h, vector, model);
	}

	public ColorSprite(float w, float h, KVector vector, MovingModel model) {
		super(w, h, vector, model);
	}

	@Override
	public void draw(GraphicsContext g) {
		g.setColor(color);
		g.fillRect(this);
	}

	@Override
	public String toString() {
		return "ColorSprite{" + "color=" + color + '}';
	}

	@Override
	public ColorSprite clone() {
		return (ColorSprite) super.clone();
	}

}
