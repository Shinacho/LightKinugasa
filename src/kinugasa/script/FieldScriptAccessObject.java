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

import java.util.List;
import kinugasa.game.GameLog;
import kinugasa.game.I18NText;
import kinugasa.game.annotation.Nullable;
import kinugasa.game.annotation.Singleton;
import kinugasa.game.annotation.VariableResult;
import kinugasa.field4.D2Idx;
import kinugasa.field4.FieldMap;
import kinugasa.field4.FieldMapSystem;
import kinugasa.field4.Vehicle;
import kinugasa.system.FlagSystem;
import kinugasa.system.GameSystem;
import kinugasa.system.PlayableChara;
import kinugasa.system.UniversalValue;
import kinugasa.system.actor.Follower;
import kinugasa.system.actor.NPC;
import kinugasa.ui.Choice;
import kinugasa.ui.MessageWindow;
import kinugasa.ui.Text;
import kinugasa.graphics.KImage;
import kinugasa.resource.sound.SoundSystem;
import kinugasa.resource.sound.Sound;
import kinugasa.util.Random;
import kinugasa.util.StringUtil;
import kinugasa.ui.MWSpeaker;
import kinugasa.object.FourDirection;

/**
 * ScriptAccessObjectはスクリプトに渡されるオブジェクトで、ここからすべての処理が実行されます.<br>
 *
 * @vesion 1.0.0 - 2025/07/31_12:15:42<br>
 * @author Shinacho.<br>
 */
@Singleton
public class FieldScriptAccessObject implements ScriptAccessObject {

	private static final FieldScriptAccessObject INSTANCE = new FieldScriptAccessObject();

	public static FieldScriptAccessObject getInstance() {
		return INSTANCE;
	}

	private FieldScriptAccessObject() {
	}

	//------------------------------------common-------------------------------
	@ScriptAccessMethod
	public ScriptResultType end() {
		ScriptSystem.getInstance().end();
		return ScriptResultType.END;
	}

	@ScriptAccessMethod
	public void setDebugMode(UniversalValue f) {
		GameSystem.setDebugMode(f.asBoolean());
	}

	@ScriptAccessMethod
	public boolean isDebugMode() {
		return GameSystem.isDebugMode();
	}

	@ScriptAccessMethod
	public boolean paramIs(UniversalValue name, UniversalValue v) {
		return ScriptSystem.getInstance().getCurrentArgsValMap().get(name.value()).equals(v);
	}

	@ScriptAccessMethod
	public void printLogDirect(UniversalValue val) {
		GameLog.print(val.value());
	}

	@ScriptAccessMethod
	public void printLogI18N(UniversalValue val) {
		GameLog.print(val.asI18N());
	}

	@ScriptAccessMethod
	public void setPauseMode(UniversalValue v) {
		ScriptSystem.getInstance().setPauseMode(v.asBoolean());
	}

	@ScriptAccessMethod
	public void setManualIdxMode(UniversalValue v) {
		ScriptSystem.getInstance().setManualIdxMode(v.asBoolean());
	}

	@ScriptAccessMethod
	public void nextStep() {
		ScriptSystem.getInstance().nextStep();
	}

	//------------------------------------game system--------------------------
	@ScriptAccessMethod
	public GameSystem gameSystem() {
		return GameSystem.getInstance();
	}

	@ScriptAccessMethod
	public boolean gameModeIs(UniversalValue v) {
		return GameSystem.getInstance().getMode() == v.of(GameSystem.Mode.class);
	}

	//------------------------------------flag--------------------------------
	@ScriptAccessMethod
	public FlagSystem flagSystem() {
		return FlagSystem.getInstance();
	}

	//------------------------------------sound--------------------------------
	@ScriptAccessMethod
	public SoundSystem soundSystem() {
		return SoundSystem.getInstance();
	}

	@ScriptAccessMethod
	public void changeBGM(UniversalValue id) {
		SoundSystem.getInstance().toNext(id.asSoundID());
	}

	@ScriptAccessMethod
	public Sound soundOf(UniversalValue id) {
		return SoundSystem.getInstance().of(id.asId());
	}

	@ScriptAccessMethod
	public boolean currentBGMIs(UniversalValue id) {
		return SoundSystem.getInstance().getCurrent().getId().equals(id.value());
	}

	//------------------------------------field4--------------------------------
	@ScriptAccessMethod
	public FieldMapSystem fieldMapSystem() {
		return FieldMapSystem.getInstance();
	}

	@ScriptAccessMethod
	public FieldMap currentFieldMap() {
		return FieldMapSystem.getInstance().getCurrent();
	}

	@ScriptAccessMethod
	public boolean currentMapIDIs(UniversalValue id) {
		return FieldMapSystem.getInstance().getCurrent().getId().equals(id.asId());
	}

	@ScriptAccessMethod
	public boolean currentLocationOnFieldMapIs(UniversalValue idx) {
		return FieldMapSystem.getInstance().getCamera().getPcLocation().equals(idx.asD2IdxCSV());
	}

	@ScriptAccessMethod
	public Vehicle currentVehicle() {
		return FieldMapSystem.getInstance().getCurrentVehicle();
	}

	@ScriptAccessMethod
	public boolean currentVehicleIs(UniversalValue idx) {
		return FieldMapSystem.getInstance().getCurrentVehicle() == idx.of(Vehicle.class);
	}

	@ScriptAccessMethod
	public void setNode(UniversalValue nextMapId, UniversalValue x, UniversalValue y, UniversalValue exitDir, UniversalValue tooltipI18N) {
		setNode(nextMapId, x, y, exitDir, tooltipI18N, null);
	}

	@ScriptAccessMethod
	public void setNode(UniversalValue nextMapId, UniversalValue x, UniversalValue y, UniversalValue exitDir, UniversalValue tooltipI18N, UniversalValue sound) {
		String nextId = nextMapId.value();
		D2Idx i = new D2Idx(x.asInt(), y.asInt());
		FourDirection d = exitDir.asFourDirection();
		I18NText tooltip = tooltipI18N.asI18N();

		if (sound == null) {
			FieldMapSystem.getInstance().setNode(nextId, i, d, tooltip);
		} else {
			FieldMapSystem.getInstance().setNode(nextId, i, d, sound.asSoundID(), tooltip);
		}
	}

	@ScriptAccessMethod
	public void unsetNode() {
		FieldMapSystem.getInstance().unsetNode();
	}

	@ScriptAccessMethod
	public void changeVehicle(UniversalValue v) {
		Vehicle vc = v.of(Vehicle.class);
		FieldMapSystem.getInstance().setVehicle(vc);
		for (var p : GameSystem.getInstance().getPcList()) {
			p.getSprite().setVehicle(vc);
		}
	}

	@ScriptAccessMethod
	public NPC getNpc(UniversalValue id) {
		return FieldMapSystem.getInstance().getCurrent().getNPCMap().get(id.asD2IdxCSV());
	}

	@ScriptAccessMethod
	public void addNPCToCurrentFieldMap(UniversalValue npcFile, UniversalValue x, UniversalValue y) {
		D2Idx i = new D2Idx(x.asInt(), y.asInt());
		NPC n = new NPC(npcFile.asFile(), i);
		FieldMapSystem.getInstance().getCurrent().getNPCMap().add(n);
	}

	//------------------------------------ui-------------------------------------
	public static class SpeakerOption {

		private static final SpeakerOption INSTANCE = new SpeakerOption();

		private SpeakerOption() {
		}

		public static SpeakerOption getInstance() {
			return INSTANCE;
		}

		@ScriptAccessMethod
		public void by(UniversalValue imageOrSpeaker) {
			String id = imageOrSpeaker.value();

			//PC ID
			MWSpeaker pc = GameSystem.getInstance().getPcById(PlayableChara.valueOf(id));
			if (pc != null) {
				ScriptSystem.getInstance().getMessageWindow().getText().setSpeaker(pc);
			}

			//NPC ID
			if (FieldMapSystem.getInstance().getCurrent().getNPCMap().has(id)) {
				MWSpeaker speaker = FieldMapSystem.getInstance().getCurrent().getNPCMap().get(id);
				ScriptSystem.getInstance().getMessageWindow().getText().setSpeaker(speaker);
			}

			//image, name
			String image = null;
			String name = null;
			if (id.contains(",")) {
				image = StringUtil.safeSplit(id, ",")[0];
				name = StringUtil.safeSplit(id, ",")[1];
			} else {
				image = id;// as filepath
			}

			MWSpeaker speaker = MWSpeaker.of(new I18NText(name), new KImage(image));
			ScriptSystem.getInstance().getMessageWindow().getText().setSpeaker(speaker);

		}

		@ScriptAccessMethod
		public void byMe() {
			NPC n = ScriptSystem.getInstance().getCurrentTalkNpc();
			if (n != null) {
				ScriptSystem.getInstance().getMessageWindow().getText().setSpeaker(n);
			}
		}

	}

	@ScriptAccessMethod
	public SpeakerOption openMessageWindow(UniversalValue firstText) {
		Text t = firstText.asI18N().toText();
		ScriptSystem.getInstance().openMessageWindow();
		ScriptSystem.getInstance().setLastText(t);
		ScriptSystem.getInstance().getMessageWindow().setText(t);
		return SpeakerOption.getInstance();
	}

	@ScriptAccessMethod
	public void addTextReplaceMapDirect(UniversalValue key, UniversalValue value) {
		Text.getReplaceMap().put(key.value(), value.value());
	}

	@ScriptAccessMethod
	public void addTextReplaceMapI18N(UniversalValue key, UniversalValue value) {
		Text.getReplaceMap().put(key.value(), value.asI18N().toString());
	}

	@ScriptAccessMethod
	public void resetMessageWindow() {
		if (ScriptSystem.getInstance().getMessageWindow() != null) {
			ScriptSystem.getInstance().getMessageWindow().reset();
		}
	}

	@ScriptAccessMethod
	public SpeakerOption nextText(UniversalValue i18n) {
		Text t = i18n.asI18N().toText();
		ScriptSystem.getInstance().getMessageWindow().next(t);
		ScriptSystem.getInstance().setLastText(t);
		return SpeakerOption.getInstance();
	}

	@ScriptAccessMethod
	public SpeakerOption nextTextAsChoice(UniversalValue v, UniversalValue o1, UniversalValue o2) {
		Choice c = v.asI18N().toText().toChoice(List.of(o1.asI18N().toText(), o2.asI18N().toText()));
		ScriptSystem.getInstance().getMessageWindow().setText(c);
		ScriptSystem.getInstance().setLastText(c);
		return SpeakerOption.getInstance();
	}

	@ScriptAccessMethod
	public SpeakerOption nextTextAsChoice(UniversalValue v, UniversalValue o1, UniversalValue o2, UniversalValue o3) {
		Choice c = v.asI18N().toText().toChoice(List.of(o1.asI18N().toText(), o2.asI18N().toText(), o3.asI18N().toText()));
		ScriptSystem.getInstance().getMessageWindow().setText(c);
		ScriptSystem.getInstance().setLastText(c);
		return SpeakerOption.getInstance();
	}

	@ScriptAccessMethod
	public SpeakerOption nextTextAsChoice(UniversalValue v, UniversalValue o1, UniversalValue o2, UniversalValue o3, UniversalValue o4) {
		Choice c = v.asI18N().toText().toChoice(List.of(o1.asI18N().toText(), o2.asI18N().toText(), o3.asI18N().toText(), o4.asI18N().toText()));
		ScriptSystem.getInstance().getMessageWindow().setText(c);
		ScriptSystem.getInstance().setLastText(c);
		return SpeakerOption.getInstance();
	}

	@ScriptAccessMethod
	public SpeakerOption nextTextAsChoice(UniversalValue v, UniversalValue o1, UniversalValue o2, UniversalValue o3, UniversalValue o4, UniversalValue o5) {
		Choice c = v.asI18N().toText().toChoice(List.of(o1.asI18N().toText(), o2.asI18N().toText(), o3.asI18N().toText(), o4.asI18N().toText(), o5.asI18N().toText()));
		ScriptSystem.getInstance().getMessageWindow().setText(c);
		ScriptSystem.getInstance().setLastText(c);
		return SpeakerOption.getInstance();
	}

	@ScriptAccessMethod
	public SpeakerOption nextTextAsChoice(UniversalValue v, UniversalValue o1, UniversalValue o2, UniversalValue o3, UniversalValue o4, UniversalValue o5, UniversalValue o6) {
		Choice c = v.asI18N().toText().toChoice(List.of(o1.asI18N().toText(), o2.asI18N().toText(), o3.asI18N().toText(), o4.asI18N().toText(), o5.asI18N().toText(), o6.asI18N().toText()));
		ScriptSystem.getInstance().getMessageWindow().setText(c);
		ScriptSystem.getInstance().setLastText(c);
		return SpeakerOption.getInstance();
	}

	@ScriptAccessMethod
	public void closeMessageWindow() {
		ScriptSystem.getInstance().closeMessageWindow();
	}

	@ScriptAccessMethod
	@Nullable
	public MessageWindow getMessageWindow() {
		return ScriptSystem.getInstance().getMessageWindow();
	}

	@ScriptAccessMethod
	public boolean lastSelectedChoiceIs(UniversalValue id) {
		return ScriptSystem.getInstance().getLastSelectedChoiceOption().getId().equals(id.asId());
	}

	@ScriptAccessMethod
	public boolean lastSelectedChoiceIdxIs(UniversalValue idx) {
		return ScriptSystem.getInstance().getLastSelectedChoiceIdx() == idx.asInt();
	}

	@ScriptAccessMethod
	public boolean lastTextIs(UniversalValue id) {
		return ScriptSystem.getInstance().getLastText().getId().equals(id.asId());
	}
	//------------------------------------util-------------------------------------

	@ScriptAccessMethod
	@VariableResult
	public boolean random(UniversalValue chance) {
		return Random.percent(chance.asFloat());
	}
	//------------------------------------PC/NPC-------------------------------------

	@ScriptAccessMethod
	public void followerPartyIn(UniversalValue actorFile) {
		Follower f = actorFile.asFollower();
		GameSystem.getInstance().getPcList().add(f);
		FieldMapSystem.getInstance().resetFollowerLocation();
	}

	@ScriptAccessMethod
	public void followerPartyOutIfExists(UniversalValue id) {
		var pcList = GameSystem.getInstance().getPcList();
		int i = 0;
		boolean remove = false;
		for (; i < pcList.size(); i++) {
			if (pcList.get(i).getId().equals(id.asId())) {
				remove = true;
				break;
			}
		}
		if (remove) {
			pcList.remove(i);
		}

	}

}
