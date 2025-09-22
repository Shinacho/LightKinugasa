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

import java.awt.geom.Point2D;
import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import kinugasa.game.GameLog;
import kinugasa.game.I18NText;
import kinugasa.game.annotation.LoopCall;
import kinugasa.game.annotation.Nullable;
import kinugasa.game.annotation.Singleton;
import kinugasa.game.event.ScriptBlockType;
import kinugasa.game.event.ScriptCall;
import kinugasa.game.event.ScriptSystem;
import kinugasa.game.system.GameSystem;
import kinugasa.game.system.I18NConst;
import kinugasa.game.system.actor.Follower;
import kinugasa.game.system.actor.NPC;
import kinugasa.graphics.GraphicsContext;
import kinugasa.graphics.KImage;
import kinugasa.object.Drawable;
import kinugasa.object.FourDirection;
import kinugasa.object.KVector;
import kinugasa.resource.sound.Sound;
import kinugasa.resource.text.IniFile;

/**
 * FieldMapSystem.<br>
 *
 * @vesion 1.0.0 - 2025/07/21_10:15:58<br>
 * @author Shinacho.<br>
 */
@Singleton
public final class FieldMapSystem implements Drawable {

	private static final FieldMapSystem INSTANCE = new FieldMapSystem();

	private FieldMapSystem() {
	}

	public static FieldMapSystem getInstance() {
		return INSTANCE;
	}

	public void init(File dir) {
		FieldMapStorage.getInstance().init(dir);
	}

	private FieldMap fieldMap;
	private FieldMapCamera camera;
	private Vehicle vehicle = Vehicle.WALK;

	//EVENT
	@Nullable
	private ScriptCall currentScript;
	//
	private boolean nodeLock = false;
	private FieldMapNode currentNode;
	private TooltipText tooltipText;

	private final HashSet<NPC> currentNPCs = new HashSet<>();
	private final HashSet<NPC> prevNPCs = new HashSet<>();

	//
	private void updateEvents(boolean pcMove) {
		D2Idx i = camera.getPcLocation();
		if (pcMove) {
			if (currentScript != null) {
				//STEP_DWON実行
				currentScript.exec(ScriptBlockType.STEP_DOWN);
			}
			if (fieldMap.getEventScriptMap().has(i)) {
				currentScript = fieldMap.getEventScriptMap().get(i);

				//STEPON実行
				currentScript.exec(ScriptBlockType.STEP_ON);
			} else {
				currentScript = null;
			}
		}

		//NPC
		prevNPCs.clear();
		prevNPCs.addAll(currentNPCs);
		currentNPCs.clear();

		//currentへの追加
		int x = i.x;
		int y = i.y;
		addNPC(x - 1, y - 1);
		addNPC(x, y - 1);
		addNPC(x + 1, y - 1);
		addNPC(x - 1, y);
		addNPC(x, y);
		addNPC(x + 1, y);
		addNPC(x - 1, y + 1);
		addNPC(x, y + 1);
		addNPC(x + 1, y + 1);

		//currentにいなくてprevにいるNPCのLEAVE実行
		for (var v : prevNPCs) {
			if (!currentNPCs.contains(v)) {
				v.asScript().load().getBlockOf(ScriptBlockType.LEAVE).exec().free();
			}
		}
		//currentnに入ったNPCのAPPROACH実行。ただしprevにいる場合は実行しない。
		for (var v : currentNPCs) {
			if (!prevNPCs.contains(v)) {
				v.asScript().load().getBlockOf(ScriptBlockType.APPROACH).exec();
			}
		}
	}

	private void addNPC(int x, int y) {
		D2Idx i = new D2Idx(x, y);
		if (!fieldMap.getNPCMap().has(i)) {
			return;
		}
		currentNPCs.add(fieldMap.getNPCMap().get(i));
	}

	private boolean talking = false;
	private NPC talkingNPC = null;

	public boolean talk() {
		FourDirection dir = GameSystem.getInstance().getPcList().get(0).getSprite().getWalkAnimtion().getCurrentDir();
		D2Idx i = getCamera().getPcLocation().clone().add(dir, 1);
		if (getCurrent().getNPCMap().has(i)) {
			talkingNPC = getCurrent().getNPCMap().get(i);

			talkingNPC.getSprite().getMoveModel().lockLocation();
			talkingNPC.getSprite().to(dir.reverse());

			//currentNPC設定
			ScriptSystem.getInstance().setCurrentTalkNpc(talkingNPC);

			//TALKイベント実行
			if (!talkingNPC.asScript().getBlockOf(ScriptBlockType.TALK).getCmds().isEmpty()) {
				talkingNPC.asScript().load().getBlockOf(ScriptBlockType.TALK).resetIdx();
				talkingNPC.asScript().getBlockOf(ScriptBlockType.TALK).exec();
				return talking = true;
			}
		}
		return talking = false;
	}

	public void touch(NPC n) {
		talking = false;
		if (!n.asScript().getBlockOf(ScriptBlockType.TOUCH).getCmds().isEmpty()) {
			talkingNPC = n;

			talkingNPC.getSprite().getMoveModel().lockLocation();

			//currentNPC設定
			ScriptSystem.getInstance().setCurrentTalkNpc(talkingNPC);

			talkingNPC.asScript().load().getBlockOf(ScriptBlockType.TOUCH).resetIdx();
			talkingNPC.asScript().getBlockOf(ScriptBlockType.TOUCH).exec();
			return;
		}
		return;
	}

	public void setTalking(boolean talking) {
		this.talking = talking;
	}

	public boolean isTalking() {
		return talking;
	}

	public void unsetTalk() {
		this.talking = false;
		if (talkingNPC != null) {
			talkingNPC.getSprite().getMoveModel().unlockLocation();
			talkingNPC = null;
		}
	}

	@Nullable
	public NPC getTalkingNPC() {
		return talkingNPC;
	}

	public void setInitialMap(String id) {
		fieldMap = FieldMapStorage.getInstance().get(id).load();
		camera = FieldMapCamera.getInstance();
		camera.set(fieldMap);
		updateEvents(true);
	}

	public void setVehicle(Vehicle currentVehicle) {
		this.vehicle = currentVehicle;
	}

	public Vehicle getCurrentVehicle() {
		return vehicle;
	}

	public FieldMap getCurrent() {
		return fieldMap;
	}

	public FieldMapCamera getCamera() {
		return camera;
	}

	public void setLocation(D2Idx i) {
		camera.setLocationByCenter(i);
	}

	public void resetFollowerLocation() {
		Point2D.Float pcCenter = camera.getPcLocationOnScreen();
		var pcList = GameSystem.getInstance().getPcList();
		for (var v : pcList) {
			v.getSprite().setLocationByCenter(pcCenter);
			v.getSprite().setCurrentLocationOnMap(camera.getPcLocation());
			if (v instanceof Follower f) {
				f.setFollowTgt(fieldMap, v.getSprite().getCurrentLocationOnMap());
			}
		}
	}

	public LayeredTile getTile() {
		return fieldMap.getTile(camera.getPcLocation());
	}

	public KImage createMiniMap(int w, int h, boolean pcLocation, boolean label) {
		return fieldMap.createMiniMap(w, h, pcLocation ? camera.getPcLocation() : null, label);
	}
	//----------------------------------------------オペレーション----------------------------------

	public void setNode(FieldMapNode n) {
		if (nodeLock) {
			return;
		}
		this.currentNode = n;
		updateTooltip();
	}

	public void setNode(String next, D2Idx i, FourDirection dir, I18NText tooltip) {
		if (nodeLock) {
			return;
		}
		setNode(next, i, dir, null, tooltip);
	}

	public void setNode(String next, D2Idx i, FourDirection dir, Sound s, I18NText tooltip) {
		if (nodeLock) {
			return;
		}
		currentNode = new FieldMapNode(next, i, dir, s, new SimpleMapChangeEffect(), tooltip);
		updateTooltip();
	}

	public void unsetNode() {
		if (nodeLock) {
			return;
		}
		currentNode = null;
		updateTooltip();
	}

	public boolean hasNode() {
		return currentNode != null;
	}

	private void updateTooltip() {
		if (tooltipText == null) {
			tooltipText = new TooltipText();
		}
		//PC0の向いている方向に話せるNPCがいる場合
		D2Idx tgtIdx = camera.getPcLocation().add(GameSystem.getInstance().getPcList().get(0).getSprite().getWalkAnimtion().getCurrentDir(), 1);
		if (fieldMap.getNPCMap().has(tgtIdx)) {
			//すでに話すがある場合
			if (tooltipText.getText().contains(I18NConst.TALK.get().toString())) {
				return;
			}
			//話すが入っていないが、別のテキストが入ってる場合
			if (!tooltipText.getText().isEmpty()) {
				I18NText v = I18NText.of(tooltipText.getText() + " / " + I18NConst.TALK.get().toString());
				tooltipText.setText(v);
				return;
			}
			//話すが入っておらず、空の場合
			assert tooltipText.getText().isEmpty() : "FMS toolTip illegal Valaue";
			tooltipText.setText(I18NConst.TALK.get());
			return;
		}
		//はなすがないので、テキストを変更する

		//currentNodeアリの場合：
		if (currentNode != null) {
			tooltipText.setText(I18NConst.ENTER_MAP.get().set(currentNode.getText()));
			return;
		}
		//何もない。
		tooltipText.setText(I18NText.EMPTY);
	}

	public TooltipText getTooltipText() {
		return tooltipText;
	}

	public void unsetTooltipText() {
		this.tooltipText = null;
	}

	public void changeMapStart() {
		if (currentNode == null) {
			throw new IllegalStateException("FMS : changeMapStart, but node is null");
		}
		if (currentNode.getSound() != null) {
			currentNode.getSound().load().play();
		}
		this.changeMap = true;
	}

	private void changeMapExec() {
		assert currentNode != null : "FMS : node is null";
		if (GameSystem.isDebugMode()) {
			GameLog.print("FMS : changeMap : " + currentNode);
			GameLog.addIndent();
		}
		currentNPCs.forEach(p -> p.asScript().load().getBlockOf(ScriptBlockType.LEAVE).exec().free());
		currentNPCs.forEach(p -> p.free());
		currentNPCs.clear();
		prevNPCs.clear();
		if (currentScript != null) {
			nodeLock = true;
			currentScript.exec(ScriptBlockType.STEP_DOWN);
			nodeLock = false;
			currentScript.getScriptFile().free();
		}
		currentScript = null;

		//NPC listの更新
		IniFile npcList = fieldMap.getNPCList();
		for (var v : fieldMap.getNPCMap()) {
			npcList.put(v.getSprite().getMoveModel().getInitialLocation().toString(), v.asUniversalValue());
		}
		npcList.save();

		if (fieldMap != null) {
			fieldMap.free();
		}
		FieldMap next = FieldMapStorage.getInstance().get(currentNode.getNextFieldMapID());
		fieldMap = next;
		fieldMap.load();
		camera.set(fieldMap);
		setLocation(currentNode.getNextFieldMapIdx());
		resetFollowerLocation();
		followerLocation.clear();

	}

	private boolean changeMap;

	public boolean isChangeMap() {
		return changeMap;
	}

	public void setChangeMap(boolean changeMap) {
		this.changeMap = changeMap;
	}

	@LoopCall
	public void update() {
		//node
		if (changeMap) {
			assert currentNode != null : "FMS : node is null";
			//IN_EFFECT
			if (currentNode.getEffect().getState() == MapChangeEffect.State.WAIT) {
				//IN処理完了
				changeMapExec();
				currentNode.getEffect().mapChangeEnd();
			}
			//OUT_EFFECT
			if (currentNode.getEffect().getState() == MapChangeEffect.State.END) {
				//OUT処理完了
				unsetNode();
				updateEvents(true);
				updateTooltip();
				if (GameSystem.isDebugMode()) {
					GameLog.removeIndent();
					GameLog.print("FMS : changeMap END");
				}
				changeMap = false;
			}
		}

		//npc
		fieldMap.getNPCMap().forEach(p -> p.update());
		fieldMap.getNPCMap().update();
		updateEvents(false);
		//TOOLTIPの処理
		updateTooltip();

		var pcList = GameSystem.getInstance().getPcList();

		//follower
		for (int i = 1; i < pcList.size(); i++) {
			Follower f = (Follower) pcList.get(i);
			f.update();
		}

	}

	@LoopCall
	@Override
	public void draw(GraphicsContext g) {
		//base
		camera.draw(g, GameSystem.getInstance().getPcList().stream().map(p -> p.getSprite()).toList());

		//effect
		if (isChangeMap()) {
			assert currentNode != null : "FMS : node is null";
			currentNode.getEffect().draw(g);
		}

		//tooltip
		if (tooltipText != null) {
			tooltipText.draw(g);
		}
	}

	private LinkedList<D2Idx> followerLocation = new LinkedList<>();

	@LoopCall
	public void move(Point2D.Float p) {
		if (p.x == 0 && p.y == 0) {
			return;
		}
		D2Idx prevIdx = camera.getPcLocation();
		Point2D.Float prevLayer0Location = fieldMap.getLayer0Location();
		Point2D.Float prevPcLocation = camera.getPcLocationOnScreen();

		var pcList = GameSystem.getInstance().getPcList();

		camera.move(p);

		if (!pcList.isEmpty()) {
			if (!prevLayer0Location.equals(fieldMap.getLayer0Location()) || !prevPcLocation.equals(camera.getPcLocationOnScreen())) {
				pcList.get(0).getSprite().setLocationByCenter(camera.getPcLocationOnScreen());
				pcList.get(0).getSprite().to(new KVector(p).round());
				pcList.get(0).getSprite().update();
			}
		}

		if (!prevIdx.equals(camera.getPcLocation())) {
			//新しいタイルに乗った

			if (!pcList.isEmpty()) {
				pcList.get(0).getSprite().setCurrentLocationOnMap(getCamera().getPcLocation());
			}
			//FL送り出し処理
			followerLocation.addFirst(prevIdx);
			if (followerLocation.size() > pcList.size() - 1) {
				followerLocation.removeLast();
			}

			//followerに新しい目標地点をセット
			for (int i = 1, j = 0; i < pcList.size(); i++) {
				Follower f = (Follower) pcList.get(i);
				D2Idx fl = followerLocation.get(j);
				f.setFollowTgt(fieldMap, fl);
				if (j < followerLocation.size() - 1) {
					j++;
				}
			}

			//このタイルのイベントを取得
			updateEvents(true);
		}
	}

}
