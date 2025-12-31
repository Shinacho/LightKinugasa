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
package kinugasa.resource.text;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import kinugasa.field4.D2Idx;
import kinugasa.game.GameLog;
import kinugasa.game.annotation.Immutable;
import kinugasa.game.annotation.NewInstance;
import kinugasa.game.annotation.NotNull;
import kinugasa.game.annotation.Nullable;
import kinugasa.system.UniversalValue;
import kinugasa.object.FileObject;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.FileIOException;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.IDNotFoundException;
import kinugasa.util.StringUtil;

/**
 * DataFile.<br>
 *
 * @vesion 1.0.0 - 2025/07/23_12:35:52<br>
 * @author Shinacho.<br>
 */
public class DataFile extends FileObject implements Iterable<DataFile.Element> {

	@Immutable
	public static class Element implements Iterable<DataFile.Element> {

		@NotNull
		public final UniversalValue key;
		@Nullable
		public final String saoName;
		@Nullable
		public final UniversalValue value;
		@Nullable
		private final List<Element> elements;

		private Element(UniversalValue key, String saoName, UniversalValue value, List<Element> elements) {
			this.key = key;
			this.saoName = saoName;
			this.value = value;
			this.elements = elements;
		}

		public static Element head(String key) {
			return new Element(new UniversalValue(key), null, null, new ArrayList<>());
		}

		public static Element scriptBlock(String key, String saoName) {
			return new Element(new UniversalValue(key), saoName, null, new ArrayList<>());
		}

		public static Element keyValue(String key, String value) {
			return new Element(new UniversalValue(key), null, new UniversalValue(value), null);
		}

		public static Element key(String key) {
			return new Element(new UniversalValue(key), null, null, null);
		}

		public Element createKeyChild(String key) {
			Element e = key(key);
			if (this.elements == null) {
				throw new IllegalArgumentException("DataFile : createChild, but element is null");
			}
			this.elements.add(e);
			return e;
		}

		public Element createKeyValueChild(String key, String value) {
			Element e = keyValue(key, value);
			if (this.elements == null) {
				throw new IllegalArgumentException("DataFile : createChild, but element is null");
			}
			this.elements.add(e);
			return e;
		}

		public Element createHeadChild(String key) {
			Element e = head(key);
			if (this.elements == null) {
				throw new IllegalArgumentException("DataFile : createChild, but element is null");
			}
			this.elements.add(e);
			return e;
		}

		public Element createScriptBlockChild(String key, String saoName) {
			Element e = scriptBlock(key, saoName);
			if (this.elements == null) {
				throw new IllegalArgumentException("DataFile : createChild, but element is null");
			}
			this.elements.add(e);
			return e;
		}

		private void add(Element e) {
			this.elements.add(e);
		}

		public Element get(Enum<?> e) throws IDNotFoundException {
			return get(e.toString());
		}

		public Element get(String id) throws IDNotFoundException {
			if (elements == null) {
				throw new IDNotFoundException("DataFile : element is null : " + key + "." + id);
			}
			for (var v : elements) {
				if (v.key.value().equals(id)) {
					return v;
				}
			}
			throw new IDNotFoundException(id + " not found");
		}

		public boolean has(Enum<?> e) {
			return has(e.toString());
		}

		public boolean has(String id) {
			if (elements == null) {
				throw new IDNotFoundException("DataFile : element is null : " + key + "." + id);
			}
			for (var v : elements) {
				if (v.key.value().equals(id)) {
					return true;
				}
			}
			return false;
		}

		@NewInstance
		public List<Element> getElements() {
			return new ArrayList<>(elements);
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 83 * hash + Objects.hashCode(this.key);
			hash = 83 * hash + Objects.hashCode(this.saoName);
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Element other = (Element) obj;
			if (!Objects.equals(this.saoName, other.saoName)) {
				return false;
			}
			return Objects.equals(this.key, other.key);
		}

		@Override
		public Iterator<Element> iterator() {
			if (elements == null) {
				return List.<Element>of().iterator();
			}
			return elements.iterator();
		}

		@Override
		public String toString() {
			String r = key.toString();
			if (saoName != null) {
				return r + "(" + saoName + ")" + "{";
			}
			if (value != null) {
				return r + "=" + value.toString();
			}
			if (elements != null) {
				r += "{";
			}
			return r;
		}

	}
	public static final String COMMENT_KEY = "#";
	public static final String REPLACE_KEY = "-DEFINE";
	private Charset charset;
	private boolean loaded;
	//
	private Element root;

	public DataFile(File f) {
		this(f, StandardCharsets.UTF_8);
	}

	public DataFile(String filePath) {
		this(filePath, StandardCharsets.UTF_8);
	}

	public DataFile(File f, Charset c) {
		super(f);
		this.charset = c;
	}

	public DataFile(String filePath, Charset c) {
		super(filePath);
		this.charset = c;
	}

	public List<Element> getData() {
		return root.elements;
	}

	@Override
	public DataFile load() throws FileNotFoundException, FileFormatException, ContentsIOException {
		if (!exists()) {
			throw new FileNotFoundException(getFile());
		}
		try {
			var data1 = Files.readAllLines(getPath(), charset);
			var data2 = new ArrayList<String>();
			Map<String, String> replaceMap = new HashMap<>();
			//REPLACE前
			for (int i = 0; i < data1.size(); i++) {
				String line = data1.get(i).trim();
				if (line.isEmpty()) {
					continue;
				}
				if (line.startsWith(COMMENT_KEY)) {
					continue;
				}
				if (line.contains(COMMENT_KEY)) {
					line = line.substring(0, line.indexOf(COMMENT_KEY)).trim();
				}
				if (line.startsWith(REPLACE_KEY)) {
					line = line.replaceAll(REPLACE_KEY, "").trim();
					int idx = line.indexOf(" ");
					String key = line.substring(0, idx).trim();
					String value = line.substring(idx + 1).trim();
					replaceMap.put(key, value);
					continue;
				}
				data2.add(line);
			}

			//REPLACE後の作成
			var data3 = new ArrayList<String>();
			for (int i = 0; i < data2.size(); i++) {
				String line = data2.get(i);
				for (var v : replaceMap.entrySet()) {
					if (line.contains(v.getKey())) {
						line = line.replaceAll(v.getKey(), v.getValue());
					}
				}
				data3.add(line);
			}

			//"{"と"}"のチェック
			int open = (int) data3.stream().filter(p -> p.contains("{")).count();
			int close = (int) data3.stream().filter(p -> p.contains("}")).count();
			if (open != close) {
				throw new FileFormatException("DataFile [" + getId() + "] : open-close missmatch");
			}

			//整形
			root = Element.head("ROOT");
			parse(root, data3, 0);

		} catch (IOException ex) {
			throw new FileIOException(ex);
		}
		GameLog.print(getFile().getName() + " is loaded");
		loaded = true;
		return this;
	}

	private int parse(Element parent, List<String> d, int j) throws FileFormatException {
		int i = j;
		for (; i < d.size(); i++) {
			String line = d.get(i);

			if (line.startsWith("-")) {
				throw new FileFormatException("DataFile [" + getId() + "] :  PreProcesser found : " + line);
			}

			//end
			if (line.equals("}")) {
				return i;
			}

			//CSV
			if (line.endsWith("==")) {
				line = line.replaceAll(" ", "").replaceAll("\t", "");
				line = line.substring(0, line.length() - 2); // "=="
				Element data = parent.createHeadChild(line);
				for (int y = i = i + 1; y < d.size() && !d.get(y).equals("=="); y++, i++) {
					Element e = data.createHeadChild(y + "");
					String[] csv = StringUtil.safeSplit(d.get(y), ",");
					for (int x = 0; x < csv.length; x++) {
						D2Idx key = new D2Idx(x, y);
						String value = csv[x];
						e.createKeyValueChild(key.toString(), value);
					}
				}
				continue;
			}

			//Block
			if (line.endsWith("{")) {
				line = line.replaceAll(" ", "").replaceAll("\t", "");

				//ScriptBlock
				if (line.endsWith("){")) {
					int idx = line.indexOf("(");
					String key = line.substring(0, idx);
					String saoName = line.substring(idx + 1);
					saoName = saoName.substring(0, saoName.length() - 2); // "){"
					Element e = parent.createScriptBlockChild(key, saoName);
					i = parse(e, d, i + 1);
					continue;
				}
				//head
				String key = line.substring(0, line.length() - 1); // "{"
				Element e = parent.createHeadChild(key);
				i = parse(e, d, i + 1);
				continue;
			}

			//KeyValue
			if (line.contains("=")) {
				//Key=Value
				int idx = line.indexOf('=');
				String key = line.substring(0, idx).trim();
				String value = line.substring(idx + 1).trim();
				parent.createKeyValueChild(key, value);
				continue;
			}
			//Key
			parent.createKeyChild(line);
		}
		return d.size();
	}

	@Override
	public void free() {
		clear(root);
		loaded = false;
	}

	private void clear(Element e) {
		if (e.elements != null) {
			e.elements.stream().forEach(p -> clear(p));
			e.elements.clear();
		}
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	@Override
	public Iterator<Element> iterator() {
		return root.iterator();
	}

	public Element get(Enum<?> e) throws IDNotFoundException {
		return root.get(e);
	}

	public Element get(String id) throws IDNotFoundException {
		return root.get(id);
	}

	public boolean has(Enum<?> e) {
		return root.has(e);
	}

	public boolean has(String id) {
		return root.has(id);
	}

	public void print() {
		System.out.println("-->print [" + getId() + "]");
		printAll(root, "");
		System.out.println("<--print [" + getId() + "]");
	}

	private void printAll(Element e, String tab) {
		System.out.println(tab + e.toString());
		if (e.elements != null) {
			e.elements.stream().forEach(p -> printAll(p, StringUtil.repeat(" ", tab.length() + 1)));
			System.out.println(tab + "}");
		}
	}

}
