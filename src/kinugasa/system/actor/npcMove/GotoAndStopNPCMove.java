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
import java.util.List;
import kinugasa.field4.D2Idx;
import kinugasa.field4.FieldMap;
import kinugasa.field4.FieldMapSystem;
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
 * GotoAndStopNPCMove.<br>
 *
 * @vesion 1.0.0 - 2025/09/24_0:02:02<br>
 * @author Shinacho.<br>
 */
public class GotoAndStopNPCMove extends FieldMapNPCMoveModel {

	private static final float WAIT_TIME_SPREAD = 0.75f;
	private final int waitTimeBaseOfMove;
	private FrameTimeCounter waitTimeOfMove;
	private final D2Idx tgt;
	private final D2Idx initial;
	private D2Idx nextIdx;
	private Point2D.Float nextLocation;
	private int stage;
	private boolean lockYoyaku = false;
	private int prevStage = 0;
	private List<D2Idx> route;
	private int idx;

	public GotoAndStopNPCMove(CharaSprite sp, FieldMap fm, float wt1, D2Idx tgt) {
		super(sp, fm);
		this.waitTimeBaseOfMove = (int) (60 * wt1);
		this.initial = sp.getCurrentLocationOnMap();
		this.tgt = tgt;
		setNext();
	}

	@Override
	public void setNext() {
		waitTimeOfMove = new FrameTimeCounter(Random.spread(waitTimeBaseOfMove, WAIT_TIME_SPREAD));
		stage = 0;
		idx = 0;
		route = RoutingUtil.getRoute(fm, sprite, initial, tgt);
		if (route.isEmpty()) {
			stage = 3;
		}
		nextIdx = route.get(0);
	}

	@Override
	public void moveToTgt() {

		//LOCKED
		if (stage == -1) {
			return;
		}

		//初期化されたときにすでにルートはできている。
		//移動待機
		if (stage == 0 && waitTimeOfMove.update() == TimeCounterState.INACTIVE) {
			return;
		}

		//移動開始
		if (stage == 0) {
			//next設定
			nextIdx = route.get(idx);
			//PC衝突判定
			{
				if (FieldMapSystem.getInstance().getCamera().getPcLocation().equals(nextIdx)) {
					stage = 0;
					return;
				}
				for (Actor v : GameSystem.getInstance().getPcList()) {
					if (v.getSprite().getCurrentLocationOnMap().equals(nextIdx)) {
						stage = 0;
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
					if (v.getSprite().getCurrentLocationOnMap().equals(nextIdx)) {
						stage = 0;
						return;
					}
					if (v.getSprite().getMoveModel().getCurrentTgt() != null && v.getSprite().getMoveModel().getCurrentTgt().equals(nextIdx)) {
						stage = 0;
						return;
					}
				}
			}

			//移動可能
			idx++;
			//目的地座標設定
			nextLocation = fm.getLayer0().locationOf(tgt).getCenter();

			KVector kv = new KVector(sprite.getCenter(), nextLocation);
			kv.setSpeed(sprite.getVehicle().getSpeed());
			sprite.setVector(kv);
			sprite.setAutoImageUpdate(true);
			sprite.to(kv.round());
			stage = 1;
			return;
		}

		//移動実行と現在の目的位置まで行ったか判定
		if (stage == 1) {
			//move
			double currentDistance = sprite.getCenter().distance(nextLocation);
			Point2D.Float next = sprite.simulateMoveCenterLocation();
			double nextDistance = next.distance(nextLocation);
			if (currentDistance < nextDistance) {
				//移動終了
				sprite.setLocationByCenter(nextLocation);
				sprite.setAutoImageUpdate(false);
				sprite.setSpeed(0f);
				sprite.setCurrentLocationOnMap(nextIdx.clone());
				nextIdx = null;
				if (lockYoyaku) {
					prevStage = 1;
					stage = -1;
				} else {
					stage = 2;
				}
				return;
			}
			//通常移動
			sprite.move();
			return;
		}

		//最後の位置に到着した場合はロックに入る
		if (stage == 2) {
			if (sprite.getCurrentLocationOnMap().equals(route.get(route.size() - 1))) {
				//ゴールに到着
				stage = 3;
				return;
			}
			waitTimeOfMove = new FrameTimeCounter(Random.spread(waitTimeBaseOfMove, WAIT_TIME_SPREAD));
			stage = 0;
			return;
		}

		//stage 3
	}

	@Override
	public void fmMove() {
		if (nextIdx == null) {
			return;
		}
		if (isLocked()) {
			return;
		}
		Point2D.Float p = fm.getLayer0Location();
		p.x += (nextIdx.x * fm.getLayer0ChipSize());
		p.x += fm.getLayer0ChipSize() / 2;
		p.y += (nextIdx.y * fm.getLayer0ChipSize());
		p.y += fm.getLayer0ChipSize() / 2;
		nextLocation = p;
	}

	@Override
	public FrameTimeCounter nextMoveTimeCounter() {
		return stage == 0 ? waitTimeOfMove : null;
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
		return nextIdx;
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
		prevStage = stage;
		stage = -1;
	}

	@Override
	public void unlockLocation() {
		if (isLocked()) {
			stage = prevStage;
			lockYoyaku = false;
		}
	}

	@Override
	public boolean isEnded() {
		return stage == 3;
	}

	@Override
	public String toString() {
		return "GOTO_AND_STOP_" + route.get(route.size() - 1) + " / " + this.nextMoveTimeCounter();
	}

}
