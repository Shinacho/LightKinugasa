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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import kinugasa.game.annotation.NewInstance;
import kinugasa.game.annotation.Nullable;
import kinugasa.object.CloneableObject;
import kinugasa.resource.ID;
import kinugasa.resource.Storage;
import kinugasa.system.actor.Actor;
import kinugasa.system.actor.status.cnd.Condition;
import kinugasa.system.actor.status.cnd.ConditionSystem;

/**
 * ActorEffectSetはパッシブ、状態異常、そして装備効果などのActorに作用してる効果の一覧だ！.<br>
 *
 * @vesion 1.0.0 - 2025/10/07_0:27:33<br>
 * @author Shinacho.<br>
 */
public class StatusEffectSet extends CloneableObject implements Iterable<StatusEffect<?>> {

	private final Actor a;
	private final Storage<StatusEffect<?>> e;

	public StatusEffectSet(Actor a) {
		this.a = a;
		this.e = new Storage<>();
	}

	@NewInstance
	public List<StatusEffect<?>> sorted() {
		return stream().sorted().toList();
	}

	@NewInstance
	public List<StatusEffect<Condition>> getCnds() {
		return stream().filter(p -> p.getKey() instanceof Condition).map(p -> (StatusEffect<Condition>) p).sorted().toList();
	}

	public boolean hasCondition(Condition c) {
		return stream().filter(p -> (p.getKey() instanceof Condition)).anyMatch(p -> p.getKey().equals(c));
	}

	public boolean has(ID i) {
		return (i instanceof StatusEffect<?> s) ? has(s) : has(i.getId());
	}

	public boolean has(String id) {
		return e.contains(id);
	}

	public boolean has(StatusEffect<?> t) {
		return e.contains(t);
	}

	public void forceAdd(StatusEffect<?> t) {
		if (has(t)) {
			StatusEffect<?> te = e.get(t);
			if (te.getExpireMode() != StatusEffect.ExpireMode.NON) {
				te.addTime(t.getTime());
			}
			if (te.getCountMode() != StatusEffect.ManualCountMode.NON) {
				te.addCount(t.getCount());
			}
			return;
		}
		this.e.add(t);
		if (t.effectedRecalcRequire()) {
			this.a.getStatus().updateEffectedStatus();
		}
	}

	public void add(StatusEffect<?> t) {
		//すでに持ってる場合は効果時間があれば延長し、そうでなければ何もしない
		if (has(t)) {
			StatusEffect<?> te = e.get(t);
			if (te.getExpireMode() != StatusEffect.ExpireMode.NON) {
				te.addTime(t.getTime());
			}
			if (te.getCountMode() != StatusEffect.ManualCountMode.NON) {
				te.addCount(t.getCount());
			}
			return;
		}

		if (t.getKey() instanceof Condition) {
			StatusEffect<Condition> c = (StatusEffect<Condition>) t;//safe
			for (var v : sorted()) {
				var r = v.whenAddCondition(c);
				if (!r.isCanExec()) {
					return;//追加されなかった
				}
			}
			//相関チェック
			for (var v : ConditionSystem.getInstance().getCndCorres()) {
				if (!v.apply(a, c.getKey())) {
					return;//追加されなかった
				}
			}

		}

		this.e.add(t);
		if (t.effectedRecalcRequire()) {
			this.a.getStatus().updateEffectedStatus();
		}
		return;
	}

	public void addAll(Collection<StatusEffect<?>> s) {
		boolean recalc = false;
		L1:
		for (var t : s) {
			//すでに持ってる場合は効果時間があれば延長し、そうでなければ何もしない
			if (has(t)) {
				StatusEffect<?> te = e.get(t);
				if (te.getExpireMode() != StatusEffect.ExpireMode.NON) {
					te.addTime(t.getTime());
				}
				if (te.getCountMode() != StatusEffect.ManualCountMode.NON) {
					te.addCount(t.getCount());
				}
				continue L1;
			}

			if (t.getKey() instanceof Condition) {
				StatusEffect<Condition> c = (StatusEffect<Condition>) t;//safe
				for (var v : sorted()) {
					var r = v.whenAddCondition(c);
					if (!r.isCanExec()) {
						continue L1;//追加されなかった
					}
				}
			}
			recalc |= t.effectedRecalcRequire();
			this.e.add(t);
		}
		if (recalc) {
			this.a.getStatus().updateEffectedStatus();
		}
	}

	public void forceRemove(StatusEffect<?> t) {
		//持っていない場合は何もしない
		if (!has(t)) {
			return;//削除されなかった
		}

		e.remove(t);
		if (t.effectedRecalcRequire()) {
			this.a.getStatus().updateEffectedStatus();
		}
		return;
	}

	public void forceRemove(Condition c) {
		boolean recalc = false;
		var remove = e.stream().filter(p -> p.getKey().equals(c)).toList();
		for (var t : remove) {
			recalc |= t.effectedRecalcRequire();
			e.remove(t);
		}
		if (recalc) {
			this.a.getStatus().updateEffectedStatus();
		}
	}

	public void removeCnd(Condition c) {
		var remove = e.stream().filter(p -> p.getKey().equals(c)).toList();
		removeAll(remove);
	}

	public void remove(StatusEffect<?> t) {
		//持っていない場合は何もしない
		if (!has(t)) {
			return;//削除されなかった
		}

		if (t.getKey() instanceof Condition) {
			StatusEffect<Condition> c = (StatusEffect<Condition>) t;//safe
			for (var v : sorted()) {
				var r = v.whenRemoveCondition(c);
				if (!r.isCanExec()) {
					return;//削除されなかった
				}
			}
		}
		e.remove(t);
		if (t.effectedRecalcRequire()) {
			this.a.getStatus().updateEffectedStatus();
		}
		return;
	}

	public void remove(String id) {
		for (var v : e) {
			if (v.getId().equals(id)) {
				remove(v);
			}
		}
	}

	public void remove(ID id) {
		remove(id.getId());
	}

	public void removeAll(String... id) {
		List<StatusEffect<?>> l = new ArrayList<>();
		for (var i : id) {
			for (var v : e) {
				if (i.equals(v.getId())) {
					l.add(v);
					break;
				}
			}
		}
		removeAll(l);
	}

	public void removeAll(ID... id) {
		List<StatusEffect<?>> l = new ArrayList<>();
		for (var i : id) {
			for (var v : e) {
				if (i.getId().equals(v.getId())) {
					l.add(v);
					break;
				}
			}
		}
		removeAll(l);
	}

	public void removeAll(Collection<StatusEffect<?>> s) {
		boolean recalc = false;
		L1:
		for (var t : s) {
			//持っていない場合は何もしない
			if (!has(t)) {
				continue L1;//削除されなかった
			}

			if (t.getKey() instanceof Condition) {
				StatusEffect<Condition> c = (StatusEffect<Condition>) t;//safe
				for (var v : sorted()) {
					var r = v.whenRemoveCondition(c);
					if (!r.isCanExec()) {
						continue L1;//削除されなかった
					}
				}
			}
			recalc |= t.effectedRecalcRequire();
			e.remove(t);
		}
		if (recalc) {
			this.a.getStatus().updateEffectedStatus();
		}
		return;
	}

	public StatusEffect<?> get(String id) {
		return e.get(id);
	}

	public StatusEffect<?> get(Condition id) {
		return e.get(id.getId());
	}

	public StatusEffect<?> get(StatusEffect<?> t) {
		return e.get(t);
	}

	public StatusEffect<?> getOrNull(String id) {
		return e.getOrNull(id);
	}

	public StatusEffect<?> getOrNull(Condition id) {
		return e.getOrNull(id.getId());
	}

	public void clear() {
		List<StatusEffect<?>> tgt = this.e.asList();
		removeAll(tgt);
	}

	@Nullable
	public List<StatusEffect<?>> byKey(ID i) {
		return stream().filter(p -> p.getKey().equals(i)).toList();
	}

	@Override
	public StatusEffectSet clone() {
		//中身のクローンが必要
		var r = (StatusEffectSet) super.clone();
		r.clear();
		this.forEach(p -> r.add(p.clone()));
		return r;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public Iterator<StatusEffect<?>> iterator() {
		return e.iterator();
	}

	public Stream<StatusEffect<?>> stream() {
		return e.stream();
	}

}
