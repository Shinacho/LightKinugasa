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
package kinugasa.game.system.actor;

import java.io.File;
import kinugasa.game.event.ScriptFile;
import kinugasa.game.field4.D2Idx;
import kinugasa.game.field4.FieldMap;
import kinugasa.game.system.UniversalValue;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.text.DataFile;
import kinugasa.resource.text.FileFormatException;

/**
 * NPC.<br>
 *
 * @vesion 1.0.0 - 2025/07/22_19:36:46<br>
 * @author Shinacho.<br>
 */
public class NPC extends Actor {

	private FieldMap fm;
	private D2Idx idx;

	public NPC(File f, FieldMap fm, D2Idx idx) {
		super(f);
		this.fm = fm;
		this.idx = idx;
	}
	private ScriptFile scriptFile;

	public ScriptFile asScript() {
		if (scriptFile == null) {
			scriptFile = new ScriptFile(super.getFile()).load();
		}
		return scriptFile;
	}

	@Override
	protected CharaSprite loadSprite(DataFile.Element pi) {
		var r = super.loadSprite(pi);
		r.setCurrentLocationOnMap(idx);
		return r;
	}

	@Override
	protected void loadPI(DataFile.Element pi) {
		//NPC MoveModel
		{
			if (pi.has("moveModel")) {
				UniversalValue moveModel = pi.get("moveModel").getValue();
				switch (moveModel.trim().safeSplit(",")[0]) {
					case "ROUND" -> {
						int d = moveModel.trim().safeSplitUV(",")[1].asInt();
						float wt = moveModel.trim().safeSplitUV(",")[2].asFloat();
						StandardFieldMapNPCMoveModel.round(d, wt, getSprite(), fm).set();
					}
					case "LOCKED" -> {
						StandardFieldMapNPCMoveModel.locked(getSprite(), fm).set();
					}
					default -> {
						throw new FileFormatException("Actor : undefined move model : " + moveModel);
					}
				}
			} else {
				StandardFieldMapNPCMoveModel.locked(getSprite(), fm).set();
			}
		}
	}

	@Override
	public NPC load() throws FileNotFoundException, FileFormatException, ContentsIOException {
		if (isLoaded()) {
			return this;
		}
		super.load();
		return this;
	}

}
