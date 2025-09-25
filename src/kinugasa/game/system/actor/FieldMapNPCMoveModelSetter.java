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

import kinugasa.game.annotation.Singleton;
import kinugasa.game.field4.D2Idx;
import kinugasa.game.system.actor.npcMove.FollowNPCMove;
import kinugasa.game.system.actor.npcMove.GotoAndStopNPCMove;
import kinugasa.game.system.actor.npcMove.LockedNPCMove;
import kinugasa.game.system.actor.npcMove.ProwlNPCMove;
import kinugasa.game.system.actor.npcMove.TripNPCMove;

/**
 * StandardFieldMapNPCMoveModel.<br>
 *
 * @vesion 1.0.0 - 2025/09/18_0:29:23<br>
 * @author Shinacho.<br>
 */
@Singleton

public class FieldMapNPCMoveModelSetter {

	private Actor tgt;

	FieldMapNPCMoveModelSetter(Actor n) {
		this.tgt = n;
	}

	//移動しない
	public void locked() {
		var m = new LockedNPCMove(tgt.getSprite(), tgt.getFieldMap());
		tgt.getSprite().setMoveModel(m);
	}

	//初期座標からdの距離までうろうろする
	public void prowl(int d, float wt) {
		var m = new ProwlNPCMove(d, wt, tgt.getSprite(), tgt.getFieldMap());
		tgt.getSprite().setMoveModel(m);
	}

	//隣接するはずのtgtIdxに向かって進み、そこで止まる。
	public void follow(D2Idx tgtIdx) {
		var m = new FollowNPCMove(tgt.getSprite(), tgt.getFieldMap(), tgtIdx);
		tgt.getSprite().setMoveModel(m);
	}

	//initialとtgtを行ったり来たりする。
	public void trip(float wt1, float wt2, D2Idx tgt) {
		var m = new TripNPCMove(this.tgt.getSprite(), this.tgt.getFieldMap(), wt1, wt2, tgt);
		this.tgt.getSprite().setMoveModel(m);
	}

	//tgtまで行って待機し、次のターゲットに行く。最後まで行ったら最初に戻る。
	public void patrol(float wt1, float wt2, D2Idx... tgt) {

	}

	public void randomTgt(float wt1, float wt2) {

	}

	public void gotoAndStop(float wt1, D2Idx tgt) {
		var m = new GotoAndStopNPCMove(this.tgt.getSprite(), this.tgt.getFieldMap(), wt1, tgt);
		this.tgt.getSprite().setMoveModel(m);
	}

}
