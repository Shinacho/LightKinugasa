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


package kinugasa.game.field4;

import kinugasa.game.GameOption;
import kinugasa.game.GraphicsContext;
import kinugasa.game.I18N;
import kinugasa.game.ui.FontModel;
import kinugasa.game.ui.SimpleTextLabelModel;
import kinugasa.game.ui.TextLabelSprite;

/**
 *
 * @vesion 1.0.0 - 2022/11/11_12:27:18<br>
 * @author Shinacho<br>
 */
public class SimpleTooltipModel extends TooltipModel {

	private TextLabelSprite label;

	public SimpleTooltipModel() {
		label = new TextLabelSprite("", new SimpleTextLabelModel(FontModel.DEFAULT), 0, 0, 0, 0);
		setVisible(true);
	}

	@Override
	public void drawTooltip(FieldMap fm, GraphicsContext g) {
		if (!visible) {
			label.setVisible(false);
			return;
		}
		String s = FieldMap.getEnterOperation();
		FieldMapTile t = fm.getCurrentTile();
		Mode newMode = Mode.NONE;
		if (!FieldMap.getPlayerCharacter().isEmpty() && fm.canTalk()) {
			newMode = Mode.TALK;
		}
		if (t.getNode() != null) {
			if (t.getNode().getMode() == Node.Mode.INOUT) {
				newMode = Mode.NODE;
			}
		}
		if (fm.getMessageWindow() != null && fm.getMessageWindow().isVisible()) {
			newMode = Mode.NONE;
		}
		if (mode != newMode) {
			mode = newMode;
			switch (newMode) {
				case NODE -> {
					if (t.getNode() != null) {
						if (t.getNode().getMode() != Node.Mode.OUT) {
							s += t.getNode().getTooltipI18Nd();
							label.setText(s);
							label.setVisible(true);
						} else {
							label.setVisible(false);
						}
					} else {
						label.setVisible(false);
					}
				}
				case NONE ->
					label.setVisible(false);
				case TALK -> {
					if (fm.canTalk()) {
						s += I18N.get("話す");//仮
						label.setText(s);
						label.setVisible(true);
					} else {
						label.setVisible(false);
					}
				}
				case SEARCH -> {
					s += I18N.get("調べる");//仮
					label.setText(s);
					label.setVisible(true);
				}
			}
		}
		if (label.isVisible()) {
			FontModel f = label.getLabelModel().getFontConfig();
			float x = GameOption.getInstance().getWindowSize().width / GameOption.getInstance().getDrawSize() / 2 - (f.getFont().getSize2D() * label.getText().length() / 2);
			float y = GameOption.getInstance().getWindowSize().height / GameOption.getInstance().getDrawSize() / 2 - GameOption.getInstance().getWindowSize().height / GameOption.getInstance().getDrawSize() / 4;
			label.setLocation(x, y);
			g.draw(label);
		}
	}

}
