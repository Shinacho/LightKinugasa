 /*
  * MIT License
  *
  * Copyright (c) 2025 しなちょ
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
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
