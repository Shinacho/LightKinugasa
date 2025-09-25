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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.game.field4.D2Idx;
import kinugasa.game.field4.FieldMap;
import kinugasa.game.field4.FieldMapSystem;
import kinugasa.game.system.GameSystem;
import kinugasa.game.system.actor.Actor;
import kinugasa.game.system.actor.CharaSprite;
import kinugasa.game.system.actor.FieldMapNPCMoveModel;
import kinugasa.game.system.actor.NPC;
import kinugasa.object.KVector;
import kinugasa.util.FrameTimeCounter;
import kinugasa.util.Random;
import kinugasa.util.TimeCounterState;

/**
 * TripNPCMove.<br>
 *
 * @vesion 1.0.0 - 2025/09/23_22:15:00<br>
 * @author Shinacho.<br>
 */
public class TripNPCMove extends FieldMapNPCMoveModel {

	private static final float WAIT_TIME_SPREAD = 0.75f;
	private final int waitTimeBaseOfMove;
	private FrameTimeCounter waitTimeOfMove;
	private final int waitTimeBaseOnTgt;
	private FrameTimeCounter waitRimeOnTgt;
	private final D2Idx initial;
	private final D2Idx tgtA;
	private final D2Idx tgtB;
	private D2Idx currentTgtIdx;
	private Point2D.Float currentTgtLocation;
	private int stage = 0;
	private boolean lockYoyaku = false;
	private int prevStage = 0;
	private boolean toTgtB = true;
	private List<D2Idx> route = new ArrayList<>();
	private int idx = 0;

	public TripNPCMove(CharaSprite sp, FieldMap fm, float wt1, float wt2, D2Idx tgt) {
		super(sp, fm);
		this.initial = sp.getCurrentLocationOnMap().clone();
		this.tgtA = sp.getCurrentLocationOnMap().clone();
		this.tgtB = tgt;
		this.waitTimeBaseOfMove = (int) (60 * wt1);
		this.waitTimeBaseOnTgt = (int) (60 * wt2);
		if (!RoutingUtil.canStep(fm, sp, initial) || !RoutingUtil.canStep(fm, sp, tgtB)) {
			throw new IllegalArgumentException(" NPCMove : initial or tgt is cant step : " + initial + " / " + tgt);
		}
		setNext();
	}

	@Deprecated
	@NotNewInstance
	public List<D2Idx> getRoute() {
		return route;
	}

	@Override
	public void setNext() {
		waitTimeOfMove = new FrameTimeCounter(Random.spread(waitTimeBaseOfMove, TripNPCMove.WAIT_TIME_SPREAD));
		waitRimeOnTgt = new FrameTimeCounter(Random.spread(waitTimeBaseOnTgt, TripNPCMove.WAIT_TIME_SPREAD));
		stage = 0;
		//tgtB or initial
		idx = 0;
		route.clear();
		if (toTgtB) {
			toTgtB = !toTgtB;
			route = RoutingUtil.getRoute(fm, sprite, tgtA, tgtB);
			//経路がない場合はstage4で待機してから再計算する
			if (route.isEmpty()) {
				stage = 4;
			}
		} else {
			toTgtB = !toTgtB;
			route = RoutingUtil.getRoute(fm, sprite, tgtB, tgtA);
			if (route.isEmpty()) {
				stage = 4;
			}
		}
	}

	@Override
	public void moveToTgt() {
		//LOCKED
		if (stage == -1) {
			return;
		}
		//移動ごとの待機
		if (stage == 0) {
			if (waitTimeOfMove.update() == TimeCounterState.ACTIVE) {
				waitTimeOfMove = new FrameTimeCounter(Random.spread(waitTimeBaseOfMove, TripNPCMove.WAIT_TIME_SPREAD));
				stage = 1;
			}
		}
		//移動目標設定と衝突判定
		if (stage == 1) {
			currentTgtIdx = route.get(idx);
			//PC衝突判定
			{
				if (FieldMapSystem.getInstance().getCamera().getPcLocation().equals(currentTgtIdx)) {
					stage = 0;
					return;
				}
				for (Actor v : GameSystem.getInstance().getPcList()) {
					if (v.getSprite().getCurrentLocationOnMap().equals(currentTgtIdx)) {
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
					if (v.getSprite().getCurrentLocationOnMap().equals(currentTgtIdx)) {
						stage = 0;
						return;
					}
					if (v.getSprite().getMoveModel().getCurrentTgt() != null && v.getSprite().getMoveModel().getCurrentTgt().equals(currentTgtIdx)) {
						stage = 0;
						return;
					}
				}
			}
			//移動可能なのでIDXを進める
			idx++;
			//目的地座標設定
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
			stage = 2;
		}
		//カレントに対して移動する。
		if (stage == 2) {
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
					prevStage = 3;
					stage = -1;
				} else {
					stage = 3;
				}
				return;
			}
			//通常移動
			sprite.move();
			return;
		}
		//移動終了判定
		if (stage == 3) {
			if (sprite.getCurrentLocationOnMap().equals(route.get(route.size() - 1))) {
				//ゴールに到着
				stage = 4;
				return;
			}
			//ゴールでないので次がある。
			stage = 0;
			return;
		}
		//ゴールで待機
		if (stage == 4) {
			if (waitRimeOnTgt.update() == TimeCounterState.ACTIVE) {
				//待機終了
				setNext();
				return;
			}
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
		if (stage == 0) {
			return waitTimeOfMove;
		}
		if (stage == 4) {
			return waitRimeOnTgt;
		}
		return null;
	}

	@Override
	public boolean isMoving() {
		return stage == 2;
	}

	@Override
	public boolean isWaiting() {
		return stage == 0 || stage == 4;
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
		return stage == 4;
	}

	@Override
	public String toString() {
		String v = nextMoveTimeCounter() == null ? "-" : nextMoveTimeCounter().getCurrentWaitTime() + "";
		return "TRIP(" + tgtA + " to " + tgtB + ") / " + v;
	}

}
