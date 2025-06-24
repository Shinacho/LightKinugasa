/*
 * Copyright (C) 2024 Shinacho
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

import kinugasa.game.RequiresReturnTypeChange;
import kinugasa.game.Virtual;

/**
 *
 * @vesion 1.0.0 - 2024/04/28_1:13:14<br>
 * @author Shinacho<br>
 */
public class CloneableObject implements Cloneable {

	@Override
	@Virtual
	@RequiresReturnTypeChange
	public CloneableObject clone() {
		try {
			return (CloneableObject) super.clone();
		} catch (CloneNotSupportedException ex) {
			throw new InternalError("clone failed");
		}
	}

	public static <T extends CloneableObject> T clone(T obj) {
		return (T) obj.clone();
	}
}
