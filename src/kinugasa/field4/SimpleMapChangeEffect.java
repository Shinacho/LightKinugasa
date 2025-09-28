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

import java.awt.Color;
import kinugasa.game.GraphicsContext;
import kinugasa.object.FadeEffect;

/**
 * SimpleMapChangeEffect.<br>
 *
 * @vesion 1.0.0 - 2025/08/11_8:48:51<br>
 * @author Shinacho.<br>
 */
public class SimpleMapChangeEffect extends MapChangeEffect {

	private static final int SPEED = 12;

	private FadeEffect in;
	private FadeEffect out;
	private State state = State.IN_EFFECT;

	public SimpleMapChangeEffect() {
		super();
		in = FadeEffect.alphaFade(getWidth(), getHeight(), Color.BLACK, SPEED);
		out = FadeEffect.alphaFade(getWidth(), getHeight(), Color.BLACK, -SPEED);
	}

	@Override
	public void mapChangeEnd() {
		state = State.OUT_EFFECT;
	}

	@Override
	public void draw(GraphicsContext g) {
		switch (state) {
			case IN_EFFECT -> {
				in.draw(g);
				if (in.isEnded()) {
					state = State.WAIT;
				}
			}
			case WAIT -> {
				//wait
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, (int) getWidth(), (int) getHeight());
			}
			case OUT_EFFECT -> {
				out.draw(g);
				if (out.isEnded()) {
					state = State.END;
				}
			}
			case END -> {
				//ended
			}
		}
	}

	@Override
	public State getState() {
		return state;
	}

	@Override
	protected void setState(State s) {
		this.state = s;
	}

}
