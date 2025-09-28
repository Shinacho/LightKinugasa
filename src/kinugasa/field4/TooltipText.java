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

import java.awt.Color;
import kinugasa.game.GameManager;
import kinugasa.game.I18NText;
import kinugasa.ui.FontModel;
import kinugasa.ui.SimpleTextLabelModel;
import kinugasa.ui.TextLabelSprite;
import kinugasa.game.GraphicsContext;
import kinugasa.object.Drawable;

/**
 * TooltipTextは「調べる」「入る」などのテキストです。.<br>
 *
 * @vesion 1.0.0 - 2025/08/13_19:10:59<br>
 * @author Shinacho.<br>
 */
public class TooltipText implements Drawable {

	TooltipText() {
		sprite = new TextLabelSprite("", new SimpleTextLabelModel(FontModel.DEFAULT.clone()), 0, 0, 1, 1);
		shadow = sprite.clone();
	}

	private TextLabelSprite sprite;
	private TextLabelSprite shadow;

	void setText(I18NText t) {
		if (getText().equals(t.toString())) {
			return;
		}
		sprite.setText(t.toString());
		sprite.trim();

		//座標更新
		float x = GameManager.getInstance().getOption().getWindowSize().width / 2;
		float y = GameManager.getInstance().getOption().getWindowSize().height / 2;

		x -= sprite.getWidth() / 2;
		y -= sprite.getHeight() * 6;
		sprite.setLocation(x, y);

		shadow = sprite.clone();
		shadow.getLabelModel().getFontConfig().setColor(Color.BLACK);
		shadow.addLocation(1, 1);

	}

	String getText() {
		return sprite.getText();
	}

	@Override
	public void draw(GraphicsContext g) {
		shadow.draw(g);
		sprite.draw(g);
	}

}
