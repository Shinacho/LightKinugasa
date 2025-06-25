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
