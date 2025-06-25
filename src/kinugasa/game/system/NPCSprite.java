 /*
  * MIT License
  *
  * Copyright (c) 2025 しなちょ
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */


package kinugasa.game.system;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import kinugasa.game.field4.D2Idx;
import kinugasa.game.field4.FieldMap;
import kinugasa.game.field4.FourDirAnimation;
import kinugasa.game.field4.NPCMoveModel;
import kinugasa.game.field4.NPCMoveModelStorage;
import kinugasa.game.field4.Vehicle;
import kinugasa.game.field4.VehicleStorage;
import kinugasa.object.FourDirection;
import kinugasa.resource.text.XMLElement;
import kinugasa.resource.text.XMLFile;
import kinugasa.util.FrameTimeCounter;
import kinugasa.object.ID;
import kinugasa.util.TimeCounterState;

/**
 * NPCは特殊なアニメーションスプライトです。ステータスは持ちません。
 *
 * @vesion 1.0.0 - 2022/11/08_19:20:50<br>
 * @author Shinacho<br>
 */
public class NPCSprite extends PCSprite implements ID {

	//
	private String id;
	private D2Idx targetIdx = null;
	private D2Idx currentIdx = null;
	private FrameTimeCounter nextMoveFrameTime;
	private NPCMoveModel moveModel;
	private Vehicle vehicle;
	private FieldMap map;
	private String textId;
	private String touchEventFileName;
	//

	public NPCSprite(String name,
			D2Idx initialLocationOnMap,
			NPCMoveModel moveModel,
			Vehicle vehicle,
			FieldMap map,
			String textId,
			float x, float y, float w, float h,
			D2Idx initialIdx,
			FourDirAnimation a,
			FourDirection initialDir) {
		super(x, y, w, h, initialIdx, a, initialDir);
		this.id = name;
		this.moveModel = moveModel;
		this.vehicle = vehicle;
		this.map = map;
		this.textId = textId;
		this.currentIdx = initialIdx.clone();
		if (vehicle != null) {
			setSpeed(vehicle.getSpeed());
		}
		to(initialDir);
		animationUpdate = false;
	}

	public NPCSprite(String f) {
		super(f);
	}

	public void setTouchEvent(String touchEventFileName) {
		this.touchEventFileName = touchEventFileName;
	}

	public String getTouchEvent() {
		return touchEventFileName;
	}

	@Override
	public void readFromXML(String fileName) {
		super.readFromXML(fileName);
		//NPC独自項目のREAD
		XMLFile f = new XMLFile(fileName);
		XMLElement r = f.load().getFirst();
		this.id = r.getAttributes().get("id").asId();

		this.moveModel = NPCMoveModelStorage.getInstance()
				.get(r.getElement("moveModel").get(0).getAttributes().get("name").asId());

		this.vehicle = VehicleStorage.getInstance()
				.get(r.getElement("vehicle").get(0).getAttributes().get("name").asId());

		this.textId
				= r.getElement("text").get(0).getAttributes().get("id").asId();

		this.currentIdx = getInitialIdx().clone();

		f.dispose();;
	}

	public void setMap(FieldMap map) {
		this.map = map;
	}

	public NPCMoveModel getMoveModel() {
		return moveModel;
	}

	public void setMoveModel(NPCMoveModel moveModel) {
		this.moveModel = moveModel;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	@Override
	public D2Idx getCurrentIdx() {
		return currentIdx;
	}

	@Override
	public void setCurrentIdx(D2Idx currentIDXonMapData) {
		this.currentIdx = currentIDXonMapData;
	}

	@Override
	public D2Idx getTargetIdx() {
		return targetIdx;
	}

	@Override
	public void setTargetIdx(D2Idx targetIdx) {
		this.targetIdx = targetIdx;
		outerTarget = true;
		stage = 0;
		moveStop = false;
	}

	public FrameTimeCounter getNextMoveFrameTime() {
		return nextMoveFrameTime;
	}

	public boolean isMoveStop() {
		return moveStop;
	}

	public boolean isOuterTarget() {
		return outerTarget;
	}

	public void setMoveStop(boolean moveStop) {
		this.moveStop = moveStop;
	}

	@Override
	public String getId() {
		return id;
	}

	private int stage = 0;
	private int lx, ly;
	private boolean outerTarget = false;
	private boolean moveStop = false;

	@Override
	public NPCSprite update() {
		if (moveStop) {
			return this;
		}
		updateAnimation();
		switch (stage) {
			case 0 -> {
				if (outerTarget) {
					nextMoveFrameTime = new FrameTimeCounter(1);
					assert targetIdx != null : "NPC target is null " + getId();
					nextStage();
					break;
				}
				//初期化
				nextMoveFrameTime = new FrameTimeCounter(moveModel.nextMoveFrameTime(this, map));
				targetIdx = moveModel.getNextTargetIdx(this, map);
				nextStage();
			}
			case 1 -> {
				//移動フレーム判定
				if (nextMoveFrameTime.update() != TimeCounterState.ACTIVE) {
					return this;
				}
				nextStage();
			}
			case 2 -> {
				//移動実行
				if (getTargetIdx().equals(map.getCurrentIdx())) {
					stage = 0;
					return this;
				}
				if (map.getNpcStorage().get(getTargetIdx()) != null) {
					stage = 0;
					return this;
				}
				float speed = vehicle == null ? 1f : vehicle.getSpeed() / 2;
				Point2D.Float p = (Point2D.Float) getLocation().clone();
				if (getCurrentIdx().x > getTargetIdx().x) {
					p.x -= speed;
					to(FourDirection.WEST);
					lx--;
				} else if (getCurrentIdx().x < getTargetIdx().x) {
					p.x += speed;
					to(FourDirection.EAST);
					lx++;
				}
				if (getCurrentIdx().y > getTargetIdx().y) {
					p.y -= speed;
					to(FourDirection.NORTH);
					ly--;
				} else if (getCurrentIdx().y < getTargetIdx().y) {
					p.y += speed;
					to(FourDirection.SOUTH);
					ly++;
				}
				Point2D.Float prevLocation = (Point2D.Float) getLocation().clone();
				setLocation(p);
				D2Idx prev = currentIdx.clone();
				if (lx >= map.getChipW()) {
					lx = 0;
					currentIdx.x++;
				} else if (lx <= -map.getChipW()) {
					lx = 0;
					currentIdx.x--;
				}
				if (ly >= map.getChipH()) {
					ly = 0;
					currentIdx.y++;
				} else if (ly <= -map.getChipH()) {
					ly = 0;
					currentIdx.y--;
				}
				if (!prev.equals(currentIdx)) {
					if (map.getNpcStorage().get(currentIdx) != null && map.getNpcStorage().get(currentIdx) != this) {
						setLocation(prevLocation);
						return this;
					}
				}
				if (currentIdx.equals(targetIdx)) {
					nextStage();
				}
				//目的地に近づいたら再設定
				if (new Rectangle2D.Float(targetIdx.x * getWidth(), targetIdx.y * getHeight(), map.getChipW(), map.getChipH()).contains(getLocation())) {
					nextStage();
				}
			}
			case 3 -> {
				//NPCの位置更新
				lx = ly = 0;

				float nx = (map.getBaseLayer().getX() + getCurrentIdx().x * map.getChipW());
				float ny = map.getBaseLayer().getY() + getCurrentIdx().y * map.getChipH();
				setLocation(nx, ny);
				moveStop = outerTarget;
				nextStage();
			}
			case 4 -> {
			}
			default ->
				throw new AssertionError("undefined NPCs stage : " + this);
		}
		return this;
	}

	@Override
	public void setStage(int stage) {
		this.stage = stage;
	}

	private int prevStage;

	@Override
	public void notMove() {
		prevStage = stage;
		setStage(4);
	}

	@Override
	public void canMove() {
		stage = prevStage;
	}

	@Override
	void nextStage() {
		stage++;
		if (stage >= 4) {
			stage = 0;
		}
	}

	@Override
	public void move() {
		super.move();
	}

	@Override
	public boolean isPlayer() {
		return false;
	}

	public String getTextID() {
		return textId;
	}

	@Override
	public String toString() {
		return "NPCSprite{" + "id=" + id + ", targetIdx=" + targetIdx + ", currentIdx=" + currentIdx + ", nextMoveFrameTime=" + nextMoveFrameTime + ", moveModel=" + moveModel + ", vehicle=" + vehicle + ", map=" + map + ", textId=" + textId + ", stage=" + stage + ", lx=" + lx + ", ly=" + ly + ", outerTarget=" + outerTarget + ", moveStop=" + moveStop + ", prevStage=" + prevStage + '}';
	}

}
