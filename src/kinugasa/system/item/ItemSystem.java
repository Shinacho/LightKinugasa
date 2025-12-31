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
package kinugasa.system.item;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kinugasa.game.GameLog;
import kinugasa.game.annotation.Singleton;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.text.TextFile;
import kinugasa.system.GameSystem;
import kinugasa.system.UniversalValue;

/**
 * ItemSystem.<br>
 *
 * @vesion 1.0.0 - 2025/08/13_15:16:05<br>
 * @author Shinacho.<br>
 */
@Singleton
public class ItemSystem {

	private static final ItemSystem INSTNACE = new ItemSystem();

	private ItemSystem() {
	}

	public static ItemSystem getInstance() {
		return INSTNACE;
	}

	public static final String SUFFIX = ".item.txt";
	private File root;
	private final EnumMap<ItemTags, TextFile> indexFiles = new EnumMap<>(ItemTags.class);

	public void init(File root) {
		if (GameSystem.isDebugMode()) {
			GameLog.print("ScriptSystem init start");
			GameLog.addIndent();
		}
		if (!root.isDirectory()) {
			throw new IllegalArgumentException("ItemSystem" + root.getName() + " is not directory");
		}
		if (!root.exists()) {
			throw new IllegalArgumentException("ItemSystem" + root.getName() + " is not exists");
		}
		this.root = root;
		//タグファイルの作成
		for (var v : ItemTags.values()) {
			String p = root.getPath();
			if (!p.endsWith(File.separator)) {
				p += "/";
			}
			indexFiles.put(v, new TextFile(p + v.toString() + ".itemIndex.txt"));
		}
		setIndex(root);
	}

	private String getPath(String id) {
		return root.getPath() + "/" + id + SUFFIX;
	}

	private void setIndex(File root) {
		for (var v : root.listFiles()) {
			//アイテムは1つのフォルダに入ってる必要がある。IDでPK指定するため。
//			if (v.isDirectory()) {
//				setIndex(v);
//				continue;
//			}
			if (!v.getName().endsWith(SUFFIX)) {
				continue;
			}
//			Item i = new Item(v);
//			i.load();
//			for (var t : i.getItemTags()) {
//				TextFile tf = indexFiles.get(t);
//				tf.getData().add(new UniversalValue(i.getFile().getPath()));
//				tf.save();
//			}
//			i.free();
		}
	}

//	public Item get(String id) {
//		File f = new File(getPath(id));
//		if (!f.exists()) {
//			throw new FileNotFoundException("ItemSystem : not found : " + f.getPath());
//		}
//		return new Item(f);
//	}
//
//	public Item getOrNull(String id) {
//		File f = new File(getPath(id));
//		if (!f.exists()) {
//			throw null;
//		}
//		return new Item(f);
//	}
//
//	public List<Item> getNCopies(String id, int n) {
//		Item i = getOrNull(id);
//		if (i == null) {
//			return List.of();
//		}
//		return i.nCopies(n);
//	}

	public boolean exists(ItemTags... tags) {
		return exists(Arrays.asList(tags));
	}

	public boolean exists(Collection<ItemTags> tags) {
		if (tags.isEmpty()) {
			return false;
		}
		EnumMap<ItemTags, Set<File>> list = new EnumMap<>(ItemTags.class);

		for (var t : tags) {
			TextFile f = indexFiles.get(t);
			f.load();
			for (var v : f) {
				list.get(t).add(v.asFile());
			}
			f.free();
		}

		List<ItemTags> t = new ArrayList<>(tags);
		Set<File> r = new HashSet<>(list.getOrDefault(t.get(0), Set.of()));

		for (int i = 1; i < t.size(); i++) {
			r.retainAll(list.getOrDefault(t.get(i), Set.of()));
		}

		return !r.isEmpty();

	}

//	public Set<Item> get(ItemTags... tags) {
//		return get(Arrays.asList(tags));
//	}

//	public Set<Item> get(Collection<ItemTags> tags) {
//		if (tags.isEmpty()) {
//			return Set.of();
//		}
//		EnumMap<ItemTags, Set<File>> list = new EnumMap<>(ItemTags.class);
//
//		for (var t : tags) {
//			TextFile f = indexFiles.get(t);
//			f.load();
//			for (var v : f) {
//				list.get(t).add(v.asFile());
//			}
//			f.free();
//		}
//
//		List<ItemTags> t = new ArrayList<>(tags);
//		Set<File> r = new HashSet<>(list.getOrDefault(t.get(0), Set.of()));
//
//		for (int i = 1; i < t.size(); i++) {
//			r.retainAll(list.getOrDefault(t.get(i), Set.of()));
//		}
//
//		return r.stream().map(p -> new Item(p)).collect(Collectors.toSet());
//	}

	public boolean exists(String id) {
		return new File(getPath(id)).exists();
	}

}
