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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.stream.Stream;
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.field4.D2Idx;
import kinugasa.field4.MapChip;
import kinugasa.field4.MapChipSet;
import kinugasa.game.GraphicsContext;
import kinugasa.graphics.KImage;
import kinugasa.object.EmptySprite;
import kinugasa.object.ImageSprite;
import kinugasa.resource.text.FileFormatException;

/**
 * FMNomalLayerSprite.<br>
 *
 * @vesion 1.0.0 - 2025/07/21_10:56:06<br>
 * @author Shinacho.<br>
 */
public class FMNomalLayerSprite extends ImageSprite {

	private MapChipSet chipSet;
	private float drawSize = 1f;
	private int chipDrawSize;
	private MapChip[][] data;
	private boolean debugMode = false;
	private boolean above;

	public FMNomalLayerSprite(MapChipSet chipSet, float drawSize, boolean above, MapChip[][] data) throws FileFormatException {
		this.chipSet = chipSet;
		this.drawSize = drawSize;
		this.data = data;
		this.above = above;
		build();
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	public float getDrawSize() {
		return drawSize;
	}

	public int getChipDrawSize() {
		return chipDrawSize;
	}

	private void build() throws FileFormatException {
		//サイズの確認
		chipDrawSize = (int) (chipSet.getChipSize() * drawSize);
		int w, h;
		w = data[0].length * chipDrawSize;
		h = data.length * chipDrawSize;

		for (var v : data) {
			int val = v.length * chipDrawSize;
			if (val != w) {
				throw new FileFormatException("MapData size Error : " + Arrays.toString(v));
			}
		}
		setLocation(0, 0);
		setSize(w, h);

		//image作成
		KImage image = new KImage(w, h);
		GraphicsContext g = image.createGraphicsContext();
		for (int y = 0; y < data.length; y++) {
			for (int x = 0; x < data[y].length; x++) {
				int locationX = x * chipDrawSize;
				int locationY = y * chipDrawSize;
				g.drawImage(data[y][x].getImage(), locationX, locationY, chipDrawSize, chipDrawSize);
			}
		}
		g.dispose();

		setImage(image);

	}

	@Override
	public void draw(GraphicsContext g) {
		super.draw(g);
		g.setColor(Color.GRAY);
		if (debugMode) {
			for (int y = 0; y < data.length; y++) {
				for (int x = 0; x < data[y].length; x++) {
					int locationX = (int) getX() + x * chipDrawSize;
					int locationY = (int) getY() + y * chipDrawSize;
					g.drawRect(locationX, locationY, chipDrawSize, chipDrawSize);
				}
			}
		}
	}

	public void free() {
		chipSet = null;
		drawSize = 0;
		data = null;
		setImage((KImage) null);
	}

	public boolean isAbove() {
		return above;
	}

	public MapChip of(D2Idx i) throws IndexOutOfBoundsException {
		return of(i.x, i.y);
	}

	public MapChip of(int x, int y) throws IndexOutOfBoundsException {
		return data[y][x];
	}

	public int getDataWidth() {
		return data[0].length;
	}

	public int getDataHeight() {
		return data.length;
	}

	public boolean isInArea(D2Idx i) {
		return isInArea(i.x, i.y);
	}

	public boolean isInArea(int x, int y) {
		if (x < 0 || y < 0) {
			return false;
		}
		return x < getDataWidth() && y < getDataHeight();
	}

	public boolean allIs(MapChip c) {
		return Stream.of(data).flatMap(p -> Stream.of(p)).allMatch(p -> c.equals(p));
	}

	@NotNewInstance
	public MapChip[][] getData() {
		return data;
	}

	public EmptySprite locationOf(D2Idx i) throws IndexOutOfBoundsException {
		Point2D.Float p = getLocation();
		p.x += (i.x * getChipDrawSize());
		p.y += (i.y * getChipDrawSize());
		return new EmptySprite(p, new Dimension(chipDrawSize, chipDrawSize));
	}

	@Override
	public String toString() {
		return "FieldMapLayerSprite{" + "chipSet=" + chipSet + ", drawSize=" + drawSize + '}';
	}

}
