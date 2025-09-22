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

import kinugasa.game.event.exception.EventScriptException;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import kinugasa.game.GameLog;
import kinugasa.game.annotation.Nullable;
import kinugasa.game.field4.D2Idx;
import kinugasa.game.system.GameSystem;
import kinugasa.game.system.UniversalValue;
import kinugasa.game.system.actor.CharaSprite;
import kinugasa.object.FileObject;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.text.DataFile;
import kinugasa.resource.text.FileFormatException;

/**
 * ScriptFile.<br>
 *
 * @vesion 1.0.0 - 2025/07/29_12:26:00<br>
 * @author Shinacho.<br>
 */
public class ScriptFile extends FileObject {

	@Nullable
	private D2Idx location;
	@Nullable
	private CharaSprite charaSprite;

	// PARAM
	private List<ScriptParam> param;

	// EVENT
	private EnumMap<ScriptBlockType, ScriptBlock> blocks;
	private boolean isLoaded = false;

	public ScriptFile(File f) {
		super(f);
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

	@Override
	public ScriptFile load() throws FileNotFoundException, FileFormatException, ContentsIOException {
		if (isLoaded) {
			return this;
		}
		if (GameSystem.isDebugMode()) {
			GameLog.print("SF [" + getName() + "] load start");
			GameLog.addIndent();
		}
		DataFile f = asDataFile().load();

		//PARAM
		this.param = new ArrayList<>();
		if (f.has("PARAM")) {
			boolean optionalMode = false;
			for (int i = 0, size = f.get("PARAM").getElements().size(); i < size; i++) {
				var v = f.get("PARAM").getElements().get(i);
				String name = v.getKey().value();
				ScriptParam p = new ScriptParam(name);
				UniversalValue val = v.getValue();
				if (val == null || val.isEmpty()) {
					if (optionalMode) {
						throw new FileFormatException("ES [" + getName() + "] . PARAM optional missmatch" + v);
					}
				} else {
					p.setDefaultValue(val);
					optionalMode = true;
				}
				param.add(p);
			}
		}

		//events
		blocks = new EnumMap<>(ScriptBlockType.class);
		for (var v : ScriptBlockType.values()) {
			blocks.put(v, new ScriptBlock(v, this, f.has(v.toString()) ? f.get(v.toString()).getElements() : List.of()));
		}
		if (GameSystem.isDebugMode()) {
			GameLog.removeIndent();
			GameLog.print("SF [" + getName() + "] is loaded");
		}

		f.free();
		isLoaded = true;
		return this;
	}

	@Override
	public void free() {
		if (!isLoaded) {
			return;
		}
		isLoaded = false;
		param = null;
		if (blocks != null) {
			blocks.clear();
		}
		blocks = null;
	}

	@Override
	public boolean isLoaded() {
		return isLoaded;
	}

	public ScriptFile test() throws EventScriptException {
		load();
		free();
		return this;
	}

	public D2Idx getLocation() {
		return location;
	}

	public CharaSprite getSprite() {
		return charaSprite;
	}

	public List<ScriptParam> getParam() {
		return param;
	}

	public ScriptBlock getBlockOf(ScriptBlockType tgt) {
		return blocks.get(tgt);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ScriptFile{");
		sb.append("location=").append(location);
		sb.append(", charaSprite=").append(charaSprite);
		sb.append(", param=").append(param);
		for (var v : ScriptBlockType.values()) {
			sb.append(", ").append(v).append("=").append(v);
		}
		sb.append(", isLoaded=").append(isLoaded);
		sb.append('}');
		return sb.toString();
	}

}
