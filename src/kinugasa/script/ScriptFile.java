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

	// PARAM
	private List<String> paramNames;

	// EVENT
	private EnumMap<ScriptBlockType, ScriptBlock> blocks;
	private boolean isLoaded = false;

	public ScriptFile(FileObject f) {
		super(f.getFile());
	}

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
		DataFile f = new DataFile(super.getFile()).load();

		//PARAM
		this.paramNames = new ArrayList<>();
		if (f.has("PARAM")) {
			for (var v : f.get("PARAM")) {
				paramNames.add(v.key.value());
			}
		}

		//block - sl
		this.blocks = new EnumMap<>(ScriptBlockType.class);
		for (var v : f.getData()) { //block
			String k = v.key.value();
			if (!ScriptBlockType.has(k)) {
				//SBじゃない。
				continue;
			}
			String actualSaoName = v.saoName;
			if (actualSaoName == null) {
				//SBじゃない。
				continue;
			}
			if (v.getElements() == null || v.getElements().isEmpty()) {
				//空
				continue;
			}

			//
			try {
				ScriptBlockType blockType = ScriptBlockType.valueOf(k);
				ScriptAccessObject expectSao = blockType.getSAO(actualSaoName);
				ScriptBlock blk = new ScriptBlock(blockType, expectSao, this, v.getElements());
				expectSao.set(this, blk);

				blocks.put(blockType, blk);
			} catch (ScriptSyntaxException e) {
				throw new ScriptSyntaxException("SF : ScriptSyntaxException : " + v + "\r\n" + e);
			}
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

	public List<String> getParam() {
		return paramNames;
	}

	public boolean has(ScriptBlockType t) {
		return blocks.containsKey(t);
	}

	private static ScriptBlock DUMMY;

	public ScriptBlock getBlockOf(ScriptBlockType tgt) {
		if (!has(tgt)) {
			//DUMMY
			if (DUMMY == null) {
				DUMMY = new ScriptBlock(ScriptBlockType.DUMMY, null, this, List.of());
			}
			return DUMMY;
		}
		return blocks.get(tgt);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ScriptFile{");
		sb.append("location=").append(location);
		sb.append(", param=").append(paramNames);
		for (var v : blocks.values()) {
			if (!v.isEmpty()) {
				sb.append(v.getType()).append("=").append(v.getCmds().size());
			}
		}
		sb.append(", isLoaded=").append(isLoaded);
		sb.append('}');
		return sb.toString();
	}

}
