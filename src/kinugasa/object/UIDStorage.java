/*
 * Copyright (C) 2023 Shinacho
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
package kinugasa.object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import kinugasa.resource.DuplicateNameException;
import kinugasa.resource.NameNotFoundException;
import kinugasa.resource.Storage;

/**
 * このストレージはUIDオブジェクトを格納するために使用されます。 このストレージは強参照を持つので、このストレージにある値は常にメモリに存在します。
 *
 * @author Shinacho
 * @param <T>
 */
public class UIDStorage<T extends UIDSupport> extends Storage<T> {

	public UIDStorage() {
		super();
	}

	public UIDStorage(T... t) {
		super(t);
		UIDSystem.getInstance().addAll(t);
	}

	public UIDStorage(Collection<? extends T> t) {
		super(t);
		UIDSystem.getInstance().addAll(new ArrayList<UIDSupport>(t));
	}

	public UIDStorage(Storage<? extends T> t) {
		super(t);
		UIDSystem.getInstance().addAll(t.asList());
	}

	public Set<UIDKey> keySetAsUIDK() {
		return asList().stream().map(p -> p.getUIDKey()).collect(Collectors.toSet());
	}

	public void removeAllByUIDK(UIDKey... keys) {
		removeAllByUIDK(Arrays.asList(keys));
	}

	public void removeAllByUIDK(Collection<? extends UIDKey> val) {
		for (var v : val) {
			remove(v);
		}
	}

	public void remove(UIDKey k) {
		remove(k.getId());
	}

	public void putFromUIDS(Class<? extends T> t, UIDKey... keys) throws ClassCastException {
		putFromUIDS(t, Arrays.asList(keys));
	}

	public void putFromUIDSs(Class<? extends T> t, String... keys) throws ClassCastException {
		putFromUIDSs(t, Arrays.asList(keys));
	}

	public void putFromUIDS(Class<? extends T> t, Collection<UIDKey> keys) throws ClassCastException {
		for (var v : keys) {
			put(UIDSystem.getInstance().of(v).as(t));
		}
	}

	public void putFromUIDSs(Class<? extends T> t, Collection<String> keys) throws ClassCastException {
		for (var v : keys) {
			put(UIDSystem.getInstance().of(UIDKey.of(v)).as(t));
		}
	}

	public boolean contains(UIDKey k) {
		return contains(k.getId());
	}

	public boolean containsAll(UIDKey... keys) {
		return containsAll(Arrays.asList(keys).stream().map(p -> p.getId()).toList());
	}

	public boolean containsAny(UIDKey... keys) {
		return containsAny(Arrays.asList(keys).stream().map(p -> p.getId()).toList());
	}

	public T getOrNull(UIDKey key) {
		return super.getOrNull(key.getId());
	}

	public T get(UIDKey k) throws NameNotFoundException {
		return super.get(k.getId());
	}

	@Override
	public void addAll(Collection<? extends T> values) throws DuplicateNameException {
		super.addAll(values);
		UIDSystem.getInstance().addAll(values);
	}

	@Override
	public void addAll(Storage<? extends T> s) throws DuplicateNameException {
		super.addAll(s);
		UIDSystem.getInstance().addAll(s.asList());
	}

	@Override
	public void addAll(T... values) throws DuplicateNameException {
		super.addAll(values);
		UIDSystem.getInstance().addAll(values);
	}

	@Override
	public void add(T val) throws DuplicateNameException {
		super.add(val);
		UIDSystem.getInstance().add(val);
	}

	@Override
	public T getOrCreate(ID key, Supplier<? extends T> s) {
		if (contains(UIDKey.of(key))) {
			return get(UIDKey.of(key));
		}
		T t = s.get();
		add(t);
		return t;
	}

	@Override
	public T getOrCreate(String key, Supplier<? extends T> s) {
		if (contains(UIDKey.of(key))) {
			return get(UIDKey.of(key));
		}
		T t = s.get();
		add(t);
		return t;
	}

	public T getOrCreate(UIDKey key, Supplier<? extends T> s) {
		if (contains(UIDKey.of(key))) {
			return get(UIDKey.of(key));
		}
		T t = s.get();
		add(t);
		return t;
	}

	@Override
	public String toString() {
		return super.toString(); 
	}

}
