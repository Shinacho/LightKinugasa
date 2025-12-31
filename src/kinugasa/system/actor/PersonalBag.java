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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import kinugasa.resource.ID;
import kinugasa.resource.IDNotFoundException;
import kinugasa.system.actor.status.StatusEffect;
import kinugasa.system.actor.status.StatusUpdateResult;
import kinugasa.util.KRandom;

/**
 * PersonalBag.<br>
 *
 * @vesion 1.0.0 - 2025/10/02_22:01:26<br>
 * @author Shinacho.<br>
 */
public class PersonalBag<T extends PersonalBagItem> implements Iterable<T> {

	private final Actor actor;
	private int max;
	private final List<T> list = new ArrayList<>();
	private final BiFunction<StatusEffect<?>, T, StatusUpdateResult> getFnc, dropFnc;

	public PersonalBag(Actor actor, int max,
			BiFunction<StatusEffect<?>, T, StatusUpdateResult> getFnc,
			BiFunction<StatusEffect<?>, T, StatusUpdateResult> dropFnc) {
		this.actor = actor;
		this.max = max;
		this.getFnc = getFnc;
		this.dropFnc = dropFnc;
	}

	public Actor getActor() {
		return actor;
	}

	public int max() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public void addMax(int m) {
		this.max += m;
	}

	final void initAdd(T t) {
		this.list.add(t);
	}

	public PersonalBag<T> add(T t) {
		//持てる数の判定
		if (!canAdd()) {
			//持てない
			return this;
		}
		//発生中効果による判定
		for (var v : actor.getStatus().getCurrentEffect().sorted()) {
			var r = getFnc.apply(v, t);
			if (!r.isCanExec()) {
				//追加されなかった
				return this;
			}
		}
		this.list.add(t);
		if (t.getThisEventRequire()) {
			t.whenGetThis(actor);
		}
		if (t.effectedRecalcRequire()) {
			actor.getStatus().updateEffectedStatus();
		}
		return this;
	}

	public PersonalBag<T> add(T... t) {
		return add(Arrays.asList(t));
	}

	public PersonalBag<T> add(Collection<? extends T> c) {
		boolean recalc = false;
		for (var t : c) {
			//持てる数の判定
			if (!canAdd()) {
				//持てない
				return this;
			}
			//発生中効果による判定
			for (var v : actor.getStatus().getCurrentEffect().sorted()) {
				var r = getFnc.apply(v, t);
				if (!r.isCanExec()) {
					//追加されなかった
					return this;
				}
			}
			recalc |= t.effectedRecalcRequire();
			this.list.add(t);
			if (t.getThisEventRequire()) {
				t.whenGetThis(actor);
			}
		}

		if (recalc) {
			actor.getStatus().updateEffectedStatus();
		}
		return this;
	}

	public PersonalBag<T> addForce(T t) {
		//持てる数の判定
		//発生中効果による判定
		for (var v : actor.getStatus().getCurrentEffect().sorted()) {
			var r = getFnc.apply(v, t);
			if (!r.isCanExec()) {
				//追加されなかった
				return this;
			}
		}
		this.list.add(t);
		if (t.getThisEventRequire()) {
			t.whenGetThis(actor);
		}
		if (t.effectedRecalcRequire()) {
			actor.getStatus().updateEffectedStatus();
		}
		return this;
	}

	public PersonalBag<T> addForce(T... t) {
		return addForce(Arrays.asList(t));

	}

	public PersonalBag<T> addForce(Collection<? extends T> c) {
		boolean recalc = false;
		for (var t : c) {
			//持てる数の判定
			if (!canAdd()) {
				//持てない
				return this;
			}
			//発生中効果による判定
			for (var v : actor.getStatus().getCurrentEffect().sorted()) {
				var r = getFnc.apply(v, t);
				if (!r.isCanExec()) {
					//追加されなかった
					return this;
				}
			}
			recalc |= t.effectedRecalcRequire();
			this.list.add(t);
			if (t.getThisEventRequire()) {
				t.whenGetThis(actor);
			}
		}

		if (recalc) {
			actor.getStatus().updateEffectedStatus();
		}
		return this;
	}

	public PersonalBag<T> addN(T t, int n) {
		List<T> l = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			l.add((T) t.clone());
		}
		return add(l);
	}

	public PersonalBag<T> addNForce(T t, int n) {
		List<T> l = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			l.add((T) t.clone());
		}
		return addForce(l);
	}

	public boolean has(String id) {
		return list.stream().anyMatch(p -> p.getId().equals(id));
	}

	public boolean has(ID i) {
		return has(i.getId());
	}

	public boolean has(T t) {
		return list.stream().anyMatch(p -> p.equals(t));
	}

	public int hasN(String id) {
		return (int) list.stream().filter(p -> p.getId().equals(id)).count();
	}

	public int hasN(ID i) {
		return (int) list.stream().filter(p -> p.getId().equals(i.getId())).count();
	}

	public int hasN(T t) {
		return (int) list.stream().filter(p -> p.equals(t)).count();
	}

	public boolean canAdd() {
		return currentSize() < max();
	}

	public int spaceSize() {
		return max() - currentSize();
	}

	public int currentSize() {
		return list.size();
	}

	public PersonalBag<T> drop(T t) {
		//発生中効果による判定
		for (var v : actor.getStatus().getCurrentEffect().sorted()) {
			var r = dropFnc.apply(v, t);
			if (!r.isCanExec()) {
				//されなかった
				return this;
			}
		}
		this.list.remove(t);
		if (t.dropThisEventRequire()) {
			t.whenDropThis(actor);
		}
		if (t.effectedRecalcRequire()) {
			actor.getStatus().updateEffectedStatus();
		}
		return this;
	}

	public PersonalBag<T> drop(String id) {
		for (var v : list) {
			if (v.getId().equals(id)) {
				return drop(v);
			}
		}
		return this;
	}

	public PersonalBag<T> drop(ID id) {
		return drop(id.getId());
	}

	public PersonalBag<T> dropAll(T... t) {
		return dropAll(Arrays.asList(t));
	}

	public PersonalBag<T> dropAll(Collection<? extends T> c) {
		boolean recalc = false;
		L1:
		for (var t : c) {
			//発生中効果による判定
			for (var v : actor.getStatus().getCurrentEffect().sorted()) {
				var r = dropFnc.apply(v, t);
				if (!r.isCanExec()) {
					//されなかった
					continue L1;
				}
			}
			recalc |= t.effectedRecalcRequire();
			this.list.remove(t);
			if (t.dropThisEventRequire()) {
				t.whenDropThis(actor);
			}
		}
		if (recalc) {
			actor.getStatus().updateEffectedStatus();
		}
		return this;
	}

	public PersonalBag<T> dropN(String id, int n) {
		boolean recalc = false;
		L1:
		for (int i = 0; i < n; i++) {
			//発生中効果による判定
			T t = getOrNull(id);
			for (var v : actor.getStatus().getCurrentEffect().sorted()) {
				var r = dropFnc.apply(v, t);
				if (!r.isCanExec()) {
					//されなかった
					continue L1;
				}
			}
			recalc |= t.effectedRecalcRequire();
			this.list.remove(t);
			if (t.dropThisEventRequire()) {
				t.whenDropThis(actor);
			}
		}
		if (recalc) {
			actor.getStatus().updateEffectedStatus();
		}
		return this;
	}

	public PersonalBag<T> dropN(ID id, int n) {
		return dropN(id.getId(), n);
	}

	public PersonalBag<T> dropN(T id, int n) {
		boolean recalc = false;
		L1:
		for (int i = 0; i < n; i++) {
			//発生中効果による判定
			T t = getOrNull(id);
			for (var v : actor.getStatus().getCurrentEffect().sorted()) {
				var r = dropFnc.apply(v, t);
				if (!r.isCanExec()) {
					//されなかった
					continue L1;
				}
			}
			recalc |= t.effectedRecalcRequire();
			this.list.remove(t);
			if (t.dropThisEventRequire()) {
				t.whenDropThis(actor);
			}
		}
		if (recalc) {
			actor.getStatus().updateEffectedStatus();
		}
		return this;
	}

	public Stream<T> stream() {
		return list.stream();
	}

	public T get(String id) {
		T t = getOrNull(id);
		if (t == null) {
			throw new IDNotFoundException("PersonalBag not found : " + id);
		}
		return t;
	}

	public T get(T i) {
		T t = getOrNull(i);
		if (t == null) {
			throw new IDNotFoundException("PersonalBag not found : " + i);
		}
		return t;
	}

	public T get(ID i) {
		return get(i.getId());
	}

	public T getOrNull(String id) {
		for (var v : list) {
			if (v.getId().equals(id)) {
				return v;
			}
		}
		return null;
	}

	public T getOrNull(T i) {
		for (var v : list) {
			if (v.equals(i)) {
				return v;
			}
		}
		return null;
	}

	public T getOrNull(ID i) {
		return getOrNull(i.getId());
	}

	public T random() {
		return KRandom.randomChoice(list);
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public String toString() {
		return "PersonalBag{" + "list=" + list + '}';
	}

}
