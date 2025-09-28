/*
  * MIT License
  *
  * Copyright (c) 2025 しなちょ
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
 */
package kinugasa.resource.text;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import kinugasa.game.GameLog;
import kinugasa.game.annotation.NewInstance;
import kinugasa.game.annotation.Nullable;
import kinugasa.system.KeyAndValue;
import kinugasa.system.UniversalValue;
import kinugasa.object.FileObject;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.ID;
import kinugasa.resource.IDImpl;
import kinugasa.object.Saveable;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.FileIOException;
import kinugasa.util.StringUtil;

/**
 *
 * @vesion 1.0.0 - 2021/08/17_6:55:06<br>
 * @author Shinacho<br>
 */
public final class IniFile extends FileObject implements Saveable<IniFile>, Iterable<KeyAndValue<ID, UniversalValue>> {

	private Charset charset;
	private final Set<KeyAndValue<ID, UniversalValue>> data;
	private boolean loaded;

	public IniFile(String fileName) {
		this(new File(fileName));
	}

	public IniFile(File file) {
		super(file);
		data = new HashSet<>();
		charset = StandardCharsets.UTF_8;
	}

	public IniFile(String fileName, Charset c) {
		this(new File(fileName), c);
	}

	public IniFile(File file, Charset c) {
		super(file);
		data = new HashSet<>();
		charset = c;
	}

	@Override
	public IniFile load() throws FileNotFoundException, FileFormatException {
		try {
			for (String line : Files.readAllLines(getPath(), charset)) {
				line = line.trim();
				if (line.isEmpty()) {
					continue;
				}
				if (line.startsWith("#")) {
					continue;
				}
				if (line.startsWith("[")) {
					if (!line.endsWith("]")) {
						throw new FileFormatException(getFile(), line);
					}
					continue;
				}
				if (line.endsWith("]")) {
					if (!line.startsWith("[")) {
						if (!line.endsWith("]")) {
							throw new FileFormatException(getFile(), line);
						}
						continue;
					}
				}
				if (!line.contains("=")) {
					throw new FileFormatException(getFile(), line);
				}
				String[] val = StringUtil.safeSplit(line, "=");
				if (val.length == 0 || val.length >= 3) {
					throw new FileFormatException(getFile(), line);
				}
				//すでにある場合は上書きする
				IDImpl k = new IDImpl(val[0]);
				UniversalValue v = val.length == 2 ? new UniversalValue(val[1]) : new UniversalValue("");
				if (containsKey(k)) {
					throw new FileFormatException(getFile(), line);
				}
				data.add(new KeyAndValue<>(k, v));
			}
		} catch (IOException ex) {
			throw new FileIOException(ex);
		}
		GameLog.print(getFile().getName() + " is loaded");
		loaded = true;
		return this;
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
	public void save() throws FileNotFoundException, ContentsIOException {
		if (!exists()) {
			throw new FileNotFoundException(getFile());
		}
		try {
			List<String> list = new ArrayList<>();
			data.forEach(p -> list.add(p.toString()));
			Files.write(getPath(), list, charset, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			GameLog.print(this + " is saved");
		} catch (IOException ex) {
			throw new FileIOException(ex);
		}
	}

	@Nullable
	private UniversalValue findOrNull(String key) {
		for (var v : data) {
			if (v.getKey().getId().equals(key)) {
				return v.getValue();
			}
		}
		return null;
	}

	public boolean containsKey(String key) {
		return findOrNull(key) != null;
	}

	public boolean containsKey(ID key) {
		return containsKey(key.getId());
	}

	@Nullable
	public UniversalValue getValue(String key) {
		return findOrNull(key);
	}

	@Nullable
	public UniversalValue getValue(ID key) {
		return getValue(key.getId());
	}

	public Optional<UniversalValue> get(String key) {
		return Optional.of(getValue(key));
	}

	public Optional<UniversalValue> get(ID key) {
		return get(key.getId());
	}

	public void remove(String key) {
		KeyAndValue<ID, UniversalValue> remove = null;
		for (var v : data) {
			if (v.getId().equals(key)) {
				remove = v;
				break;
			}
		}
		if (remove != null) {
			data.remove(remove);
		}
	}

	public void remove(ID key) {
		remove(key.getId());
	}

	public void put(String key, UniversalValue v) {
		put(new KeyAndValue<>(new IDImpl(key), v));
	}

	public void put(KeyAndValue<ID, UniversalValue> v) {
		data.add(new KeyAndValue<>(v));
	}

	@Override
	@NewInstance
	public Iterator<KeyAndValue<ID, UniversalValue>> iterator() {
		return data.iterator();
	}

	@Override
	public String toString() {
		return "IniFile{" + getFile().getName() + '}';
	}

}
