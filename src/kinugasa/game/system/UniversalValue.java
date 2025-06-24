/*
 * Copyright (C) 2024 Shinacho
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
package kinugasa.game.system;

import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.print.Book;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kinugasa.game.I18NText;
import kinugasa.game.Immutable;
import kinugasa.game.NotNull;
import kinugasa.game.Nullable;
import kinugasa.game.field4.BeforeLayerSprite;
import kinugasa.game.field4.BeforeLayerSpriteStorage;
import kinugasa.game.field4.D2Idx;
import kinugasa.game.field4.FieldMap;
import kinugasa.game.field4.FieldMapStorage;
import kinugasa.game.field4.Node;
import kinugasa.game.ui.TextStorage;
import kinugasa.game.ui.TextStorageStorage;
import kinugasa.graphics.SpriteSheet;
import kinugasa.object.FourDirection;
import kinugasa.object.UIDSupport;
import kinugasa.object.UIDSystem;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.NameNotFoundException;
import kinugasa.resource.sound.FramePosition;
import kinugasa.resource.sound.MasterGain;
import kinugasa.resource.sound.Sound;
import kinugasa.resource.text.XMLAttribute;
import kinugasa.util.FrameTimeCounter;
import kinugasa.util.ManualTimeCounter;
import kinugasa.util.StringUtil;
import kinugasa.util.TimeCounter;

/**
 * ユニバーサルバリュー、通称ユニバはいろいろな型に変換できる文字列です。
 *
 * @vesion 1.0.0 - 2024/04/24_20:51:46<br>
 * @author Shinacho<br>
 */
@Immutable
public sealed class UniversalValue permits XMLAttribute {

	private final String value;

	public <T extends Enum<T>> UniversalValue(T value) {
		this.value = value.toString();
	}

	public UniversalValue(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public boolean is(Object o) {
		return value.equals(o);
	}

	public String asId() {
		return value();
	}

	public int[] asSafeIntArray(String sep) {
		String[] v = safeSplit(sep);
		int[] res = new int[v.length];
		for (int i = 0; i < v.length; i++) {
			res[i] = Integer.parseInt(v[i]);
		}
		return res;
	}

	public ManualTimeCounter asManualTimeCounter() {
		return new ManualTimeCounter(asInt());
	}

	public TimeCounter asTimeCounter() {
		return new FrameTimeCounter(asSafeIntArray(","));
	}

	public float[] asSafeFloatArray(String sep) {
		String[] v = safeSplit(sep);
		float[] res = new float[v.length];
		for (int i = 0; i < v.length; i++) {
			res[i] = Float.parseFloat(v[i]);
		}
		return res;
	}

	public String[] safeSplit(String sep) {
		if (value == null) {
			return new String[]{};
		}
		if (value.contains(sep)) {
			return value.split(sep);
		}
		if (value.isEmpty()) {
			return new String[]{};
		}
		return new String[]{value};
	}

	public <T extends UIDSupport> T as(Class<T> t) {
		return UIDSystem.getInstance().of(value).as(t);
	}

	public <T extends Enum<T>> T of(Class<T> c) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		T[] values = c.getEnumConstants();
		for (T t : values) {
			if (t.toString().equals(value.toUpperCase())) {
				return t;
			}
		}
		throw new AssertionError("DBValue : not found " + value);
	}

	private <T extends Enum<T>> T of(String v, Class<T> c) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		T[] values = c.getEnumConstants();
		for (T t : values) {
			if (t.toString().toUpperCase().equals(v.toUpperCase())) {
				return t;
			}
		}
		throw new AssertionError("DBValue : not found " + value);
	}

	@NotNull
	public <T extends Enum<T>> Set<T> csvOf(Class<T> c) {
		Set<T> r = new HashSet<>();
		for (var v : safeSplit(",")) {
			r.add(of(v, c));
		}
		return r;
	}

	public <T extends Enum<T>> T orNull(Class<T> c) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		T[] values = c.getEnumConstants();
		for (T t : values) {
			if (t.toString().equals(value.toUpperCase())) {
				return t;
			}
		}
		return null;
	}

	public <T extends UIDSupport> T asTOrNull(Class<T> t) {
		return UIDSystem.getInstance().of(value).asTorNull(t);
	}

	public int asInt() {
		return Integer.parseInt(value);
	}

	public long asLong() {
		return Long.parseLong(value);
	}

	public float asFloat() {
		return Float.parseFloat(value);
	}

	public boolean is(String v) {
		return value.equals(v);
	}

	public boolean is(Class<? extends UIDSupport> i) {
		return UIDSystem.getInstance().of(asId()).is(i);
	}

	public boolean isTrue() {
		return Boolean.parseBoolean(value.toLowerCase());
	}

	public boolean asBoolean() {
		return isTrue();
	}

	public boolean isEmpty() {
		return value == null || value.isEmpty();
	}

	public String[] asCsv() {
		return StringUtil.safeSplit(value, ",");
	}

	public I18NText asI18Nd() {
		return I18NText.of(value);
	}

	public I18NText asI18N() {
		return new I18NText(value);
	}

	public String asFileName() {
		File f = new File(value);
		if (!f.exists()) {
			throw new FileNotFoundException(f);
		}
		return value;
	}

	public D2Idx asD2IdxCSV() {
		String[] v = StringUtil.safeSplit(value, ",");
		return new D2Idx(Integer.parseInt(v[0]), Integer.parseInt(v[1]));
	}

	public SpriteSheet asSpriteSheetFileName() {
		return new SpriteSheet(value);
	}

	public FourDirection asFourDirection() {
		return FourDirection.valueOf(value);
	}

	public TextStorage asTextStorage() {
		return TextStorageStorage.getInstance().get(value);
	}

	public NPCSprite asNewNPC() {
		return new NPCSprite(value);
	}

	public NPCSprite asCurrentMapNPC() {
		return FieldMap.getCurrentInstance().getNpcStorage().get(value);
	}

	public BeforeLayerSprite asBeforeLayerSprite() {
		return BeforeLayerSpriteStorage.getInstance().get(value);
	}

	public Sound asBGM() {
		return asSound(Sound.Type.BGM);
	}

	public Sound asSE() {
		return asSound(Sound.Type.SE);
	}

	public Sound asSound(Sound.Type t) {
		return new Sound(value, t, value);
	}

	public FieldMap asFieldMap() {
		return FieldMapStorage.getInstance().get(value);
	}

	public Node asNodeCSV() {
		String[] v = StringUtil.safeSplit(value, ",");
		return FieldMapStorage.getInstance().get(v[0])
				.getNodeStorage().get(new D2Idx(Integer.parseInt(v[1]), Integer.parseInt(v[2])));
	}

	public Node asNewOutNodeCSV() {
		String[] v = StringUtil.safeSplit(value, ",");
		String id = v[0];
		String outMapName = v[1];
		int x = Integer.parseInt(v[2]);
		int y = Integer.parseInt(v[3]);
		FourDirection outDir = FourDirection.valueOf(v[4]);
		return Node.ofOutNode(id, outMapName, x, y, outDir);
	}

	public FramePosition asFramePosition() {
		return FramePosition.parse(value);
	}

	public MasterGain asMasterGain() {
		return new MasterGain(asFloat());
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 67 * hash + Objects.hashCode(this.value);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final UniversalValue other = (UniversalValue) obj;
		return Objects.equals(this.value, other.value);
	}

}
