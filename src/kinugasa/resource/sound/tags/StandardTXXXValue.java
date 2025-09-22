/*
 * Copyright (C) 2025 Shinacho
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
package kinugasa.resource.sound.tags;

import kinugasa.game.annotation.NotNull;
import kinugasa.game.annotation.Nullable;
import kinugasa.resource.sound.FramePosition;
import kinugasa.resource.sound.LoopPoint;
import kinugasa.resource.sound.MasterGain;
import kinugasa.resource.sound.Sound;
import kinugasa.util.StringUtil;

/**
 * StandardTXXXValue.<br>
 *
 * @vesion 1.0.0 - 2025/08/24_14:04:47<br>
 * @author Shinacho.<br>
 */
public class StandardTXXXValue {

	private Sound.Type type = null;
	private MasterGain masterGain = new MasterGain(1f);
	private LoopPoint loop = LoopPoint.nonUse();

	public StandardTXXXValue(String value) {
		parse(value);
	}

	private void parse(String value) {
		if (value.trim().isEmpty()) {
			return;
		}
		String[] v = StringUtil.safeSplit(value, ",");

		//type
		this.type = Sound.Type.valueOf(v[0].trim());

		//masterGain
		if (v.length >= 2) {
			float val = Float.parseFloat(v[1].trim());
			this.masterGain = new MasterGain(val);
		}
		//loop
		if (v.length >= 4) {
			this.loop = LoopPoint.from(FramePosition.parse(v[2].trim())).to(FramePosition.parse(v[3].trim()));
		}
	}

	@Nullable
	public Sound.Type getType() {
		return type;
	}

	@NotNull
	public MasterGain getMasterGain() {
		return masterGain;
	}

	@NotNull
	public LoopPoint getLoop() {
		return loop;
	}

	@Override
	public String toString() {
		return "StandardTXXXValue{" + "type=" + type + ", masterGain=" + masterGain + ", loop=" + loop + '}';
	}

}
