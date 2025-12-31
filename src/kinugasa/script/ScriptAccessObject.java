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

import java.util.HashMap;
import java.util.Map;
import kinugasa.game.GameLog;
import kinugasa.resource.sound.Sound;
import kinugasa.resource.sound.SoundSystem;
import kinugasa.system.FlagSystem;
import kinugasa.system.GameSystem;
import kinugasa.system.UniversalValue;

/**
 * ScriptAccessObject.<br>
 *
 * @vesion 1.0.0 - 2025/09/28_16:28:47<br>
 * @author Shinacho.<br>
 */
public class ScriptAccessObject {

	private ScriptFile file;
	private ScriptBlock block;

	public ScriptAccessObject() {
	}

	public ScriptAccessObject set(ScriptFile file, ScriptBlock block) {
		this.file = file;
		this.block = block;
		return this;
	}

	@ScriptAccessMethod
	public ScriptFile getFile() {
		return file;
	}

	@ScriptAccessMethod
	public ScriptBlock getBlock() {
		return block;
	}

	//------------------------------------common-------------------------------
	@ScriptAccessMethod
	public boolean returnTrue() {
		return true;
	}

	@ScriptAccessMethod
	public boolean returnFalse() {
		return false;
	}

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
	public void d(UniversalValue val) {
		System.out.println(val.toString());
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

}
