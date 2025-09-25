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

import java.awt.geom.Point2D;
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.game.field4.D2Idx;
import kinugasa.game.field4.FieldMap;
import kinugasa.game.system.actor.CharaSprite;
import kinugasa.game.system.actor.FieldMapNPCMoveModel;
import kinugasa.object.FourDirection;
import kinugasa.object.KVector;
import kinugasa.util.FrameTimeCounter;

/**
 * FollowNPCMove.<br>
 *
 * @vesion 1.0.0 - 2025/09/23_22:14:22<br>
 * @author Shinacho.<br>
 */
public class FollowNPCMove extends FieldMapNPCMoveModel {

	private D2Idx tgtIdx;
	private Point2D.Float tgtLocation;
	private int stage = 0;

	public FollowNPCMove(CharaSprite me, FieldMap fm, D2Idx tgtI) {
		super(me, fm);
		this.tgtIdx = tgtI;
		Point2D.Float p = fm.getLayer0Location();
		p.x += (tgtIdx.x * fm.getLayer0ChipSize());
		p.x += fm.getLayer0ChipSize() / 2;
		p.y += (tgtIdx.y * fm.getLayer0ChipSize());
		p.y += fm.getLayer0ChipSize() / 2;
		tgtLocation = p;
		KVector kv = new KVector(sprite.getCenter(), tgtLocation);
		kv.setSpeed(sprite.getVehicle().getSpeed());
		sprite.setVector(kv);
		sprite.setAutoImageUpdate(true);
	}

	@Override
	public void setNext() {
	}

	@Override
	public void moveToTgt() {
		if (tgtIdx == null) {
			return;
		}
		if (tgtIdx.equals(sprite.getCurrentLocationOnMap())) {
			stage = -1;
			return;
		}
		//セット＝移動開始
		if (stage == 0) {
			double currentDistance = sprite.getCenter().distance(tgtLocation);
			Point2D.Float next = sprite.simulateMoveCenterLocation();
			double nextDistance = next.distance(tgtLocation);
			if (currentDistance < nextDistance) {
				//移動終了
				sprite.setLocationByCenter(tgtLocation);
				sprite.setAutoImageUpdate(false);
				sprite.setSpeed(0f);
				sprite.setCurrentLocationOnMap(tgtIdx.clone());
				tgtIdx = null;
				stage = -1; //END
				return;
			}
			//通常移動
			Point2D.Float prev = sprite.getCenter();
			sprite.move();
			FourDirection d = new KVector(prev, sprite.getCenter()).round();
			sprite.to(d);
		}
	}

	@Override
	public boolean isEnded() {
		return stage == -1;
	}

	@Override
	public void fmMove() {
		if (tgtIdx == null) {
			return;
		}
		Point2D.Float p = fm.getLayer0Location();
		p.x += (tgtIdx.x * fm.getLayer0ChipSize());
		p.x += fm.getLayer0ChipSize() / 2;
		p.y += (tgtIdx.y * fm.getLayer0ChipSize());
		p.y += fm.getLayer0ChipSize() / 2;
		tgtLocation = p;
	}

	@NotNewInstance
	@Override
	public FrameTimeCounter nextMoveTimeCounter() {
		return null;
	}

	@Override
	public boolean isMoving() {
		return stage == 0;
	}

	@Override
	public boolean isWaiting() {
		return stage == -1;
	}

	@Override
	public D2Idx getInitialLocation() {
		return null;
	}

	@Override
	public D2Idx getCurrentTgt() {
		return tgtIdx;
	}

	@Override
	public boolean isLocked() {
		return false;
	}

	@Override
	public void lockLocation() {
	}

	@Override
	public void unlockLocation() {
	}

	@Override
	public String toString() {
		return "FOLLOW(" + tgtIdx + ")";
	}

}
