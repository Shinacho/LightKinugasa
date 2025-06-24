/*
 * Copyright (C) 2024 Shinacho
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
package kinugasa.object;

import kinugasa.game.NewInstance;
import kinugasa.game.NotNewInstance;
import kinugasa.game.NotNull;
import kinugasa.game.OneceTime;

/**
 *
 * @vesion 1.0.0 - 2024/04/27_12:27:59<br>
 * @author Shinacho<br>
 */
public interface UIDSupport extends ID {

	@Override
	public default String getId() {
		return getUIDKey().getId();
	}

	//都度newしてもいい
	@NotNull
	public UIDKey getUIDKey();

	//都度newするとキャッシュに乗らない。
	@NotNewInstance
	@NotNull
	public UIDValue getUIDValue();

	public default boolean equals(UIDSupport s) {
		if (this == s) {
			return true;
		}
		if (s == null) {
			return false;
		}
		if (getClass() != s.getClass()) {
			return false;
		}
		return getUIDKey().equals(s.getUIDKey());
	}

	@OneceTime
	@NewInstance
	public default UIDValue createWeakUIDValue() {
		return UIDValue.weakOf(getUIDKey(), this);
	}

	@OneceTime
	@NewInstance
	public default UIDValue createSoftUIDValue() {
		return UIDValue.softOf(getUIDKey(), this);
	}
}
