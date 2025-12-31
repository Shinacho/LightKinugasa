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
package kinugasa.xshare;

/**
 * HashTag.<br>
 *
 * @vesion 1.0.0 - 2025/12/24_19:50:28<br>
 * @author Shinacho.<br>
 */
public class XHashTag {

	private String value;

	public XHashTag(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public int lengrh() {
		int i = 0;
		for (char c : value.toCharArray()) {
			i += XUtil.is半角(c) ? 1 : 2;
		}
		return i;
	}

	@Override
	public String toString() {
		return "#" + value;
	}

}
