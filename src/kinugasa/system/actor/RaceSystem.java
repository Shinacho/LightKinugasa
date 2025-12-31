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
import java.util.EnumSet;
import java.util.List;
import kinugasa.game.annotation.Singleton;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.text.DataFile;
import kinugasa.system.actor.status.cnd.Condition;
import kinugasa.system.item.EqipSlot;

/**
 * RaceSystem.<br>
 *
 * @vesion 1.0.0 - 2025/12/14_17:47:31<br>
 * @author Shinacho.<br>
 */
@Singleton
public class RaceSystem {

	private static final RaceSystem INSTANCE = new RaceSystem();

	private RaceSystem() {
	}

	public static RaceSystem getInstance() {
		return INSTANCE;
	}

	public void init(File f) {
		DataFile d = new DataFile(f);
		if (!d.exists()) {
			throw new FileNotFoundException(f);
		}
		d.load();
		for (var v : d) {
			String id = v.key.toString();
			int itemBagSize = v.get("itemBagSize").value.asInt();

		}

		d.free();
	}

}

class RaceImpl implements Race {

	private final String id;
	private final int itemBagSize;
	private final int bookBagSize;
	private final int scriptBagSize;
	private final EnumSet<EqipSlot> slot;
	private final List<Condition> passive;

	public RaceImpl(String id, int itemBagSize, int bookBagSize, int scriptBagSize, EnumSet<EqipSlot> slot, List<Condition> passive) {
		this.id = id;
		this.itemBagSize = itemBagSize;
		this.bookBagSize = bookBagSize;
		this.scriptBagSize = scriptBagSize;
		this.slot = slot;
		this.passive = passive;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public int getItemBagSize() {
		return itemBagSize;
	}

	@Override
	public int getBookBagSize() {
		return bookBagSize;
	}

	@Override
	public int getScriptBagSize() {
		return scriptBagSize;
	}

	@Override
	public EnumSet<EqipSlot> getSlot() {
		return slot;
	}

	@Override
	public List<Condition> getPassive() {
		return passive;
	}

	@Override
	public String toString() {
		return id;
	}

}
