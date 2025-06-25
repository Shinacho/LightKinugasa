 /*
  * MIT License
  *
  * Copyright (c) 2025 しなちょ
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */


package kinugasa.game.field4;

import java.awt.Color;
import java.awt.Graphics2D;
import kinugasa.game.system.NPCSprite;
import kinugasa.game.system.PCSprite;
import kinugasa.object.FourDirection;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import kinugasa.game.GameLog;
import kinugasa.game.GameOption;
import kinugasa.game.GraphicsContext;
import kinugasa.game.I18N;
import kinugasa.game.I18NText;
import kinugasa.game.LoopCall;
import kinugasa.game.system.GameSystem;
import kinugasa.game.system.GameSystemException;
import kinugasa.game.ui.MessageWindow;
import kinugasa.game.ui.SimpleMessageWindowModel;
import kinugasa.game.ui.Text;
import kinugasa.game.ui.TextLabelSprite;
import kinugasa.game.ui.TextStorage;
import kinugasa.game.ui.TextStorageStorage;
import kinugasa.graphics.ARGBColor;
import kinugasa.graphics.Animation;
import kinugasa.graphics.ImageUtil;
import kinugasa.graphics.RenderingQuality;
import kinugasa.graphics.SpriteSheet;
import kinugasa.object.Drawable;
import static kinugasa.object.FourDirection.EAST;
import static kinugasa.object.FourDirection.NORTH;
import static kinugasa.object.FourDirection.SOUTH;
import static kinugasa.object.FourDirection.WEST;
import kinugasa.object.KVector;
import kinugasa.resource.Disposable;
import kinugasa.graphics.KImage;
import kinugasa.resource.sound.Sound;
import kinugasa.resource.text.XMLElement;
import kinugasa.resource.text.XMLFile;
import kinugasa.util.FrameTimeCounter;
import kinugasa.util.ManualTimeCounter;
import kinugasa.util.Random;
import kinugasa.object.ID;
import kinugasa.resource.sound.SoundStorage;

/**
 *
 * @vesion 1.0.0 - 2022/11/08_17:18:11<br>
 * @author Shinacho<br>
 */
public class FieldMap implements Drawable, ID, Disposable {

	private static String enterOperation = "(A)";

	public static String getEnterOperation() {
		return enterOperation;
	}

	public static void setEnterOperation(String enterOperation) {
		FieldMap.enterOperation = enterOperation;
	}

	public FieldMap(String name, XMLFile data) {
		this.id = name;
		this.data = data;
	}

	@Override
	public String getId() {
		return id;
	}

	public static void setDebugMode(boolean debugMode) {
		FieldMap.debugMode = debugMode;
	}

	public static boolean isDebugMode() {
		return debugMode;
	}

	public static List<PCSprite> getPlayerCharacter() {
		return playerCharacter;
	}

	public static void setPlayerCharacter(List<PCSprite> playerCharacter) {
		FieldMap.playerCharacter = playerCharacter;
	}

	private final String id;
	private final XMLFile data;
	//------------------------------------------------
	private BackgroundLayerSprite backgroundLayerSprite; //nullable
	private List<FieldMapLayerSprite> backlLayeres = new ArrayList<>();
	private static List<PCSprite> playerCharacter = new ArrayList<>();
	private List<FieldMapLayerSprite> frontlLayeres = new ArrayList<>();
	private List<FieldAnimationSprite> animationLayer = new ArrayList<>();
	private List<BeforeLayerSprite> beforeLayerSprites = new ArrayList<>();
	//------------------------------------------------
	private NPCStorage npcStorage = new NPCStorage();
	private static Map<String, List<String>> removedNPC = new HashMap<>();
	private static Map<String, List<NPCSprite>> addedNPC = new HashMap<>();
	private NodeStorage nodeStorage = new NodeStorage();
	private TextStorage textStorage;//nullable
	private List<MiniMapLabel> miniMapLabels;
	//------------------------------------------------
	private FieldMapCamera camera;
	//------------------------------------------------
	private int chipW, chipH;//1タイルのサイズ
	private D2Idx currentIdx;// 自キャラクタ表示データ座標
	//------------------------------------------------
	private static boolean debugMode = false;
	//
	private boolean visible = true;
	//
	private TooltipModel tooltipModel = new SimpleTooltipModel();
	private MapNameModel mapNameModel = new SimpleMapNameModel();

	private LinkedList<D2Idx> prevLocationList = new LinkedList<>();
	private String enemyStorageName;
	//
	private float mg;
	private ManualTimeCounter encountCounter;
	private I18NText visibleName;
	//マップネームラベルの追加処理

	public static Map<String, List<NPCSprite>> getAddedNPC() {
		return addedNPC;
	}

	public static Map<String, List<String>> getRemovedNPC() {
		return removedNPC;
	}

	public D2Idx getCurrentIdx() {
		return currentIdx;
	}

	public TooltipModel getTooltipModel() {
		return tooltipModel;
	}

	public void setTooltipModel(TooltipModel tooltipModel) {
		this.tooltipModel = tooltipModel;
	}

	public BackgroundLayerSprite getBackgroundLayerSprite() {
		return backgroundLayerSprite;
	}

	public List<FieldMapLayerSprite> getBacklLayeres() {
		return backlLayeres;
	}

	public FieldMapLayerSprite getBaseLayer() {
		return backlLayeres.get(0);
	}

	public List<FieldMapLayerSprite> getFrontlLayeres() {
		return frontlLayeres;
	}

	public List<FieldAnimationSprite> getFrontAnimation() {
		return animationLayer;
	}

	public List<BeforeLayerSprite> getBeforeLayerSprites() {
		return beforeLayerSprites;
	}

	public NPCStorage getNpcStorage() {
		return npcStorage;
	}

	public LinkedList<D2Idx> getPrevLocationList() {
		return prevLocationList;
	}

	public NodeStorage getNodeStorage() {
		return nodeStorage;
	}

	public TextStorage getTextStorage() {
		return textStorage;
	}

	public int getChipW() {
		return chipW;
	}

	public int getChipH() {
		return chipH;
	}

	public FieldMapCamera getCamera() {
		return camera;
	}

	public MapNameModel getMapNameModel() {
		return mapNameModel;
	}

	public void setMapNameModel(MapNameModel mapNameModel) {
		this.mapNameModel = mapNameModel;
	}

	public String getEnemyStorageName() {
		return enemyStorageName;
	}

	private static FieldMap currentInstance;

	public static FieldMap getCurrentInstance() {
		return currentInstance;
	}

	void setBeforeLayerSprites(List<BeforeLayerSprite> beforeLayerSprites) {
		this.beforeLayerSprites = beforeLayerSprites;
	}

	float getMg() {
		return mg;
	}

	public I18NText getVisibleName() {
		return visibleName;
	}

	public FieldMap build() throws FieldMapDataException {
		visibleName = new I18NText(getId());
		data.load();
		XMLElement root = data.getFirst();
		int screenW = (int) (GameOption.getInstance().getWindowSize().width / GameOption.getInstance().getDrawSize());
		int screenH = (int) (GameOption.getInstance().getWindowSize().height / GameOption.getInstance().getDrawSize());

		//事前に別設定されたテキストストレージの取得
		//テキストのないマップの場合、ロードしないことを許可する
		if (root.getAttributes().contains("textStorageName")) {
			String textStorageName = root.getAttributes().get("textStorageName").asId();
			textStorage = TextStorageStorage.getInstance().get(textStorageName).build();
		}

		//FM描画倍率を取得
		mg = 1;
		if (root.hasAttribute("mg")) {
			mg = root.getAttributes().get("mg").asFloat();
		}
		//miniMapLabels
		{
			miniMapLabels = new ArrayList<>();
			for (XMLElement e : root.getElement("miniMapLabel")) {
				int x = e.getAttributes().get("x").asInt();
				int y = e.getAttributes().get("y").asInt();
				String val = e.getAttributes().get("value").asId();
				miniMapLabels.add(new MiniMapLabel(new D2Idx(x, y), val));
			}
		}

		// バックグラウンドレイヤー
		{
			if (root.getElement("background").size() >= 2) {
				throw new FieldMapDataException("background must be 0 or 1 : " + data);
			}
			if (root.hasElement("background")) {
				XMLElement backgroundElement = root.getElement("background").get(0);
				String imageName = backgroundElement.getAttributes().get("image").value();

				String[] frame = backgroundElement.getAttributes().get("frame").value().split(",");
				FrameTimeCounter tc = new FrameTimeCounter(Arrays.stream(frame).mapToInt(v -> Integer.parseInt(v)).toArray());

				int cutW = backgroundElement.getAttributes().get("w").asInt();
				int cutH = backgroundElement.getAttributes().get("h").asInt();

				List<BufferedImage> images = new SpriteSheet(imageName).split2D(cutW, cutH).images().stream().map(p -> p.asBufferedImage()).toList();
				backgroundLayerSprite = new BackgroundLayerSprite(screenW, screenH, mg);
				backgroundLayerSprite.build(tc, images);
			}

		}

		//出入り口ノード
		{
			for (XMLElement e : root.getElement("inOutNode")) {
				String name = e.getAttributes().get("name").value();
				int x = e.getAttributes().get("x").asInt();
				int y = e.getAttributes().get("y").asInt();
				String tgtMapName = e.getAttributes().get("tgtMap").value();
				String exitNodeName = e.getAttributes().get("exitNode").value();
				String tooltip = e.getAttributes().get("tooltip").value();
				NodeAccepter accepter = NodeAccepterStorage.getInstance().get(e.getAttributes().get("accepterName").value());
				FourDirection outDir = FourDirection.valueOf(e.getAttributes().get("outDir").value());
				Node node = Node.ofInOutNode(name, tgtMapName, exitNodeName, x, y, tooltip, accepter, outDir);
				if (e.getAttributes().contains("se")) {
					String soundName = e.getAttributes().get("se").value();
					node.setSe(SoundStorage.getInstance().get(soundName));
				}
				nodeStorage.add(node);
			}
		}
		//出口専用ノード
		{
			for (XMLElement e : root.getElement("outNode")) {
				String name = e.getAttributes().get("name").value();
				String outMapName = e.getAttributes().get("outMap").value();
				int x = e.getAttributes().get("x").asInt();
				int y = e.getAttributes().get("y").asInt();
				FourDirection dir = e.getAttributes().get("outDir").of(FourDirection.class);
				Node node = Node.ofOutNode(name, outMapName, x, y, dir);
				nodeStorage.add(node);
			}

		}
		// バックレイヤー
		{
			//	バックレイヤーが1つ以上ない場合エラー
			if (root.getElement("backLayer").isEmpty()) {
				throw new FieldMapDataException("backLayer is need 1 or more");
			}
			for (XMLElement e : root.getElement("backLayer")) {
				MapChipSet chipset = MapChipSetStorage.getInstance().get(e.getAttributes().get("chipSet").value());
				String lineSep = e.getAttributes().get("lineSeparator").value();
				String[] lineVal = e.getValue().replaceAll("\n", "").replaceAll("\r\n", "").replaceAll(" ", "").replaceAll("\t", "").split(lineSep);
				MapChip[][] data = new MapChip[lineVal.length][];
				for (int y = 0; y < lineVal.length; y++) {
					String[] val = lineVal[y].split(",");
					data[y] = new MapChip[val.length];
					for (int x = 0; x < val.length; x++) {
						data[y][x] = chipset.get(val[x]);
					}
				}
				int w = (int) (data[0][0].getImage().getWidth());
				int h = (int) (data[0][0].getImage().getWidth());

				FieldMapLayerSprite layerSprite = new FieldMapLayerSprite(chipset, w, h, mg, data);
				backlLayeres.add(layerSprite);
			}

		}
		// フロントレイヤー
		{
			for (XMLElement e : root.getElement("frontLayer")) {
				MapChipSet chipset = MapChipSetStorage.getInstance().get(e.getAttributes().get("chipSet").value());
				String lineSep = e.getAttributes().get("lineSeparator").value();
				String[] lineVal = e.getValue().replaceAll("\n", "").replaceAll("\r\n", "").replaceAll(" ", "").replaceAll("\t", "").split(lineSep);
				MapChip[][] data = new MapChip[lineVal.length][];
				for (int y = 0; y < lineVal.length; y++) {
					String[] val = lineVal[y].split(",");
					data[y] = new MapChip[val.length];
					for (int x = 0; x < val.length; x++) {
						data[y][x] = chipset.get(val[x]);
					}
				}
				int w = (int) (data[0][0].getImage().getWidth());
				int h = (int) (data[0][0].getImage().getWidth());

				FieldMapLayerSprite layerSprite = new FieldMapLayerSprite(chipset, w, h, mg, data);
				frontlLayeres.add(layerSprite);
			}

		}

		//ビフォアレイヤー
		{
			for (XMLElement e : root.getElement("before")) {
				String name = e.getAttributes().get("name").value();
				if (BeforeLayerSpriteStorage.getInstance().contains(name)) {
					beforeLayerSprites.add(BeforeLayerSpriteStorage.getInstance().get(name));
				} else {

					String imageName = e.getAttributes().get("image").value();
					float angle = e.getAttributes().contains("angle") ? e.getAttributes().get("angle").asFloat() : 0;
					float speed = e.getAttributes().contains("speed") ? e.getAttributes().get("speed").asFloat() : 0;
					float tp = e.getAttributes().contains("tp") ? e.getAttributes().get("tp").asFloat() : 1;
					float bmg = e.getAttributes().contains("mg") ? e.getAttributes().get("mg").asFloat() : 1;

					BeforeLayerSprite bls = new BeforeLayerSprite(name, ImageUtil.load(imageName), tp, bmg, new KVector(angle, speed));
					beforeLayerSprites.add(bls);
				}
			}

		}
		// チップサイズの取得
		chipW = (int) (getBaseLayer().getChip(0, 0).getImage().getWidth() * mg);
		chipH = (int) (getBaseLayer().getChip(0, 0).getImage().getHeight() * mg);

		//アニメーション
		{
			for (XMLElement e : root.getElement("animation")) {
				int x = e.getAttributes().get("x").asInt();
				int y = e.getAttributes().get("y").asInt();
				String image = e.getAttributes().get("image").value();
				String[] frame = e.getAttributes().get("frame").value().split(",");
				FrameTimeCounter tc = new FrameTimeCounter(Arrays.stream(frame).mapToInt(v -> Integer.parseInt(v)).toArray());
				int w = (int) (e.getAttributes().get("w").asInt());
				int h = (int) (e.getAttributes().get("h").asInt());
				List<KImage> images = new SpriteSheet(image).split2D(w, h).images().stream().map(p -> p.resizeTo(mg)).toList();
				int locationX = (int) (x * (chipW));
				int locationY = (int) (y * (chipH));
				animationLayer.add(new FieldAnimationSprite(new D2Idx(x, y), locationX, locationY, (int) (w * mg), (int) (h * mg),
						new Animation(tc, images)));
			}

		}

		// カメラ初期化
		camera = new FieldMapCamera(this);

		//NPC
		{
			for (XMLElement e : root.getElement("npc")) {
				String name = e.getAttributes().get("name").value();
				//削除されたNPCの場合は追加しない
				if (removedNPC.containsKey(this.getId())) {
					if (removedNPC.get(getId()).contains(name)) {
						continue;
					}
				}
				String initialIdxStr = e.getAttributes().get("initialIdx").value();
				D2Idx idx = new D2Idx(Integer.parseInt(initialIdxStr.split(",")[0]), Integer.parseInt(initialIdxStr.split(",")[1]));
				// 領域のチェック
				if (!getBaseLayer().include(idx)) {
					throw new FieldMapDataException("npc : " + name + " is out of bounds");
				}
				NPCMoveModel moveModel = NPCMoveModelStorage.getInstance().get(e.getAttributes().get("NPCMoveModel").value());

				Vehicle v = e.hasAttribute("vehicle") ? VehicleStorage.getInstance().get(e.getAttributes().get("vehicle").value()) : null;
				String textID = e.hasAttribute("textID") ? e.getAttributes().get("textID").value() : null;

				int frame = e.hasAttribute("frame") ? e.getAttributes().get("frame").asInt() : Integer.MAX_VALUE;

				BufferedImage image = ImageUtil.load(e.getAttributes().get("image").value());
				int sx, sw, sh;
				int ex, ew, eh;
				int wx, ww, wh;
				int nx, nw, nh;
				String[] cs = e.getAttributes().get("s").value().split(",");
				String[] ce = e.getAttributes().get("e").value().split(",");
				String[] cw = e.getAttributes().get("w").value().split(",");
				String[] cn = e.getAttributes().get("n").value().split(",");
				sx = Integer.parseInt(cs[0]);
				sw = Integer.parseInt(cs[1]);
				sh = Integer.parseInt(cs[2]);
				ex = Integer.parseInt(ce[0]);
				ew = Integer.parseInt(ce[1]);
				eh = Integer.parseInt(ce[2]);
				wx = Integer.parseInt(cw[0]);
				ww = Integer.parseInt(cw[1]);
				wh = Integer.parseInt(cw[2]);
				nx = Integer.parseInt(cn[0]);
				nw = Integer.parseInt(cn[1]);
				nh = Integer.parseInt(cn[2]);
				Animation south = new Animation(new FrameTimeCounter(frame), new SpriteSheet(image).rows(sx, sw, sh).images());
				Animation west = new Animation(new FrameTimeCounter(frame), new SpriteSheet(image).rows(wx, ww, wh).images());
				Animation east = new Animation(new FrameTimeCounter(frame), new SpriteSheet(image).rows(ex, ew, eh).images());
				Animation north = new Animation(new FrameTimeCounter(frame), new SpriteSheet(image).rows(nx, nw, nh).images());
				FourDirAnimation anime = new FourDirAnimation(south, west, east, north);
				FourDirection initialDir = FourDirection.valueOf(e.getAttributes().get("initialDir").value());
				int w = sw;
				int h = sh;
				float x = getBaseLayer().getX() + idx.x * chipW;
				float y = getBaseLayer().getY() + idx.y * chipH;
				NPCSprite npc = new NPCSprite(name, currentIdx, moveModel, v, this, textID, x, y, w, h, idx, anime, initialDir);
				//タッチイベント
				if (e.hasAttribute("touchEvent")) {
					npc.setTouchEvent(e.getAttributes().get("touchEvent").value());
				}
				npcStorage.add(npc);
			}
			//追加NPCを設定
			if (addedNPC.containsKey(getId())) {
				npcStorage.addAll(addedNPC.get(getId()));
			}
		}

		//BGMの処理
		{
			if (root.getElement("bgm").size() >= 2) {
				throw new FieldMapDataException("bgm must be 0 or 1 : " + data);
			}
			if (root.hasElement("bgm")) {
				XMLElement e = root.getElement("bgm").get(0);
				BGMMode mode = BGMMode.valueOf(e.getAttributes().get("mode").value());
				String soundName = e.getAttributes().get("soundName").value();
				bgm = SoundStorage.getInstance().get(soundName);
				switch (mode) {
					case NOTHING:
						break;
					case PAUSE:
						bgm.pause();
						break;
					case STOP:
						SoundStorage.getInstance().forEach(p -> p.stop().dispose());
						break;
					case STOP_AND_PLAY:
						SoundStorage.getInstance().forEach(p -> p.stop().dispose());
						bgm.load().stopAndPlay();
						break;
					default:
						throw new AssertionError("FM undefined bgm mode");
				}

			}
		}
		mapNameModel.reset();

		currentInstance = this;

		data.dispose();

		return this;
	}
	private Sound bgm;

	public Sound getBgm() {
		return bgm;
	}

	@Override
	public void draw(GraphicsContext g) {
		if (!visible) {
			return;
		}
		if (backgroundLayerSprite != null) {
			backgroundLayerSprite.draw(g);
		}
		backlLayeres.forEach(e -> e.draw(g));

		animationLayer.forEach(e -> e.draw(g));
		if (playerCharacter != null) {
			List<PCSprite> list = new ArrayList<>(playerCharacter);
			Collections.reverse(list);
			list.forEach(v -> v.draw(g));
		}
		npcStorage.forEach(e -> e.draw(g));

		frontlLayeres.forEach(e -> e.draw(g));
		if (debugMode) {
			DebugLayerSprite.debugDrawPC(g, playerCharacter, currentIdx, getBaseLayer().getLocation(), chipW, chipH);
			DebugLayerSprite.debugDrawNPC(g, npcStorage.asList(), getBaseLayer().getLocation(), chipW, chipH);
			DebugLayerSprite.debugDrawCamera(g, getBaseLayer().getLocation(), chipW, chipH);
		}
		beforeLayerSprites.forEach(e -> e.draw(g));
		if (mapNameModel != null) {
			mapNameModel.drawMapName(this, g);
		}
		if (tooltipModel != null) {
			tooltipModel.drawTooltip(this, g);
		}

	}

	@Override
	public void dispose() {
		if (backgroundLayerSprite != null) {
			backgroundLayerSprite.dispose();
			backgroundLayerSprite = null;
		}
		backlLayeres.forEach(e -> e.dispose());
		backlLayeres.clear();
		frontlLayeres.forEach(e -> e.dispose());
		frontlLayeres.clear();
		animationLayer.clear();
		beforeLayerSprites.clear();
		npcStorage.clear();
		nodeStorage.clear();
		if (textStorage != null) {
			textStorage.clear();
		}
		if (GameSystem.isDebugMode()) {
			GameLog.print("FieldMap[" + getId() + "] is disposed");
		}
	}

	public void update() {
		if (prevLocationList.isEmpty()) {
			for (int i = 0; i < playerCharacter.size() - 1; i++) {
				prevLocationList.add(currentIdx);
			}
			if (playerCharacter.size() > 1) {
				playerCharacter.subList(1, playerCharacter.size()).forEach(v -> v.setCurrentIdx(currentIdx));
				playerCharacter.subList(1, playerCharacter.size()).forEach(v -> v.setTargetIdx(currentIdx));
			}
		}
		npcStorage.forEach(c -> c.update());
		if (mw != null) {
			mw.update();
		}
		if (playerCharacter.size() > 1) {
			for (int i = 1, j = 0; i < playerCharacter.size(); i++) {
				playerCharacter.get(i).updatePartyMemberLocation(this, prevLocationList.get(j));
				if (j < prevLocationList.size() - 1) {
					j++;
				}
			}
		}
		//スタックチェック
		if (GameSystem.isDebugMode()) {
			if (!getTile(currentIdx).canStep()) {
				throw new GameSystemException("FM : PC IS STUCK!!!!!!!!:" + getTile(currentIdx));
			}
		}
	}

	public ManualTimeCounter getEncountCounter() {
		return encountCounter;
	}

	public void setEncountCounter(ManualTimeCounter encountCounter) {
		this.encountCounter = encountCounter;
	}

	@LoopCall
	public void move() {
		if (getBaseLayer().getVector().getSpeed() == 0) {
			return;
		}
		D2Idx prevIdx = currentIdx.clone();
		camera.move();
		if (!prevIdx.equals(currentIdx)) {
			if (debugMode) {
				kinugasa.game.GameLog.print("FM MOVE " + currentIdx + " /TILE=" + getCurrentTile());
			}
			prevLocationList.addFirst(prevIdx);
			if (playerCharacter.size() <= prevLocationList.size()) {
				prevLocationList.removeLast();
			}
			for (int i = 1, j = 0; i < playerCharacter.size(); i++) {
				playerCharacter.get(i).updatePartyMemberLocation(this, prevLocationList.get(j));
				if (j < prevLocationList.size() - 1) {
					j++;
				}
			}
		}
	}

	public void setVector(KVector vector) {
		camera.setVector(vector.reverse());
	}

	public void setSpeed(float speed) {
		camera.setSpeed(speed);
	}

	public void setAngle(float angle) {
		camera.setAngle(angle);
	}

	public void setLocation(Point2D.Float location) {
		camera.setLocation(location);
	}

	public void setLocation(float x, float y) {
		camera.setLocation(x, y);
	}

	public void setX(float x) {
		camera.setX(x);
	}

	public void setY(float y) {
		camera.setY(y);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public FieldMapTile getTile(D2Idx idx) throws ArrayIndexOutOfBoundsException {
		List<MapChip> chip = new ArrayList<>();
		for (FieldMapLayerSprite s : backlLayeres) {
			chip.add(s.getChip(idx.x, idx.y));
		}
		for (FieldMapLayerSprite s : frontlLayeres) {
			chip.add(s.getChip(idx.x, idx.y));
		}
		NPCSprite npc = npcStorage.get(idx);
		Node node = nodeStorage.get(idx);

		if (playerCharacter.isEmpty()) {
			return new FieldMapTile(chip, npc, null, node);
		}

		return new FieldMapTile(chip, npc, idx.equals(currentIdx) ? playerCharacter.get(0) : null, node);
	}

	public FieldMapTile getCurrentTile() {
		return getTile(currentIdx);
	}

	/**
	 * プレイヤーの座標を更新します。x,yが中心（プレイヤーロケーション）になるようにマップの表示座標を設定します。
	 * このメソッドでは、移動可能かどうかの判定は行いません。移動判定はgetTileから行ってください。
	 *
	 * @param idx マップデータのインデックス。
	 */
	public void setCurrentIdx(D2Idx idx) {
		this.currentIdx = idx.clone();
	}

	/**
	 * フィールドマップのキャラクタとビフォアレイヤー以外を描画した画像を生成します。 これは地図＜ミニマップ＞を作成する機能です。
	 * バックグラウンド及びフロントアニメーションは、現在の状態が使用されます。 NPC及びキャラクタは表示されません。
	 *
	 * @param w 結果イメージの幅ピクセル。
	 * @param h 結果イメージの高さピクセル。
	 * @param pcLocation PCの位置に点を打つかどうか。trueの場合赤点をつける。
	 * @param label ミニマップラベルの描画をするかどうか。trueの場合描画する。
	 * @return 指定の拡大率で描画された画像。
	 */
	public KImage createMiniMap(int w, int h, boolean pcLocation, boolean label) {
		//ベースレイヤーが全部同じタイルの場合は背景と判断して1を取る
		FieldMapLayerSprite tgt
				= getBaseLayer().allIs(getBaseLayer().getChip(0, 0)) && backlLayeres.size() >= 2
				? this.backlLayeres.get(1) : getBaseLayer();

		int imageW = (int) (tgt.getChip(0, 0).getImage().getWidth() * mg);
		int imageH = (int) (tgt.getChip(0, 0).getImage().getHeight() * mg);
		List<List<KImage>> baseImage = new KImage(tgt.getImage()).split2D(imageW, imageH);

		BufferedImage image = ImageUtil.newImage(tgt.getDataWidth(), tgt.getDataHeight());
		int[] pix = new int[tgt.getDataWidth() * tgt.getDataHeight()];

		if (getBackgroundLayerSprite() != null) {
			int col = ARGBColor.toARGB(getBackgroundLayerSprite().getImage().averageColor());
			for (int i = 0; i < pix.length; i++) {
				pix[i] = col;
			}
		}

		for (int i = 0, y = 0; y < baseImage.size(); y++) {
			for (int x = 0; x < baseImage.get(y).size(); x++) {
				//ベースイメージyxに透明タイルがある場合はこのタイルをスキップする
				if (baseImage.get(y).get(x).hasClaerPixel()) {
					i++;
					continue;
				}
				int col = ARGBColor.toARGB(baseImage.get(y).get(x).averageColor());
				if (ARGBColor.getAlpha(col) == 0) {
					i++;
					continue;
				}
				if (pcLocation && currentIdx.x == x && currentIdx.y == y) {
					pix[i] = ARGBColor.RED;
					i++;
					continue;
				}
				pix[i] = col;
				i++;
			}
		}
		ImageUtil.setPixel(image, pix);
		baseImage = null;

		//スケール計算
		float ws = w / image.getWidth();
		float hs = h / image.getHeight();
		KImage result = new KImage(image).resizeTo(ws, hs);

		//ミニマップラベルの処理
		if (label) {
			Graphics2D g2 = ImageUtil.createGraphics2D(result.asBufferedImage(), RenderingQuality.SPEED);
			for (MiniMapLabel l : miniMapLabels) {
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

		return result;

	}

	public boolean canTalk() {
		D2Idx idx = playerDirIdx();
		// 範囲外のチェック
		if (idx.x < 0 || idx.y < 0) {
			return false;
		}
		if (idx.x >= getBaseLayer().getDataWidth() || idx.y >= getBaseLayer().getDataHeight()) {
			return false;
		}
		//idxが机の場合はその先のタイルも確認
		FieldMapTile tile = getTile(idx);
		if (tile.getChip().stream().map(p -> p.getAttr()).anyMatch(p -> p.equals(MapChipAttributeStorage.getInstance().get("机")))) {

			D2Idx idx2 = idx.clone();
			FourDirection currentDir = playerCharacter.get(0).getCurrentDir();

			switch (currentDir) {
				case EAST ->
					idx2.x += 1;
				case WEST ->
					idx2.x -= 1;
				case NORTH ->
					idx2.y -= 1;
				case SOUTH ->
					idx2.y += 1;
			}
			return getTile(idx2).getNpc() != null;
		}
		//NPCがいるかどうかを返す
		return tile.getNpc() != null;
	}

	//プレイヤーの向いている方向のD2IDXを返します。
	public D2Idx playerDirIdx() {
		D2Idx idx = this.currentIdx.clone();
		FourDirection currentDir = playerCharacter.get(0).getCurrentDir();

		switch (currentDir) {
			case EAST:
				idx.x += 1;
				break;
			case WEST:
				idx.x -= 1;
				break;
			case NORTH:
				idx.y -= 1;
				break;
			case SOUTH:
				idx.y += 1;
				break;
		}
		return idx;
	}

	private MessageWindow mw;

	/**
	 * プレイヤーキャラクタの向いている方向にいるNPCに対して、いい感じの位置にメッセ-ーウインドウを表示します。
	 * メッセージウインドウのモデルはSimpleMessageWindowModelが使用されます。
	 *
	 * @return
	 * メッセージウインドウ。NPCがいない場合はnoNPCMessageを表示します。ただし、これは例外処置的なもので、この制御は、ゲームマネージャ側で行うべきです。
	 */
	public MessageWindow talk() {
		D2Idx idx = playerDirIdx();
		FieldMapTile tile = getTile(idx);
		if (tile.getChip().stream().map(p -> p.getAttr()).anyMatch(p -> p.equals(MapChipAttributeStorage.getInstance().get("机")))) {

			D2Idx idx2 = idx.clone();
			FourDirection currentDir = playerCharacter.get(0).getCurrentDir();

			switch (currentDir) {
				case EAST ->
					idx2.x += 1;
				case WEST ->
					idx2.x -= 1;
				case NORTH ->
					idx2.y -= 1;
				case SOUTH ->
					idx2.y += 1;
			}
			tile = getTile(idx2);
		}
		NPCSprite n = tile.getNpc();
		if (n != null) {
			n.notMove();
		}
		//NPCが画面下半分にいる場合はメッセージウインドウを上に表示。上半分にいる場合は下に表示。
		float buffer = 24;
		float x = buffer;
		float y = n == null || n.getCenterY() < GameOption.getInstance().getWindowSize().width / 2
				? GameOption.getInstance().getWindowSize().height / GameOption.getInstance().getDrawSize() / 2 + buffer * 2
				: buffer;
		float w = GameOption.getInstance().getWindowSize().width / GameOption.getInstance().getDrawSize() - (buffer * 2);
		float h = GameOption.getInstance().getWindowSize().height / GameOption.getInstance().getDrawSize() / 3;
		Text t = n == null ? Text.empty() : textStorage.get(n.getTextID());
		if (n != null) {
			n.to(playerCharacter.get(0).getCurrentDir().reverse());
		}
		t.reset();
		mw = new MessageWindow(x, y, w, h, new SimpleMessageWindowModel(), textStorage, t);

		if (isDebugMode()) {
			kinugasa.game.GameLog.print("FM TALK: textID[" + t.getId() + "]");
		}

		return mw;
	}

	/**
	 * メッセージウインドウを閉じます。ついでにNPCの移動を解除します。
	 */
	public void closeMessagWindow() {
		if (mw == null) {
			return;
		}
		mw.setVisible(false);
		if (mw != null) {
			mw = null;
		}
		npcStorage.forEach(c -> c.canMove());
	}

	MessageWindow getMessageWindow() {
		return mw;
	}

	void setMW(MessageWindow mw) {
		this.mw = mw;
	}

	/**
	 * NPCの移動を一括停止します。
	 */
	public void NPCMoveStop() {
		npcStorage.forEach(c -> c.notMove());
	}

	/**
	 * NPCの移動停止を一括解除します。
	 */
	public void NPCMoveStart() {
		npcStorage.forEach(c -> c.canMove());
	}

	public FieldMap changeMap() {
		return changeMap(getCurrentTile().getNode());
	}

	public FieldMap changeMap(Node n) {
		for (Node o : getNodeStorage()) {
			if (o.getSe() != null) {
				o.getSe().dispose();
			}
		}
		dispose();
		if (n.getSe() != null) {
			n.getSe().load().stopAndPlay();
		}
		Sound currentBGM = this.bgm;
		FieldMap fm = FieldMapStorage.getInstance().get(n.getExitFieldMapName()).build();
		if (fm.bgm == null) {
			fm.bgm = currentBGM;
		}
		if (n.getExitNodeName() == null) {
			fm.setCurrentIdx(n.getIdx());
		} else {
			if (!fm.getNodeStorage().contains(n.getExitNodeName())) {
				throw new GameSystemException("Node not found : " + fm.getId() + "." + n.getExitNodeName() + " from " + this.getId());
			}
			fm.setCurrentIdx(fm.getNodeStorage().get(n.getExitNodeName()).getIdx());
		}
		fm.getCamera().updateToCenter();
		fm.prevLocationList.clear();
		if (n.getExitNodeName() != null
				&& fm.getNodeStorage() != null
				&& fm.getNodeStorage().get(n.getExitNodeName()) != null
				&& fm.getNodeStorage().get(n.getExitNodeName()).getOutDir() != null) {
			FieldMap.getPlayerCharacter().get(0).to(n.getOutDir());
		}
		if (FieldMap.getPlayerCharacter().size() > 1) {
			FieldMap.getPlayerCharacter().subList(1, getPlayerCharacter().size()).forEach(v -> v.setCurrentIdx(getCurrentIdx()));
			FieldMap.getPlayerCharacter().subList(1, getPlayerCharacter().size()).forEach(v -> v.setLocation(playerCharacter.get(0).getLocation()));
		}
		if (GameSystem.isDebugMode()) {
			kinugasa.game.GameLog.print("CHANGE_MAP: " + n);
		}
		return fm;
	}

	@Override
	public String toString() {
		return "FieldMap{" + "name=" + id + '}';
	}

}
