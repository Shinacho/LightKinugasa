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
package kinugasa.game.system.actor;

import java.awt.geom.Point2D;
import java.io.File;
import kinugasa.game.event.ScriptFile;
import kinugasa.game.field4.D2Idx;
import kinugasa.game.field4.FieldMap;
import kinugasa.game.system.UniversalValue;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.text.DataFile;
import kinugasa.resource.text.FileFormatException;

/**
 * Follower.<br>
 *
 * @vesion 1.0.0 - 2025/09/20_16:02:26<br>
 * @author Shinacho.<br>
 */
public class Follower extends Actor {

	private D2Idx currentTgt;

	public Follower(File f) {
		super(f);
	}

	@Override
	public Follower load() throws FileNotFoundException, FileFormatException, ContentsIOException {
		if (isLoaded()) {
			return this;
		}
		super.load();
		return this;
	}

	public void setFollowTgt(FieldMap fm, D2Idx tgtIdx) {
		if (currentTgt == null || !tgtIdx.equals(currentTgt)) {
			currentTgt = tgtIdx;
			getSprite().setMoveModel(StandardFieldMapNPCMoveModel.follow(getSprite(), fm, tgtIdx));
		}
	}
}
