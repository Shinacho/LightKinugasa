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
package kinugasa.resource.sound;

import kinugasa.util.StringUtil;

public final class FramePosition {

	public static FramePosition frameOf(int i) {
		return new FramePosition(i);
	}

	public static FramePosition secondOf(float sec, int hz) {
		return new FramePosition((int) (sec * hz));
	}

	public static FramePosition eof() {
		return new FramePosition(-1);
	}

	public static FramePosition start() {
		return new FramePosition(0);
	}

	public static FramePosition parse(String v) {
		if ("EOF".equals(v.toUpperCase())) {
			return eof();
		}
		if ("START".equals(v.toUpperCase())) {
			return start();
		}
		if (StringUtil.isDigit(v)) {
			return frameOf(Integer.parseInt(v));
		}
		throw new IllegalArgumentException(v + " is not frame position");
	}
	public final int VALUE;

	private FramePosition(int val) {
		this.VALUE = val;
	}

	@Override
	public String toString() {
		return VALUE + "";
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + this.VALUE;
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		final FramePosition other = (FramePosition) obj;
		return this.VALUE == other.VALUE;
	}

}
