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
package kinugasa.system.actor.status.cnd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import kinugasa.game.annotation.Singleton;
import kinugasa.resource.IDNotFoundException;
import kinugasa.system.actor.Actor;
import kinugasa.system.actor.status.Status;

/**
 * ConditionSystem.<br>
 *
 * @vesion 1.0.0 - 2025/10/03_20:00:24<br>
 * @author Shinacho.<br>
 */
@Singleton
public class ConditionSystem {

	private static final ConditionSystem INSTANCE = new ConditionSystem();

	public static ConditionSystem getInstance() {
		return INSTANCE;
	}

	private ConditionSystem() {
	}

	public void init(File root) {
		if (!root.exists()) {
			throw new IllegalArgumentException("Condition root is not found : " + root);
		}
		if (!root.isDirectory()) {
			throw new IllegalArgumentException("Condition root is not directory : " + root);
		}
		ConditionStorage.getInstance().init(root);

	}

	public Condition of(String id) throws IDNotFoundException {
		return ConditionStorage.getInstance().get(id);
	}

	private final List<Condition> visible = new ArrayList<>();

	public void addVisibleCnd(List<Condition> cnd) {
		cnd = cnd.stream().distinct().toList();
		visible.addAll(cnd);
	}

	public List<Condition> getVisible() {
		return visible;
	}

	private final List<BiFunction<Actor, Condition, Boolean>> cndCorres = new ArrayList<>();

	public void addCorre(List<BiFunction<Actor, Condition, Boolean>> l) {

//		{
//			イメージ 木化しているとき浮遊できない
//			cndCorres.add((s, c) -> {
//				if (c == "浮遊") {
//					if (s.getCurrentEffect().has("木化")) {
//						return false;
//					}
//				}
//				灰になっているとき、浮遊しようとすると消滅
//				if( c == 浮遊){
//					if(s.getCurrentEffect().has("灰燼")){
//						s.getCurrentEffect.forceRemove("灰燼");
//						s.getCurrentEffect().forceAdd("消滅");
//						return false;
//					}
//				}
//				return true;
//			});
//		}
		cndCorres.addAll(l);
	}

	public List<BiFunction<Actor, Condition, Boolean>> getCndCorres() {
		return cndCorres;
	}

}
