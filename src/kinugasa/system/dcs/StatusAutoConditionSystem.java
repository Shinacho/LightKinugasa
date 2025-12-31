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
package kinugasa.system.dcs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import kinugasa.game.annotation.Singleton;
import kinugasa.system.actor.Actor;
import kinugasa.system.actor.status.Status;

/**
 * StatusAutoConditionSystem.<br>
 *
 * @vesion 1.0.0 - 2025/10/11_4:01:34<br>
 * @author Shinacho.<br>
 */
@Singleton

public class StatusAutoConditionSystem {

	private StatusAutoConditionSystem() {
	}
	private static final StatusAutoConditionSystem INSTANCE = new StatusAutoConditionSystem();

	public static StatusAutoConditionSystem getInstance() {
		return INSTANCE;
	}
	private List<Consumer<Status>> fnc = new ArrayList<>();

	public void add(Consumer<Status> a) {
		fnc.add(a);
	}

	//何を呼び出すかがゲームにより異なるので、ゲームで設定する
	public void addAutoCnd(Status s) {
		for (var v : fnc) {
			v.accept(s);
		}
	}

}
