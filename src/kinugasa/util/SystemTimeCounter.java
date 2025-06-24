/*
 * Copyright (C) 2023 Shinacho
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
