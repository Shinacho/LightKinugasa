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

import kinugasa.game.I18NText;
import kinugasa.ui.FontModel;
import kinugasa.ui.SimpleTextLabelModel;
import kinugasa.ui.TextLabelSprite;
import kinugasa.resource.ID;

/**
 * MiniMapLabel.<br>
 *
 * @vesion 1.0.0 - 2025/07/22_22:20:27<br>
 * @author Shinacho.<br>
 */
public class MiniMapLabel implements ID {

	private D2Idx idx;
	private I18NText text;

	public MiniMapLabel(D2Idx idx, I18NText text) {
		this.idx = idx;
		this.text = text;
	}

	@Override
	public String getId() {
		return idx.getId();
	}

	public D2Idx getIdx() {
		return idx;
	}

	public I18NText getText() {
		return text;
	}

	public int getX() {
		return idx.x;
	}

	public int getY() {
		return idx.y;
	}

	public TextLabelSprite getSprite() {
		return new TextLabelSprite(text.toString(), new SimpleTextLabelModel(FontModel.DEFAULT.clone()), 0, 0).trimWSize().trimHSize();
	}

	@Override
	public String toString() {
		return "MiniMapLabel{" + "idx=" + idx + ", text=" + text + '}';
	}

}
