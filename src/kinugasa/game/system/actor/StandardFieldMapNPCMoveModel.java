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
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.game.field4.D2Idx;
import kinugasa.game.field4.FieldMap;
import kinugasa.game.field4.FieldMapSystem;
import kinugasa.game.field4.LayeredTile;
import kinugasa.game.system.GameSystem;
import kinugasa.object.EmptySprite;
import kinugasa.object.FourDirection;
import kinugasa.object.KVector;
import kinugasa.util.FrameTimeCounter;
import kinugasa.util.Random;
import kinugasa.util.TimeCounterState;

/**
 * StandardFieldMapNPCMoveModel.<br>
 *
 * @vesion 1.0.0 - 2025/09/18_0:29:23<br>
 * @author Shinacho.<br>
 */
public class StandardFieldMapNPCMoveModel {

	public static float DEBUG_MOVE_CHANCE = 0f;

	private StandardFieldMapNPCMoveModel() {
	}

	public static FieldMapNPCMoveModel locked(CharaSprite sp, FieldMap fm) {
		return new LockedNPCMove(sp, fm);
	}

	public static FieldMapNPCMoveModel round(int d, float wt, CharaSprite sp, FieldMap fm) {
		return new RoundNPCMove(d, wt, sp, fm);
	}

	public static FieldMapNPCMoveModel follow(CharaSprite me, FieldMap fm, D2Idx tgtIdx) {
		return new FollowNPCMove(me, fm, tgtIdx);
	}
}

class FollowNPCMove extends FieldMapNPCMoveModel {

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
		//セット＝移動開始
		if (stage == 0) {
			double currentDistance = sprite.getCenter().distance(tgtLocation);
			var next = sprite.simulateMoveCenterLocation();
			double nextDistance = next.distance(tgtLocation);
			if (currentDistance < nextDistance) {
				//移動終了
				sprite.setLocationByCenter(tgtLocation);
				sprite.setAutoImageUpdate(false);
				sprite.setSpeed(0f);
				sprite.setCurrentLocationOnMap(tgtIdx.clone());
				tgtIdx = null;
				stage = -1;//END
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

class LockedNPCMove extends FieldMapNPCMoveModel {

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

class RoundNPCMove extends FieldMapNPCMoveModel {

	private static final float WAIT_TIME_SPREAD = 0.75f;

	private final int waitTimeBase;
	private final D2Idx initial;
	private final int dist;
	private FrameTimeCounter waitTime;
	private D2Idx currentTgtIdx;
	private Point2D.Float currentTgtLocation;
	private int stage = 0;
	private boolean lockYoyaku = false;

	public RoundNPCMove(int d, float waitTime, CharaSprite sp, FieldMap fm) {
		super(sp, fm);
		this.dist = d;
		this.initial = sp.getCurrentLocationOnMap().clone();
		this.waitTimeBase = (int) (60 * waitTime);
		setNext();
	}

	@Override
	public void setNext() {
		waitTime = new FrameTimeCounter(Random.spread(waitTimeBase, WAIT_TIME_SPREAD));
		stage = 0;
	}

	@Override
	public void moveToTgt() {
		if (stage == -1) {
			return;
		}
		if (stage == 0 && waitTime.update() == TimeCounterState.INACTIVE) {
			return;
		}
		if (stage == 0) {
			//tgt
			D2Idx current = sprite.getCurrentLocationOnMap().clone();
			int x = current.x;
			int y = current.y;
			switch (Random.d6(1)) {
				case 1, 2 ->
					x -= 1;
				case 3, 4 ->
					x += 1;
				default -> {
				}
			}
			switch (Random.d6(1)) {
				case 1, 2 ->
					y -= 1;
				case 3, 4 ->
					y += 1;
				default -> {
				}
			}
			D2Idx tgt = new D2Idx(x, y);

			//距離判定
			{
				D2Idx distance = initial.distance(tgt);
				if (Math.abs(distance.x) > dist) {
					setNext();
					return;
				}
				if (Math.abs(distance.y) > dist) {
					setNext();
					return;
				}
			}

			//PC衝突判定
			{
				if (FieldMapSystem.getInstance().getCamera().getPcLocation().equals(tgt)) {
					setNext();
					return;
				}
				for (var v : GameSystem.getInstance().getPcList()) {
					if (v.getSprite().getCurrentLocationOnMap().equals(tgt)) {
						setNext();
						return;
					}
				}
			}

			//NPC衝突判定
			{
				for (var v : fm.getNPCMap()) {
					if (v.getSprite().equals(sprite)) {
						continue;
					}
					if (v.getSprite().getCurrentLocationOnMap().equals(tgt)) {
						setNext();
						return;
					}
					if (v.getSprite().getMoveModel().getCurrentTgt() != null && v.getSprite().getMoveModel().getCurrentTgt().equals(tgt)) {
						setNext();
						return;
					}
				}
			}
			//乗れるチップ判定
			{
				LayeredTile tile = fm.getTile(tgt);
				if (!sprite.getVehicle().canStep(tile)) {
					setNext();
					return;
				}
			}
			//同一座標判定
			{
				if (tgt.equals(sprite.getCurrentLocationOnMap())) {
					setNext();
					return;
				}
			}
			//commit
			this.currentTgtIdx = tgt;

			this.currentTgtLocation = sprite.getCenter();
			if (currentTgtIdx.x < sprite.getCurrentLocationOnMap().x) {
				currentTgtLocation.x -= fm.getLayer0ChipSize();
			} else if (currentTgtIdx.x > sprite.getCurrentLocationOnMap().x) {
				currentTgtLocation.x += fm.getLayer0ChipSize();
			}
			if (currentTgtIdx.y < sprite.getCurrentLocationOnMap().y) {
				currentTgtLocation.y -= fm.getLayer0ChipSize();
			} else if (currentTgtIdx.y > sprite.getCurrentLocationOnMap().y) {
				currentTgtLocation.y += fm.getLayer0ChipSize();
			}

			KVector kv = new KVector(sprite.getCenter(), currentTgtLocation);
			kv.setSpeed(sprite.getVehicle().getSpeed());
			sprite.setVector(kv);
			sprite.setAutoImageUpdate(true);
			sprite.to(kv.round());
			stage = 1;
		}
		if (stage == 1) {
			//move
			double currentDistance = sprite.getCenter().distance(currentTgtLocation);
			var next = sprite.simulateMoveCenterLocation();
			double nextDistance = next.distance(currentTgtLocation);
			if (currentDistance < nextDistance) {
				//移動終了
				sprite.setLocationByCenter(currentTgtLocation);
				sprite.setAutoImageUpdate(false);
				sprite.setSpeed(0f);
				sprite.setCurrentLocationOnMap(currentTgtIdx.clone());
				currentTgtIdx = null;
				if (lockYoyaku) {
					stage = -1;
				} else {
					setNext();
				}
				return;
			}
			//通常移動
			sprite.move();
		}

	}

	@Override
	public void fmMove() {
		if (currentTgtIdx == null) {
			return;
		}
		if (isLocked()) {
			return;
		}

		Point2D.Float p = fm.getLayer0Location();
		p.x += (currentTgtIdx.x * fm.getLayer0ChipSize());
		p.x += fm.getLayer0ChipSize() / 2;
		p.y += (currentTgtIdx.y * fm.getLayer0ChipSize());
		p.y += fm.getLayer0ChipSize() / 2;

		currentTgtLocation = p;
	}

	@NotNewInstance
	@Override
	public FrameTimeCounter nextMoveTimeCounter() {
		return waitTime;
	}

	@Override
	public boolean isMoving() {
		return stage == 1;
	}

	@Override
	public boolean isWaiting() {
		return stage == 0;
	}

	@Override
	public D2Idx getInitialLocation() {
		return initial;
	}

	@Override
	public D2Idx getCurrentTgt() {
		return currentTgtIdx;
	}

	@Override
	public boolean isLocked() {
		return stage == -1;
	}

	@Override
	public void lockLocation() {
		if (isMoving()) {
			lockYoyaku = true;
			return;
		}
		stage = -1;
	}

	@Override
	public void unlockLocation() {
		lockYoyaku = false;
		if (isLocked()) {
			stage = 0;
		}
	}

	@Override
	public String toString() {
		if (isLocked()) {
			return "ROUND(" + dist + ")_" + initial + " / " + "LOCKED";
		}
		return "ROUND(" + dist + ")_" + initial + " / " + waitTime.getCurrentWaitTime();
	}

}
