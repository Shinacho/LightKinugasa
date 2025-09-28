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
package kinugasa.field4;

import kinugasa.game.I18NText;
import kinugasa.game.annotation.Immutable;
import kinugasa.game.annotation.Nullable;
import kinugasa.object.FourDirection;
import kinugasa.resource.sound.Sound;

/**
 * FieldMapNodeはマップ間移動するときの情報を持っているクラスです.<br>
 *
 * @vesion 1.0.0 - 2025/08/11_8:45:57<br>
 * @author Shinacho.<br>
 */
@Immutable
public class FieldMapNode {

	private String nextFieldMapID;
	private D2Idx nextFieldMapIdx;
	private FourDirection nextFieldMapDir;
	@Nullable
	private Sound sound;
	private MapChangeEffect effect;
	private I18NText text;

	public FieldMapNode(String nextFieldMapID, D2Idx nextFieldMapIdx, FourDirection nextFieldMapDir, Sound sound, MapChangeEffect effect, I18NText text) {
		this.nextFieldMapID = nextFieldMapID;
		this.nextFieldMapIdx = nextFieldMapIdx;
		this.nextFieldMapDir = nextFieldMapDir;
		this.sound = sound;
		this.effect = effect;
		this.text= text;
	}

	public String getNextFieldMapID() {
		return nextFieldMapID;
	}

	public D2Idx getNextFieldMapIdx() {
		return nextFieldMapIdx;
	}

	public FourDirection getNextFieldMapDir() {
		return nextFieldMapDir;
	}

	public Sound getSound() {
		return sound;
	}

	public MapChangeEffect getEffect() {
		return effect;
	}

	public I18NText getText() {
		return text;
	}

	@Override
	public String toString() {
		return "FieldMapNode{" + "nextFieldMapID=" + nextFieldMapID + ", nextFieldMapIdx=" + nextFieldMapIdx + ", nextFieldMapDir=" + nextFieldMapDir + ", sound=" + sound + ", effect=" + effect + '}';
	}

}
