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
package kinugasa.resource.sound;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import kinugasa.game.annotation.Singleton;
import kinugasa.object.Updateable;

/**
 * BGMSystem.<br>
 *
 * @vesion 1.0.0 - 2025/07/23_8:00:33<br>
 * @author Shinacho.<br>
 */
@Singleton
public class SoundSystem implements Updateable<SoundSystem.Mode>, Iterable<Sound> {

	@Override
	public Iterator<Sound> iterator() {
		return SoundStorage.getInstance().iterator();
	}

	public enum Mode {
		END_TO_NEXT,
		PLAYING,
		STOP,;
	}
	private static final SoundSystem INSTANCE = new SoundSystem();

	public void init(File root) {
		if (!root.exists()) {
			throw new IllegalArgumentException("sound root is not found : " + root);
		}
		if (!root.isDirectory()) {
			throw new IllegalArgumentException("sound root is not directory : " + root);
		}
		SoundStorage.getInstance().init(root);
	}

	public Sound of(String id) {
		return SoundStorage.getInstance().get(id);
	}

	private SoundSystem() {

	}

	public static SoundSystem getInstance() {
		return INSTANCE;
	}

	public void free() {
		SoundStorage.getInstance().forEach(p -> p.stop().free());
	}

	private Sound current, next;

	private final Set<Sound> autoCloseSound = new HashSet<>();

	public void setCurrent(Sound current) {
		this.current = current;
	}

	public void setNext(Sound next) {
		this.next = next;
	}

	public Sound getCurrent() {
		return current;
	}

	public Sound getNext() {
		return next;
	}

	public void toNext() {
		if (current != null) {
			current.stop().free();
		}
		if (next != null) {
			current = next;
			current.load().play();
			next = null;
		}
	}

	public void toNext(Sound next) {
		setNext(next);
		toNext();
	}

	public void addAutoCloseSound(Sound s) {
		autoCloseSound.add(s);
	}

	@Override
	public Mode update() {
		//自動解放の処理
		Set<Sound> remove = new HashSet<>();
		for (var v : autoCloseSound) {
			if (v.isEnded()) {
				v.stop().free();
				remove.add(v);
			}
		}
		autoCloseSound.removeAll(remove);

		//カレントのフェード
		if (current != null) {
			if (current.update() == Sound.State.LOADED_STOP) {
				toNext();
				return Mode.END_TO_NEXT;
			}
			return Mode.PLAYING;
		}
		return Mode.STOP;
	}

	@Override
	public boolean isEnded() {
		return false;
	}

	@Override
	public boolean isRunning() {
		return true;
	}

}
