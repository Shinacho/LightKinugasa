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

import kinugasa.game.annotation.LoopCall;
import kinugasa.game.annotation.NewInstance;
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.game.annotation.Nullable;
import kinugasa.game.field4.D2Idx;
import kinugasa.game.field4.FieldMap;
import kinugasa.object.KVector;
import kinugasa.util.FrameTimeCounter;

/**
 * FieldMapNPCMoveModel.<br>
 *
 * @vesion 1.0.0 - 2025/09/18_0:29:02<br>
 * @author Shinacho.<br>
 */
public abstract class FieldMapNPCMoveModel {

	protected final CharaSprite sprite;
	protected final FieldMap fm;

	public FieldMapNPCMoveModel(CharaSprite sprite, FieldMap fm) {
		this.sprite = sprite;
		this.fm = fm;
	}

	public abstract void setNext();

	@LoopCall
	public abstract void moveToTgt();

	public abstract void fmMove();

	@NotNewInstance
	public abstract FrameTimeCounter nextMoveTimeCounter();

	public abstract boolean isMoving();

	public abstract boolean isWaiting();

	@NewInstance
	public abstract D2Idx getInitialLocation();

	@Nullable
	public abstract D2Idx getCurrentTgt();

	public abstract boolean isLocked();

	public abstract void lockLocation();

	public abstract void unlockLocation();
	
	public abstract boolean isEnded();

}
