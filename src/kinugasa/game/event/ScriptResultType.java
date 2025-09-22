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
package kinugasa.game.event;

/**
 * ScriptResultType.<br>
 *
 * @vesion 1.0.0 - 2025/07/30_0:40:31<br>
 * @author Shinacho.<br>
 */
public enum ScriptResultType {

	/**
	 * 次のイベントを実行してください。
	 */
	CONTINUE,
	/**
	 * イベントはIF条件により起動されませんでした。
	 */
	MISFIRE,
	/**
	 * イベントは終了しました。
	 */
	END,
	/**
	 * イベントは自動進行されず、次のステータスに進めるにはnextStepを実行する必要があります。
	 */
	PAUSE,
	/**
	 *
	 */
	FADEOUT_ON,
	/**
	 *
	 */
	FADEOUT_OFF,
	/**
	 *
	 */
	CHANGE_MODE,
	/**
	 * イベントは自動進行し、次のステータスに変更されるまで待機する必要があります。
	 */
	WAIT,
	RESET_SCRIPT,
	;

}
