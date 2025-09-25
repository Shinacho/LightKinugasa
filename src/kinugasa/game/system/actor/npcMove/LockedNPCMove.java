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
package kinugasa.game.system.actor.npcMove;

import kinugasa.game.field4.D2Idx;
import kinugasa.game.field4.FieldMap;
import kinugasa.game.system.actor.CharaSprite;
import kinugasa.game.system.actor.FieldMapNPCMoveModel;
import kinugasa.util.FrameTimeCounter;

/**
 * LockedNPCMove.<br>
 *
 * @vesion 1.0.0 - 2025/09/23_22:14:33<br>
 * @author Shinacho.<br>
 */
public class LockedNPCMove extends FieldMapNPCMoveModel {

	private final D2Idx initlal;

	public LockedNPCMove(CharaSprite sp, FieldMap fm) {
		super(sp, fm);
		this.initlal = sp.getCurrentLocationOnMap();
	}

	@Override
	public void setNext() {
	}

	@Override
	public void moveToTgt() {
	}

	@Override
	public void fmMove() {
	}

	@Override
	public boolean isEnded() {
		return true;
	}

	@Override
	public FrameTimeCounter nextMoveTimeCounter() {
		return null;
	}

	@Override
	public boolean isMoving() {
		return false;
	}

	@Override
	public boolean isWaiting() {
		return false;
	}

	@Override
	public D2Idx getInitialLocation() {
		return initlal;
	}

	@Override
	public D2Idx getCurrentTgt() {
		return initlal;
	}

	@Override
	public boolean isLocked() {
		return true;
	}

	@Override
	public void lockLocation() {
	}

	@Override
	public void unlockLocation() {
	}

	@Override
	public String toString() {
		return "LOCKED(" + initlal + ")";
	}

}
