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
package kinugasa.system.actor.npcMove;

import java.awt.geom.Point2D;
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.field4.D2Idx;
import kinugasa.field4.FieldMap;
import kinugasa.field4.FieldMapSystem;
import kinugasa.field4.LayeredTile;
import kinugasa.system.GameSystem;
import kinugasa.system.actor.Actor;
import kinugasa.system.actor.CharaSprite;
import kinugasa.system.actor.FieldMapNPCMoveModel;
import kinugasa.system.actor.NPC;
import kinugasa.object.KVector;
import kinugasa.util.FrameTimeCounter;
import kinugasa.util.Random;
import kinugasa.util.TimeCounterState;

/**
 * RoundNPCMove.<br>
 *
 * @vesion 1.0.0 - 2025/09/23_22:14:44<br>
 * @author Shinacho.<br>
 */
public class ProwlNPCMove extends FieldMapNPCMoveModel {

	private static final float WAIT_TIME_SPREAD = 0.75f;
	private final int waitTimeBase;
	private final D2Idx initial;
	private final int dist;
	private FrameTimeCounter waitTime;
	private D2Idx currentTgtIdx;
	private Point2D.Float currentTgtLocation;
	private int stage = 0;
	private boolean lockYoyaku = false;

	public ProwlNPCMove(int d, float waitTime, CharaSprite sp, FieldMap fm) {
		super(sp, fm);
		this.dist = d;
		this.initial = sp.getCurrentLocationOnMap().clone();
		this.waitTimeBase = (int) (60 * waitTime);
		setNext();
	}

	@Override
	public void setNext() {
		waitTime = new FrameTimeCounter(Random.spread(waitTimeBase, ProwlNPCMove.WAIT_TIME_SPREAD));
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
				for (Actor v : GameSystem.getInstance().getPcList()) {
					if (v.getSprite().getCurrentLocationOnMap().equals(tgt)) {
						setNext();
						return;
					}
				}
			}
			//NPC衝突判定
			{
				for (NPC v : fm.getNPCMap()) {
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
			Point2D.Float next = sprite.simulateMoveCenterLocation();
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
	public boolean isEnded() {
		return false;
	}

	@Override
	public String toString() {
		if (isLocked()) {
			return "PROWL(" + dist + ")_" + initial + " / " + "LOCKED";
		}
		return "PROWL(" + dist + ")_" + initial + " / " + waitTime.getCurrentWaitTime();
	}

}
