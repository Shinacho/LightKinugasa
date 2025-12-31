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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import kinugasa.game.VisibleNameIDInjector;
import kinugasa.game.annotation.NeedSuperCallWhenOverride;
import kinugasa.game.annotation.RequiresReturnTypeChange;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.text.FileFormatException;
import kinugasa.script.ScriptBlockType;
import kinugasa.script.ScriptFile;
import kinugasa.script.ScriptResult;
import kinugasa.system.GameSystem;
import kinugasa.system.actor.status.StatusEffectable;
import kinugasa.system.actor.status.StatusUpdateResult;

/**
 * BagItem.<br>
 *
 * @vesion 1.0.0 - 2025/08/13_14:52:44<br>
 * @author Shinacho.<br>
 */
public abstract class PersonalBagItem extends ScriptFile
		implements
		Cloneable,
		VisibleNameIDInjector<PersonalBagItem>{

	private boolean recalc = false;

	protected PersonalBagItem(File f) {
		super(f);
		recalc = load().has(ScriptBlockType.EFFECT);
	}

	@RequiresReturnTypeChange
	@NeedSuperCallWhenOverride
	@Override
	public final PersonalBagItem load() throws FileNotFoundException, FileFormatException, ContentsIOException {
		super.load();
		return this;
	}

	@Override
	@NeedSuperCallWhenOverride
	public final void free() {
		super.free();
	}

	@RequiresReturnTypeChange
	@Override
	public PersonalBagItem clone() {
		try {
			return (PersonalBagItem) super.clone();
		} catch (CloneNotSupportedException ex) {
			throw new InternalError(ex);
		}
	}

	//----------------------------------------THIS ITEM----------------------------------------

	public abstract boolean getThisEventRequire();

	/* 持ってるだけ効果がある場合はここでパッシブを追加する */
	public abstract StatusUpdateResult whenGetThis(Actor a);

	public abstract boolean dropThisEventRequire();

	/* 持ってるだけ効果がある場合はここでパッシブを削除する */
	public abstract StatusUpdateResult whenDropThis(Actor a);

	public ScriptResult use() {
		if (GameSystem.getInstance().getMode() == GameSystem.Mode.FIELD) {
			return load().getBlockOf(ScriptBlockType.USE_FIELD).exec();
		}
		if (GameSystem.getInstance().getMode() == GameSystem.Mode.BATTLE) {
			return load().getBlockOf(ScriptBlockType.USE_BATTLE).exec();
		}
		throw new IllegalStateException();
	}
	

	//EQIP,UNEQIPはアイテムのみなので、ITEMで。
	
	
	public boolean effectedRecalcRequire() {
		return recalc;
	}

//	public<T extends PersonalBagItem> StatusEffectable<T> effect1(){
//		
//	}
//	
//	public<T extends PersonalBagItem> StatusEffectable<T> effect2(){
//	
//	}
	
}
