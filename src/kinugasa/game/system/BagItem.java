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
package kinugasa.game.system;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import kinugasa.game.DescSupport;
import kinugasa.game.I18NText;
import kinugasa.game.VisibleNameIDInjector;
import kinugasa.game.annotation.RequiresReturnTypeChange;
import kinugasa.object.FileObject;
import kinugasa.object.Saveable;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.text.FileFormatException;

/**
 * BagItem.<br>
 *
 * @vesion 1.0.0 - 2025/08/13_14:52:44<br>
 * @author Shinacho.<br>
 */
public abstract class BagItem extends FileObject
		implements Saveable<BagItem>,
		Actionable,
		VisibleNameIDInjector<BagItem>, DescSupport, Cloneable {

	protected BagItem(File f) {
		super(f);
	}

	@RequiresReturnTypeChange
	@Override
	public BagItem setFile(File file) {
		super.setFile(file);
		return this;
	}

	@RequiresReturnTypeChange
	@Override
	public abstract FileObject load() throws FileNotFoundException, FileFormatException, ContentsIOException;

	@Override
	public abstract void free();

	@Override
	public abstract boolean isLoaded();

	@Override
	public abstract void save() throws FileNotFoundException, ContentsIOException;

	@Override
	public I18NText getDesc() {
		return new I18NText(getId() + "_DESC");
	}

	@RequiresReturnTypeChange
	@Override
	public BagItem clone() {
		try {
			return (BagItem) super.clone();
		} catch (CloneNotSupportedException ex) {
			throw new InternalError(ex);
		}
	}

}
