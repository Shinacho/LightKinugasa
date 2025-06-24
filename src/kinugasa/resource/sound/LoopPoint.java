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

import kinugasa.game.Nullable;

/**
 *
 * @version 1.0.0 - 2013/01/13_18:47:52<br>
 * @vesion 2.0.0 - 2024/08/15_22:20:11<br>
 * @author Shinacho<br>
 */
public class LoopPoint {

	public static final class Builder {

		private FramePosition from;

		private Builder(FramePosition from) {
			this.from = from;
		}

		public LoopPoint to(FramePosition to) {
			return new LoopPoint(from, to);
		}

		@Override
		public String toString() {
			return "Builder{" + "from=" + from + '}';
		}

	}
	private final FramePosition from, to;

	private LoopPoint(FramePosition from, FramePosition to) {
		this.from = from;
		this.to = to;
	}

	public static Builder from(FramePosition from) {
		return new Builder(from);
	}
	
	public static LoopPoint nonUse(){
		return null;
	}

	public FramePosition getFrom() {
		return from;
	}

	public FramePosition getTo() {
		return to;
	}

	@Override
	public String toString() {
		return "LoopPoint{" + "from=" + from + ", to=" + to + '}';
	}

}
