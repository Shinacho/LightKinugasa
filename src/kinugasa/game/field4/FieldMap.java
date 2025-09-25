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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import kinugasa.game.GameLog;
import kinugasa.game.GameManager;
import kinugasa.game.I18N;
import kinugasa.game.I18NText;
import kinugasa.game.UpdateLogicInjector;
import kinugasa.game.VisibleNameIDInjector;
import kinugasa.game.annotation.NewInstance;
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.game.annotation.Nullable;
import kinugasa.game.event.ScriptCall;
import kinugasa.game.event.ScriptSystem;
import kinugasa.game.field4.layer.FMAnimationLayerSprite;
import kinugasa.game.field4.layer.FMBackLayerSprite;
import kinugasa.game.field4.layer.FMFrontLayerSprite;
import kinugasa.game.field4.layer.FMNomalLayerSprite;
import kinugasa.game.input.InputType;
import kinugasa.game.input.Keys;
import kinugasa.game.system.GameSystem;
import kinugasa.game.system.actor.NPC;
import kinugasa.game.ui.TextLabelSprite;
import kinugasa.graphics.Animation;
import kinugasa.graphics.KImage;
import kinugasa.object.CompositeSprite;
import kinugasa.object.FileObject;
import kinugasa.object.KVector;
import kinugasa.object.Sprite;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.Storage;
import kinugasa.resource.text.DataFile;
import kinugasa.resource.text.FileFormatException;
import kinugasa.resource.text.IniFile;
import kinugasa.util.FrameTimeCounter;
import kinugasa.util.TimeCounter;
import kinugasa.util.TimeCounterState;

/**
 * FieldMap.<br>
 *
 * @vesion 1.0.0 - 2025/07/19_7:55:34<br>
 * @author Shinacho.<br>
 */
public class FieldMap extends FileObject implements VisibleNameIDInjector<FieldMap> {

	private static final UpdateLogicInjector debugMenuOnOff = (gtm, is) -> {
		if (is.isPressed(Keys.AT, InputType.SINGLE)) {
			boolean f = !FieldMapSystem.getInstance().getCurrent().debugMode;
			FieldMapSystem.getInstance().getCurrent().debugMode = f;
			FieldMapSystem.getInstance().getCurrent().nomalLayerSprite.forEach(p -> p.setDebugMode(f));
		}
	};

	protected FieldMap(File f) {
		super(f);
	}
	private boolean debugMode = false;
	private Storage<MiniMapLabel> miniMapLabelStorage;
	//
	private FMBackLayerSprite backLayerSprite;
	private List<FMNomalLayerSprite> nomalLayerSprite;
	private List<FMAnimationLayerSprite> animationLayerSprite;
	private FMFrontLayerSprite frontLayerSprite;
	private FieldEventScriptMap eventScriptMap;
	private FieldNPCMap npcMap;
	private boolean loaded = false;
	private boolean loadScriptCall = true;
	//

	public void setLoadScriptCall(boolean loadScriptCall) {
		this.loadScriptCall = loadScriptCall;
	}

	public boolean isLoadScriptCall() {
		return loadScriptCall;
	}

	@Override
	public String getId() {
		String res = super.getId();
		if (res.contains(".")) {
			res = res.substring(0, res.lastIndexOf("."));
		}
		if (res.contains(".")) {
			res = res.substring(0, res.lastIndexOf("."));
		}
		return res;
	}

	public CompositeSprite asCompositeSprite() {
		List<Sprite> s = new ArrayList<>();
		s.add(backLayerSprite);
		s.addAll(nomalLayerSprite);
		s.addAll(animationLayerSprite);
		return new CompositeSprite(s);
	}

	public IniFile getNPCList() {
		String path = getDir() + "npc/";
		return new IniFile(path + "npcList.txt");
	}

	@Override
	public FieldMap load() throws FileNotFoundException, FileFormatException, ContentsIOException {
		if (!exists()) {
			throw new FileNotFoundException(getFile());
		}
		if (GameSystem.isDebugMode()) {
			GameLog.print("-FM LOAD START");
			GameLog.addIndent();
		}
		miniMapLabelStorage = new Storage<>();
		nomalLayerSprite = new ArrayList<>();
		animationLayerSprite = new ArrayList<>();

		DataFile f = asDataFile();
		f.load();

		this.debugMode = f.has("debugMode") ? f.get("debugMode").getValue().asBoolean() : false;
		if (debugMode) {
			if (!GameManager.getInstance().getUpdateLogicInjectors().contains(debugMenuOnOff)) {
				GameManager.getInstance().getUpdateLogicInjectors().add(debugMenuOnOff);
			}
		}

		if (f.has("MINIMAL_LABEL") && f.get("MINIMAP_LABEL").getElements() != null) {
			for (var v : f.get("MINIMAP_LABEL").getElements()) {
				D2Idx i = v.getKey().asD2IdxCSV();
				I18NText t = v.getValue().asI18N();
				miniMapLabelStorage.add(new MiniMapLabel(i, t));
			}
		}

		//BACK_LAYER
		if (f.has("BACK_LAYER") && f.get("BACK_LAYER").getElements() != null) {
			List<KImage> image;
			if (f.get("BACK_LAYER").has("cutW")) {
				int w = f.get("BACK_LAYER").get("cutW").getValue().asInt();
				int h = f.get("BACK_LAYER").get("cutH").getValue().asInt();
				image = f.get("BACK_LAYER").get("image").getValue().asKImageFile().splitRows(0, w, h);
			} else {
				image = List.of(f.get("BACK_LAYER").get("image").getValue().asKImageFile());
			}

			float drawSize = f.get("BACK_LAYER").has("drawSize") ? f.get("BACK_LAYER").get("drawSize").getValue().asFloat() : 1f;
			FrameTimeCounter tc = f.get("BACK_LAYER").get("frame").getValue().asFrameTimeCounterCSV();
			int windowW = GameManager.getInstance().getOption().getWindowSize().width;
			int windowH = GameManager.getInstance().getOption().getWindowSize().height;

			MapChipAttribute attr = f.get("BACK_LAYER").get("attr").getValue().of(MapChipAttribute.class);

			this.backLayerSprite = new FMBackLayerSprite(windowW, windowH, attr, drawSize, tc, image);
			if (f.get("BACK_LAYER").has("traceMove")) {
				this.backLayerSprite.setTraceMove(f.get("BACK_LAYER").get("traceMove").getValue().asBoolean());
			}
		}

		//NOMAL_LAYER
		{
			for (var v : f.getData().stream().filter(p -> p.getKey().value().equals("NOMAL_LAYER")).toList()) {
				MapChipSet chipSet = v.get("chipSet").getValue().asMapChipSetFile();
				chipSet.load();
				boolean above = v.has("above") ? v.get("above").getValue().asBoolean() : false;
				float drawSize = v.has("drawSize") ? v.get("drawSize").getValue().asFloat() : 1f;
				MapChip[][] data = new MapChip[v.get("DATA").getElements().size()][];
				for (int y = 0; y < data.length; y++) {
					data[y] = new MapChip[v.get("DATA").getElements().get(y).getElements().size()];
					for (int x = 0; x < data[y].length; x++) {
						data[y][x] = chipSet.get(v.get("DATA").getElements().get(y).getElements().get(x).getValue().asId());
					}
				}
				var l = new FMNomalLayerSprite(chipSet, drawSize, above, data);
				if (this.debugMode) {
					l.setDebugMode(true);
				}
				nomalLayerSprite.add(l);
			}
		}

		//ANIMATION_LAYER
		{
			for (var v : f.getData().stream().filter(p -> p.getKey().value().equals("ANIMATION_LAYER")).toList()) {
				D2Idx idx = v.get("idx").getValue().asD2IdxCSV();
				List<KImage> image;
				if (f.get("ANIMATION_LAYER").has("cutW")) {
					int w = f.get("ANIMATION_LAYER").get("cutW").getValue().asInt();
					int h = f.get("ANIMATION_LAYER").get("cutH").getValue().asInt();
					image = f.get("ANIMATION_LAYER").get("image").getValue().asKImageFile().splitRows(0, w, h);
				} else {
					image = List.of(f.get("ANIMATION_LAYER").get("image").getValue().asKImageFile());
				}
				FrameTimeCounter tc = v.get("frame").getValue().asFrameTimeCounterCSV();
				if (v.has("drawSize")) {
					image = image.stream().map(p -> p.resizeTo(v.get("drawSize").getValue().asFloat())).toList();
				}
				Animation a = new Animation(tc, image);
				animationLayerSprite.add(new FMAnimationLayerSprite(nomalLayerSprite.get(0).getChipDrawSize(), idx, a));
			}
		}

		//FRONT_LAYER
		{
			List<KImage> image;
			if (f.get("FRONT_LAYER").has("cutW")) {
				int w = f.get("FRONT_LAYER").get("cutW").getValue().asInt();
				int h = f.get("FRONT_LAYER").get("cutH").getValue().asInt();
				image = f.get("FRONT_LAYER").get("image").getValue().asKImageFile().splitRows(0, w, h);
			} else {
				image = List.of(f.get("FRONT_LAYER").get("image").getValue().asKImageFile());
			}

			float drawSize = f.get("FRONT_LAYER").has("drawSize") ? f.get("FRONT_LAYER").get("drawSize").getValue().asFloat() : 1f;
			float angle = f.get("FRONT_LAYER").get("angle").getValue().asFloat();
			float speed = f.get("FRONT_LAYER").get("speed").getValue().asFloat();
			float tp = f.get("FRONT_LAYER").get("tp").getValue().asFloat();
			TimeCounter tc = f.get("FRONT_LAYER").has("frame")
					? f.get("FRONT_LAYER").get("frame").getValue().asFrameTimeCounterCSV()
					: TimeCounter.always(TimeCounterState.INACTIVE);

			this.frontLayerSprite = new FMFrontLayerSprite(tp, drawSize, new KVector(angle, speed), tc, image);
		}

		//SCRIPT_MAP
		this.eventScriptMap = new FieldEventScriptMap();
		{
			if (f.has("EVENT") && f.get("EVENT").getElements() != null) {
				for (var v : f.get("EVENT").getElements()) {
					if (v.getKey().is("cloneToRim")) {
						continue;
					}
					D2Idx idx = v.getKey().asD2IdxCSV();
					ScriptCall sc = v.getValue().asScriptCall();
					this.eventScriptMap.add(idx, sc);
				}
				//CLONE RIM
				if (f.get("EVENT").has("cloneToRim")) {
					D2Idx tgt = f.get("EVENT").get("cloneToRim").getValue().asD2IdxCSV();
					if (!this.eventScriptMap.has(tgt)) {
						throw new FileFormatException("FM : cloneToRim, but tgt event not found : " + tgt);
					}
					ScriptCall sc = this.eventScriptMap.get(tgt);
					int w = nomalLayerSprite.get(0).getDataWidth() - 1;
					int h = nomalLayerSprite.get(0).getDataHeight() - 1;
					for (int i = 0; i < nomalLayerSprite.get(0).getDataWidth(); i++) {
						this.eventScriptMap.add(new D2Idx(0, i), sc);
						this.eventScriptMap.add(new D2Idx(i, 0), sc);
						this.eventScriptMap.add(new D2Idx(w, i), sc);
						this.eventScriptMap.add(new D2Idx(i, h), sc);
					}
				}
			}
		}
		//NPC
		this.npcMap = new FieldNPCMap();
		{
			IniFile npcList = getNPCList();
			if (npcList.exists()) {
				for (var v : npcList.load()) {
					D2Idx idx = v.getKey().asUniversalValue().trim().asD2IdxCSV();
					//ファイルパス
					File file = new File(getDir() + "npc/" + v.getValue().trim().value() + ".npc.txt");
					NPC npc = new NPC(file, this, idx).load();
					npc.asScript().load();

					this.npcMap.add(idx, npc);
				}
				npcList.free();
			}
		}

		if (loadScriptCall) {
			if (f.has("LOAD_SCRIPT")) {
				if (!ScriptSystem.getInstance().isLoaded()) {
					throw new IllegalStateException("FM LOAD_SCRIPT: ScriptSystem is not yet init() : " + this);
				}
				f.get("LOAD_SCRIPT").getElements().forEach(v -> {
					ScriptCall s = v.getKey().asScriptCall();
					if (GameSystem.isDebugMode()) {
						GameLog.print("--- " + s);
					}
					s.exec();
				});
			}
		}

		f.free();
		loaded = true;

		if (GameSystem.isDebugMode()) {
			GameLog.removeIndent();
			GameLog.print("-FM LOAD END");
		}

		return this;
	}

	@Override
	public void free() {
		miniMapLabelStorage.clear();
		if (backLayerSprite != null) {
			backLayerSprite.free();
		}
		nomalLayerSprite.forEach(p -> p.free());
		nomalLayerSprite.clear();
		frontLayerSprite.free();

		miniMapLabelStorage = null;
		backLayerSprite = null;
		nomalLayerSprite = null;
		frontLayerSprite = null;
		eventScriptMap.free();
		eventScriptMap = null;
		npcMap.free();
		npcMap = null;

		loaded = false;
		GameLog.print("FM FREE : " + getId());
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	//--------------------------------------------------------------------------
	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
		nomalLayerSprite.forEach(p -> p.setDebugMode(debugMode));
	}

	@NewInstance
	public Storage<MiniMapLabel> getMiniMapLabelStorage() {
		return miniMapLabelStorage.clone();
	}

	public FMBackLayerSprite getBackLayerSprite() {
		return backLayerSprite;
	}

	@NewInstance
	public List<FMNomalLayerSprite> getNomalLayerSprite() {
		return new ArrayList<>(nomalLayerSprite);
	}

	@NewInstance
	public List<FMAnimationLayerSprite> getAnimationLayerSprite() {
		return new ArrayList<>(animationLayerSprite);
	}

	public FMFrontLayerSprite getFrontLayerSprite() {
		return frontLayerSprite;
	}

	public LayeredTile getTile(D2Idx i) {
		//iが領域外の場合、CLOSEで終わる
		if (i.x < 0 || i.y < 0 || i.x >= nomalLayerSprite.get(0).getDataWidth() || i.y >= nomalLayerSprite.get(0).getDataHeight()) {
			return new LayeredTile(i, List.of(MapChipAttribute.CLOSE));
		}

		List<MapChipAttribute> c = new ArrayList<>();
		for (var v : nomalLayerSprite) {
			if (v.isInArea(i)) {
				c.add(v.of(i).getAttr());
			}
		}
		//ノーマルレイヤーがすべてVOIDだった場合はBackレイヤーの属性を入れる
		if (c.isEmpty() || c.stream().allMatch(p -> p == MapChipAttribute.VOID)) {
			if (backLayerSprite != null) {
				c.add(0, backLayerSprite.getAttr());
			}
		}
		return new LayeredTile(i, c);
	}

	public int getLayer0ChipSize() {
		return nomalLayerSprite.get(0).getChipDrawSize();
	}

	@NotNewInstance
	public FMNomalLayerSprite getLayer0() {
		return nomalLayerSprite.get(0);
	}

	@NewInstance
	public Point2D.Float getLayer0Location() {
		return nomalLayerSprite.get(0).getLocation();
	}

	@NotNewInstance
	public FieldEventScriptMap getEventScriptMap() {
		return eventScriptMap;
	}

	@NotNewInstance
	public FieldNPCMap getNPCMap() {
		return npcMap;
	}

	public KImage createMiniMap(int w, int h, @Nullable D2Idx pcLocation, boolean label) {
		//back Layer
		Color backColor = backLayerSprite != null ? backLayerSprite.getImage().averageColor() : new Color(0, 0, 0, 0);
		KImage res = new KImage(nomalLayerSprite.get(0).getDataWidth(), nomalLayerSprite.get(0).getDataHeight()).fillBy(backColor);

		//nomal layer
		{
			KImage.KRaster r = res.asRaster();
			for (var l : nomalLayerSprite) {
				for (int y = 0; y < l.getData().length; y++) {
					for (int x = 0; x < l.getData()[y].length; x++) {
						KImage cell = l.getData()[y][x].getImage();
						if (pcLocation != null) {
							if (pcLocation.x == x && pcLocation.y == y) {
								r.of(x, y).to(Color.RED);
								continue;
							}
						}
						if (!cell.hasClaerPixel()) {
							r.of(x, y).to(cell.averageColor());
						}
					}
				}
			}
			res = r.updateImage();
		}

		//resize
		float ws = w / res.getWidth();
		float hs = h / res.getHeight();
		res = res.resizeTo(ws, hs);

		//label
		if (label) {
			Graphics2D g2 = res.createGraphics2D();
			for (MiniMapLabel l : miniMapLabelStorage) {
				TextLabelSprite s = l.getSprite();
				g2.setFont(s.getLabelModel().getFontConfig().getFont());
				g2.setColor(Color.BLACK);
				float x = l.getX() * ws;
				float y = l.getY() * hs + 8;
				s.setLocationByCenter(new Point2D.Float(x, y));
				g2.drawString(I18N.get(s.getText()), (int) s.getX() + 2, (int) s.getY() + 2);
				g2.setColor(Color.WHITE);
				g2.drawString(I18N.get(s.getText()), (int) s.getX(), (int) s.getY());
			}
			g2.dispose();
		}

		return res;
	}

	@Override
	public String toString() {
		return "FieldMap{ id=" + getId() + ", debugMode=" + debugMode + ", loaded=" + loaded + '}';
	}

}
