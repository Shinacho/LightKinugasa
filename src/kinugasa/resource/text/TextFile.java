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
import kinugasa.resource.Input;
import kinugasa.resource.InputStatus;
import kinugasa.resource.Output;
import kinugasa.resource.OutputResult;
import kinugasa.object.ID;
import kinugasa.resource.FileIOException;

/**
 *
 * @vesion 1.0.0 - 2021/11/23_6:35:59<br>
 * @author Shinacho<br>
 */
public class TextFile implements Input<TextFile>, Output, Iterable<String>, ID {

	private File file;
	private Charset charset;

	public TextFile(File file) {
		this.file = file;
		charset = StandardCharsets.UTF_8;
	}

	public TextFile(String path) {
		this.file = new File(path);
		charset = StandardCharsets.UTF_8;
	}

	public TextFile(File file, Charset c) {
		this.file = file;
		this.charset = c;
	}

	public TextFile(String path, Charset c) {
		this.file = new File(path);
		this.charset = c;
	}

	public boolean exists() {
		return file.exists();
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public String getId() {
		return file.getName();
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<String> getData() {
		return data;
	}

	@Override
	public Iterator<String> iterator() {
		return data.iterator();
	}

	//--------------------------------------------------------------------------
	private List<String> data = new ArrayList<>();

	@Override
	public TextFile load() throws FileIOException {
		data = new ArrayList<>();
		try {
			for (String line : Files.readAllLines(file.toPath(), charset)) {
				data.add(line);
			}
		} catch (IOException ex) {
			throw new FileIOException(ex);
		}
		return this;
	}

	@Override
	public void dispose() {
		data.clear();
		data = null;
	}

	@Override
	public InputStatus getInputStatus() {
		return data == null ? InputStatus.NOT_LOADED : InputStatus.LOADED;
	}

	@Override
	public OutputResult save() throws FileIOException {
		return saveTo(file);
	}

	@Override
	public OutputResult saveTo(File f) throws FileIOException {
		try {
			Files.write(f.toPath(), data, charset, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException ex) {
			throw new FileIOException(ex);
		}
		return OutputResult.OK;
	}

}
