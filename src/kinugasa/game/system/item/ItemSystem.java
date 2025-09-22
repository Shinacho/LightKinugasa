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
package kinugasa.game.system.item;

import kinugasa.game.annotation.Singleton;
import kinugasa.resource.Storage;

/**
 * ItemSystem.<br>
 *
 * @vesion 1.0.0 - 2025/08/13_15:16:05<br>
 * @author Shinacho.<br>
 */
@Singleton
public class ItemSystem {

	private static final ItemSystem INSTNACE = new ItemSystem();

	private ItemSystem() {
	}

	public static ItemSystem getInstance() {
		return INSTNACE;
	}
	private Storage<Item> data;

}
