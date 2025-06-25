 /*
  * MIT License
  *
  * Copyright (c) 2025 しなちょ
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */


package kinugasa.object;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kinugasa.resource.NameNotFoundException;

/**
 *
 * @vesion 1.0.0 - 2024/04/26_20:12:34<br>
 * @author Shinacho<br>
 */
public final class UIDSystem implements Iterable<UIDValue> {

	private static final UIDSystem INSTANCE = new UIDSystem();

	public static UIDSystem getInstance() {
		return INSTANCE;
	}

	private final Map<UIDKey, UIDValue> cache = new HashMap<>();

	public void add(UIDSupport v) {
		update();
		cache.put(v.getUIDKey(), v.getUIDValue());
	}

	public void addAll(UIDSupport... vv) {
		update();
		for (var v : vv) {
			cache.put(v.getUIDKey(), v.getUIDValue());
		}
	}

	public void addAll(Collection<? extends UIDSupport> vv) {
		update();
		for (var v : vv) {
			cache.put(v.getUIDKey(), v.getUIDValue());
		}
	}

	public void addByVal(UIDValue v) {
		update();
		cache.put(v.getKey(), v);
	}

	public void addAllByVal(UIDValue... vv) {
		update();
		for (var v : vv) {
			cache.put(v.getKey(), v);
		}
	}

	public void addAllByVal(Collection<? extends UIDValue> vv) {
		update();
		for (var v : vv) {
			cache.put(v.getKey(), v);
		}
	}

	public void remove(UIDKey key) {
		cache.remove(key);
	}

	public void remove(ID key) {
		remove(key.getId());
	}

	public void remove(UIDSupport s) {
		cache.remove(s.getUIDKey());
	}

	public void remove(String key) {
		UIDKey remove = null;
		for (var v : cache.keySet()) {
			if (key.equals(v.getId())) {
				remove = v;
				break;
			}
		}
		if (remove != null) {
			remove(remove);
		}
	}

	public void removeAll(String... id) {
		Arrays.asList(id).forEach(p -> remove(p));
		update();
	}

	public void removeAll(ID... id) {
		Arrays.asList(id).forEach(p -> remove(p));
		update();
	}

	public void removeAll(UIDKey... id) {
		Arrays.asList(id).forEach(p -> remove(p));
		update();
	}

	public void removeAll(UIDSupport... id) {
		Arrays.asList(id).forEach(p -> remove(p));
		update();
	}

	public void removeAllByID(List<? extends ID> id) {
		id.forEach(p -> remove(p));
		update();
	}

	public void removeAllByUID(List<? extends UIDSupport> id) {
		id.forEach(p -> remove(p));
		update();
	}

	public void removeAllByValue(List<? extends UIDValue> id) {
		id.forEach(p -> remove(p));
		update();
	}

	public void removeAll(List<String> id) {
		id.forEach(p -> remove(p));
		update();
	}

	public boolean has(String id) {
		for (var v : cache.keySet()) {
			if (id.equals(v.getId())) {
				return true;
			}
		}
		return false;
	}

	public boolean has(UIDKey key) {
		return cache.containsKey(key);
	}

	public boolean has(UIDSupport s) {
		return cache.containsKey(s.getUIDKey());
	}

	public UIDValue orNull(String id) {
		for (var v : cache.keySet()) {
			if (id.equals(v.getId())) {
				return cache.get(v);
			}
		}
		return null;
	}

	public UIDValue orNull(UIDKey key) {
		return has(key) ? of(key) : null;
	}

	public UIDValue orNull(UIDSupport s) {
		return has(s) ? of(s) : null;
	}

	public UIDValue of(UIDKey key) throws NameNotFoundException {
		if (!cache.containsKey(key)) {
			throw new NameNotFoundException("uid : name not found : " + key);
		}
		return cache.get(key);
	}

	public UIDValue of(UIDSupport s) throws NameNotFoundException {
		return of(s.getUIDKey());
	}

	public UIDValue of(String key) throws NameNotFoundException {
		for (var v : cache.keySet()) {
			if (key.equals(v.getId())) {
				return cache.get(v);
			}
		}
		throw new NameNotFoundException("uid : name not found : " + key);
	}

	public <E extends Enum<E>> UIDValue of(E key) throws NameNotFoundException {
		return of(key.toString());
	}

	private void update() {
		//valueの参照が切れてるものをマップから削除する
		Set<UIDKey> remove = new HashSet<>();
		for (var v : cache.entrySet()) {
			if (!v.getValue().has()) {
				remove.add(v.getKey());
			}
		}
		remove.forEach(p -> cache.remove(p));
	}

	@Override
	public Iterator<UIDValue> iterator() {
		return cache.values().iterator();
	}
}
