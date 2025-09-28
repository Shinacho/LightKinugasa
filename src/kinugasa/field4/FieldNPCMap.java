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
package kinugasa.field4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.game.annotation.Nullable;
import kinugasa.system.actor.NPC;

/**
 * FieldNPCMap.<br>
 *
 * @vesion 1.0.0 - 2025/08/14_20:40:17<br>
 * @author Shinacho.<br>
 */
public class FieldNPCMap implements Iterable<NPC> {

	private final Map<D2Idx, NPC> npcMap;

	public FieldNPCMap() {
		npcMap = new HashMap<>();
	}

	public boolean has(D2Idx i) {
		return npcMap.containsKey(i);
	}

	public boolean has(String id) {
		for (var v : npcMap.values()) {
			if (v.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	@Nullable
	public NPC get(String id) {
		for (var v : npcMap.values()) {
			if (v.getId().equals(id)) {
				return v;
			}
		}
		return null;
	}

	public void add(D2Idx i, NPC e) {
		npcMap.put(i, e);
	}

	public void add(NPC n) {
		add(n.getSprite().getCurrentLocationOnMap(), n);
	}

	public NPC get(D2Idx i) {
		return npcMap.get(i);
	}

	@NotNewInstance
	public Map<D2Idx, NPC> getMap() {
		return npcMap;
	}

	@NotNewInstance
	public Collection<NPC> values() {
		return npcMap.values();
	}

	void free() {
		npcMap.values().forEach(p -> p.asScript().free());
		npcMap.values().forEach(p -> p.free());
		npcMap.clear();
	}

	@Override
	public Iterator<NPC> iterator() {
		return npcMap.values().iterator();
	}

	public Stream<NPC> stream() {
		return npcMap.values().stream();
	}

	public void update() {
		List<NPC> n = new ArrayList<>(npcMap.values());
		npcMap.clear();
		for (var v : n) {
			npcMap.put(v.getSprite().getCurrentLocationOnMap(), v);
		}
	}

	@Override
	public String toString() {
		return "FieldNPCMap{" + "npcMap=" + npcMap + '}';
	}

}
