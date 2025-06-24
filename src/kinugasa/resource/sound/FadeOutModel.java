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

import java.util.Objects;
import kinugasa.object.CloneableObject;
import kinugasa.resource.Updateable;

/**
 * これが設定された瞬間から適用される、音量の調整スライダー機能です。 音量がになったとき、サウンドは停止されキューから削除されます。
 *
 * @vesion 1.0.0 - 2024/08/16_23:15:27<br>
 * @author Shinacho<br>
 */
public class FadeOutModel extends CloneableObject implements Updateable<FadeOutResult> {

	private float sub;
	private float current = 1f;
	private float initial = -1;
	private FadeOutResult lastUpdateResult = FadeOutResult.INPROGRESS;

	public FadeOutModel(int frame) {
		if (frame == 0) {
			throw new IllegalArgumentException("fade out frame is 0");
		}
		sub = 1f / frame;
	}

	void set(Sound s) {
		if (initial < 0) {
			initial = s.getMasterGain().value();
		}
		s.setMasterGain(new MasterGain(initial * current));
	}

	public float getCurrent() {
		return current;
	}

	public float getInitial() {
		return initial;
	}

	public FadeOutResult getLastUpdateResult() {
		return lastUpdateResult;
	}

	public float getSub() {
		return sub;
	}

	@Override
	public FadeOutResult update() {
		current -= sub;
		if (current <= 0) {
			current = 0;
			return lastUpdateResult = FadeOutResult.END;
		}
		return lastUpdateResult = FadeOutResult.INPROGRESS;
	}

	@Override
	public boolean isRunning() {
		return lastUpdateResult == FadeOutResult.INPROGRESS;
	}

	@Override
	public boolean isEnded() {
		return lastUpdateResult == FadeOutResult.END;
	}

	@Override
	public FadeOutModel clone() {
		return (FadeOutModel) super.clone();
	}

	@Override
	public String toString() {
		return "VolumeFadeOutModel{" + "sub=" + sub + ", current=" + current + ", initial=" + initial + ", lastUpdateResult=" + lastUpdateResult + '}';
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 83 * hash + Float.floatToIntBits(this.sub);
		hash = 83 * hash + Float.floatToIntBits(this.current);
		hash = 83 * hash + Float.floatToIntBits(this.initial);
		hash = 83 * hash + Objects.hashCode(this.lastUpdateResult);
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
		final FadeOutModel other = (FadeOutModel) obj;
		if (Float.floatToIntBits(this.sub) != Float.floatToIntBits(other.sub)) {
			return false;
		}
		if (Float.floatToIntBits(this.current) != Float.floatToIntBits(other.current)) {
			return false;
		}
		if (Float.floatToIntBits(this.initial) != Float.floatToIntBits(other.initial)) {
			return false;
		}
		return this.lastUpdateResult == other.lastUpdateResult;
	}

}
