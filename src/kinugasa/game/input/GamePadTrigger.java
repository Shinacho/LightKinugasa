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
 * @vesion 1.0.0 - 2021/11/22_16:26:06<br>
 * @author Shinacho<br>
 */
public class GamePadTrigger extends InputDeviceState {

	public final float value;

	GamePadTrigger(float value) {
		this.value = value;
	}

	@Override
	public boolean isAnyInput() {
		return value >= GamePadConnection.TRIGGER_MAX / 2;
	}

	@Override
	public boolean isAnyButtonInput() {
		return value >= GamePadConnection.TRIGGER_MAX / 2;
	}

	@Override
	public boolean isEmptyInput() {
		return value == GamePadConnection.TRIGGER_MIN;
	}

	@Override
	public String toString() {
		return "GamePadTrigger{" + "value=" + value + '}';
	}

	
}
