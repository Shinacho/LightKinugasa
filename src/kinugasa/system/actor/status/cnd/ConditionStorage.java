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
import kinugasa.game.GameLog;
import kinugasa.game.annotation.Singleton;
import kinugasa.object.FileObject;
import kinugasa.resource.IDNotFoundException;
import kinugasa.resource.Storage;
import kinugasa.system.actor.status.cnd.ConditionRegistValueSet;

/**
 * ConditionStorage.<br>
 *
 * @vesion 1.0.0 - 2025/10/03_20:53:30<br>
 * @author Shinacho.<br>
 */
@Singleton
class ConditionStorage {

	private static final ConditionStorage INSTANCE = new ConditionStorage();
	private Storage<Condition> data = new Storage<>();

	private ConditionStorage() {
	}

	static ConditionStorage getInstance() {
		return INSTANCE;
	}

	private File dir;

	void init(File dir) {
		this.dir = dir;
	}

	public Condition get(String id) throws IDNotFoundException {
		//メモリ探索
		if (data.contains(id)) {
			data.get(id);
		}
		//ファイル探索
		File f = new File(dir.getName() + "/" + id);

		if (f.exists()) {
			Condition c = new Condition(f);
			data.add(c);
			return c;
		}

		throw new IDNotFoundException("Condition Not Found : " + f.getName());

	}

}
