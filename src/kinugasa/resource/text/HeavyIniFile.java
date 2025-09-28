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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import kinugasa.game.annotation.Nullable;
import kinugasa.system.UniversalValue;
import kinugasa.object.FileObject;
import kinugasa.resource.ID;
import kinugasa.util.StringUtil;

/**
 * HeavyIniFileは省メモリですが低速なIniファイルです.<br>
 * 大量のデータを扱う場合に使うオプションです。getAllを使うとパフォ―マスが向上します。<br>
 *
 * @vesion 1.0.0 - 2025/07/24_19:17:55<br>
 * @author Shinacho.<br>
 */
public class HeavyIniFile{

	private File file;
	private Charset charset;
	private boolean loaded;
	private BufferedReader br;

	public HeavyIniFile(String fileName) {
		this(new File(fileName));
	}

	public HeavyIniFile(File file) {
		this.file = file;
		charset = StandardCharsets.UTF_8;
	}

	public HeavyIniFile(String fileName, Charset c) {
		this(new File(fileName), c);
	}

	public HeavyIniFile(File file, Charset c) {
		this.file = file;
		charset = c;
	}

	public File getFile() {
		return file;
	}

	@Nullable
	private UniversalValue findOrNull(String key) {
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("[")) {
					continue;
				}
				if (line.isEmpty()) {
					continue;
				}
				if (line.startsWith("#")) {
					continue;
				}
				String[] val = StringUtil.safeSplit(line, "=");
				if (val[0].trim().equals(key)) {
					return new UniversalValue(val[1].trim());
				}
			}
		} catch (IOException ex) {
		}
		return new UniversalValue((String) null);
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

	public Map<String, UniversalValue> getAll(Collection<String> s) {
		Map<String, UniversalValue> res = new HashMap<>();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("[")) {
					continue;
				}
				if (line.isEmpty()) {
					continue;
				}
				if (line.startsWith("#")) {
					continue;
				}
				String[] val = StringUtil.safeSplit(line, "=");
				for (var v : s) {
					if (val[0].trim().equals(s)) {
						res.put(v, new UniversalValue(val[1]));
					}
				}
			}
		} catch (IOException ex) {
		}
		return res;
	}

	@Override
	public String toString() {
		return "HeavyIniFile{" + getFile().getName() + '}';
	}
}
