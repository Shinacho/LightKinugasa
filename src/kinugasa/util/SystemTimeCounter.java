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

import java.time.Duration;

/**
 * システム時計の経過時間によって待機時間を評価するTimeCounterの実装です.
 * <br>
 * 全ての時間はミリ秒単位です。<br>
 * <br>
 *
 * @version 1.0.0 - 2013/01/12_14:46:33<br>
 * @author Shinacho<br>
 */
public final class SystemTimeCounter extends TimeCounter {

	private long startTime, waitTime, endTime;

	public SystemTimeCounter(long waitTimeMs) {
		if (waitTimeMs < 0) {
			throw new IllegalArgumentException("sysTC : wait time is minus");
		}
		this.startTime = System.currentTimeMillis();
		this.waitTime = waitTimeMs;
		this.endTime = startTime + waitTime;
	}

	public SystemTimeCounter(Duration t) {
		this(t.toMillis());
	}

	public long getEndTime() {
		return endTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getWaitTime() {
		return waitTime;
	}

	@Override
	protected boolean isReaching() {
		return System.currentTimeMillis() >= endTime;
	}

	@Override
	public SystemTimeCounter reset() {
		return this;
	}

	@Override
	public SystemTimeCounter next() {
		this.startTime = System.currentTimeMillis();
		this.endTime = startTime + waitTime;
		return this;
	}

	@Override
	public SystemTimeCounter setLoopWhenEnd(boolean loopWhenEnd) {
		return (SystemTimeCounter) super.setLoopWhenEnd(loopWhenEnd);
	}

	@Override
	public SystemTimeCounter clone() {
		return (SystemTimeCounter) super.clone();
	}

	@Override
	public String toString() {
		return "SystemTimeCounter{" + "startTime=" + startTime + ", waitTime=" + waitTime + ", endTime=" + endTime + '}';
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 19 * hash + (int) (this.startTime ^ (this.startTime >>> 32));
		hash = 19 * hash + (int) (this.waitTime ^ (this.waitTime >>> 32));
		hash = 19 * hash + (int) (this.endTime ^ (this.endTime >>> 32));
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
		final SystemTimeCounter other = (SystemTimeCounter) obj;
		if (this.startTime != other.startTime) {
			return false;
		}
		if (this.waitTime != other.waitTime) {
			return false;
		}
		return this.endTime == other.endTime;
	}

}
