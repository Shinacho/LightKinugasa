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
package kinugasa.game.field4;

import kinugasa.game.GameManager;
import kinugasa.graphics.GraphicsContext;
import kinugasa.object.Effect;

/**
 * MapChangeEffect.<br>
 *
 * @vesion 1.0.0 - 2025/08/11_8:47:21<br>
 * @author Shinacho.<br>
 */
public abstract class MapChangeEffect extends Effect {

	public enum State {
		IN_EFFECT,
		WAIT,
		OUT_EFFECT,
		END,;
	}

	public MapChangeEffect() {
		super(GameManager.getInstance().getWindow().getWidth(), GameManager.getInstance().getWindow().getHeight());
	}

	@Override
	public abstract void draw(GraphicsContext g);

	public abstract void mapChangeEnd();

	public abstract State getState();

	protected abstract void setState(State s);

	@Override
	public boolean isEnded() {
		return getState() == State.END;
	}

	@Override
	public boolean isRunning() {
		return getState() != State.END;
	}

	@Override
	public void reset() {
		setState(MapChangeEffect.State.IN_EFFECT);
	}

}
