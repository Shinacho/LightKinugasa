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
import java.util.List;
import kinugasa.game.I18NText;
import kinugasa.graphics.GraphicsContext;
import kinugasa.game.annotation.LoopCall;
import kinugasa.object.Sprite;
import kinugasa.util.StringUtil;

/**
 * メッセージウインドウです。 <br>
 * 注意！：必ずsetTextを通してテキストを設定すること。そうしないとサブクラスで変になる可能性アリ
 *
 * @vesion 1.0.0 - 2021/11/25_15:05:14<br>
 * @author Shinacho<br>
 */
public class MessageWindow extends Sprite {

	private MessageWindowModel model;
	private Text text;

	public MessageWindow(float x, float y, float w, float h, MessageWindowModel model) {
		this(x, y, w, h, model, Text.EMPTY);
	}

	public MessageWindow(float x, float y, float w, float h) {
		this(x, y, w, h, new SimpleMessageWindowModel(), Text.EMPTY);
	}

	public MessageWindow(float x, float y, float w, float h, Text text) {
		this(x, y, w, h, new SimpleMessageWindowModel(), text);
	}

	public MessageWindow(float x, float y, float w, float h, MessageWindowModel model, Text text) {
		super(x, y, w, h);
		this.model = model;
		setText(text);
	}

	public Text getText() {
		return text;
	}

	@LoopCall
	@Override
	public MessageWindow update() {
		text.isReaching();

		return this;
	}

	public void setModel(MessageWindowModel model) {
		this.model = model;
	}

	public MessageWindowModel getModel() {
		return model;
	}

	public void showAllNow() {
		text.allText();
	}

	public void setText(Text text) {
		this.text = text;
		select = 0;
		if (this.text.getScript() != null) {
			this.text.getScript().exec();
		}
	}

	public void setTextDirect(String text) {
		setText(Text.of(text));
	}

	public void clearText() {
		setText(Text.empty());
	}

	public boolean isAllVisible() {
		return text.isAllVisible();
	}

	public List<String> getVisibleText() {
		String[] r = StringUtil.safeSplit(text.getText(), Text.getLineSep());

		int visible = text.getVisibleIdx();

		List<String> res = new ArrayList<>();
		for (var v : r) {
			if (v.length() < visible) {
				res.add(v);
				visible -= v.length();
			} else {
				res.add(v.substring(0, visible));
				break;
			}
		}
		return res;

	}

	@Override
	public void draw(GraphicsContext g) {
		model.draw(g, this);
	}

	public void reset() {
		text.reset();
		this.select = 0;
	}

	public boolean hasNext() {
		return text.hasNext();
	}

	public void next(Text t) {
		setText(t);
		getText().reset();
		select = 0;
	}

	public void next() {
		String nid = getText().getNextId();
		Text t = new I18NText(nid).toText();
		setText(t);
		getText().reset();
		select = 0;
	}

	public void choicesNext() {
		String nid = getSelectedChoiceOption().getNextId();
		Text t = new I18NText(nid).toText();
		setText(t);
		getText().reset();
	}

	public boolean isChoice() {
		return (getText() instanceof Choice);
	}

	public Choice getChoice() {
		return (Choice) getText();
	}

	private int select = 0;

	public int getSelect() {
		return select;
	}

	public void close() {
		setVisible(false);
	}

	public void setSelect(int select) {
		this.select = select;
	}

	public void nextSelect() {
		if (!isChoice()) {
			return;
		}
		select++;
		if (getChoice().getOptions().size() <= select) {
			select = 0;
		}
	}

	public void prevSelect() {
		if (!isChoice()) {
			return;
		}
		select--;
		if (0 > select) {
			select = getChoice().getOptions().size() - 1;
		}
	}

	public Text getSelectedChoiceOption() {
		return getChoice().getOptions().get(select);
	}

	@Override
	public String toString() {
		return "MessageWindow{" + "text=" + text + ", select=" + select + '}';
	}

}
