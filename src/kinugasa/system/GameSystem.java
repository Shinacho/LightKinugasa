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
package kinugasa.system;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kinugasa.game.annotation.NewInstance;
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.game.annotation.Nullable;
import kinugasa.system.actor.Actor;
import kinugasa.resource.IDNotFoundException;

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
	private final List<Actor> pcList = new ArrayList<>();

	@NotNewInstance
	public List<Actor> getPcList() {
		return pcList;
	}

	@Nullable
	public Actor getPc0() {
		return pcList.isEmpty() ? null : pcList.get(0);
	}

	@Nullable
	public Actor getPcById(PlayableChara id) {
		for (var v : pcList) {
			if (v.getId().equals(id.getId())) {
				return v;
			}
		}
		return null;
	}

	public boolean pcInParty(PlayableChara id) {
		for (var v : pcList) {
			if (v.getId().equals(id.getId())) {
				return true;
			}
		}
		return false;
	}

	//
	//--------------------------------------------------------------------------
	//mode
	public enum Mode {
		BATTLE,
		FIELD,
		OTHER;
	}
	private Mode mode = Mode.OTHER;

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public Mode getMode() {
		return mode;
	}

}
