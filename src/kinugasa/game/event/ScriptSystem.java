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
package kinugasa.game.event;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import kinugasa.game.event.exception.EventScriptFileException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import kinugasa.game.GameLog;
import kinugasa.game.GameManager;
import kinugasa.game.annotation.Nullable;
import kinugasa.game.annotation.Singleton;
import static kinugasa.game.event.ScriptBlockType.MAIN;
import kinugasa.game.event.exception.EventScriptException;
import kinugasa.game.field4.D2Idx;
import kinugasa.game.field4.FieldMapSystem;
import kinugasa.game.system.GameSystem;
import kinugasa.game.system.UniversalValue;
import kinugasa.game.system.actor.NPC;
import kinugasa.game.ui.MessageWindow;
import kinugasa.game.ui.Text;
import kinugasa.graphics.GraphicsContext;
import kinugasa.resource.IDNotFoundException;
import kinugasa.resource.Storage;

/**
 * ScriptSystem.<br>
 *
 * @vesion 1.0.0 - 2025/07/31_22:06:44<br>
 * @author Shinacho.<br>
 */
@Singleton
public final class ScriptSystem {

	private static boolean testEnable = true;

	public static boolean isTestEnable() {
		return testEnable;
	}

	public static void setTestEnable(boolean testEnable) {
		ScriptSystem.testEnable = testEnable;
	}

	private static final ScriptSystem INSTANCE = new ScriptSystem();

	public static ScriptSystem getInstance() {
		return INSTANCE;
	}

	private ScriptSystem() {
	}

	public static final String SUFFIX = ".ks.txt";

	private File root;
	private Storage<ScriptFile> data;
	private boolean loaded = false;

	//current
	public void init(File root) throws EventScriptFileException {
		if (GameSystem.isDebugMode()) {
			GameLog.print("ScriptSystem init start");
			GameLog.addIndent();
		}
		if (!root.isDirectory()) {
			throw new EventScriptFileException("ScriptSystem" + root.getName() + " is not directory");
		}
		if (!root.exists()) {
			throw new EventScriptFileException("ScriptSystem" + root.getName() + " is not exists");
		}
		this.root = root;
		this.data = new Storage<>();
		addFile(root);
		loaded = true;
		if (GameSystem.isDebugMode()) {
			GameLog.removeIndent();
			GameLog.print("ScriptSystem init end [" + data.size() + "]");
		}
	}

	public boolean isLoaded() {
		return loaded;
	}

	private void addFile(File dir) {
		for (var v : dir.listFiles()) {
			if (v.isDirectory()) {
				addFile(v);
			}
			if (v.getName().toLowerCase().endsWith(SUFFIX)) {
				ScriptFile s = new ScriptFile(v);
				if (testEnable) {
					s.test();
				}
				data.add(s);
				if (GameSystem.isDebugMode()) {
					GameLog.print("-ScriptSystem add : " + s.getId());
				}
			}
		}
	}

	public ScriptFile of(String name) throws IDNotFoundException {
		return data.get(name);
	}

	public ScriptLine.Result instantCall(String line) throws EventScriptException {
		//hoge().piyo()

		if (line.startsWith("@")) {
			throw new EventScriptException("SS : instantCall cant use file call");
		}
		//. = ScriptAccessObject
		ScriptLine sl = new ScriptLine(null, line, List.of());
		return sl.exec(new ScriptArgs(Map.of(), List.of()));
	}

	public ScriptBlock.Result call(ScriptCall c, ScriptBlockType t) throws EventScriptException {
		var f = of(c.getScriptName()).load();
		var r = f.getBlockOf(t).exec(c.getParam());
		f.free();
		return r;
	}

	public ScriptBlock.Result call(ScriptCall c) throws EventScriptException {
		var f = of(c.getScriptName()).load();
		var r = f.getBlockOf(c.getBlockType()).exec(c.getParam());
		f.free();
		return r;
	}

	public ScriptBlock.Result call(ScriptCall c, ScriptArgs args) throws EventScriptException {
		//マージ済みARGSの作成
		List<UniversalValue> merged = new ArrayList<>();

		//argsのキーに同じ名前があったらそのValueを使う
		for (var v : c.getParam()) {
			if (args.getMap().containsKey(v.value())) {
				merged.add(args.getMap().get(v.value()));
			} else {
				merged.add(v);
			}
		}

		var f = of(c.getScriptName()).load();
		var r = f.getBlockOf(c.getBlockType()).exec(merged);
		f.free();
		return r;
	}

	public ScriptBlock.Result execDirect(String name) {
		ScriptFile s = of(name).load();
		var r = s.getBlockOf(MAIN).exec();
		s.free();
		return r;
	}

	public ScriptBlock.Result execDirect(String name, UniversalValue... v) {
		return execDirect(name, MAIN, Arrays.asList(v));
	}

	public ScriptBlock.Result execDirect(String name, List<UniversalValue> param) {
		ScriptFile s = of(name).load();
		var r = s.getBlockOf(MAIN).exec(param);
		s.free();
		return r;
	}

	public ScriptBlock.Result execDirect(String name, ScriptBlockType tgt) {
		ScriptFile s = of(name).load();

		var r = s.getBlockOf(tgt).exec();

		s.free();
		return r;
	}

	public ScriptBlock.Result execDirect(String name, ScriptBlockType tgt, UniversalValue... v) {
		return execDirect(name, tgt, Arrays.asList(v));
	}

	public ScriptBlock.Result execDirect(String name, ScriptBlockType tgt, List<UniversalValue> param) {
		ScriptFile s = of(name).load();
		var r = s.getBlockOf(tgt).exec();
		s.free();
		return r;
	}

	//---------------------------------------------------------------------------
	public void nextStep() {
		if (messageWindow != null && messageWindow.isChoice()) {
			//choice結果保存
			int selected = messageWindow.getSelect();
			ScriptSystem.getInstance().setLastChoiceIdx(selected);

			Text selectedChoiceOption = messageWindow.getSelectedChoiceOption();
			ScriptSystem.getInstance().setLastSelectedChoiceOption(selectedChoiceOption);

		}
		if (currentExecBlock.hasNext()) {
			currentExecBlock.next();
			currentExecBlock.exec(currentParam);
		}
	}

	public boolean hasNext() {
		return currentExecBlock.hasNext();
	}

	public void end() {
		currentExecFile.free();
		currentExecBlock.resetIdx();
		currentExecFile = null;
		currentExecBlock = null;
		currentParam = null;
		lastResultType = null;
		currentBlockIdx = 0;
		FieldMapSystem.getInstance().unsetTalk();
		pauseMode = false;
	}

	//---------------------------------------------------------------------------
	private ScriptFile currentExecFile = null;
	private ScriptBlock currentExecBlock = null;
	private ScriptArgs currentParam = null;
	private ScriptResultType lastResultType = null;
	private int currentBlockIdx = 0;
	private boolean pauseMode = false;
	private boolean manualIdxMode = false;
	//---------------------------------------------------------------------------

	public void setManualIdxMode(boolean manualIdxMode) {
		this.manualIdxMode = manualIdxMode;
	}

	public boolean isManualIdxMode() {
		return manualIdxMode;
	}

	public void setPauseMode(boolean pauseMode) {
		this.pauseMode = pauseMode;
	}

	boolean isPauseMode() {
		return pauseMode;
	}

	void setCurrentExecFile(ScriptFile currentExecFile) {
		this.currentExecFile = currentExecFile;
	}

	void setCurrentExecBlock(ScriptBlock currentExecBlock) {
		this.currentExecBlock = currentExecBlock;
	}

	void setCurrentArgs(ScriptArgs currentParam) {
		this.currentParam = currentParam;
	}

	void setLastResultType(ScriptResultType lastResultType) {
		this.lastResultType = lastResultType;
	}

	void setCurrentBlockIdx(int currentBlockIdx) {
		this.currentBlockIdx = currentBlockIdx;
	}

	public ScriptFile getCurrentExecFile() {
		return currentExecFile;
	}

	public ScriptBlock getCurrentExecBlock() {
		return currentExecBlock;
	}

	public ScriptArgs getCurrentArgs() {
		return currentParam;
	}

	public ScriptResultType getLastResultType() {
		return lastResultType;
	}

	public int getCurrentBlockIdx() {
		return currentBlockIdx;
	}

	//---------------------------------------------------------------------------
	private MessageWindow messageWindow;
	private int lastChoiceIdx = -1;
	private Text lastChoice = null;
	private Text lastText = null;
	private NPC currentTalkNpc;

	public static final int MESSAGE_WINDOW_X = 12;
	public static final int MESSAGE_WINDOW_Y = 12;
	public static final float MESSAGE_WINDOW_H_OF_WINDOW_H = 0.38f;

	public boolean talk() {
		return FieldMapSystem.getInstance().talk();
	}

	public boolean isTalking() {
		return FieldMapSystem.getInstance().isTalking();
	}

	public NPC getCurrentTalkNpc() {
		return currentTalkNpc;
	}

	public void setCurrentTalkNpc(NPC currentNpc) {
		this.currentTalkNpc = currentNpc;
	}

	public void unsetCurrentTalkNpc() {
		this.currentTalkNpc = null;
	}

	void openMessageWindow() {
		if (messageWindow != null) {
			messageWindow.setVisible(false);
		}
		pauseMode = true;
		D2Idx current = FieldMapSystem.getInstance().getCamera().getPcLocation();
		D2Idx center = FieldMapSystem.getInstance().getCamera().getIdxOfScreenCenter();

		int w = GameManager.getInstance().getOption().getWindowSize().width - (MESSAGE_WINDOW_X * 2);
		int h = (int) (GameManager.getInstance().getOption().getWindowSize().height * MESSAGE_WINDOW_H_OF_WINDOW_H);
//		if (current.y >= center.y) {
//			//上
//			Point2D.Float p = new Point2D.Float(MESSAGE_WINDOW_X, MESSAGE_WINDOW_Y);
//			Dimension s = new Dimension(w, h);
//			this.messageWindow = new MessageWindow(p.x, p.y, s.width, s.height);
//		} else 
		{
			//下
			Point2D.Float p = new Point2D.Float(MESSAGE_WINDOW_X, GameManager.getInstance().getOption().getWindowSize().height - MESSAGE_WINDOW_Y - h);
			Dimension s = new Dimension(w, h);
			this.messageWindow = new MessageWindow(p.x, p.y, s.width, s.height);
		}
		this.messageWindow.setVisible(true);
	}

	void setMessageWindowText(Text t) {
		if (this.messageWindow == null || !this.messageWindow.isVisible()) {
			openMessageWindow();
		}
		this.messageWindow.setText(t);
	}

	public void closeMessageWindow() {
		this.messageWindow.setText(Text.EMPTY);
		this.messageWindow.reset();
		this.messageWindow.setVisible(false);
		this.messageWindow.setExist(false);
		this.messageWindow = null;
		this.lastChoiceIdx = -1;
		this.lastChoice = null;
		this.lastText = null;
		this.pauseMode = false;

		FieldMapSystem.getInstance().unsetTalk();
	}

	public void showAllNow() {
		if (this.messageWindow != null) {
			this.messageWindow.showAllNow();;
		}
	}

	void setLastSelectedChoiceOption(Text lastChoice) {
		this.lastChoice = lastChoice;
	}

	void setLastChoiceIdx(int lastChoiceIdx) {
		this.lastChoiceIdx = lastChoiceIdx;
	}

	void setLastText(Text lastText) {
		this.lastText = lastText;
	}

	@Nullable
	public MessageWindow getMessageWindow() {
		return messageWindow;
	}

	public int getLastSelectedChoiceIdx() {
		return lastChoiceIdx;
	}

	public Text getLastSelectedChoiceOption() {
		return lastChoice;
	}

	public Text getLastText() {
		return lastText;
	}

	//--------------------------------------------------------------------------
	public void update() {
		if (this.messageWindow != null && this.messageWindow.isVisible()) {
			this.messageWindow.update();
		}
	}

	public void draw(GraphicsContext g) {
		if (this.messageWindow != null && this.messageWindow.isVisible()) {
			this.messageWindow.draw(g);
		}
	}

}
