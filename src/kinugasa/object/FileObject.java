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
package kinugasa.object;

import kinugasa.resource.ID;
import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import kinugasa.game.annotation.RequiresReturnTypeChange;
import kinugasa.game.annotation.Virtual;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.text.CSVFile;
import kinugasa.resource.text.DataFile;
import kinugasa.resource.text.FileFormatException;
import kinugasa.resource.text.IniFile;
import kinugasa.resource.text.TextFile;
import kinugasa.resource.text.XMLFile;

/**
 * FilesObjectはファイルの1:1に対応するクラスです.<br>
 * これは通常、ファイルからロードしてデータを設定するものですが、必ずしもファイルを使用しなくてもよい場合もあります。<br>
 *
 * @vesion 1.0.0 - 2025/07/19_13:43:00<br>
 * @author Shinacho.<br>
 */
public abstract class FileObject implements ID {

	private File file;

	public FileObject(String fileName) {
		this(new File(fileName));
	}

	public FileObject(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	@Virtual
	protected FileObject setFile(File file) {
		this.file = file;
		return this;
	}

	public TextFile asTextFile() {
		return new TextFile(file);
	}

	public IniFile asIniFile() {
		return new IniFile(file);
	}

	public CSVFile asCSVFile() {
		return new CSVFile(file);
	}

	public DataFile asDataFile() {
		return new DataFile(file);
	}

	public XMLFile asXMLFile() {
		return new XMLFile(file);
	}

	public boolean exists() {
		return file.exists();
	}

	@RequiresReturnTypeChange
	public abstract FileObject load() throws FileNotFoundException, FileFormatException, ContentsIOException;

	public abstract void free();

	public abstract boolean isLoaded();

	public String getName() {
		String s = file.getName();
		return s.substring(0, s.indexOf("."));
	}

	@Override
	public String getId() {
		return getName();
	}

	public Path getPath() {
		return file.toPath();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 83 * hash + Objects.hashCode(this.file);
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
		final FileObject other = (FileObject) obj;
		return Objects.equals(this.file, other.file);
	}

}
