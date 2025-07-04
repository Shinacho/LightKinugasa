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


package kinugasa.game.input;

/**
 *
 * @vesion 1.0.0 - 2021/11/22_16:34:55<br>
 * @author Shinacho<br>
 */
public class GamePadTriggeres extends InputDeviceState {

	public final GamePadTrigger RIGHT;
	public final GamePadTrigger LEFT;

	public GamePadTriggeres(GamePadTrigger LEFT, GamePadTrigger RIGHT) {
		this.LEFT = LEFT;
		this.RIGHT = RIGHT;
	}

	@Override
	public boolean isAnyInput() {
		return RIGHT.isAnyInput() || LEFT.isAnyInput();
	}

	@Override
	public boolean isAnyButtonInput() {
		return RIGHT.isAnyInput() || LEFT.isAnyInput();
	}

	@Override
	public boolean isEmptyInput() {
		return !(RIGHT.isAnyInput() && LEFT.isAnyInput());
	}

	@Override
	public String toString() {
		return "GamePadTriggeres{" + "RIGHT=" + RIGHT + ", LEFT=" + LEFT + '}';
	}

}
