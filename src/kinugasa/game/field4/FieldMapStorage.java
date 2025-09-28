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
package kinugasa.game.field4;

import java.io.File;
import kinugasa.game.GameLog;
import kinugasa.game.annotation.Singleton;
import kinugasa.game.system.GameSystem;
import kinugasa.resource.FileIOException;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.IDNotFoundException;
import kinugasa.resource.Storage;

/**
 * FieldMapStorage.<br>
 *
 * @vesion 1.0.0 - 2025/07/19_8:11:39<br>
 * @author Shinacho.<br>
 */
@Singleton
public final class FieldMapStorage {

	private static final FieldMapStorage INSTANCE = new FieldMapStorage();
	private final Storage<FieldMap> storage = new Storage<>();
	public static final String SUFFIX = ".kfm.txt";

	public static FieldMapStorage getInstance() {
		return INSTANCE;
	}

	private FieldMapStorage() {
	}

	void init(File dir) throws FileNotFoundException, FileIOException {
		if (!dir.isDirectory()) {
			throw new FileIOException(dir.getName() + " is not directory");
		}
		if (GameSystem.isDebugMode()) {
			GameLog.print("FMS : init start : " + dir.getPath());
			GameLog.addIndent();
		}
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				init(f);
				continue;
			}
			if (!f.getName().toLowerCase().endsWith(SUFFIX)) {
				continue;
			}
			FieldMap d = new FieldMap(f);
			//check
			d.setLoadScriptCall(false);
			d.load().free();
			d.setLoadScriptCall(true);
			this.storage.add(d);
		}
		if (GameSystem.isDebugMode()) {
			GameLog.removeIndent();
			GameLog.print("FMS : init end");
		}
	}

	public FieldMap get(String id) throws IDNotFoundException {
		return storage.get(id);
	}
	//--------------------------------------------------------------------------

}
