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
package kinugasa.system.actor.action;

import java.io.File;
import kinugasa.game.DescSupport;
import kinugasa.game.I18NText;
import kinugasa.game.VisibleNameIDInjector;
import kinugasa.system.Actionable;
import kinugasa.object.FileObject;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.text.FileFormatException;

/**
 * Action.<br>
 *
 * @vesion 1.0.0 - 2025/08/13_14:56:52<br>
 * @author Shinacho.<br>
 */
public class Action extends FileObject implements VisibleNameIDInjector<Action>, DescSupport, Actionable {

	public Action(File f) {
		super(f);
	}

	@Override
	public FileObject load() throws FileNotFoundException, FileFormatException, ContentsIOException {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}

	@Override
	public void free() {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}

	@Override
	public boolean isLoaded() {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}

	@Override
	public I18NText getDesc() {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}

}
