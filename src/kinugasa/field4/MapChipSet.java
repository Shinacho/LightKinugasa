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
package kinugasa.field4;

import java.io.File;
import java.util.Map;
import kinugasa.game.GameLog;
import kinugasa.system.UniversalValue;
import kinugasa.graphics.KImage;
import kinugasa.object.FileObject;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.IDNotFoundException;
import kinugasa.resource.Storage;
import kinugasa.resource.text.FileFormatException;
import kinugasa.resource.text.TextFile;

/**
 * MapChipSet.<br>
 *
 * @vesion 1.0.0 - 2025/07/19_7:56:29<br>
 * @author Shinacho.<br>
 */
public class MapChipSet extends FileObject {

	private final Storage<MapChip> storage = new Storage<>();
	private boolean loaded = false;

	public MapChipSet(String fileName) {
		super(fileName);
	}

	public MapChipSet(File file) {
		super(file);
	}
	private boolean check = false;

	public int getChipSize() {
		int size = storage.first().getImage().getWidth();
		if (!check) {
			for (var v : storage) {
				if (v.getImage().getHeight() != size) {
					throw new ContentsIOException("MapChip size missmatch " + v);
				}
				if (v.getImage().getWidth() != size) {
					throw new ContentsIOException("MapChip size missmatch " + v);
				}
			}
		}
		check = true;
		return size;
	}

	public MapChip get(String id) throws IDNotFoundException {
		if (!loaded) {
			throw new IllegalStateException("MapChip [" + getId() + "] not yet load");
		}
		return storage.get(id);
	}

	@Override
	public MapChipSet load() throws FileNotFoundException, FileFormatException, ContentsIOException {
		if (isLoaded()) {
			throw new ContentsIOException("MapChipSet double load : " + this);
		}
		/*

	パースサンプル
	#CHIPSET
	
	image=resource/fieldmap/chipSet_outer16.png
	width=16
	height=16
	
	
	VALUE{
	
	000000,CLOSE
	000001,SHALLOW_WATER
	000002,SHALLOW_WATER
	000003,SHALLOW_WATER
	000004,SHALLOW_WATER
	000005,PORT
	000006,PORT
	000007,PORT
	}
		 */

		TextFile file = new TextFile(getFile()).load();

		KImage image = null;
		int w, h;
		w = h = 0;
		// 3設定
		for (var v : file) {
			if (v.value().trim().startsWith("image")) {
				if (!v.value().contains("=")) {
					throw new FileFormatException("key=value missing : image");
				}
				image = v.safeSplitUV("=")[1].asKImageFile();
				continue;
			}
			if (v.value().trim().startsWith("width")) {
				if (!v.value().contains("=")) {
					throw new FileFormatException("key=value missing : width");
				}
				w = v.safeSplitUV("=")[1].asInt();
				continue;
			}
			if (v.value().trim().startsWith("height")) {
				if (!v.value().contains("=")) {
					throw new FileFormatException("key=value missing : height");
				}
				h = v.safeSplitUV("=")[1].asInt();
				continue;
			}
		}
		if (image == null) {
			throw new FileFormatException("key=value missing : image");
		}
		if (w == 0) {
			throw new FileFormatException("key=value missing : width");
		}
		if (h == 0) {
			throw new FileFormatException("key=value missing : height");
		}

		//イメージカット
		Map<String, KImage> images = image.splitAsMap(w, h, KImage.DEFAULT_NAME_MAPPER);

		//VALUEロード
		int i = 0;
		for (; i < file.getData().size(); i++) {
			UniversalValue line = file.getData().get(i);
			if (line.value().contains("#")) {
				line = new UniversalValue(line.value().substring(0, line.value().indexOf("#")));
			}
			if (line.value().trim().replaceAll(" ", "").startsWith("VALUE")) {
				i += 1;
				if (!line.value().trim().endsWith("{")) {
					i += 1;
				}
				break;
			}
		}
		for (; i < file.getData().size() && !file.getData().get(i).value().trim().endsWith("}"); i++) {
			UniversalValue line = file.getData().get(i);
			if (line.isEmpty()) {
				continue;
			}
			if (line.value().trim().startsWith("#")) {
				continue;
			}
			if (line.value().contains(",")) {
				if (line.value().contains("#")) {
					line = new UniversalValue(line.value().substring(0, line.value().indexOf("#")));
				}
				UniversalValue[] val = line.safeSplitUV(",");
				String id = val[0].value();
				MapChipAttribute attr = val[1].of(MapChipAttribute.class);
				if (!images.containsKey(id)) {
					GameLog.print("IMAGES : " + images.keySet().stream().sorted().toList());
					throw new FileFormatException("MapChipSet[" + getFile().getName() + "] : image id missing : " + id);
				}
				MapChip c = new MapChip(id, attr, images.get(id));
				storage.add(c);
			}
		}

		file.free();
		loaded = true;
		GameLog.print("MapChipSet [" + getFile().getName() + "] is loaded : " + storage.size());
		return this;

	}

	@Override
	public void free() {
		loaded = false;
		storage.forEach(p -> p.free());
		storage.clear();
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	@Override
	public String toString() {
		return "MapChipSet{" + "id=" + getId() + ", storage=" + storage.size() + ", loaded=" + loaded + '}';
	}

}
