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
package kinugasa.system.item;

import kinugasa.script.ScriptAccessMethod;
import kinugasa.system.UniversalValue;
import kinugasa.system.actor.status.ScriptedStatusEffect;
import kinugasa.system.actor.status.StatusEffect;
import kinugasa.system.actor.status.StatusUpdateSAO;

/**
 * ItemEqipScriptAccessObject.<br>
 *
 * @vesion 1.0.0 - 2025/10/04_1:37:13<br>
 * @author Shinacho.<br>
 */
public class ItemGetSAO extends StatusUpdateSAO {

	//PARAM----------------------
	private Item tgtItem;
	//
	//
	private boolean requireEqip;

	public ItemGetSAO() {
	}

	public boolean isRequireEqip() {
		return requireEqip;
	}

	public ItemGetSAO setRequireEqip(boolean requireEqip) {
		this.requireEqip = requireEqip;
		return this;
	}

	public ItemGetSAO setTgtItem(Item tgtItem) {
		this.tgtItem = tgtItem;
		return this;
	}

	//------------------------------------------------------------------SAM-------------------------------------------------------
	@ScriptAccessMethod
	public Item getTgtItem() {
		return tgtItem;
	}

	//EFFECTを持っているなら実行しなければならない。
	@ScriptAccessMethod
	public void addItemAsPassiveEffect() {
//		super.addEffect(new ScriptedStatusEffect<>(tgtItem, getTgt()));
	}

	@ScriptAccessMethod
	public void removeItemAsPassiveEffect() {
//		super.removeEffect(new ScriptedStatusEffect<>(tgtItem, getTgt()));
	}

	@ScriptAccessMethod
	public void addItemAsEffect(UniversalValue eMode, UniversalValue time, UniversalValue cMode, UniversalValue count) {
		StatusEffect.ExpireMode em = eMode.of(StatusEffect.ExpireMode.class);
		int eTime = time.asInt();
		StatusEffect.ManualCountMode cm = cMode.of(StatusEffect.ManualCountMode.class);
		int c = count.asInt();
//		super.addEffect(new ScriptedStatusEffect<>(tgtItem, getTgt(), em, eTime, cm, c));
	}
}
