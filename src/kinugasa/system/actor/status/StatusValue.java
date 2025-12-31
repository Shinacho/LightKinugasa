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
package kinugasa.system.actor.status;

import kinugasa.game.I18NText;
import kinugasa.game.VisibleNameSupport;
import kinugasa.object.CloneableObject;
import kinugasa.system.UniversalValue;

/**
 * StatusValue.<br>
 *
 * @vesion 1.0.0 - 2025/10/05_12:38:39<br>
 * @author Shinacho.<br>
 */
public abstract class StatusValue<K extends Enum<K> & VisibleNameSupport> extends CloneableObject {

	public final K key;

	public StatusValue(K key) {
		this.key = key;
	}

	public I18NText getText() {
		return I18NText.of(key.getVisibleName().toString() + ":" + this.toString());
	}

}
