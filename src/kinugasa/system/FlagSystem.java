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
package kinugasa.system;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import kinugasa.game.GameLog;
import kinugasa.game.annotation.Nullable;
import kinugasa.game.annotation.Singleton;
import kinugasa.resource.text.TextFile;

/**
 * FlagSystem.<br>
 *
 * @vesion 1.0.0 - 2025/08/10_2:18:22<br>
 * @author Shinacho.<br>
 */
@Singleton
public class FlagSystem {

	private static final FlagSystem INSTANCE = new FlagSystem();
	private File dir;

	private FlagSystem() {
	}

	public static FlagSystem getInstance() {
		return INSTANCE;
	}

	public void init(File f) {
		if (!f.isDirectory()) {
			throw new IllegalArgumentException("flag dir is not dir : " + f);
		}
		this.dir = f;
	}

	public boolean exeist(String name) {
		return get(name).exists;
	}

	public boolean notExistsOrOFF(String name) {
		var r = get(name);
		if (r.exists) {
			return false;
		}
		return !r.status;
	}

	public boolean existsAndON(String name) {
		var r = get(name);
		if (!r.exists) {
			return false;
		}
		return r.status;
	}

	public void createAndON(String name) {
		String d = dir.getName();
		if (!d.endsWith("/") && !d.endsWith("\\")) {
			d += "/";
		}
		String path = d + name + ".flg.txt";
		File f = new File(path);
		set(f, true);
	}

	public void createAndOFF(String name) {
		String d = dir.getName();
		if (!d.endsWith("/") && !d.endsWith("\\")) {
			d += "/";
		}
		String path = d + name + ".flg.txt";
		File f = new File(path);
		set(f, false);
	}

	public void delete(String name) {
		String d = dir.getName();
		if (!d.endsWith("/") && !d.endsWith("\\")) {
			d += "/";
		}
		String path = d + name + ".flg.txt";
		File f = new File(path);
		if (f.exists()) {
			f.delete();
		}
	}

	private void set(File f, boolean status) {
		if (f.exists()) {
			f.delete();
		}
		TextFile tf = new TextFile(f);
		tf.getData().clear();
		tf.getData().add(new UniversalValue(status ? "1" : "0"));
		tf.save();
	}

	public static class FlagFile {

		public final TextFile file;
		public final boolean exists;
		@Nullable
		public final Boolean status;

		public FlagFile(TextFile file, boolean exists, Boolean value) {
			this.file = file;
			this.exists = exists;
			this.status = value;
		}

		@Override
		public String toString() {
			return "FlagFile{" + "file=" + file + ", exists=" + exists + ", status=" + status + '}';
		}

	}

	public FlagFile get(String name) {
		String d = dir.getName();
		if (!d.endsWith("/") && !d.endsWith("\\")) {
			d += "/";
		}
		String path = d + name + ".flg.txt";
		return getFile(new File(path));
	}

	private FlagFile getFile(File f) {
		TextFile tf = new TextFile(f).setAutoTrim(true).setSkipCommentValue("#").setSkipEmptyLine(true);
		if (!tf.exists()) {
			return new FlagFile(tf, false, null);
		}
		String value = tf.load().getData().get(0).value();
		tf.free();
		boolean s = value.toLowerCase().equals("on") || value.toLowerCase().equals("true") || value.equals("1");
		return new FlagFile(tf, true, s);

	}

	public List<FlagFile> list() {
		List<FlagFile> r = new ArrayList<>();
		for (var f : dir.listFiles()) {
			if (f.getName().toLowerCase().endsWith(".flg.txt")) {
				r.add(getFile(f));
			}
		}
		return r;
	}

	public void printAll() {
		for (var v : list()) {
			System.out.println(v);
			GameLog.print(v);
		}
	}

}
