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
package kinugasa.field4;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import kinugasa.game.GameLog;
import kinugasa.game.GameManager;
import kinugasa.game.annotation.NewInstance;
import kinugasa.game.annotation.Nullable;
import kinugasa.game.annotation.Singleton;
import kinugasa.script.ScriptFileCall;
import kinugasa.field4.layer.FMNomalLayerSprite;
import kinugasa.system.GameSystem;
import kinugasa.system.actor.NPC;
import kinugasa.system.actor.npcMove.TripNPCMove;
import kinugasa.game.GraphicsContext;
import kinugasa.graphics.GraphicsUtil;
import kinugasa.graphics.KImage;
import kinugasa.object.EmptySprite;
import kinugasa.object.FourDirection;
import kinugasa.object.KVector;
import kinugasa.object.Sprite;
import kinugasa.util.FrameTimeCounter;
import kinugasa.util.TimeCounter;
import kinugasa.util.TimeCounterState;

/**
 * FieldMapCamera.<br>
 *
 * @vesion 1.0.0 - 2025/07/23_22:28:52<br>
 * @author Shinacho.<br>
 */
@Singleton
public class FieldMapCamera {

	private static boolean ignoreVhicle = false;

	public static void setIgnoreVhicle(boolean canStepAll) {
		FieldMapCamera.ignoreVhicle = canStepAll;
	}
	private FieldMap fm;
	private FMNomalLayerSprite layer0;
	private EmptySprite layer0MoveArea;
	private D2Idx idxOfScreenCenter;
	private D2Idx pcIDXOnTile;
	private EmptySprite pcLocationOnScreen;
	private int chipSize;
	private EmptySprite windowArea;
	private boolean isSmallMapX = false, isSmallMapY = false;
	//
	private TimeCounter debugRimmedBlinkRate;
	private boolean debugRimmedBlinkVisible = false;

	private static final FieldMapCamera INSTANCE = new FieldMapCamera();

	public static FieldMapCamera getInstance() {
		return INSTANCE;
	}

	void set(FieldMap fm) {
		this.fm = fm;
		init();
	}

	private FieldMapCamera() {
	}

	private void init() {
		debugMapImage = null;
		layer0 = fm.getNomalLayerSprite().get(0);
		chipSize = layer0.getChipDrawSize();
		int insetsTop = GameManager.getInstance().getWindow().getInsets().top;

		//window Size
		{
			int w = GameManager.getInstance().getOption().getWindowSize().width;
			int h = GameManager.getInstance().getOption().getWindowSize().height;
			windowArea = new EmptySprite(0, 0, w, h);
		}

		//layer0 move area
		{
			float x = layer0.getWidth() - windowArea.getWidth();
			float y = layer0.getHeight() - windowArea.getHeight();
			layer0MoveArea = new EmptySprite(-x, -y, x + 1, y + 1);
		}

		//pcLocation
		idxOfScreenCenter = new D2Idx(
				(int) ((windowArea.getWidth() / chipSize / 2f)),
				(int) (((windowArea.getHeight() - insetsTop) / chipSize / 2f))
		);
		pcIDXOnTile = idxOfScreenCenter.clone();
		pcLocationOnScreen = new EmptySprite(idxOfScreenCenter.x * chipSize, idxOfScreenCenter.y * chipSize, chipSize, chipSize);
		isSmallMapX = isSmallMapY = false;
	}

	void setLocationByCenter(D2Idx tgt) throws IllegalArgumentException {
		if (layer0 == null) {
			init();
		}
		if (tgt.x < 0 || tgt.y < 0) {
			throw new IllegalArgumentException("tgt idx is over map size : " + tgt + " / " + layer0.getDataWidth() + "," + layer0.getDataHeight());
		}
		if (tgt.x >= layer0.getDataWidth() || tgt.y >= layer0.getDataHeight()) {
			throw new IllegalArgumentException("tgt idx is over map size : " + tgt + " / " + layer0.getDataWidth() + "," + layer0.getDataHeight());
		}
		this.pcIDXOnTile = tgt.clone();
		this.pcLocationOnScreen.setLocation(idxOfScreenCenter.x * chipSize, idxOfScreenCenter.y * chipSize);
		{
			int x = (int) (pcIDXOnTile.x * chipSize - windowArea.getCenterX() + pcLocationOnScreen.getWidth() / 2);
			int y = (int) (pcIDXOnTile.y * chipSize - (windowArea.getCenterY() - pcLocationOnScreen.getHeight() / 2) + pcLocationOnScreen.getHeight() / 2);

			if (layer0MoveArea.contains(new Point2D.Float(-x, -y))) {
				//移動可能
				Point2D.Float gap = layer0.getLocation();

				fm.getNomalLayerSprite().forEach(p -> p.setLocation(-x, -y));
				gap.x += layer0.getX();
				gap.y += layer0.getY();

				if (fm.getBackLayerSprite() != null) {
					fm.getBackLayerSprite().addLocation(gap.x, gap.y);
				}
				fm.getAnimationLayerSprite().forEach(p -> p.addLocation(gap.x, gap.y));

				//NPC
				for (var v : fm.getNPCMap().getMap().entrySet()) {
					D2Idx i = v.getKey();
					NPC n = v.getValue();
					float nx = gap.x + (i.x * chipSize);
					float ny = gap.y + (i.y * chipSize);
					n.getSprite().setCurrentLocationOnMap(i);
					n.getSprite().setLocation(nx, ny);
				}

				if (fm.isDebugMode()) {
					GameLog.print("FM SET:" + fm.getTile(pcIDXOnTile));
				}
				return;
			}
		}
		//小さいマップの判定
		{
			if (layer0.getDataWidth() < idxOfScreenCenter.x * 2) {
				isSmallMapX = true;
			}
			if (layer0.getDataHeight() < idxOfScreenCenter.y * 2) {
				isSmallMapY = true;
			}
			if (isSmallMapX || isSmallMapY) {

				int x = (int) (isSmallMapX
						? this.windowArea.getCenterX() - layer0.getWidth() / 2
						: -(pcIDXOnTile.x * chipSize - windowArea.getCenterX() + pcLocationOnScreen.getWidth() / 2));
				int y = (int) (isSmallMapY
						? this.windowArea.getCenterY() - layer0.getHeight() / 2
						: -(pcIDXOnTile.y * chipSize - (windowArea.getCenterY() - pcLocationOnScreen.getHeight() / 2) + pcLocationOnScreen.getHeight() / 2));

				//移動可能
				Point2D.Float gap = layer0.getLocation();

				fm.getNomalLayerSprite().forEach(p -> p.setLocation(x, y));
				gap.x += layer0.getX();
				gap.y += layer0.getY();
				if (fm.getBackLayerSprite() != null) {
					fm.getBackLayerSprite().addLocation(gap.x, gap.y);
				}
				fm.getAnimationLayerSprite().forEach(p -> p.addLocation(gap.x, gap.y));

				//NPC
				for (var v : fm.getNPCMap().getMap().entrySet()) {
					D2Idx i = v.getKey();
					NPC n = v.getValue();
					float nx = gap.x + (i.x * chipSize);
					float ny = gap.y + (i.y * chipSize);
					n.getSprite().setCurrentLocationOnMap(i);
					n.getSprite().setLocation(nx, ny);
				}

				//PC
				{
					float px = layer0.getX() + (pcIDXOnTile.x * chipSize);
					float py = layer0.getY() + (pcIDXOnTile.y * chipSize);

					this.pcLocationOnScreen.setLocation(px, py);

				}

				if (fm.isDebugMode()) {
					GameLog.print("FM SET *SMALL[" + isSmallMapX + "," + isSmallMapY + "]*:" + fm.getTile(pcIDXOnTile));
				}
				return;
			}

		}
		{
			float x;
			if (pcIDXOnTile.x < idxOfScreenCenter.x) {
				x = 0;
			} else if (pcIDXOnTile.x > (layer0.getDataWidth() - idxOfScreenCenter.x)) {
				x = layer0MoveArea.getWidth() - 1;
			} else {
				x = (int) (pcIDXOnTile.x * chipSize - windowArea.getCenterX() + pcLocationOnScreen.getWidth() / 2);
			}
			float y;
			if (pcIDXOnTile.y < idxOfScreenCenter.y) {
				y = 0;
			} else if (pcIDXOnTile.y > (layer0.getDataHeight() - idxOfScreenCenter.y)) {
				y = layer0MoveArea.getHeight() - 1;
			} else {
				y = (int) (pcIDXOnTile.y * chipSize - windowArea.getCenterY() + pcLocationOnScreen.getHeight() / 2);
			}
			if (layer0MoveArea.contains(new Point2D.Float(-x, -y))) {
				//移動可能
				Point2D.Float gap = layer0.getLocation();

				fm.getNomalLayerSprite().forEach(p -> p.setLocation(-x, -y));
				gap.x += layer0.getX();
				gap.y += layer0.getY();
				if (fm.getBackLayerSprite() != null) {
					fm.getBackLayerSprite().addLocation(gap.x, gap.y);
				}
				fm.getAnimationLayerSprite().forEach(p -> p.addLocation(gap.x, gap.y));

				//NPC
				for (var v : fm.getNPCMap().getMap().entrySet()) {
					D2Idx i = v.getKey();
					NPC n = v.getValue();
					float nx = gap.x + (i.x * chipSize);
					float ny = gap.y + (i.y * chipSize);
					n.getSprite().setCurrentLocationOnMap(i);
					n.getSprite().setLocation(nx, ny);
				}

				this.pcLocationOnScreen.setLocation(pcIDXOnTile.x * chipSize + gap.x, pcIDXOnTile.y * chipSize + gap.y);
				if (fm.isDebugMode()) {
					GameLog.print("FM SET *RIM* :" + fm.getTile(pcIDXOnTile));
				}
				return;
			} else {
				throw new AssertionError("FieldMapCamera initial location is over moveable area *RIM");
			}

		}

	}

	public void move(Point2D.Float p) {
		move(p.x, p.y);
	}

	public void move(float xSpeed, float ySpeed) {
		D2Idx prev = pcIDXOnTile.clone();
		move(xSpeed < 0 ? FourDirection.WEST : FourDirection.EAST, Math.abs(xSpeed));
		move(ySpeed < 0 ? FourDirection.NORTH : FourDirection.SOUTH, Math.abs(ySpeed));
		if (!pcIDXOnTile.equals(prev)) {
			//新しいタイルに乗った
			debugMovePrint();
		}
	}

	private void move(FourDirection d, float speed) {
		if (d == null) {
			return;
		}
		KVector v = new KVector(d, speed);
		if (v.getSpeed() == 0) {
			return;
		}
		KVector v2 = v.reverse();
		Point2D.Float layer0Next = layer0.simulateMove(v2);
		Point2D.Float plosNext = pcLocationOnScreen.simulateMoveCenterLocation(v);

		if (isSmallMapX) {
			switch (d) {
				case EAST, WEST -> {
					plosMoveSmall(plosNext);
				}
				case NORTH, SOUTH -> {
					if (!isSmallMapY) {
						layerMove(plosNext, layer0Next, v2);
					}
				}
			}
		}
		if (isSmallMapY) {
			switch (d) {
				case NORTH, SOUTH -> {
					plosMoveSmall(plosNext);
				}
				case EAST, WEST -> {
					if (!isSmallMapX) {
						layerMove(plosNext, layer0Next, v2);
					}
				}
			}
		}
		if (isSmallMapX || isSmallMapY) {
			return;
		}

		RimInfo ri = isRimArea();

		if (layer0MoveArea.contains(layer0Next) && !ri.any()) {
			layerMove(plosNext, layer0Next, v2);
		}

		MoveInfo m = getMoveInfo(ri, plosNext);

		if (m.plosMove) {
			plosMoveRim(plosNext);
		}

		if (m.layerMove) {
			if (layer0MoveArea.contains(layer0Next)) {
				layerMove(plosNext, layer0Next, v2);
			}
		}

	}

	private void npcMove(KVector kv) {
		for (var v : fm.getNPCMap()) {
			v.getSprite().fmMove(kv);
		}
		var pcList = GameSystem.getInstance().getPcList();
		for (int i = 1; i < pcList.size(); i++) {
			pcList.get(i).getSprite().fmMove(kv);
		}
	}

	@Nullable
	private KImage debugMapImage;

	public class RimInfo {

		public final boolean north, south, east, west;

		public RimInfo(boolean north, boolean south, boolean east, boolean west) {
			this.north = north;
			this.south = south;
			this.east = east;
			this.west = west;
		}

		public boolean any() {
			return north || south || east || west;
		}

	}

	public RimInfo isRimArea() {
		float speed = FieldMapSystem.getInstance().getCurrentVehicle().getSpeed();

		boolean north, south, east, west;

		KVector v = new KVector(FourDirection.NORTH, speed);
		north = !layer0MoveArea.contains(layer0.simulateMove(v.reverse()));

		v = new KVector(FourDirection.SOUTH, speed);
		south = !layer0MoveArea.contains(layer0.simulateMove(v.reverse()));

		v = new KVector(FourDirection.EAST, speed);
		east = !layer0MoveArea.contains(layer0.simulateMove(v.reverse()));

		v = new KVector(FourDirection.WEST, speed);
		west = !layer0MoveArea.contains(layer0.simulateMove(v.reverse()));
		return new RimInfo(north, south, east, west);
	}

	public void draw(GraphicsContext g, List<? extends Sprite> pcSprite) {
		if (fm.getBackLayerSprite() != null) {
			fm.getBackLayerSprite().draw(g);
		}
		fm.getNomalLayerSprite().stream().filter(p -> !p.isAbove()).forEach(p -> p.draw(g));
		fm.getNPCMap().values().forEach(p -> p.getSprite().draw(g));
		pcSprite.forEach(p -> p.draw(g));
		fm.getNomalLayerSprite().stream().filter(p -> p.isAbove()).forEach(p -> p.draw(g));

		fm.getAnimationLayerSprite().forEach(p -> p.draw(g));
		fm.getFrontLayerSprite().draw(g);
		if (fm.isDebugMode()) {
			debugDraw(g);
		}
	}

	private void debugDraw(GraphicsContext g) {
		//中心レティクル
		{
			g.setColor(Color.WHITE);
			int x = (int) (windowArea.getCenterX() - chipSize / 2);
			int y = (int) (windowArea.getCenterY() - chipSize / 2);
			g.drawOval(x, y, chipSize, chipSize);

			String v = (int) windowArea.getCenterX() + "," + (int) windowArea.getCenterY();
			g.drawString("SCREEN_CENTER:" + v, 4, 66);
		}
		//ベースレイヤー範囲
		{
			Rectangle2D.Float r = fm.getNomalLayerSprite().get(0).getBounds();
			g.setColor(Color.GREEN);
			g.fillOval(r.x - 9, r.y - 9, 18, 18);
			g.fillOval(r.x + r.width - 9, r.y - 9, 18, 18);
			g.fillOval(r.x - 9, r.y + r.height - 9, 18, 18);
			g.fillOval(r.x + r.width - 9, r.y + r.height - 9, 18, 18);
			g.drawRect((int) r.x + 3, (int) r.y + 3, (int) r.width - 6, (int) r.height - 6);
			String v = (int) layer0.getX() + "," + (int) layer0.getY() + "," + (int) layer0.getWidth() + "," + (int) layer0.getHeight();
			g.drawString("LAYER[0]:" + v + " / " + "dataW=" + layer0.getDataWidth() + ", dataH=" + layer0.getDataHeight(), 4, 42);
		}
		//Rimmed
		{
			g.setColor(Color.PINK);
			RimInfo i = isRimArea();
			List<String> v = new ArrayList<>();
			if (i.north) {
				v.add("N");
			}
			if (i.south) {
				v.add("S");
			}
			if (i.east) {
				v.add("E");
			}
			if (i.west) {
				v.add("W");
			}
			g.drawString("RIM:" + (v.isEmpty() ? "-" : String.join(",", v)), 4, 54);

			//RIMMED AREA
			g.drawRect((int) layer0.getX(), (int) layer0.getY(), (int) layer0MoveArea.getWidth(), (int) layer0MoveArea.getHeight());

			if (debugRimmedBlinkRate == null) {
				debugRimmedBlinkRate = new FrameTimeCounter(12);
			}
			if (isRimArea().any()) {
				if (debugRimmedBlinkRate.update() == TimeCounterState.ACTIVE) {
					debugRimmedBlinkRate.reset();
					debugRimmedBlinkVisible = !debugRimmedBlinkVisible;
				}
				if (debugRimmedBlinkVisible) {
					g.drawString("RIM", (int) windowArea.getCenterX() - 10, (int) windowArea.getCenterY() - 30);
				}
			}
		}
		//FM領域デバッグ表示システム
		if (debugMapImage == null) {
			// base
			{
				//back Layer
				Color backColor = fm.getBackLayerSprite() != null ? fm.getBackLayerSprite().getImage().averageColor() : new Color(0, 0, 0, 0);
				debugMapImage = new KImage(layer0.getDataWidth(), layer0.getDataHeight()).fillBy(backColor);

				//nomal layer
				{
					KImage.KRaster r = debugMapImage.asRaster();
					for (var l : fm.getNomalLayerSprite()) {
						for (int y = 0; y < l.getData().length; y++) {
							for (int x = 0; x < l.getData()[y].length; x++) {
								KImage cell = l.getData()[y][x].getImage();
								if (!cell.hasClaerPixel()) {
									r.of(x, y).to(cell.averageColor());
								}
							}
						}
					}
					debugMapImage = r.updateImage();
				}
			}
			debugMapImage.addAlpha(-128);
		}
		//AREA
		{
			int x = 36;
			int y = 130;
			//base
			{
				g.setColor(Color.WHITE);
				g.drawString("AREA", 4, 114);
				g.drawImage(debugMapImage, x, y);
				g.drawRect(x, y, debugMapImage.getWidth() - 1, debugMapImage.getHeight() - 1);
			}
			//Move Area
			{
				float w = layer0MoveArea.getWidth() * (debugMapImage.getWidth() / layer0.getWidth());
				float h = layer0MoveArea.getHeight() * (debugMapImage.getHeight() / layer0.getHeight());
				g.setColor(GraphicsUtil.transparent(Color.PINK, 72));
				g.fillRect(x, y, (int) w, (int) h);
			}
			//PC IDX
			{
				g.setColor(Color.RED);
				g.fillRect(x + pcIDXOnTile.x, y + pcIDXOnTile.y, 1, 1);
			}
			//CAMERA
			{
				float wScale = debugMapImage.getWidth() / layer0.getWidth();
				float hScale = debugMapImage.getHeight() / layer0.getHeight();
				float cx = x + -layer0.getX() * wScale;
				float cy = y + -layer0.getY() * hScale;
				float w = windowArea.getWidth() * wScale;
				float h = windowArea.getHeight() * hScale;
				g.setColor(Color.BLACK);
				g.drawRect((int) cx, (int) cy, (int) w, (int) h);
			}
		}
		//PC IDX ON TILE
		{
			float x = fm.getNomalLayerSprite().get(0).getX();
			float y = fm.getNomalLayerSprite().get(0).getY();

			x += pcIDXOnTile.x * chipSize;
			y += pcIDXOnTile.y * chipSize;

			g.setColor(Color.RED);
			g.drawRect((int) x, (int) y, chipSize, chipSize);
			String miniMapLabel = fm.getMiniMapLabelStorage().contains(pcIDXOnTile.getId())
					? " \"" + fm.getMiniMapLabelStorage().get(pcIDXOnTile.getId()).getText().toString() + "\""
					: "";
			g.drawString("PC_IDX_ON_TILE:" + fm.getTile(pcIDXOnTile) + miniMapLabel, 4, 18);
		}
		//PC LOCATION ON LAYER
		{
			g.setColor(Color.ORANGE);
			Point2D.Float p = pcLocationOnScreen.getCenter();
			g.drawLine(p.x - chipSize, p.y, p.x + chipSize, p.y);
			g.drawLine(p.x, p.y - chipSize, p.x, p.y + chipSize);
			String v = (int) pcLocationOnScreen.getCenterX() + "," + (int) pcLocationOnScreen.getCenterY();
			g.drawString("PC_LOCATION_ON_SCREEN:" + v, 4, 30);
		}
		//PC_LIST
		{
			g.setColor(Color.MAGENTA);
			var pcList = GameSystem.getInstance().getPcList();
			for (int i = 0; i < pcList.size(); i++) {
				Sprite s = pcList.get(i).getSprite();
				g.drawRect(s);
				String name = pcList.get(i).getVisibleName().toString();
				if (name != null && !name.isEmpty()) {
					name = "\"" + name + "\"";
				}
				g.drawString(pcList.get(i).getId() + name, (int) s.getX(), (int) s.getY());
			}

		}
		//IGNORE VHICLE
		{
			g.setColor(Color.WHITE);
			g.drawString("IGNORE_VHICLE:" + ignoreVhicle, 4, 78);
		}
		//CHIP_SIZE
		{
			g.setColor(Color.WHITE);
			g.drawString("CHIP_SIZE:" + chipSize, 4, 90);

		}
		//SMALL_MAP
		{
			g.setColor(Color.WHITE);
			g.drawString("SMALL_MAP:" + (isSmallMapX ? "X" : "-") + "," + (isSmallMapY ? "Y" : "-"), 4, 102);

		}
		//NPC
		{
			for (var v : fm.getNPCMap()) {
				g.setColor(Color.LIGHT_GRAY);

				String name = v.getVisibleName().toString();
				if (name != null && !name.isEmpty()) {
					name = "\"" + name + "\"";
				}
				g.drawString(v.getId() + name + ":" + v.getSprite().getMoveModel().toString(), (int) v.getSprite().getX(), (int) v.getSprite().getY());
				g.drawRect(v.getSprite());
				if (v.getSprite().getMoveModel() instanceof TripNPCMove m) {
					var route = m.getRoute();
					if (route == null || route.isEmpty()) {
						continue;
					}
					Point2D.Float prev = layer0.getLocation();
					prev.x += (route.get(0).x * fm.getLayer0ChipSize());
					prev.x += fm.getLayer0ChipSize() / 2;
					prev.y += (route.get(0).y * fm.getLayer0ChipSize());
					prev.y += fm.getLayer0ChipSize() / 2;
					for (int i = 1; i < route.size(); i++) {
						var r = route.get(i);
						Point2D.Float p = layer0.getLocation();
						p.x += (r.x * fm.getLayer0ChipSize());
						p.x += fm.getLayer0ChipSize() / 2;
						p.y += (r.y * fm.getLayer0ChipSize());
						p.y += fm.getLayer0ChipSize() / 2;

						g.drawLine(prev, p);
						prev = (Point2D.Float) p.clone();
					}
				} else {
					float x = fm.getNomalLayerSprite().get(0).getX();
					x += (v.getSprite().getMoveModel().getInitialLocation().x * chipSize);
					x += v.getSprite().getWidth() / 2;
					float y = fm.getNomalLayerSprite().get(0).getY();
					y += (v.getSprite().getMoveModel().getInitialLocation().y * chipSize);
					y += v.getSprite().getHeight() / 2;

					Point2D.Float c = v.getSprite().getCenter();
					g.drawLine(x, y, c.x, c.y);
				}
			}
		}
		//EVENT
		{
			g.setColor(Color.YELLOW);
			for (var v : fm.getEventScriptMap().all().entrySet()) {
				D2Idx i = v.getKey();
				ScriptFileCall sc = v.getValue();

				float x = fm.getNomalLayerSprite().get(0).getX();
				x += (i.x * chipSize);
				float y = fm.getNomalLayerSprite().get(0).getY();
				y += (i.y * chipSize);

				g.drawRect((int) x, (int) y, chipSize, chipSize);
				g.drawString(sc.getScriptName(), (int) x, (int) y);
			}
		}

	}

	public D2Idx getPcLocation() {
		return pcIDXOnTile;
	}

	public D2Idx getIdxOfScreenCenter() {
		return idxOfScreenCenter;
	}

	@NewInstance
	public Point2D.Float getPcLocationOnScreen() {
		return pcLocationOnScreen.getCenter();
	}

	private void debugMovePrint() {
		if (fm.isDebugMode()) {
			GameLog.print("FMC MOVE " + fm.getTile(pcIDXOnTile));
		}
	}

	private boolean plosMoveSmall(Point2D.Float plosNext) {
		//plos移動
		if (!layer0.contains(plosNext)) {
			return false;
		}
		return plosMove(plosNext);
	}

	private boolean plosMoveRim(Point2D.Float plosNext) {
		if (!windowArea.contains(plosNext)) {
			return false;
		}
		return plosMove(plosNext);
	}

	private boolean plosMove(Point2D.Float plosNext) {

		Point2D.Float newLocation = (Point2D.Float) plosNext.clone();
		newLocation.x -= layer0.getX();
		newLocation.x /= chipSize;
		newLocation.y -= layer0.getY();
		newLocation.y /= chipSize;
		D2Idx prev = pcIDXOnTile.clone();
		D2Idx newPcIDXOnTile = new D2Idx((int) (newLocation.x), (int) (newLocation.y));
		if (!prev.equals(newPcIDXOnTile)) {
			if (!canStep(newPcIDXOnTile, plosNext)) {
				return false;
			}
		}
		pcIDXOnTile = newPcIDXOnTile;
		pcLocationOnScreen.setLocationByCenter(plosNext);
		return true;
	}

	private boolean layerMove(Point2D.Float plosNext, Point2D.Float layer0Next, KVector v2) {
		//通常移動
		Point2D.Float newLocation = pcLocationOnScreen.getCenter();
		newLocation.x -= layer0Next.getX();
		newLocation.x /= chipSize;
		newLocation.y -= layer0Next.getY();
		newLocation.y /= chipSize;
		D2Idx prev = pcIDXOnTile.clone();
		D2Idx newPcIDXOnTile = new D2Idx((int) (newLocation.x), (int) (newLocation.y));

		if (!prev.equals(newPcIDXOnTile)) {
			if (!canStep(newPcIDXOnTile, plosNext)) {
				return false;
			}
		}
		pcIDXOnTile = newPcIDXOnTile;
		fm.getNomalLayerSprite().forEach(p -> p.setVector(v2));
		fm.getNomalLayerSprite().forEach(p -> p.move());
		if (fm.getBackLayerSprite() != null) {
			if (fm.getBackLayerSprite().isTraceMove()) {
				fm.getBackLayerSprite().setVector(v2);
				fm.getBackLayerSprite().move();
			}
		}
		fm.getAnimationLayerSprite().forEach(p -> p.setVector(v2));
		fm.getAnimationLayerSprite().forEach(p -> p.move());
		npcMove(v2);
		return true;
	}

	private boolean canStep(D2Idx newPcIDXOnTile, Point2D.Float plosNext) {
		//領域判定
		if (newPcIDXOnTile.x < 0 || newPcIDXOnTile.y < 0) {
			return false;
		}
		if (newPcIDXOnTile.x >= layer0.getDataWidth() || newPcIDXOnTile.y >= layer0.getDataHeight()) {
			return false;
		}
		//乗れるチップの判定
		if (!ignoreVhicle) {
			if (!FieldMapSystem.getInstance().getCurrentVehicle().canStep(fm.getTile(newPcIDXOnTile))) {
				//移動不可だがPC0のTOだけする
				pc0To(plosNext);
				return false;
			}
		}
		//NPC衝突判定
		{
			for (var n : fm.getNPCMap()) {
				D2Idx npcTgt = n.getSprite().getMoveModel().getCurrentTgt();
				if (npcTgt != null && newPcIDXOnTile.equals(npcTgt)) {
					//移動不可だがPC0のTOだけする
					pc0To(plosNext);
					return false;
				}
			}
		}
		{
			if (fm.getNPCMap().has(newPcIDXOnTile)) {
				//移動不可だがPC0のTOだけする
				pc0To(plosNext);
				//乗れないが、TOUCHを起動する
				NPC n = fm.getNPCMap().get(newPcIDXOnTile);
				FieldMapSystem.getInstance().touch(n);
				return false;
			}
		}
		return true;
	}

	private void pc0To(Point2D.Float p) {
		FourDirection d = new KVector(pcLocationOnScreen.getCenter(), p).round();
		if (!GameSystem.getInstance().getPcList().isEmpty()) {
			GameSystem.getInstance().getPc0().getSprite().to(d);
		}
	}

	private static class MoveInfo {

		public final boolean plosMove;
		public final boolean layerMove;

		public MoveInfo(boolean plosMove, boolean layerMove) {
			this.plosMove = plosMove;
			this.layerMove = layerMove;
		}

		@Override
		public String toString() {
			return "moveInfo{" + "plosMove=" + plosMove + ", layerMove=" + layerMove + '}';
		}

	}

	private MoveInfo getMoveInfo(RimInfo ri, Point2D.Float plosNext) {
		boolean plosMove = false;
		boolean layerMove = false;
		if (ri.north) {
			//north
			if (plosNext.y < pcLocationOnScreen.getCenterY()) {
				//layerは動かさず、plosを動かす
				plosMove = true;
			}
			//south
			if (plosNext.y > pcLocationOnScreen.getCenterY()) {
				//centerYまでplosを動かす
				plosMove = plosNext.y < windowArea.getCenterY();
				layerMove = !plosMove;
				if (!plosMove) {
					plosNext.y = windowArea.getCenterY();
				}
			}
			//west
			if (plosNext.x < pcLocationOnScreen.getCenterX()) {
				plosMove = ri.west ? true : plosNext.x > windowArea.getCenterX();
				layerMove = !plosMove;
			}
			//east8
			if (plosNext.x > pcLocationOnScreen.getCenterX()) {
				plosMove = ri.east ? true : plosNext.x < windowArea.getCenterX();
				layerMove = !plosMove;
			}
		}
		if (ri.south) {
			//south
			if (plosNext.y > pcLocationOnScreen.getCenterY()) {
				//layerは動かさず、plosを動かす
				plosMove = true;
			}
			//north
			if (plosNext.y < pcLocationOnScreen.getCenterY()) {
				//centerYまでplosを動かす
				plosMove = plosNext.y > windowArea.getCenterY();
				layerMove = !plosMove;
				if (!plosMove) {
					plosNext.y = windowArea.getCenterY();
				}
			}
			//west
			if (plosNext.x < pcLocationOnScreen.getCenterX()) {
				plosMove = ri.west ? true : plosNext.x > windowArea.getCenterX();
				layerMove = !plosMove;
			}
			//east
			if (plosNext.x > pcLocationOnScreen.getCenterX()) {
				plosMove = ri.east ? true : plosNext.x < windowArea.getCenterX();
				layerMove = !plosMove;
			}
		}
		if (ri.west) {
			//west
			if (plosNext.x < pcLocationOnScreen.getCenterX()) {
				//laxerは動かさず、plosを動かす
				plosMove = true;
			}
			//east
			if (plosNext.x > pcLocationOnScreen.getCenterX()) {
				//centerXまでplosを動かす
				plosMove = plosNext.x < windowArea.getCenterX();
				layerMove = !plosMove;
				if (!plosMove) {
					plosNext.x = windowArea.getCenterX();
				}
			}
			//north
			if (plosNext.y < pcLocationOnScreen.getCenterY()) {
				plosMove = ri.north ? true : plosNext.y > windowArea.getCenterY();
				layerMove = !plosMove;
			}
			//south
			if (plosNext.y > pcLocationOnScreen.getCenterY()) {
				plosMove = ri.south ? true : plosNext.y < windowArea.getCenterY();
				layerMove = !plosMove;
			}
		}
		if (ri.east) {
			//west
			if (plosNext.x > pcLocationOnScreen.getCenterX()) {
				//laxerは動かさず、plosを動かす
				plosMove = true;
			}
			//east
			if (plosNext.x < pcLocationOnScreen.getCenterX()) {
				//centerXまでplosを動かす
				plosMove = plosNext.x > windowArea.getCenterX();
				layerMove = !plosMove;
				if (!plosMove) {
					plosNext.x = windowArea.getCenterX();
				}
			}
			//north
			if (plosNext.y < pcLocationOnScreen.getCenterY()) {
				plosMove = ri.north ? true : plosNext.y > windowArea.getCenterY();
				layerMove = !plosMove;
			}
			//south
			if (plosNext.y > pcLocationOnScreen.getCenterY()) {
				plosMove = ri.south ? true : plosNext.y < windowArea.getCenterY();
				layerMove = !plosMove;
			}
		}
		return new MoveInfo(plosMove, layerMove);
	}

}
