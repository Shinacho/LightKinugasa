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
import kinugasa.resource.ID;
import kinugasa.system.actor.Actor;
import kinugasa.system.actor.status.cnd.Condition;
import kinugasa.system.item.EqipSlot;
import kinugasa.system.item.Item;

/**
 * ActorEffectAdaptor.<br>
 *
 * @vesion 1.0.0 - 2025/10/06_20:32:44<br>
 * @author Shinacho.<br>
 */
public class StatusEffectAdaptor<T extends ID> extends StatusEffect<T> {

	public StatusEffectAdaptor(T key, Actor tgt) {
		super(key, tgt);
	}

	public StatusEffectAdaptor(T key, Actor tgt, ExpireMode mode, int time) {
		super(key, tgt, mode, time);
	}

	@Override
	public I18NText text() {
		return I18NText.EMPTY;
	}

	@Override
	public StatusUpdateResult whenIMDead(Set<Condition> deadCnd) {
		return new StatusUpdateResult();
	}

	@Override
	public StatusUpdateResult whenIMResurrect(Set<Condition> deadCnd) {
		return new StatusUpdateResult();
	}

	@Override
	public StatusUpdateResult whenFMWalk(FieldMap fm, LayeredTile current) {
		return new StatusUpdateResult();
	}

	@Override
	public StatusUpdateResult whenIGetItem(Item i) {
		return new StatusUpdateResult();
	}

	@Override
	public StatusUpdateResult whenIDropItem(Item i) {
		return new StatusUpdateResult();
	}

	@Override
	public StatusUpdateResult whenIEqipItem(Item i, EqipSlot slot) {
		return new StatusUpdateResult();
	}

	@Override
	public StatusUpdateResult whenIUnEqipItem(Item i, EqipSlot slot) {
		return new StatusUpdateResult();
	}

	@Override
	public StatusUpdateResult whenAddCondition(StatusEffect<?> tgtCnd) {
		return new StatusUpdateResult();
	}

	@Override
	public StatusUpdateResult whenRemoveCondition(StatusEffect<?> tgtCnd) {
		return new StatusUpdateResult();
	}

	@Override
	public StatusUpdateResult setEffectedStatus(Status.Value val) {
		return new StatusUpdateResult();
	}

	@Override
	public boolean effectedRecalcRequire() {
		return false;
	}

}
