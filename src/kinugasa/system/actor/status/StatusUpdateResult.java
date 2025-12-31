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
package kinugasa.system.actor.status;

import kinugasa.game.I18NText;
import kinugasa.game.annotation.Nullable;
import kinugasa.script.ScriptResult;

/**
 * StatusUpdateResult.<br>
 *
 * @vesion 1.0.0 - 2025/10/03_20:12:16<br>
 * @author Shinacho.<br>
 */
public class StatusUpdateResult {

	@Nullable
	private StatusUpdateSAO sao;
	@Nullable
	private ScriptResult result;

	public StatusUpdateResult() {
	}

	public StatusUpdateResult(StatusUpdateSAO sao, ScriptResult result) {
		this.sao = sao;
		this.result = result;
		setCanExec(sao.isCanExec());
		setMsg(I18NText.of(msg));
	}

	public ScriptResult getResult() {
		return result;
	}

	public StatusUpdateSAO getSao() {
		return sao;
	}

	//RESULT----------------------------
	//阻止フラグ
	private boolean canExec = true;

	public StatusUpdateResult setCanExec(boolean canExec) {
		this.canExec = canExec;
		return this;
	}

	public boolean isCanExec() {
		return canExec;
	}

	//メッセージ
	private I18NText msg;

	public I18NText getMsg() {
		return msg;
	}

	public void setMsg(I18NText msg) {
		this.msg = msg;
	}

}
