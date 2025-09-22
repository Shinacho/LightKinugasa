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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import kinugasa.game.GameLog;
import kinugasa.game.system.UniversalValue;
import kinugasa.object.FileObject;
import kinugasa.util.StringUtil;
import kinugasa.object.Saveable;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.FileIOException;
import kinugasa.resource.FileNotFoundException;

/**
 *
 * @vesion 1.0.0 - 2021/11/23_6:23:42<br>
 * @author Shinacho<br>
 */
public class CSVFile extends FileObject implements Saveable<CSVFile>, Iterable<List<UniversalValue>> {

	private Charset charset;
	private List<List<UniversalValue>> data = new ArrayList<>();
	private boolean loaded = false;

	public CSVFile(File file) {
		super(file);
		charset = StandardCharsets.UTF_8;
	}

	public CSVFile(String path) {
		this(new File(path));
	}

	public CSVFile(File file, Charset c) {
		super(file);
		this.charset = c;
	}

	public CSVFile(String path, Charset c) {
		this(new File(path), c);
		this.charset = c;
	}

	public List<List<UniversalValue>> getData() {
		return data;
	}

	@Override
	public Iterator<List<UniversalValue>> iterator() {
		return data.iterator();
	}

	public void add(List<UniversalValue> list) {
		this.data.add(list);
	}

	public void add(String[] data) {
		add(Arrays.stream(data).map(p -> new UniversalValue(p)).toList());
	}

	//--------------------------------------------------------------------------
	@Override
	public CSVFile load() throws FileIOException {
		if (!exists()) {
			throw new FileNotFoundException(getFile());
		}
		data = new ArrayList<>();
		try {
			for (String line : Files.readAllLines(getFile().toPath(), charset)) {
				if (line.isEmpty() || line.equals("\r") || line.equals("\r\n")) {
					continue;
				}
				if(line.trim().startsWith("#")){
					continue;
				}
				if (line.contains("#")) {
					line = line.substring(0, line.indexOf("#"));
				}
				line = line.trim();
				data.add(new ArrayList<>(Arrays.stream(StringUtil.safeSplit(line, ",")).map(p -> new UniversalValue(p)).toList()));
			}
		} catch (IOException ex) {
			throw new FileIOException(ex);
		}
		loaded = true;
		GameLog.print(getFile().getName() + " is loaded");
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
		List<String> lines = new ArrayList<>();
		for (List<UniversalValue> l : data) {
			lines.add(String.join(",", l.stream().map(p -> p.toString()).toList()));
		}
		try {
			Files.write(getFile().toPath(), lines, charset, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			GameLog.print(this + " is saved");
		} catch (IOException ex) {
			throw new FileIOException(ex);
		}
	}

	@Override
	public String toString() {
		return "CSVFile{" + getFile().getName() + '}';
	}

}
