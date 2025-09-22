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
package kinugasa.resource;

/**
 *
 * @vesion 1.0.0 - 2025/02/08_12:25:51<br>
 * @author Shinacho<br>
 */
public interface IDEnumInjector<T extends Enum<T> & IDEnumInjector<T>> extends ID {

	@Override
	public default String getId() {
		return ((Enum<T>) this).name();
	}

}
