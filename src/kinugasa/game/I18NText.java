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
package kinugasa.game;

import java.util.Objects;
import kinugasa.game.ui.Text;
import kinugasa.object.ID;

/**
 *
 * @vesion 1.0.0 - 2024/04/28_1:27:07<br>
 * @author Shinacho<br>
 */
@Immutable
public class I18NText implements ID {

	private final String key;

	public static I18NText of(String msg) {
		return new I18NText(msg) {
			@Override
			public String i18nd() {
				return msg;
			}

			@Override
			public I18NText set(Object... o) {
				throw new UnsupportedOperationException("set has already been executed.");
			}

		};
	}

	public I18NText(String key) {
		this.key = key;
	}

	public I18NText(Enum<?> e) {
		this.key = e.toString();
	}

	public final String getKey() {
		return key;
	}

	public final boolean isEmpty() {
		return key == null || key.isEmpty();
	}

	@Override
	public final String getId() {
		return key;
	}

	@Override
	public final String toString() {
		return i18nd();
	}

	@NewInstance
	public final Text toText() {
		return Text.of(this);
	}

	@Virtual
	public String i18nd() {
		return I18N.get(getKey());
	}

	@Virtual
	@NewInstance
	public I18NText set(Object... o) {
		final String s = I18N.get(getKey(), o);
		return new I18NText(getKey()) {
			@Override
			public String i18nd() {
				return s;
			}

			@Override
			public I18NText set(Object... o) {
				throw new UnsupportedOperationException("set has already been executed.");
			}

		};
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 71 * hash + Objects.hashCode(this.i18nd());
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj.getClass() == String.class) {
			Objects.equals(this.i18nd(), obj.toString());
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final I18NText other = (I18NText) obj;
		return Objects.equals(this.i18nd(), other.i18nd());
	}

}
