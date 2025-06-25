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
import java.awt.geom.Point2D;
import kinugasa.game.GameManager;
import kinugasa.game.GameOption;
import kinugasa.game.GameTimeManager;
import kinugasa.game.GraphicsContext;
import kinugasa.game.LockUtil;
import kinugasa.game.input.InputState;
import kinugasa.game.input.InputType;
import kinugasa.game.input.Keys;
import kinugasa.game.ui.FontModel;
import kinugasa.game.ui.SimpleTextLabelModel;
import kinugasa.game.ui.TextLabelSprite;
import kinugasa.graphics.KImage;
import kinugasa.object.FourDirection;
import kinugasa.object.ImageSprite;
import kinugasa.object.KVector;
import kinugasa.util.StringUtil;

/**
 * SampleMain.<br>
 *
 * @vesion 1.0.0 - 2025/06/24_19:29:36<br>
 * @author Shinacho.<br>
 */
public class SampleMain extends GameManager {

	public static void main(String[] args) {
		LockUtil.deleteAllLockFile();
		new SampleMain().gameStart();
	}

	private SampleMain() {
		super(GameOption.defaultOption().setTitle("Sample Game").setDrawSize(2f).setCenterOfScreen());
	}
	//------------------------------------------
	private TextLabelSprite label1;
	private TextLabelSprite fpsLabel;
	private ImageSprite jiki;
	private static final float SPEED = 6f;
	//------------------------------------------

	@Override
	protected void startUp() {
		label1 = new TextLabelSprite("矢印キーで動きます", new SimpleTextLabelModel(FontModel.DEFAULT), 12, 12, 999, 24);

		fpsLabel = new TextLabelSprite("", new SimpleTextLabelModel(FontModel.DEFAULT.clone().setColor(Color.CYAN)), 600, 12, 30, 30);

		KImage image = new KImage(32, 32);
		image = image.fillBy(Color.YELLOW);
		jiki = new ImageSprite(255, 255, 32, 32, image);
	}

	@Override
	protected void dispose() {
	}

	@Override
	protected void update(GameTimeManager gtm, InputState is) {
		fpsLabel.setText("FPS:" + gtm.getFPSStr(3));
		jiki.setSpeed(0f);
		if (is.isPressed(Keys.UP, InputType.CONTINUE)) {
			jiki.setVector(new KVector(FourDirection.NORTH.getAngle(), SPEED));
		} else if (is.isPressed(Keys.DOWN, InputType.CONTINUE)) {
			jiki.setVector(new KVector(FourDirection.SOUTH.getAngle(), SPEED));
		}
		if (is.isPressed(Keys.RIGHT, InputType.CONTINUE)) {
			jiki.setVector(new KVector(FourDirection.EAST.getAngle(), SPEED));
		} else if (is.isPressed(Keys.LEFT, InputType.CONTINUE)) {
			jiki.setVector(new KVector(FourDirection.WEST.getAngle(), SPEED));
		}
		if (getWindow().getVisibleBounds().contains(jiki.simulateMoveCenterLocation())) {
			jiki.move();
		}

	}

	@Override
	protected void draw(GraphicsContext gc) {
		label1.draw(gc);
		fpsLabel.draw(gc);

		jiki.draw(gc);
	}

}
