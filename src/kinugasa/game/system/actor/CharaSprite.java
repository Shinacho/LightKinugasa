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
import kinugasa.game.field4.D2Idx;
import kinugasa.game.field4.FieldMap;
import kinugasa.game.field4.Vehicle;
import kinugasa.graphics.GraphicsContext;
import kinugasa.object.AnimationSprite;
import kinugasa.object.FourDirection;
import kinugasa.object.KVector;

/**
 * CharaSprite.<br>
 *
 * @vesion 1.0.0 - 2025/07/23_7:43:55<br>
 * @author Shinacho.<br>
 */
public class CharaSprite extends AnimationSprite {

	private final WalkAnimation walkAnimtion;
	private D2Idx currentLocationOnMap;
	private FieldMapNPCMoveModel moveModel;
	private Vehicle vehicle;

	public CharaSprite(WalkAnimation walkAnimtion) {
		super(walkAnimtion);
		this.walkAnimtion = walkAnimtion;
		super.setAutoImageUpdate(false);
		vehicle = Vehicle.WALK;
		super.update();
	}

	public void to(FourDirection d) {
		if (walkAnimtion.getCurrentDir() != d) {
			walkAnimtion.to(d);
			update();
		}
	}

	public D2Idx getCurrentLocationOnMap() {
		return currentLocationOnMap;
	}

	public CharaSprite setCurrentLocationOnMap(D2Idx currentLocationOnMap) {
		this.currentLocationOnMap = currentLocationOnMap;
		return this;
	}

	public void setMoveModel(FieldMapNPCMoveModel moveModel) {
		this.moveModel = moveModel;
	}

	public FieldMapNPCMoveModel getMoveModel() {
		return moveModel;
	}

	public WalkAnimation getWalkAnimtion() {
		return walkAnimtion;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	//キャラのグラフィックを動かすときだけ、updateを実行する
	@Override
	public AnimationSprite update() {
		return super.update();
	}

	@Override
	public void draw(GraphicsContext g) {
		super.draw(g);
	}

	public void fmMove(KVector v) {
		KVector prev = getVector();
		setVector(v);
		super.move();
		if (moveModel != null) {
			moveModel.fmMove();
		}
		setVector(prev);
	}

	public void setFollow(FieldMap fm, D2Idx tgtIdx) {
		this.moveModel = StandardFieldMapNPCMoveModel.follow(this, fm, tgtIdx);
	}

	@Override
	public String toString() {
		return "CharaSprite{" + "walkAnimtion=" + walkAnimtion + '}';
	}

}
