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
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import kinugasa.game.GameManager;
import kinugasa.game.GameOption;
import kinugasa.game.GameTimeManager;
import kinugasa.game.GraphicsContext;
import kinugasa.game.LockUtil;
import kinugasa.game.input.InputState;
import kinugasa.game.input.InputType;
import kinugasa.game.input.Keys;
import kinugasa.game.ui.FontModel;
import kinugasa.game.ui.MessageWindow;
import kinugasa.game.ui.SimpleMessageWindowModel;
import kinugasa.game.ui.SimpleTextLabelModel;
import kinugasa.game.ui.Text;
import kinugasa.game.ui.TextLabelSprite;
import kinugasa.graphics.KImage;
import kinugasa.object.FourDirection;
import kinugasa.object.ImageSprite;
import kinugasa.object.KVector;
import kinugasa.resource.sound.FramePosition;
import kinugasa.resource.sound.LoopPoint;
import kinugasa.resource.sound.Sound;
import kinugasa.util.FrameTimeCounter;
import kinugasa.util.TimeCounterState;

/**
 * SampleMain.<br>
 * これはサンプルゲームの実装を示すものです。<br>
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
	private MessageWindow mw;
	private TextLabelSprite fpsLabel;
	private ImageSprite jiki;
	private static final float SPEED = 6f;
	private List<ImageSprite> tama = new ArrayList<>();
	private FrameTimeCounter rensyaSokudo;

	private Sound bgm;
	//------------------------------------------

	@Override
	protected void startUp() {
		mw = new MessageWindow(12, 12, 480, 120, new SimpleMessageWindowModel().setNextIcon(""));
		mw.setText(Text.of("これはサンプルゲームです" + Text.getLineSep() + "矢印キーで動きます" + Text.getLineSep() + "スペースキーで弾を発射します"));
		mw.showAllNow();

		fpsLabel = new TextLabelSprite("", new SimpleTextLabelModel(FontModel.DEFAULT.clone().setColor(Color.CYAN)), 600, 12, 30, 30);

		KImage image = new KImage(32, 32);
		image = image.fillBy(Color.YELLOW);
		jiki = new ImageSprite(255, 255, 32, 32, image);

		bgm = new Sound(Sound.Type.BGM, "sampleResource/sampleMusic.wav").setLoopPoint(LoopPoint.from(FramePosition.eof()).to(FramePosition.start())).load().play();

		rensyaSokudo = new FrameTimeCounter(4);
	}

	@Override
	protected void dispose() {
		bgm.stop().dispose();
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

		//弾の処理
		if (is.isPressed(Keys.SPACE, InputType.CONTINUE)) {
			if (rensyaSokudo.update() == TimeCounterState.ACTIVE) {
				KImage image = new KImage(6, 6);
				image = image.fillBy(Color.ORANGE);
				ImageSprite t = new ImageSprite(jiki.getCenterX() - 3, jiki.getCenterY() - 3, 6, 6, image);
				t.setVector(new KVector(FourDirection.NORTH.getAngle(), 12f));
				tama.add(t);
			}
		}

		List<ImageSprite> remove = new ArrayList<>();
		for (var v : tama) {
			v.move();
			if (!getWindow().getVisibleBounds().contains(v.getCenter())) {
				remove.add(v);
			}
		}
		tama.removeAll(remove);

	}

	@Override
	protected void draw(GraphicsContext gc) {
		mw.draw(gc);
		fpsLabel.draw(gc);

		tama.forEach(v -> v.draw(gc));
		jiki.draw(gc);
	}

}
