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

import kinugasa.script.exception.ScriptSyntaxException;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import kinugasa.game.GameLog;
import kinugasa.game.annotation.Nullable;
import kinugasa.script.exception.ScriptException;
import kinugasa.field4.D2Idx;
import kinugasa.system.GameSystem;
import kinugasa.system.actor.CharaSprite;
import kinugasa.object.FileObject;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.text.DataFile;
import kinugasa.resource.text.FileFormatException;
import kinugasa.util.StringUtil;

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
	private List<String> paramNames;

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
	public ScriptFile load() throws FileNotFoundException, FileFormatException, ContentsIOException, ScriptException {
		if (isLoaded) {
			return this;
		}
		if (GameSystem.isDebugMode()) {
			GameLog.print("SF [" + getName() + "] load start");
			GameLog.addIndent();
		}
		DataFile f = asDataFile().load();

		//PARAM
		this.paramNames = new ArrayList<>();
		if (f.has("PARAM")) {
			for (var v : f.get("PARAM")) {
				paramNames.add(v.getKey().value());
			}
		}

		//block - sl
		this.blocks = new EnumMap<>(ScriptBlockType.class);
		for (var v : ScriptBlockType.values()) {
			blocks.put(v, new ScriptBlock(v, null, this, List.of()));
		}
		for (var v : f.getData()) { //block
			String k = v.getKey().value();
			var ele = v.getElements();
			if (ele == null || ele.isEmpty()) {
				continue;
			}
			k = k.replaceAll(" ", "").replaceAll("\t", "").trim();
			String[] kk = StringUtil.safeSplit(k, "(");
			if (!ScriptBlockType.has(kk[0])) {
				continue;
			}
			if (kk.length != 2) {
				throw new ScriptSyntaxException("SF : block SAO not found : " + v);
			}
			ScriptBlockType type = ScriptBlockType.valueOf(kk[0].trim());

			String saoName = kk[1].replaceAll("[)]", "").trim().toUpperCase();
			if (!ScriptAccessObjects.has(saoName)) {
				throw new ScriptSyntaxException("SF : block SAO not found : " + v);
			}
			ScriptAccessObject sao = ScriptAccessObjects.valueOf(saoName).getSAO();

			blocks.put(type, new ScriptBlock(type, sao, this, ele));

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
		paramNames = null;
		if (blocks != null) {
			blocks.clear();
		}
		blocks = null;
	}

	@Override
	public boolean isLoaded() {
		return isLoaded;
	}

	public ScriptFile test() throws ScriptSyntaxException {
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

	public List<String> getParam() {
		return paramNames;
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
		sb.append(", param=").append(paramNames);
		for (var v : ScriptBlockType.values()) {
			sb.append(", ").append(v).append("=").append(v);
		}
		sb.append(", isLoaded=").append(isLoaded);
		sb.append('}');
		return sb.toString();
	}

}
