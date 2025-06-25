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

import kinugasa.game.RequiresReturnTypeChange;
import kinugasa.game.Virtual;
import kinugasa.object.CloneableObject;
import kinugasa.resource.Updateable;
import kinugasa.game.NeedSuperCallWhenOverride;

/**
 * 経過時間を計測し、条件判定をするための機能を定義します.
 * <br>
 *
 * @version 1.0.0 - 2013/01/11_17:07:17.<br>
 * @author Shinacho.<br>
 */
public abstract class TimeCounter extends CloneableObject implements Updateable<TimeCounterState> {

	protected boolean loopWhenEnd;
	protected TimeCounterState lastState;

	protected TimeCounter() {
		this(false);
	}

	protected TimeCounter(boolean loopWhenState) {
		this.loopWhenEnd = loopWhenState;
	}

	@Virtual
	@RequiresReturnTypeChange
	@NeedSuperCallWhenOverride
	public TimeCounter setLoopWhenEnd(boolean loopWhenEnd) {
		this.loopWhenEnd = loopWhenEnd;
		return this;
	}

	public final boolean isLoopWhenState() {
		return loopWhenEnd;
	}

	@Override
	@Virtual
	public TimeCounterState update() {
		if (isReaching()) {
			if (loopWhenEnd) {
				next();
			}
			return lastState = TimeCounterState.ACTIVE;
		}
		return lastState = TimeCounterState.INACTIVE;
	}

	protected abstract boolean isReaching();

	@Virtual
	@RequiresReturnTypeChange
	public TimeCounter next() {
		reset();
		return this;
	}

	@RequiresReturnTypeChange
	public abstract TimeCounter reset();

	@Override
	public final boolean isRunning() {
		if (lastState == null) {
			return false;
		}
		return lastState == TimeCounterState.ACTIVE;
	}

	@Override
	public final boolean isEnded() {
		if (lastState == null) {
			return false;
		}
		return lastState == TimeCounterState.INACTIVE;
	}

	@Override
	@Virtual
	@RequiresReturnTypeChange
	public TimeCounter clone() {
		return (TimeCounter) super.clone();
	}

	//-------------------------static-------------------------------
	public static TimeCounter of(final TimeCounterState... state) {
		return new TimeCounter() {

			private TimeCounterState[] v = state;
			private int idx = 0;

			@Override
			protected boolean isReaching() {
				return true;
			}

			@Override
			public TimeCounter reset() {
				idx = 0;
				return this;
			}

			@Override
			public TimeCounterState update() {
				if (idx >= v.length) {
					return TimeCounterState.INACTIVE;
				}
				return v[idx++];
			}

		};
	}

	public static TimeCounter always(final TimeCounterState r) {
		return new TimeCounter() {

			@Override
			protected boolean isReaching() {
				return r == TimeCounterState.ACTIVE;
			}

			@Override
			public TimeCounter reset() {
				//処理なし
				return this;
			}

		};
	}

	public static TimeCounter oneTime(final TimeCounterState r) {
		return new TimeCounter() {
			private boolean call = false;

			@Override
			protected boolean isReaching() {
				if (!call) {
					call = true;
					return r == TimeCounterState.ACTIVE;
				}
				return r != TimeCounterState.ACTIVE;
			}

			@Override
			public TimeCounter reset() {
				//処理なし
				return this;
			}

		};
	}

}
