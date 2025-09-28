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
package kinugasa.game;

import static java.awt.SystemColor.text;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.logging.Level;
import java.util.logging.Logger;
import kinugasa.game.input.InputState;
import kinugasa.system.GameSystem;
import kinugasa.ui.Dialog;
import kinugasa.ui.DialogIcon;
import kinugasa.ui.DialogOption;

/**
 * ゲームの進行を行うスレッドの実装です.
 * <br>
 * このクラスをゲーム作成者が使用することはありません。<br>
 * <br>
 *
 * @version 1.0.0 - 2013/01/14_12:52:13<br>
 * @version 1.0.1 - 2013/04/27_21:11<br>
 * @author Shinacho<br>
 */
public final class GameLoop implements Runnable {

	/**
	 * このスレッドが更新する対象のゲーム.
	 */
	private GameManager game;
	/**
	 * FPSおよび待機時間の管理.
	 */
	private GameTimeManager gtm;
	/**
	 * 起動するメインのスレッド.
	 */
	private Thread thread;
	/**
	 * 起動状態.
	 */
	private volatile boolean exec = false;
	private boolean updateIfNotActive;

	/**
	 * 新しいゲーム用スレッドを作成します. 作成しただけでは、起動されません.<br>
	 *
	 * @param game 対象のゲームインスタンス.<br>
	 * @param gtm FPSおよび待機時間を管理するGameTimeManager.<br>
	 */
	GameLoop(GameManager game, GameTimeManager gtm) {
		this.game = game;
		this.gtm = gtm;
		this.updateIfNotActive = false;
	}

	/**
	 * 新しいゲーム用スレッドを作成します. 作成しただけでは、起動されません.<br>
	 *
	 * @param game 対象のゲームインスタンス.<br>
	 * @param gtm FPSおよび待機時間を管理するGameTimeManager.<br>
	 */
	GameLoop(GameManager game, GameTimeManager gtm, boolean updateIfNotActive) {
		this.game = game;
		this.gtm = gtm;
		this.updateIfNotActive = updateIfNotActive;
	}

	/**
	 * メインループの実装です.
	 */
	@Override
	public void run() {
		try {
			long startTime;
			if (updateIfNotActive) {
				while (exec) {
					startTime = System.nanoTime();
					InputState is = InputState.getInstance();
					SystemUpdateInjector.update();
					game.update(gtm, is);
					game.getUpdateLogicInjectors().forEach(p -> p.update(gtm, is));
					game.repaint();
					gtm.sleep(startTime);
				}
			} else {
				while (exec) {
					if (game.getWindow().isActive() || GameSystem.isDebugMode()) {
						startTime = System.nanoTime();
						InputState is = InputState.getInstance();
						SystemUpdateInjector.update();
						game.update(gtm, is);
						game.getUpdateLogicInjectors().forEach(p -> p.update(gtm, is));
						game.repaint();
						game.clearEndedEffects();
						gtm.sleep(startTime);
					} else {
						startTime = System.nanoTime();
						try {//ダミー処理
							Thread.sleep(1000 / gtm.getIdealFPS());
						} catch (InterruptedException ex) {
							Logger.getLogger(GameLoop.class.getName()).log(Level.SEVERE, null, ex);
						}
						gtm.sleep(startTime);
					}
				}
			}
		} catch (Throwable ex) {
			//例外情報の収集と表示
			Toolkit.getDefaultToolkit().beep();
			String v = getExceptionMsg(ex);
			GameLog.print(v);
			if (Dialog.yesOrNo("Sorry, Kinugasa is clashed", DialogIcon.QUESTION, v + "\r\n-------------------\r\n" + "copy to clipboard?") == DialogOption.YES) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringSelection selection = new StringSelection(v);
				clipboard.setContents(selection, null);
			}
			LockUtil.deleteAllLockFile();
			System.exit(1);
		}
	}

	private String getExceptionMsg(Throwable t) {
		StringBuilder sb = new StringBuilder();
		sb.append("!> Sorry, the game was crashed").append("\n");
		sb.append("Please report a this capture or log file and it may be corrected\n");
		sb.append("--------------catch:").append(t.toString()).append("\n");
		//Cause
		Throwable tt = t;
		sb.append("--------------Cause:").append("\n");
		while (tt.getCause() != null) {
			sb.append(" ").append(tt.getCause()).append("\n");
			tt = tt.getCause();
		}
		//Suppressed
		sb.append("--------------Suppressed:").append("\n");
		for (var v : t.getSuppressed()) {
			sb.append(" ").append(v).append("\n");
		}
		//msg
		sb.append("--------------Msg:").append("\n");
		sb.append(t.getMessage()).append("\n");
		//StackTrace
		sb.append("--------------StackTrace:").append("\n");
		for (var v : t.getStackTrace()) {
			sb.append(" ").append(v).append("\n");
		}
		return sb.toString();
	}

	/**
	 * メインループを終了します.
	 */
	void end() {
		exec = false;
	}

	/**
	 * スレッドを起動し、再描画や更新を開始します.
	 *
	 * @throws IllegalStateException すでに起動している場合に投げられる.<br>
	 */
	void start() throws IllegalStateException {
		if (isStarted()) {
			throw new IllegalStateException("");
		}
		exec = true;
		thread = new Thread(this, game.getOption().getTitle());
		thread.start();
	}

	/**
	 * スレッドが起動しているかどうかを検査します.
	 *
	 * @return startが呼び出され、起動している場合はtrueを返します。<br>
	 */
	boolean isStarted() {
		return thread != null;
	}
}
