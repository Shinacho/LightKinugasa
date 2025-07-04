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


package kinugasa.game.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kinugasa.game.I18N;
import kinugasa.game.Nullable;
import kinugasa.game.system.GameSystemException;
import kinugasa.util.FrameTimeCounter;
import kinugasa.util.TimeCounter;
import kinugasa.object.ID;
import kinugasa.game.I18NText;
import kinugasa.graphics.KImage;
import kinugasa.util.TimeCounterState;

/**
 *
 * @vesion 1.0.0 - 2021/11/25_13:55:40<br>
 * @author Shinacho<br>
 */
public sealed class Text implements ID permits Choice {

	public static final Text EMPTY = new Text("");

	public static Text empty() {
		return EMPTY;
	}

	public static Text collect(List<Text> t) {
		StringBuilder sb = new StringBuilder();
		for (Text tt : t) {
			sb.append(tt.getText()).append(Text.lineSep);
		}
		return of(sb.substring(0, sb.length() - 1));
	}

	public static Text collect(Text... t) {
		StringBuilder sb = new StringBuilder();
		for (Text tt : t) {
			sb.append(tt.getText()).append(Text.lineSep);
		}
		return of(sb.substring(0, sb.length() - 1));
	}

	public static List<Text> split(Text t) {
		List<Text> r = new ArrayList<>();
		for (String v : t.getText().split(lineSep)) {
			r.add(Text.of(v));
		}
		return r;
	}

	private static Map<String, String> replaceMap = new HashMap<>();

	public static void setReplaceMap(Map<String, String> replaceMap) {
		Text.replaceMap = replaceMap;
	}

	public static Map<String, String> getReplaceMap() {
		return replaceMap;
	}

	//
	protected String id;
	protected String text;
	private TimeCounter tc = new FrameTimeCounter(0);
	private int visibleIdx = 0;
	private String nextId;
	private static int autoId = 0;
	private static String lineSep = "/";
	private KImage image;

	public static final Text LINE_SEP = new Text("LINE_SEP");

	static {
		LINE_SEP.text = lineSep;
	}

	public static void setLineSep(String lineSep) {
		Text.lineSep = lineSep;
	}

	public static String getLineSep() {
		return lineSep;
	}

	protected Text() {
	}

	private void setReplace() {
		for (var v : replaceMap.entrySet()) {
			text = text.replaceAll(v.getKey(), v.getValue());
		}
	}

	private Text(String textID) {
		this.id = textID;
		this.text = "";
	}

	//i18nd
	protected Text(String textID, String text, TimeCounter tc, int visibleIdx) {
		if (text == null) {
			throw new GameSystemException("text is null, use Text.empty");
		}
		this.id = textID;
		this.text = text;
		this.tc = tc;
		this.visibleIdx = visibleIdx;
		setReplace();
	}

	public static Text doI18N(String text) {
		if (text == null) {
			throw new GameSystemException("text is null, use Text.empty");
		}
		Text t = new Text();
		t.id = "TEXT_" + autoId++;
		t.text = I18N.get(text);
		t.setReplace();
		return t;
	}

	public static Text doI18N(String textID, String text) {
		if (text == null) {
			throw new GameSystemException("text is null, use Text.empty");
		}
		Text t = new Text();
		t.id = textID;
		t.text = I18N.get(text);
		t.setReplace();
		return t;
	}

	public static Text doI18N(String textID, String text, TimeCounter tc) {
		if (text == null) {
			throw new GameSystemException("text is null, use Text.empty");
		}
		Text t = new Text();
		t.id = textID;
		t.text = I18N.get(text);
		t.tc = tc;
		t.setReplace();
		return t;
	}

	public static Text doI18N(String text, TimeCounter tc) {
		if (text == null) {
			throw new GameSystemException("text is null, use Text.empty");
		}
		Text t = new Text();
		t.id = "TEXT_" + autoId++;
		t.text = I18N.get(text);
		t.tc = tc;
		t.setReplace();
		return t;
	}

	public static Text doI18N(String textID, String text, TimeCounter tc, int visibleIdx) {
		if (text == null) {
			throw new GameSystemException("text is null, use Text.empty");
		}
		Text t = new Text();
		t.id = textID;
		t.text = I18N.get(text);
		t.tc = tc;
		t.visibleIdx = visibleIdx;
		t.setReplace();
		return t;
	}

	public static Text doI18N(String text, TimeCounter tc, int visibleIdx) {
		if (text == null) {
			throw new GameSystemException("text is null, use Text.empty");
		}
		Text t = new Text();
		t.id = "TEXT_" + autoId++;
		t.text = I18N.get(text);
		t.tc = tc;
		t.visibleIdx = visibleIdx;
		t.setReplace();
		return t;
	}

	public static Text of(String text) {
		if (text == null) {
			throw new GameSystemException("text is null, use Text.empty");
		}
		Text t = new Text();
		t.id = "TEXT_" + autoId++;
		t.text = text;
		t.setReplace();
		return t;
	}

	public static Text of(I18NText text) {
		if (text == null) {
			throw new GameSystemException("text is null, use Text.empty");
		}
		if (text.getKey() == null || text.getKey().isEmpty() || text.i18nd() == null || text.i18nd().isEmpty()) {
			throw new GameSystemException("text is null, use Text.empty");
		}
		Text t = new Text();
		t.id = "TEXT_" + autoId++;
		t.text = text.toString();

		t.setReplace();
		return t;
	}

	public static Text of(String textID, String text) {
		if (text == null) {
			throw new GameSystemException("text is null, use Text.empty");
		}
		Text t = new Text();
		t.id = textID;
		t.text = text;
		t.setReplace();
		return t;
	}

	public static Text of(String textID, String text, TimeCounter tc) {
		if (text == null) {
			throw new GameSystemException("text is null, use Text.empty");
		}
		Text t = new Text();
		t.id = textID;
		t.text = text;
		t.tc = tc;
		t.setReplace();
		return t;
	}

	public static Text of(String text, TimeCounter tc) {
		if (text == null) {
			throw new GameSystemException("text is null, use Text.empty");
		}
		Text t = new Text();
		t.id = "TEXT_" + autoId++;
		t.text = text;
		t.tc = tc;
		t.setReplace();
		return t;
	}

	public static Text of(String textID, String text, TimeCounter tc, int visibleIdx) {
		if (text == null) {
			throw new GameSystemException("text is null, use Text.empty");
		}
		Text t = new Text();
		t.id = textID;
		t.text = text;
		t.tc = tc;
		t.visibleIdx = visibleIdx;
		t.setReplace();
		return t;
	}

	public static Text of(String text, TimeCounter tc, int visibleIdx) {
		if (text == null) {
			throw new GameSystemException("text is null, use Text.empty");
		}
		Text t = new Text();
		t.id = "TEXT_" + autoId++;
		t.text = text;
		t.tc = tc;
		t.visibleIdx = visibleIdx;
		t.setReplace();
		return t;
	}

	public boolean hasImage() {
		return image != null;
	}

	@Nullable
	public KImage getImage() {
		return image;
	}

	public void setImage(KImage image) {
		this.image = image;
	}

	public void setTextDirect(String t) {
		if (t == null) {
			throw new GameSystemException("text is null, use Text.empty");
		}
		this.text = t;
		for (Map.Entry<String, String> e : replaceMap.entrySet()) {
			this.text = this.text.replaceAll(e.getKey(), e.getValue());
		}
	}

	public void setTextByI18NID(String t) {
		if (t == null) {
			throw new GameSystemException("text is null, use Text.empty");
		}
		this.text = I18N.get(t);
		for (Map.Entry<String, String> e : replaceMap.entrySet()) {
			this.text = this.text.replaceAll(e.getKey(), e.getValue());
		}
	}

	public boolean isReaching() {
		if (text == null || text.isEmpty()) {
			return true;
		}
		if (visibleIdx >= text.length()) {
			return true;
		}
		if (tc.update() != TimeCounterState.ACTIVE) {
			return false;
		}
		visibleIdx++;
		return visibleIdx >= text.length();
	}

	@Override
	public String getId() {
		return id;
	}

	public final String getText() {
		if (text == null || text.isEmpty()) {
			return "";
		}
		return text;
	}

	public String getVisibleText() {
		if (text == null || text.isEmpty()) {
			return "";
		}
		return visibleIdx >= text.length() ? text : text.substring(0, visibleIdx);
	}

	public TimeCounter getTc() {
		return tc;
	}

	public int getVisibleIdx() {
		return visibleIdx;
	}

	public void allText() {
		if (text != null) {
			visibleIdx = text.length();
		}
	}

	public boolean isAllVisible() {
		if (text == null || text.isEmpty()) {
			return true;
		}
		return visibleIdx >= text.length();
	}

	public void setTc(FrameTimeCounter tc) {
		this.tc = tc;
	}

	public void setVisibleIdx(int visibleIdx) {
		this.visibleIdx = visibleIdx;
	}

	public String getNextId() {
		return nextId;
	}

	public boolean hasNext() {
		return nextId != null;
	}

	public void setNextId(String nextId) {
		this.nextId = nextId;
	}

	@Override
	public String toString() {
		return "Text{" + "name=" + id + ", nextId=" + nextId + '}';
	}

	public void reset() {
		tc.reset();
		visibleIdx = 0;
	}

}
