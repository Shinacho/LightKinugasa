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
package kinugasa.game;

import kinugasa.resource.NameNotFoundException;
import kinugasa.resource.text.IniFile;

/**
 * IniI18NReader.<br>
 *
 * @vesion 1.0.0 - 2025/06/24_18:17:58<br>
 * @author Shinacho.<br>
 */
public class IniI18NReader implements I18NReader {

	private IniFile file;

	public IniI18NReader(IniFile file) {
		this.file = file;
	}

	@Override
	public String getValue(String key) throws NameNotFoundException {
		if (!file.getInputStatus().loaded()) {
			file.load();
		}
		return file.containsKey(key) ? file.get(key).get().toString() : null;

	}
}
