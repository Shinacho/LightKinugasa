/*
 * Copyright (C) 2023 Shinacho
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kinugasa.game.NotNewInstance;
import kinugasa.game.Nullable;
import kinugasa.game.field4.*;
import kinugasa.resource.NameNotFoundException;

/**
 * ステータス管理系のマスターです。
 *
 * @vesion 1.0.0 - 2022/11/16_15:45:53<br>
 * @author Shinacho<br>
 */
public class GameSystem {

	private static boolean debugMode = false;

	public static void setDebugMode(boolean debugMode) {
		GameSystem.debugMode = debugMode;
		FieldMap.setDebugMode(debugMode);
	}

	public static boolean isDebugMode() {
		return debugMode;
	}
	private static final GameSystem INSTANCE = new GameSystem();

	private GameSystem() {
	}

	public static GameSystem getInstance() {
		return INSTANCE;
	}

	//
	//--------------------------------------------------------------------------
	//
	private List<PCSprite> party = new ArrayList();

	@NotNewInstance
	public List<PCSprite> getParty() {
		return party;
	}

}
