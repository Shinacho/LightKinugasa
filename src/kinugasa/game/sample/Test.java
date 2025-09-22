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

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.File;
import kinugasa.game.GameManager;
import kinugasa.game.GameOption;
import kinugasa.game.GameTimeManager;
import kinugasa.game.I18NText;
import kinugasa.game.IniI18NReader;
import kinugasa.game.event.ScriptSystem;
import kinugasa.game.field4.D2Idx;
import kinugasa.game.field4.FieldMapCamera;
import kinugasa.game.field4.FieldMapSystem;
import kinugasa.game.input.GamePadButton;
import kinugasa.game.input.InputState;
import kinugasa.game.input.InputType;
import kinugasa.game.input.Keys;
import kinugasa.game.system.GameSystem;
import kinugasa.game.system.actor.Actor;
import kinugasa.game.system.actor.Follower;
import kinugasa.game.system.actor.StandardFieldMapNPCMoveModel;
import kinugasa.game.ui.Dialog;
import kinugasa.game.ui.MessageWindow;
import kinugasa.graphics.GraphicsContext;
import kinugasa.resource.sound.SoundSystem;
import kinugasa.util.MathUtil;

/**
 * Test.<br>
 *
 * @vesion 1.0.0 - 2025/07/19_14:06:34<br>
 * @author Shinacho.<br>
 */
public class Test extends GameManager {

	public static void main(String[] args) {
		new Test().gameStart();
	}

	public Test() {
		super(new GameOption("My Game")
				.setUseLock(false)
				.setUseLog(false)
				.setWindowSize(new Dimension(720, 480))
				.setDrawSize(2f)
				.setI18nReader(new IniI18NReader(new File("data/i18n.ini")))
				.setUseGamePad(true));
	}

	private ScriptSystem ss;
	private FieldMapSystem fms;

	@Override
	protected void startUp() {
		//デバッグ用
		FieldMapCamera.setIgnoreVhicle(true);
		GameSystem.setDebugMode(true);
		StandardFieldMapNPCMoveModel.DEBUG_MOVE_CHANCE = 0.9f;

		SoundSystem.getInstance().init(new File("data/sound/"));
		ScriptSystem.getInstance().init(new File("data/"));

		ss = ScriptSystem.getInstance();

		fms = FieldMapSystem.getInstance();
		fms.init(new File("data/field/"));
		fms.setInitialMap("M001");
		fms.setLocation(new D2Idx(32, 35));

		//テストキャラ登録
		{
			Actor a = new Actor(new File("data/pc/PC001.pc.txt")).load();
			GameSystem.getInstance().getPcList().add(a);
		}
		{
			Follower a = new Follower(new File("data/pc/PC002.pc.txt")).load();
			GameSystem.getInstance().getPcList().add(a);
		}
		{
			Follower a = new Follower(new File("data/pc/PC003.pc.txt")).load();
			GameSystem.getInstance().getPcList().add(a);
		}
		{
			Follower a = new Follower(new File("data/pc/PC004.pc.txt")).load();
			GameSystem.getInstance().getPcList().add(a);
		}
		fms.resetFollowerLocation();
	}

	@Override
	protected void dispose() {
	}

	@Override
	protected void update(GameTimeManager gtm, InputState is) {
		SoundSystem.getInstance().update();
		
		fms.update();
		ss.update();

		if (fms.isChangeMap()) {
			return;
		}

		if (ss.isTalking()) {

			MessageWindow mw = ss.getMessageWindow();
			if (mw.isChoice()) {
				//カーソル移動と決定ができる
				if (is.isPressed(Keys.DOWN, InputType.SINGLE)) {
					mw.nextSelect();
				}
				if (is.isPressed(Keys.UP, InputType.SINGLE)) {
					mw.prevSelect();
				}
				if (is.isPressed(Keys.SPACE, InputType.SINGLE)) {
					if (mw.isAllVisible()) {
						if (ss.hasNext()) {
							ss.nextStep();
						} else {
							ss.closeMessageWindow();
							ss.end();
						}
					} else {
						mw.showAllNow();
					}
				}
			} else {
				if (is.isPressed(Keys.SPACE, InputType.SINGLE)) {
					if (mw.isAllVisible()) {
						if (ss.hasNext()) {
							ss.nextStep();
						} else {
							ss.closeMessageWindow();
							ss.end();
						}
					} else {
						ss.showAllNow();
					}
				}
			}
			return;
		}

		if (is.isPressed(Keys.SPACE, InputType.SINGLE)) {
			//話す
			if (fms.talk()) {
				return;
			}

			//入る
			if (fms.hasNode()) {
				fms.changeMapStart();
				return;
			}
		}

		if (is.isPressed(Keys.M, InputType.SINGLE)) {
			Dialog.image(new I18NText("MAP_OF_X").set(fms.getCurrent().getVisibleName()), fms.createMiniMap(512, 512, true, true));
		}
		if (is.isPressed(Keys.D, InputType.SINGLE)) {
			fms.getCurrent().setDebugMode(!fms.getCurrent().isDebugMode());
		}

		Point2D.Float p = new Point2D.Float();
		if (is.isPressed(GamePadButton.POV_UP, Keys.UP, InputType.CONTINUE)) {
			p.y = -1f;
			if (is.isPressed(GamePadButton.POV_RIGHT, Keys.RIGHT, InputType.CONTINUE)) {
				p.x = 1f;
			}
			if (is.isPressed(GamePadButton.POV_LEFT, Keys.LEFT, InputType.CONTINUE)) {
				p.x = -1f;
			}
		}
		if (is.isPressed(GamePadButton.POV_DOWN, Keys.DOWN, InputType.CONTINUE)) {
			p.y = 1f;
			if (is.isPressed(GamePadButton.POV_RIGHT, Keys.RIGHT, InputType.CONTINUE)) {
				p.x = 1f;
			}
			if (is.isPressed(GamePadButton.POV_LEFT, Keys.LEFT, InputType.CONTINUE)) {
				p.x = -1f;
			}
		}
		if (is.isPressed(GamePadButton.POV_LEFT, Keys.LEFT, InputType.CONTINUE)) {
			p.x = -1f;
			if (is.isPressed(GamePadButton.POV_UP, Keys.UP, InputType.CONTINUE)) {
				p.y = -1f;
			}
			if (is.isPressed(GamePadButton.POV_DOWN, Keys.DOWN, InputType.CONTINUE)) {
				p.y = 1f;
			}
		}
		if (is.isPressed(GamePadButton.POV_RIGHT, Keys.RIGHT, InputType.CONTINUE)) {
			p.x = 1f;
			if (is.isPressed(GamePadButton.POV_UP, Keys.UP, InputType.CONTINUE)) {
				p.y = -1f;
			}
			if (is.isPressed(GamePadButton.POV_DOWN, Keys.DOWN, InputType.CONTINUE)) {
				p.y = 1f;
			}
		}
		//斜め移動時のgame pad との速度差の補正処理
		p = MathUtil.clipOnCircle(p);

		if (is.getGamePadState().sticks.LEFT.isAnyInput()) {
			p = is.getGamePadState().sticks.LEFT.getLocation();
		}

		//速度合成
		float speed = fms.getCurrentVehicle().getSpeed();
		p.x *= speed;
		p.y *= speed;

		fms.move(p);
	}

	@Override
	protected void draw(GraphicsContext g2) {
		fms.draw(g2);
		ss.draw(g2);
	}

}
