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
package kinugasa.script;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kinugasa.game.GameLog;
import kinugasa.game.GameManager;
import kinugasa.game.annotation.Nullable;
import kinugasa.game.annotation.Singleton;
import static kinugasa.script.ScriptBlockType.MAIN;
import kinugasa.script.exception.ScriptFileException;
import kinugasa.script.exception.ScriptSyntaxException;
import kinugasa.field4.D2Idx;
import kinugasa.field4.FieldMapSystem;
import kinugasa.system.GameSystem;
import kinugasa.system.UniversalValue;
import kinugasa.system.actor.NPC;
import kinugasa.ui.MessageWindow;
import kinugasa.ui.Text;
import kinugasa.game.GraphicsContext;
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

	//-------------------------------------------------------------------------
	private ScriptSystem() {
	}

	public static final String SUFFIX = ".ks.txt";

	private File root;
	private Storage<ScriptFile> data;
	private boolean loaded = false;

	//current
	public void init(File root) throws ScriptSyntaxException, ScriptFileException {
		if (GameSystem.isDebugMode()) {
			GameLog.print("ScriptSystem init start");
			GameLog.addIndent();
		}
		if (!root.isDirectory()) {
			throw new ScriptFileException("ScriptSystem" + root.getName() + " is not directory");
		}
		if (!root.exists()) {
			throw new ScriptFileException("ScriptSystem" + root.getName() + " is not exists");
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

	public boolean has(String name) {
		return data.contains(name);
	}

	public ScriptResult.Value instantCall(String line, ScriptAccessObject sao) throws ScriptSyntaxException {
		ScriptLine sl = new ScriptLine(sao, line, List.of());
		return sl.exec(Map.of());
	}

	public ScriptResult execDirect(String name) {
		return execDirect(name, MAIN, List.of());
	}

	public ScriptResult execDirect(String name, UniversalValue... v) {
		return execDirect(name, MAIN, Arrays.asList(v));
	}

	public ScriptResult execDirect(String name, List<UniversalValue> param) {
		return execDirect(name, MAIN, param);
	}

	public ScriptResult execDirect(String name, ScriptBlockType block) {
		return execDirect(name, block, List.of());
	}

	public ScriptResult execDirect(String name, ScriptBlockType block, UniversalValue... v) {
		return execDirect(name, block, Arrays.asList(v));
	}

	public ScriptResult execDirect(String name, ScriptBlockType block, List<UniversalValue> param) {
		return of(name).load().getBlockOf(block).exec(param).free();
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
			currentExecBlock.exec(currentArgs);
		} else {
			end();
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
		currentArgs = null;
		currentArgsValMap = null;
		last = null;
		currentBlockIdx = 0;
		FieldMapSystem.getInstance().unsetTalk();
		pauseMode = false;
	}

	//---------------------------------------------------------------------------
	private ScriptFile currentExecFile = null;
	private ScriptBlock currentExecBlock = null;
	private List<UniversalValue> currentArgs = null;
	private Map<String, UniversalValue> currentArgsValMap = null;
	private ScriptResult.Value last = null;
	private int currentBlockIdx = 0;
	private boolean pauseMode = false;
	private boolean manualIdxMode = false;
	//---------------------------------------------------------------------------

	void unsetScript() {
		setCurrentExecFile(null);
		setCurrentExecBlock(null);
		setCurrentArgs(null);
		setCurrentArgsValMap(null);
	}

	public ScriptFile getCurrentExecFile() {
		return currentExecFile;
	}

	void setCurrentExecFile(ScriptFile currentExecFile) {
		this.currentExecFile = currentExecFile;
	}

	public ScriptBlock getCurrentExecBlock() {
		return currentExecBlock;
	}

	void setCurrentExecBlock(ScriptBlock currentExecBlock) {
		this.currentExecBlock = currentExecBlock;
	}

	public List<UniversalValue> getCurrentArgs() {
		return currentArgs;
	}

	void setCurrentArgs(List<UniversalValue> currentArgs) {
		this.currentArgs = currentArgs;
	}

	public Map<String, UniversalValue> getCurrentArgsValMap() {
		return currentArgsValMap;
	}

	void setCurrentArgsValMap(Map<String, UniversalValue> currentArgsValMap) {
		this.currentArgsValMap = currentArgsValMap;
	}

	public ScriptResult.Value getLast() {
		return last;
	}

	void setLast(ScriptResult.Value last) {
		this.last = last;
	}

	public int getCurrentBlockIdx() {
		return currentBlockIdx;
	}

	void setCurrentBlockIdx(int currentBlockIdx) {
		this.currentBlockIdx = currentBlockIdx;
	}

	public boolean isPauseMode() {
		return pauseMode;
	}

	void setPauseMode(boolean pauseMode) {
		this.pauseMode = pauseMode;
	}

	public boolean isManualIdxMode() {
		return manualIdxMode;
	}

	void setManualIdxMode(boolean manualIdxMode) {
		this.manualIdxMode = manualIdxMode;
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

	private DebugConsole cns;

	public void debugConsoleWindow() {
		if (cns == null) {
			cns = new DebugConsole();
		}
		cns.setVisible(!cns.isVisible());
	}

}
