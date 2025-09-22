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
package kinugasa.game.sample;

import java.awt.Color;
import kinugasa.game.GameManager;
import kinugasa.game.GameOption;
import kinugasa.game.GameTimeManager;
import kinugasa.game.input.InputState;
import kinugasa.graphics.GraphicsContext;
import kinugasa.graphics.RenderingQuality;
import kinugasa.object.Sprite;

/**
 * Test2.<br>
 *
 * @vesion 1.0.0 - 2025/08/10_1:48:23<br>
 * @author Shinacho.<br>
 */
public class Test2 extends GameManager {

	public static void main(String[] args) {
		new Test2().gameStart();
	}

	private Test2() {
		super(new GameOption().setDrawSize(2f).setDebugMode(true).setUseLog(false).setUseLock(false).setRenderingQuality(RenderingQuality.QUALITY).setBackColor(Color.WHITE));
	}

	private Sprite s;

	@Override
	protected void startUp() {
	}

	@Override
	protected void dispose() {
	}

	@Override
	protected void update(GameTimeManager gtm, InputState is) {
	}

	@Override
	protected void draw(GraphicsContext g2) {
	}
}
