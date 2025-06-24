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
