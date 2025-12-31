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
package kinugasa.system.actor.status;

import java.util.Set;
import kinugasa.field4.FieldMap;
import kinugasa.field4.LayeredTile;
import kinugasa.game.I18NText;
import kinugasa.script.ScriptBlock;
import kinugasa.script.ScriptBlockType;
import kinugasa.script.ScriptFile;
import kinugasa.script.TextBuilderScriptAcccessObject;
import kinugasa.system.actor.Actor;
import kinugasa.system.actor.ConditionChangeScriptAccessObject;
import kinugasa.system.actor.status.cnd.Condition;
import kinugasa.system.item.EqipSlot;
import kinugasa.system.item.Item;
import kinugasa.system.item.ItemEqipSAO;
import kinugasa.system.item.ItemGetSAO;

/**
 * ScriptedActorEffect.<br>
 *
 * @vesion 1.0.0 - 2025/10/07_21:23:51<br>
 * @author Shinacho.<br>
 */
public class ScriptedStatusEffect<T extends ScriptFile> extends StatusEffect<T> {

	public ScriptedStatusEffect(T key, Actor tgt) {
		this(key, tgt, ExpireMode.NON, TIME_NOT_USER, ManualCountMode.NON, TIME_NOT_USER);
	}

	public ScriptedStatusEffect(T key, Actor tgt, ExpireMode mode, int time) {
		this(key, tgt, mode, time, ManualCountMode.NON, TIME_NOT_USER);
	}

	public ScriptedStatusEffect(T key, Actor tgt, ManualCountMode mode, int c) {
		this(key, tgt, ExpireMode.NON, TIME_NOT_USER, mode, c);
	}

	public ScriptedStatusEffect(T key, Actor tgt, ExpireMode em, int time, ManualCountMode mm, int c) {
		super(key, tgt, em, time, mm, c);
	}

	public ScriptedStatusEffect(T key, Object extendId, Actor tgt) {
		this(key, extendId, tgt, ExpireMode.NON, TIME_NOT_USER, ManualCountMode.NON, TIME_NOT_USER);
	}

	public ScriptedStatusEffect(T key, Object extendId, Actor tgt, ExpireMode mode, int time) {
		this(key, extendId, tgt, mode, time, ManualCountMode.NON, TIME_NOT_USER);
	}

	public ScriptedStatusEffect(T key, Object extendId, Actor tgt, ManualCountMode mode, int c) {
		this(key, extendId, tgt, ExpireMode.NON, TIME_NOT_USER, mode, c);
	}

	public ScriptedStatusEffect(T key, Object extendId, Actor tgt, ExpireMode em, int time, ManualCountMode mm, int c) {
		super(key, extendId, tgt, em, time, mm, c);
	}

	public ScriptFile asScript() {
		return getKey();
	}

	@Override
	public I18NText text() {
		ScriptFile sf = asScript().load();
		ScriptBlock blk = sf.getBlockOf(ScriptBlockType.TEXT);
		if (blk.isEmpty()) {
			return I18NText.EMPTY;
		}
		var sao = (TextBuilderScriptAcccessObject) blk.getSao();
		var r = blk.exec().free();
		return sao.build();
	}

	@Override
	public StatusUpdateResult whenIMDead(Set<Condition> deadCnd) {
		ScriptFile sf = asScript().load();
		ScriptBlock blk = sf.getBlockOf(ScriptBlockType.IM_DEAD);
		if (blk.isEmpty()) {
			return new StatusUpdateResult();
		}
		var sao = (StatusUpdateSAO) blk.getSao();
		sao.setTgt(super.getTgt());
		var r = blk.exec().free();
		return new StatusUpdateResult(sao, r);
	}

	@Override
	public StatusUpdateResult whenIMResurrect(Set<Condition> deadCnd) {
		ScriptFile sf = asScript().load();
		ScriptBlock blk = sf.getBlockOf(ScriptBlockType.IM_RESURRECT);
		if (blk.isEmpty()) {
			return new StatusUpdateResult();
		}
		var sao = (StatusUpdateSAO) blk.getSao();
		sao.setTgt(super.getTgt());
		var r = blk.exec().free();
		return new StatusUpdateResult(sao, r);
	}

	@Override
	public StatusUpdateResult whenFMWalk(FieldMap fm, LayeredTile current) {
		ScriptFile sf = asScript().load();
		ScriptBlock blk = sf.getBlockOf(ScriptBlockType.FM_WALK);
		if (blk.isEmpty()) {
			return new StatusUpdateResult();
		}
		var sao = (StatusUpdateSAO) blk.getSao();
		sao.setTgt(super.getTgt());
		var r = blk.exec().free();
		return new StatusUpdateResult(sao, r);
	}

	@Override
	public StatusUpdateResult whenIGetItem(Item i) {
		ScriptFile sf = asScript().load();
		ScriptBlock blk = sf.getBlockOf(ScriptBlockType.GET_ITEM);
		if (blk.isEmpty()) {
			return new StatusUpdateResult();
		}
		var sao = (ItemGetSAO) blk.getSao();
		sao.setTgt(super.getTgt());
		sao.setTgtItem(i);
		var r = blk.exec().free();
		return new StatusUpdateResult(sao, r);
	}

	@Override
	public StatusUpdateResult whenIDropItem(Item i) {
		ScriptFile sf = asScript().load();
		ScriptBlock blk = sf.getBlockOf(ScriptBlockType.DROP_ITEM);
		if (blk.isEmpty()) {
			return new StatusUpdateResult();
		}
		var sao = (ItemGetSAO) blk.getSao();
		sao.setTgt(super.getTgt());
		sao.setTgtItem(i);
		var r = blk.exec().free();
		return new StatusUpdateResult(sao, r);
	}

	@Override
	public StatusUpdateResult whenIEqipItem(Item i, EqipSlot slot) {
		ScriptFile sf = asScript().load();
		ScriptBlock blk = sf.getBlockOf(ScriptBlockType.EQIP_ITEM);
		if (blk.isEmpty()) {
			return new StatusUpdateResult();
		}
		var sao = (ItemEqipSAO) blk.getSao();
		sao.setTgt(super.getTgt());
		sao.setTgtItem(i);
		var r = blk.exec().free();
		return new StatusUpdateResult(sao, r);
	}

	@Override
	public StatusUpdateResult whenIUnEqipItem(Item i, EqipSlot slot) {
		ScriptFile sf = asScript().load();
		ScriptBlock blk = sf.getBlockOf(ScriptBlockType.UNEQIP_ITEM);
		if (blk.isEmpty()) {
			return new StatusUpdateResult();
		}
		var sao = (ItemEqipSAO) blk.getSao();
		sao.setTgt(super.getTgt());
		sao.setTgtItem(i);
		var r = blk.exec().free();
		return new StatusUpdateResult(sao, r);
	}

	@Override
	public StatusUpdateResult whenAddCondition(StatusEffect<?> tgtCnd) {
		ScriptFile sf = asScript().load();
		ScriptBlock blk = sf.getBlockOf(ScriptBlockType.ADD_CONDITION);
		if (blk.isEmpty()) {
			return new StatusUpdateResult();
		}
		var sao = (ConditionChangeScriptAccessObject) blk.getSao();
		sao.setTgt(super.getTgt());
		sao.setTgtCnd(tgtCnd);
		var r = blk.exec().free();
		return new StatusUpdateResult(sao, r);
	}

	@Override
	public StatusUpdateResult whenRemoveCondition(StatusEffect<?> tgtCnd) {
		ScriptFile sf = asScript().load();
		ScriptBlock blk = sf.getBlockOf(ScriptBlockType.REMOVE_CONDITION);
		if (blk.isEmpty()) {
			return new StatusUpdateResult();
		}
		var sao = (ConditionChangeScriptAccessObject) blk.getSao();
		sao.setTgt(super.getTgt());
		sao.setTgtCnd(tgtCnd);
		var r = blk.exec().free();
		return new StatusUpdateResult(sao, r);
	}

	@Override
	public StatusUpdateResult setEffectedStatus(Status.Value val) {
		ScriptFile sf = asScript().load();
		ScriptBlock blk = sf.getBlockOf(ScriptBlockType.EFFECT);
		if (blk.isEmpty()) {
			return new StatusUpdateResult();
		}
		var sao = (StatusEffectSAO) blk.getSao();
		sao.setTgt(super.getTgt());
		sao.setValue(val);
		var r = blk.exec().free();
		return new StatusUpdateResult(sao, r);

	}

	@Override
	public boolean effectedRecalcRequire() {
		var r = asScript().load().has(ScriptBlockType.EFFECT);
		asScript().free();
		return r;
	}

}
