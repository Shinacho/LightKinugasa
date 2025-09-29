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
import kinugasa.game.GameLog;
import kinugasa.game.annotation.NotNewInstance;
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

	public static class Element implements Iterable<DataFile.Element> {

		private final String origin;
		private final UniversalValue key;
		@Nullable
		private UniversalValue value;
		@Nullable
		private List<Element> elements;

		public Element(String origin, UniversalValue key) {
			this.origin = origin;
			this.key = key;
		}

		public Element(String origin, UniversalValue key, UniversalValue value) {
			this.origin = origin;
			this.key = key;
			this.value = value;
		}

		public UniversalValue getKey() {
			return key;
		}

		@Nullable
		public UniversalValue getValue() {
			return value;
		}

		@Nullable
		public List<Element> getElements() {
			return elements;
		}

		public Element get(String id) throws IDNotFoundException {
			for (var v : elements) {
				if (v.key.value().equals(id)) {
					return v;
				}
			}
			throw new IDNotFoundException(id + " not found");
		}

		public boolean has(String id) {
			for (var v : elements) {
				if (v.key.value().equals(id)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public String toString() {
			return "Element{" + "key=" + key + ", value=" + value + ", size=" + (elements == null ? "null" : elements.size()) + '}';
		}

		public String original() {
			return origin;
		}

		@Override
		public Iterator<Element> iterator() {
			return elements == null ? List.<Element>of().iterator() : elements.iterator();
		}

	}
	private Charset charset;
	private List<Element> data = new ArrayList<>();
	private boolean loaded;

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

	public static final String COMMENT_KEY = "#";
	public static final String REPLACE_KEY = "-DEFINE";
	private boolean skipEmptyLine = true;
	private boolean autoTrim = true;

	public DataFile setAutoTrim(boolean autoTrim) {
		this.autoTrim = autoTrim;
		return this;
	}

	public DataFile setSkipEmptyLine(boolean skipEmptyLine) {
		this.skipEmptyLine = skipEmptyLine;
		return this;
	}

	@NotNewInstance
	public List<Element> getData() {
		return data;
	}

	@Override
	public DataFile load() throws FileNotFoundException, FileFormatException, ContentsIOException {
		if (!exists()) {
			throw new FileNotFoundException(getFile());
		}
		try {
			var d = Files.readAllLines(getPath(), charset);
			var data2 = new ArrayList<String>();
			Map<String, String> replaceMap = new HashMap<String, String>();
			//整形
			for (int i = 0; i < d.size(); i++) {
				String line = d.get(i);
				if (skipEmptyLine) {
					if (line.trim().isEmpty()) {
						continue;
					}
				}
				if (autoTrim) {
					line = line.trim();
				}
				if (line.startsWith(COMMENT_KEY)) {
					continue;
				}
				if (line.contains(COMMENT_KEY)) {
					line = line.substring(0, line.indexOf(COMMENT_KEY));
				}
				if (line.startsWith(REPLACE_KEY)) {
					line = line.replaceAll(REPLACE_KEY, "").trim();
					int idx = line.indexOf(" ");
					String key = line.substring(0, idx).trim();
					String value = line.substring(idx + 1).trim();
					replaceMap.put(key, value);
					continue;
				}
				for (var v : replaceMap.entrySet()) {
					if (line.contains(v.getKey())) {
						line = line.replaceAll(v.getKey(), v.getValue());
					}
				}
				data2.add(line);
			}
			parse(null, data2, 0);

		} catch (IOException ex) {
			throw new FileIOException(ex);
		}
		GameLog.print(getFile().getName() + " is loaded");
		loaded = true;
		return this;
	}

	private int parse(Element root, List<String> d, int j) throws FileFormatException {
		for (int i = j; i < d.size(); i++) {
			String v = d.get(i);
			if (root != null && v.equals("}")) {
				return i;
			}
			if (v.endsWith("{")) {
				if (!v.contains("=")) {
					throw new FileFormatException("{ has not =" + this);
				}
				Element e = new Element(v, new UniversalValue(StringUtil.safeSplit(v, "=")[0].trim()));
				if (root == null) {
					this.data.add(e);
				} else {
					if (root.elements == null) {
						root.elements = new ArrayList<>();
					}
					root.elements.add(e);
				}
				i = parse(e, d, ++i);
			} else if (v.endsWith("==")) {
				i++;
				String key = v.replaceAll("==", "").trim();
				Element e0 = new Element(v, new UniversalValue(key));
				e0.elements = new ArrayList<>();
				if (root == null) {
					this.data.add(e0);
				} else {
					if (root.elements == null) {
						root.elements = new ArrayList<>();
					}
					root.elements.add(e0);
				}
				for (int n = 0; i < d.size() && !d.get(i).equals("=="); i++, n++) {
					String nk = "" + n;
					Element e1 = new Element(v, new UniversalValue(nk));
					e0.elements.add(e1);
					e1.elements = new ArrayList<>();
					UniversalValue[] val = new UniversalValue(d.get(i)).safeSplitUV(",");
					for (int x = 0; x < val.length; x++) {
						Element e2 = new Element(v, new UniversalValue(n + "," + x), val[x]);
						e1.elements.add(e2);
					}

				}

			} else if (v.contains("=")) {
				UniversalValue key = new UniversalValue(StringUtil.safeSplit(v, "=")[0].trim());
				UniversalValue value = new UniversalValue(StringUtil.safeSplit(v, "=")[1].trim());
				Element e = new Element(v, key, value);
				if (root == null) {
					this.data.add(e);
				} else {
					if (root.elements == null) {
						root.elements = new ArrayList<>();
					}
					root.elements.add(e);
				}
			} else {
				if (root == null) {
					this.data.add(new Element(v, new UniversalValue(v)));
				} else {
					if (root.elements == null) {
						root.elements = new ArrayList<>();
					}
					root.elements.add(new Element(v, new UniversalValue(v)));
				}
			}
		}
		return d.size();
	}

	@Override
	public void free() {
		data.clear();
		loaded = false;
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	@Override
	public Iterator<Element> iterator() {
		return data.iterator();
	}

	public Element get(Enum<?> e) throws IDNotFoundException {
		return get(e.toString());
	}

	public Element get(String id) throws IDNotFoundException {
		for (var v : data) {
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
		for (var v : data) {
			if (v.key.value().equals(id)) {
				return true;
			}
		}
		return false;
	}

	public void print() {
		System.out.println("-->print [" + getId() + "]");
		printAll(data, "");
		System.out.println("<--print [" + getId() + "]");
	}

	private void printAll(List<Element> e, String tab) {
		for (var v : e) {
			if (v.getValue() != null) {
				System.out.println(tab + v.getKey() + "=" + v.getValue());
				continue;
			}
			if (v.getElements() != null && !v.getElements().isEmpty()) {
				System.out.println(tab + v.getKey() + "={");
				printAll(v.getElements(), StringUtil.repeat(" ", tab.length() + 1));
				System.out.println(tab + "}");
			}
		}
	}

}
