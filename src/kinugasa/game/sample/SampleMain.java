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
package kinugasa.game.sample;

import java.awt.Color;
import java.awt.geom.Point2D;
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
import kinugasa.graphics.ColorChanger;
import kinugasa.graphics.ColorTransitionModel;
import kinugasa.graphics.FadeCounter;
import kinugasa.graphics.KImage;
import kinugasa.object.BasicMoving;
import kinugasa.object.FourDirection;
import kinugasa.object.ImageSprite;
import kinugasa.object.KVector;
import kinugasa.object.movemodel.AngleChange;
import kinugasa.object.movemodel.CompositeMove;
import kinugasa.object.movemodel.SpeedChange;
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
	private TextLabelSprite tamaShurui;
	private int tamaMode = 0;
	private ColorChanger tamaCC;
	private ImageSprite jiki;
	private static final float SPEED = 6f;
	private List<ImageSprite> tama = new ArrayList<>();
	private FrameTimeCounter rensyaSokudo;

	private Sound bgm;
	//------------------------------------------

	@Override
	protected void startUp() {
		mw = new MessageWindow(12, 12, 480, 120, new SimpleMessageWindowModel().setNextIcon(""));
		mw.setText(Text.of("これはサンプルゲームです" + Text.getLineSep()
				+ "矢印キーで動きます" + Text.getLineSep()
				+ "スペースキーで弾を発射します" + Text.getLineSep()
				+ "Vキーで弾の種類を変えます"));
		mw.showAllNow();

		fpsLabel = new TextLabelSprite("", new SimpleTextLabelModel(FontModel.DEFAULT.clone().setColor(Color.CYAN)), 640, 12, 30, 30);
		tamaShurui = new TextLabelSprite("", new SimpleTextLabelModel(FontModel.DEFAULT), 640, 32, 30, 30);
		tamaShurui.setVisible(false);

		jiki = new ImageSprite(255, 255, 32, 32, new KImage(32, 32).fillBy(Color.YELLOW));

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

		Point2D.Float dir = new Point2D.Float(0f, 0f);
		if (is.isPressed(Keys.UP, InputType.CONTINUE)) {
			dir.y -= 1f;
		}
		if (is.isPressed(Keys.DOWN, InputType.CONTINUE)) {
			dir.y += 1f;
		}
		if (is.isPressed(Keys.LEFT, InputType.CONTINUE)) {
			dir.x -= 1f;
		}
		if (is.isPressed(Keys.RIGHT, InputType.CONTINUE)) {
			dir.x += 1f;
		}
		dir.x *= SPEED;
		dir.y *= SPEED;
		KVector kv = new KVector(dir);
		jiki.setVector(kv);
		if (getWindow().getVisibleBounds().contains(jiki.simulateMoveCenterLocation())) {
			jiki.move();
		}

		if (is.isPressed(Keys.V, InputType.SINGLE)) {
			tamaMode++;
			if (tamaMode >= 3) {
				tamaMode = 0;
			}
			tamaShurui.setText("弾種類：" + tamaMode);
			tamaShurui.setVisible(true);
			tamaCC = new ColorChanger(ColorTransitionModel.valueOf(255), ColorTransitionModel.valueOf(255), ColorTransitionModel.valueOf(255), new FadeCounter(255, -2));
		}
		if (tamaShurui.isVisible()) {
			tamaCC.update();
			Color c = tamaCC.createColor();
			if (c.getAlpha() <= 0) {
				tamaShurui.setVisible(false);
			} else {
				tamaShurui.getLabelModel().getFontConfig().setColor(tamaCC.createColor());
			}
		}

		//弾の処理
		if (is.isPressed(Keys.SPACE, InputType.CONTINUE)) {
			switch (tamaMode) {
				case 0 -> {
					if (rensyaSokudo.update() == TimeCounterState.ACTIVE) {
						ImageSprite t = new ImageSprite(jiki.getCenterX() - 3, jiki.getCenterY() - 3, 6, 6, new KImage(6, 6).fillBy(Color.ORANGE));
						t.setVector(new KVector(FourDirection.NORTH, 12f));
						tama.add(t);
					}
				}
				case 1 -> {
					if (rensyaSokudo.update() == TimeCounterState.ACTIVE) {
						{
							ImageSprite t = new ImageSprite(jiki.getCenterX() - 12, jiki.getCenterY() - 3, 6, 6, new KImage(6, 6).fillBy(Color.BLUE));
							t.setVector(new KVector(FourDirection.NORTH.getAngle() - 12, 12f));
							t.setMovingModel(new CompositeMove(BasicMoving.getInstance(), new AngleChange(-0.6f)));
							tama.add(t);
						}
						{
							ImageSprite t = new ImageSprite(jiki.getCenterX() - 3, jiki.getCenterY() - 3, 6, 6, new KImage(6, 6).fillBy(Color.BLUE));
							t.setVector(new KVector(FourDirection.NORTH, 12f));
							tama.add(t);
						}
						{
							ImageSprite t = new ImageSprite(jiki.getCenterX() + 6, jiki.getCenterY() - 3, 6, 6, new KImage(6, 6).fillBy(Color.BLUE));
							t.setVector(new KVector(FourDirection.NORTH.getAngle() + 12, 12f));
							t.setMovingModel(new CompositeMove(BasicMoving.getInstance(), new AngleChange(+0.6f)));
							tama.add(t);
						}
					}
				}
				case 2 -> {
					if (rensyaSokudo.update() == TimeCounterState.ACTIVE) {
						for (int i = 0;; i++) {
							float kakudo = i * 4f;
							if (kakudo > 360f) {
								break;
							}
							ImageSprite t = new ImageSprite(jiki.getCenterX() - 3, jiki.getCenterY() - 3, 6, 6, new KImage(6, 6).fillBy(Color.GREEN));
							t.setVector(new KVector(kakudo, 12f));
							t.setMovingModel(new CompositeMove(BasicMoving.getInstance(), new SpeedChange(-0.13f, 2f, 999f)));
							tama.add(t);
						}
					}
				}
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
		tamaShurui.draw(gc);

		tama.forEach(v -> v.draw(gc));
		jiki.draw(gc);
	}

}
