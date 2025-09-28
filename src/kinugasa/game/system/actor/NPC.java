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
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import kinugasa.game.annotation.DataChange;
import kinugasa.game.event.ScriptFile;
import kinugasa.game.field4.D2Idx;
import kinugasa.game.field4.FieldMap;
import kinugasa.game.field4.FieldMapStorage;
import kinugasa.game.field4.FieldMapSystem;
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

	public NPC(File f, D2Idx i) {
		this(f, FieldMapSystem.getInstance().getCurrent(), i);
	}

	public NPC(File f, FieldMap fm, D2Idx idx) {
		super(f, fm, idx);
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
	public NPC load() throws FileNotFoundException, FileFormatException, ContentsIOException {
		if (isLoaded()) {
			return this;
		}
		super.load();
		if (getSprite().getMoveModel() == null) {
			throw new FileFormatException("NPC moveModel is null : " + getId());
		}
		return this;
	}

	@DataChange
	public boolean gotoOtherFieldMap(String fieldMapId, D2Idx idx) {

		var next = FieldMapStorage.getInstance().get(fieldMapId);

		//NPCデータ自体の移動
		String path = next.getDir() + "npc/" + getFile().getName();

		try {
			Files.move(getFile().toPath(), new File(path).toPath());
		} catch (IOException ex) {
			throw new ContentsIOException(ex);
		}
		D2Idx i = null;

		//キー探索
		for (var v : super.getFieldMap().getNPCMap().getMap().entrySet()) {
			if (v.getValue().equals(this)) {
				i = v.getKey();
				break;
			}
		}
		if (i == null) {
			return false;
		}

		//削除
		super.getFieldMap().getNPCMap().getMap().remove(i);

		//追加
		if (next.isLoaded()) {
			next.getNPCMap().add(idx, this);
		}
		super.setFieldMap(next);

		var npcListFile = next.getNPCList().load();
		for (var v : npcListFile) {
			if (v.getKey().getId().equals(idx.getId())) {
				//位置重複
				throw new IllegalArgumentException("NPC goto, but next fm idx is duplicated : " + this + " to " + next.getId() + " / " + idx);
			}
			//すでにいる場合失敗
			if (v.getValue().getId().equals(this.getId())) {
				return false;
			}
		}

		//いない場合は追加して保存
		npcListFile.put(idx.getId(), new UniversalValue(this.getId()));
		npcListFile.save();
		npcListFile.free();

		return true;
	}

}
