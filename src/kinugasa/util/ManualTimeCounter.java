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


package kinugasa.util;

/**
 * 手動式のタイムカウンタの実装です。 値が0を下回ったとき、完了となります。
 *
 * @vesion 1.0.0 - 2022/11/12_13:50:47<br>
 * @author Shinacho<br>
 */
public final class ManualTimeCounter extends TimeCounter {

	private int currentWaitTime;
	private final int initialWaitTime;

	public ManualTimeCounter(int waitTime) {
		initialWaitTime = currentWaitTime = waitTime;
	}

	public void count() {
		currentWaitTime -= 1;
	}

	public int getCurrentWaitTime() {
		return currentWaitTime;
	}

	public int getInitialWaitTime() {
		return initialWaitTime;
	}

	@Override
	protected boolean isReaching() {
		return currentWaitTime <= 0;
	}

	@Override
	public ManualTimeCounter reset() {
		currentWaitTime = initialWaitTime;
		return this;
	}

	@Override
	public ManualTimeCounter next() {
		return (ManualTimeCounter) super.next();
	}

	@Override
	public ManualTimeCounter setLoopWhenEnd(boolean loopWhenEnd) {
		return (ManualTimeCounter) super.setLoopWhenEnd(loopWhenEnd);
	}

	@Override
	public ManualTimeCounter clone() {
		return (ManualTimeCounter) super.clone();
	}

	@Override
	public String toString() {
		return "ManualTimeCounter{" + "currentWaitTime=" + currentWaitTime + ", initialWaitTime=" + initialWaitTime + '}';
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 89 * hash + this.currentWaitTime;
		hash = 89 * hash + this.initialWaitTime;
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
		final ManualTimeCounter other = (ManualTimeCounter) obj;
		if (this.currentWaitTime != other.currentWaitTime) {
			return false;
		}
		return this.initialWaitTime == other.initialWaitTime;
	}

}
