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
import java.util.Iterator;
import java.util.List;
import kinugasa.game.GameLog;
import kinugasa.system.UniversalValue;
import kinugasa.object.FileObject;
import kinugasa.object.Saveable;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.FileIOException;
import kinugasa.resource.FileNotFoundException;

/**
 *
 * @vesion 1.0.0 - 2021/11/23_6:35:59<br>
 * @author Shinacho<br>
 */
public class TextFile extends FileObject implements Saveable<TextFile>, Iterable<UniversalValue> {

	private List<UniversalValue> data = new ArrayList<>();
	private Charset charset;
	private boolean loaded;

	public TextFile(String fileName) {
		this(new File(fileName), StandardCharsets.UTF_8);
	}

	public TextFile(File file) {
		this(file, StandardCharsets.UTF_8);
	}

	public TextFile(String fileName, Charset cs) {
		this(new File(fileName), cs);
	}

	public TextFile(File file, Charset cs) {
		super(file);
		this.charset = cs;
		data = new ArrayList<>();
	}

	public List<UniversalValue> getData() {
		return data;
	}

	@Override
	public Iterator<UniversalValue> iterator() {
		return data.iterator();
	}
	private boolean skipEmptyLine = false;
	private String skipCommentValue = null;
	private boolean autoTrim = false;

	public TextFile setAutoTrim(boolean autoTrim) {
		this.autoTrim = autoTrim;
		return this;
	}

	public TextFile setSkipCommentValue(String skipCommentValue) {
		this.skipCommentValue = skipCommentValue;
		return this;
	}

	public TextFile setSkipEmptyLine(boolean skipEmptyLine) {
		this.skipEmptyLine = skipEmptyLine;
		return this;
	}

	//--------------------------------------------------------------------------
	@Override
	public TextFile load() throws FileIOException, FileNotFoundException {
		if (!exists()) {
			throw new FileNotFoundException(getFile());
		}
		try {
			for (String line : Files.readAllLines(getPath(), charset)) {
				if (skipEmptyLine) {
					if (line.trim().isEmpty()) {
						continue;
					}
				}
				if (skipCommentValue != null) {
					if (line.trim().startsWith(skipCommentValue)) {
						continue;
					}
					if (line.contains(skipCommentValue)) {
						line = line.substring(0, line.indexOf(skipCommentValue));
					}
				}
				if (autoTrim) {
					data.add(new UniversalValue(line.trim()));
				} else {
					data.add(new UniversalValue(line));
				}
			}
		} catch (IOException ex) {
			throw new FileIOException(ex);
		}
		GameLog.print(getFile().getName() + " is loaded");
		loaded = true;
		return this;
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	@Override
	public void free() {
		data.clear();
		loaded = false;
	}

	@Override
	public void save() throws FileNotFoundException, ContentsIOException {
		try {
			Files.write(getPath(), data.stream().map(p -> p.toString()).toList(), charset, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			GameLog.print(this + " is saved");
		} catch (IOException ex) {
			throw new FileIOException(ex);
		}
	}

	@Override
	public String toString() {
		return "TextFile{" + getFile().getName() + '}';
	}

}
