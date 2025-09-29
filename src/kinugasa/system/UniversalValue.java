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
package kinugasa.system;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import kinugasa.game.I18NText;
import kinugasa.game.annotation.Immutable;
import kinugasa.game.annotation.NewInstance;
import kinugasa.game.annotation.NotNull;
import kinugasa.game.annotation.Nullable;
import kinugasa.script.ScriptFileCall;
import kinugasa.field4.D2Idx;
import kinugasa.field4.FieldMapSystem;
import kinugasa.field4.MapChipSet;
import kinugasa.system.actor.Actor;
import kinugasa.system.actor.Follower;
import kinugasa.system.actor.NPC;
import kinugasa.graphics.KImage;
import kinugasa.graphics.SpriteSheet;
import kinugasa.object.FourDirection;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.ID;
import kinugasa.resource.sound.FramePosition;
import kinugasa.resource.sound.MasterGain;
import kinugasa.resource.sound.Sound;
import kinugasa.resource.sound.SoundSystem;
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
public sealed class UniversalValue implements ID permits XMLAttribute {

	public static final UniversalValue EMPTY = new UniversalValue("");
	private final String value;

	public <T extends Enum<T>> UniversalValue(T value) {
		this.value = value.toString();
	}

	public UniversalValue(String value) {
		if (value == null || "null".equals(value)) {
			this.value = null;
		} else {
			this.value = value.trim();
		}
	}

	public String value() {
		return value;
	}

	@NewInstance
	public UniversalValue trim() {
		return new UniversalValue(value.trim());
	}

	@NewInstance
	public UniversalValue replaceAll(String v1, String v2) {
		return new UniversalValue(value.replaceAll(v1, v2));
	}

	@Override
	public String getId() {
		return value;
	}

	public boolean startWith(String s) {
		return value.startsWith(s);
	}

	public boolean is(Object o) {
		return value.equals(o);
	}

	public KImage asKImageFile() {
		if (value.equals("null")) {
			return null;
		}
		return new KImage(asFile());
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
			return StringUtil.safeSplit(value.trim(), sep);
		}
		if (value.isEmpty()) {
			return new String[]{};
		}
		return new String[]{value};
	}

	public UniversalValue[] safeSplitUV(String sep) {
		if (value == null) {
			return new UniversalValue[]{};
		}
		if (value.contains(sep)) {
			String[] v = safeSplit(sep);
			UniversalValue[] res = new UniversalValue[v.length];
			for (int i = 0; i < res.length; i++) {
				res[i] = new UniversalValue(v[i].trim());
			}
			return res;
		}
		if (value.isEmpty()) {
			return new UniversalValue[]{};
		}
		return new UniversalValue[]{this};
	}

	public <T extends Enum<T>> T of(Class<T> c) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		T[] values = c.getEnumConstants();
		for (T t : values) {
			if (t.toString().equals(value.trim().toUpperCase())) {
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

	public int asInt() {
		return Integer.parseInt(value.trim());
	}

	public long asLong() {
		return Long.parseLong(value.trim());
	}

	public float asFloat() {
		return Float.parseFloat(value.trim());
	}

	public boolean is(String v) {
		return value.equals(v);
	}

	public boolean isTrue() {
		return Boolean.parseBoolean(value.trim().toLowerCase());
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

	public File asFile() {
		return new File(value);
	}

	public SpriteSheet asSpriteSheetFileName() {
		return new SpriteSheet(value);
	}

	public FourDirection asFourDirection() {
		return FourDirection.valueOf(value);
	}

	public Sound asSoundID() {
		return SoundSystem.getInstance().of(value);
	}

	public FramePosition asFramePosition() {
		return FramePosition.parse(value);
	}

	public MasterGain asMasterGain() {
		return new MasterGain(asFloat());
	}

	public D2Idx asD2IdxCSV() {
		int x = safeSplitUV(",")[0].trim().asInt();
		int y = safeSplitUV(",")[1].trim().asInt();
		return new D2Idx(x, y);
	}

	public FrameTimeCounter asFrameTimeCounterCSV() {
		int[] v = asSafeIntArray(",");
		return new FrameTimeCounter(v);
	}

	public MapChipSet asMapChipSetFile() {
		return new MapChipSet(asFile());
	}

	public ScriptFileCall asScriptCall() {
		return new ScriptFileCall(value);
	}

	@Nullable
	public FlagSystem.FlagFile asFlag() {
		return FlagSystem.getInstance().get(value);
	}

	public Actor asActor() {
		if (value.endsWith(".npc.txt")) {
			return asNPCCSV();
		}
		if (value.endsWith(".flw.txt")) {
			return asFollower();
		}
		return new Actor(asFile());
	}

	public Follower asFollower() {
		return new Follower(asFile());
	}

	public NPC asNPCCSV() {
		UniversalValue[] v = safeSplitUV(",");
		return new NPC(v[0].asFile(), new D2Idx(v[1].asInt(), v[2].asInt()));
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
