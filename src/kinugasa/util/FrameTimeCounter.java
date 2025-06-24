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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 複数の、呼び出し回数ベースの待機時間を順番に評価するTimeCounterの実装です.
 * <br>
 * このクラスは、TimeCounterの基本の実装です。たとえば、STGにおける射撃間隔の制御などに使用します。<br>
 * <br>
 *
 * @version 1.0.0 - 2013/01/11_18:35:20<br>
 * @author Shinacho<br>
 */
public final class FrameTimeCounter extends TimeCounter {

	private final int[] waitTimes;
	private ArrayIndexModel indexModel;
	private final ArrayIndexModel initialIndexModel;
	private int currentWaitTime;

	public FrameTimeCounter(int... waitTime) {
		if (waitTime.length == 0) {
			throw new IllegalArgumentException("wait time is null");
		}
		this.waitTimes = waitTime;
		indexModel = new SimpleIndex();
		initialIndexModel = indexModel.clone();
		currentWaitTime = this.waitTimes[0];
	}

	public FrameTimeCounter(List<Integer> waitTime) {
		if (waitTime.isEmpty()) {
			throw new IllegalArgumentException("wait time is null");
		}
		this.waitTimes = new int[waitTime.size()];
		for (int i = 0; i < waitTime.size(); i++) {
			this.waitTimes[i] = waitTime.get(i);
		}
		indexModel = new SimpleIndex();
		initialIndexModel = indexModel.clone();
		currentWaitTime = this.waitTimes[0];
	}

	public FrameTimeCounter setIndexModel(ArrayIndexModel indexModel) {
		this.indexModel = indexModel;
		return this;
	}

	public ArrayIndexModel getIndexModel() {
		return indexModel;
	}

	@Override
	protected boolean isReaching() {
		return --currentWaitTime <= 0;
	}

	@Override
	public FrameTimeCounter reset() {
		currentWaitTime = waitTimes[0];
		indexModel = initialIndexModel.clone();
		return this;
	}

	@Override
	public FrameTimeCounter next() {
		currentWaitTime = waitTimes[indexModel.next(waitTimes.length)];
		return this;
	}

	@Override
	public TimeCounterState update() {
		if (isReaching()) {
			next();
			return lastState = TimeCounterState.ACTIVE;
		}
		return lastState = TimeCounterState.INACTIVE;
	}

	@Override
	public FrameTimeCounter setLoopWhenEnd(boolean loopWhenEnd) {
		return (FrameTimeCounter) super.setLoopWhenEnd(loopWhenEnd);
	}

	@Override
	public FrameTimeCounter clone() {
		FrameTimeCounter t = (FrameTimeCounter) super.clone();
		t.indexModel = this.indexModel.clone();
		return t;
	}

	@Override
	public String toString() {
		return "FrameTimeCounter{" + "waitTimes=" + Arrays.toString(waitTimes) + ", currentWaitTime=" + currentWaitTime + '}';
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 67 * hash + Arrays.hashCode(this.waitTimes);
		hash = 67 * hash + Objects.hashCode(this.indexModel);
		hash = 67 * hash + Objects.hashCode(this.initialIndexModel);
		hash = 67 * hash + this.currentWaitTime;
		return hash;
	}

	public int getCurrentWaitTime() {
		return currentWaitTime;
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
		final FrameTimeCounter other = (FrameTimeCounter) obj;
		if (this.currentWaitTime != other.currentWaitTime) {
			return false;
		}
		if (!Arrays.equals(this.waitTimes, other.waitTimes)) {
			return false;
		}
		if (!Objects.equals(this.indexModel, other.indexModel)) {
			return false;
		}
		return Objects.equals(this.initialIndexModel, other.initialIndexModel);
	}

}
