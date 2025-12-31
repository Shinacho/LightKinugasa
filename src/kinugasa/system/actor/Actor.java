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
package kinugasa.system.actor;

import kinugasa.system.actor.status.Status;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kinugasa.game.I18NText;
import kinugasa.game.VisibleNameSupport;
import kinugasa.game.annotation.Nullable;
import kinugasa.game.annotation.RequiresReturnTypeChange;
import kinugasa.game.annotation.Virtual;
import kinugasa.field4.D2Idx;
import kinugasa.field4.FieldMap;
import kinugasa.field4.FieldMapSystem;
import kinugasa.game.annotation.LoopCall;
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.system.UniversalValue;
import kinugasa.system.item.Item;
import kinugasa.graphics.Animation;
import kinugasa.graphics.KImage;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.text.DataFile;
import kinugasa.resource.text.FileFormatException;
import kinugasa.script.ScriptFile;
import kinugasa.system.actor.status.StatusEffect;
import kinugasa.system.actor.status.cnd.Condition;
import kinugasa.system.actor.status.StatusKey;
import static kinugasa.system.actor.status.StatusKey.Type.CHANCE;
import kinugasa.system.actor.status.StatusValueChance;
import kinugasa.system.actor.status.StatusValueInt;
import kinugasa.system.actor.status.StatusValueMul;
import kinugasa.system.actor.status.attr.AttributeKey;
import kinugasa.system.item.EqipItemSet;
import kinugasa.system.item.EqipSlot;
import kinugasa.util.FrameTimeCounter;
import kinugasa.ui.MWSpeaker;

/**
 * Actor.<br>
 *
 * @vesion 1.0.0 - 2025/08/13_2:57:31<br>
 * @author Shinacho.<br>
 */
public class Actor extends ScriptFile implements MWSpeaker, VisibleNameSupport {

	//------------------------------------------------
	public Actor(File f) {
		this(f, FieldMapSystem.getInstance().getCurrent(), FieldMapSystem.getInstance().getCamera().getPcLocation());
	}

	public Actor(File f, FieldMap fm, D2Idx initial) {
		super(f);
		this.fieldMap = fm;
		this.initialIdx = initial;
	}

	//------------------------------------------------
	private Map<String, UniversalValue> globalValue = new HashMap<>();
	private D2Idx initialIdx;
	//
	private Status status;
	private CharaSprite fieldSprite;
	//
	private Race race;
	private EqipItemSet eqip;
//	private PersonalBag<Item> itemBag;
	//
	private KImage speakerImage;
	private boolean named = false;
	//
	private boolean isLoaded = false;
	//
	private FieldMap fieldMap;

	public Status getStatus() {
		return status;
	}

//	public PersonalBag<Item> getItemBag() {
//		return itemBag;
//	}

	public CharaSprite getSprite() {
		return fieldSprite;
	}

	public FieldMap getFieldMap() {
		return fieldMap;
	}

	public Actor setFieldMap(FieldMap fieldMap) {
		this.fieldMap = fieldMap;
		return this;
	}

	//------------------------------------------------
	@Override
	@RequiresReturnTypeChange
	public Actor load() throws FileNotFoundException, FileFormatException, ContentsIOException {
		super.load();
		DataFile f = new DataFile(super.getFile()).load();

		//PI		
		var pi = f.get("PI");

		//RACE
		{
			this.race = RaceStorage.getInstance().get(pi.get("race").value.value());
			eqip = new EqipItemSet(this, race.getSlot());
		}

		//WAni
		{
			this.fieldSprite = loadSprite(pi);
			this.fieldSprite.setId(getId());
			this.fieldSprite.setSizeByImage();
			this.fieldSprite.setCurrentLocationOnMap(initialIdx);
		}
		//speaker
		{
			if (pi.has("speakerImage")) {
				this.speakerImage = pi.get("speakerImage").value.asKImageFile();
			}
			if (pi.has("named")) {
				this.named = pi.get("named").value.asBoolean();
			}
		}
		//NPC MoveModel
		{
			if (pi.has("moveModel")) {
				UniversalValue moveModel = pi.get("moveModel").value;
				switch (moveModel.trim().safeSplit(",")[0].toUpperCase()) {
					case "LOCKED" -> {
						new FieldMapNPCMoveModelSetter(this).locked();
					}
					case "PROWL" -> {
						int d = moveModel.trim().safeSplitUV(",")[1].asInt();
						float wt = moveModel.trim().safeSplitUV(",")[2].asFloat();
						new FieldMapNPCMoveModelSetter(this).prowl(d, wt);
					}
					case "TRIP" -> {
						UniversalValue[] val = moveModel.safeSplitUV(",");
						float tt = val[1].asFloat();
						float ti = val[2].asFloat();
						int sx = val[3].asInt();
						int sy = val[4].asInt();
						new FieldMapNPCMoveModelSetter(this).trip(tt, ti, new D2Idx(sx, sy));
					}
					case "PATROL" -> {

					}
					case "RANDOM_TGT" -> {
						UniversalValue[] val = moveModel.safeSplitUV(",");
						float w1 = val[1].asFloat();
						float w2 = val[2].asFloat();
						new FieldMapNPCMoveModelSetter(this).randomTgt(w1, w2);
					}
					case "GOTO_AND_STOP" -> {
						UniversalValue[] val = moveModel.safeSplitUV(",");
						float w = val[1].asFloat();
						int x = val[2].asInt();
						int y = val[3].asInt();
						new FieldMapNPCMoveModelSetter(this).gotoAndStop(w, new D2Idx(x, y));
					}
					case "FOLLOW" -> {
						new FieldMapNPCMoveModelSetter(this).follow(this.fieldSprite.getCurrentLocationOnMap());
					}
					default -> {
						throw new FileFormatException("Actor : undefined move model : " + moveModel);
					}
				}
			}
		}
		//STATUS
		{
			this.status = new Status(this);
			if (f.has("STATUS")) {
				var s = f.get("STATUS");
				//BASE
				if (s.has("BASE")) {
					for (var v : s.get("BASE")) {
						//KEY=CURRENT {, PMAX}
						StatusKey sk = v.key.asStatusKey();
						UniversalValue[] val = v.value.safeSplitUV(",");
						switch (sk.getType()) {
							case INT -> {
								int c = val[0].asInt();
								if (val.length >= 2) {
									((StatusValueInt) this.status.getBase().statusValueSet.of(sk)).setPersonalMax(val[1].asInt());
								}
								((StatusValueInt) this.status.getBase().statusValueSet.of(sk)).setValue(c);
							}
							case MUL -> {
								float c = val[0].asFloat();
								((StatusValueMul) this.status.getBase().statusValueSet.of(sk)).set(c);
							}
							case CHANCE -> {
								float c = val[0].asFloat();
								((StatusValueChance) this.status.getBase().statusValueSet.of(sk)).set(c);
							}
							case ENUM -> {
								if (sk == StatusKey.MGA) {
									this.status.getBase().statusValueSet.MGA.set(val[0].asMagicAptitude());
								}
								if (sk == StatusKey.EQA) {
									this.status.getBase().statusValueSet.EQA.set(val[0].asEqipAttr());
								}
							}
						}
					}
				}
				//ATTR_IN
				if (s.has("ATTR_IN")) {
					for (var v : s.get("ATTR_IN")) {
						//KEY=VALUE
						AttributeKey ak = v.key.asAttributeKey();
						float value = v.value.asFloat();
						this.status.getBase().attrIn.of(ak).set(value);
					}
				}

				//ATTR_OUT
				if (s.has("ATTR_OUT")) {
					for (var v : s.get("ATTR_OUT")) {
						//KEY=VALUE
						AttributeKey ak = v.key.asAttributeKey();
						float value = v.value.asFloat();
						this.status.getBase().attrOut.of(ak).set(value);
					}
				}
				//CND_REGIST
				if (s.has("CND_REGIST")) {
					for (var v : s.get("CND_REGIST")) {
						//KEY=VALUE
						Condition c = v.key.asCondition();
						float value = v.value.asFloat();
						if (value != 1.0f) {
							this.status.getBase().cndRegist.set(c, value);
						}
					}
				}

				//EFFECT
				//設定できるのはCndだけ！！！！！
				if (s.has("CND")) {
					List<StatusEffect<?>> list = new ArrayList<>();
					for (var v : s.get("CND")) {
						//SCRIPTNAME {=EM,TIME,CM,COUNT}

						Condition cnd = v.key.asCondition();
						if (v.value == null || v.value.isEmpty()) {
							list.add(cnd.createEffect(this));
							continue;
						}
						UniversalValue[] val = v.value.safeSplitUV(",");
						StatusEffect.ExpireMode em = val[0].of(StatusEffect.ExpireMode.class);
						int time = val[1].asInt();
						StatusEffect.ManualCountMode cm = val[2].of(StatusEffect.ManualCountMode.class);
						int count = val[3].asInt();
						list.add(cnd.createEffect(this, em, time, cm, count));
					}
					this.status.getCurrentEffect().addAll(list);
				}
			}
		}
		//ITEM
//		{
//			this.itemBag = new PersonalBag<>(this, race.getItemBagSize(),
//					(s, i) -> s.whenIGetItem(i),
//					(s, i) -> s.whenIDropItem(i)
//			);
//			if (f.has("ITEM")) {
//				for (var v : f.get("ITEM")) {
//					//ID{=EQIP_SLOT}
//					Item i = v.key.asItem();
//					this.itemBag.initAdd(i);
//					if (v.value != null && !v.value.isEmpty()) {
//						if (EqipSlot.has(v.value.value())) {
//							EqipSlot slot = v.value.of(EqipSlot.class);
//							this.eqip.setEqip(i, slot);
//						}
//					}
//				}
//			}
//
//		}
		loadPI(pi);

		f.free();

		//更新
		getStatus().updateEffectedStatus();
		isLoaded = true;
		return this;
	}

	@Virtual
	protected CharaSprite loadSprite(DataFile.Element pi) {
		int waTime = pi.get("waTime").value.asInt();
		KImage image = pi.get("sprite").value.asKImageFile();
		Animation south = new Animation(new FrameTimeCounter(waTime), image.splitX(0, 16, 16));
		Animation north = new Animation(new FrameTimeCounter(waTime), image.splitX(16, 16, 16));
		Animation east = new Animation(new FrameTimeCounter(waTime), image.splitX(32, 16, 16));
		Animation west = new Animation(new FrameTimeCounter(waTime), image.splitX(48, 16, 16));
		WalkAnimation wa = new WalkAnimation(north, south, east, west);
		return new CharaSprite(wa);
	}

	@Virtual
	protected void loadPI(DataFile.Element pi) {
	}

	@Override
	public void free() {
		status = null;
		fieldSprite = null;
		isLoaded = false;
	}

	@Override
	public boolean isLoaded() {
		return isLoaded;
	}

	@LoopCall
	public void update() {
		if (fieldSprite.getMoveModel() != null) {
			fieldSprite.getMoveModel().moveToTgt();
		}
	}

	@Override
	public KImage getMWSpeakerImage() {
		return speakerImage;
	}

	@Override
	public I18NText getVisibleName() {
		if (named) {
			return new I18NText(getId());
		}
		return I18NText.EMPTY;
	}

	@Override
	@Nullable
	public I18NText getMWSpeakerName() {
		return getVisibleName();
	}

	@NotNewInstance
	public Map<String, UniversalValue> getGlobalValue() {
		return globalValue;
	}

	public FieldMapNPCMoveModelSetter setMoveModelTo() {
		return new FieldMapNPCMoveModelSetter(this);
	}

	public Race getRace() {
		return race;
	}

}
