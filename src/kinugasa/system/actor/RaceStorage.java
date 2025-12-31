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
package kinugasa.system.actor;

import kinugasa.game.annotation.Singleton;
import kinugasa.resource.Storage;

/**
 * RaceStorage.<br>
 *
 * @vesion 1.0.0 - 2025/10/19_18:04:52<br>
 * @author Shinacho.<br>
 */
@Singleton
public class RaceStorage extends Storage<Race> {

	private static final RaceStorage INSTANCE = new RaceStorage();

	private RaceStorage() {
	}

	public static RaceStorage getInstance() {
		return INSTANCE;
	}

}
