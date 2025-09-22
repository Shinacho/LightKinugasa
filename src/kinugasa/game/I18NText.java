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

import java.util.List;
import kinugasa.game.annotation.Immutable;
import kinugasa.game.annotation.Virtual;
import kinugasa.game.annotation.NewInstance;
import java.util.Objects;
import kinugasa.game.ui.Choice;
import kinugasa.game.ui.Text;
import kinugasa.resource.ID;

/**
 *
 * @vesion 1.0.0 - 2024/04/28_1:27:07<br>
 * @author Shinacho<br>
 */
@Immutable
public class I18NText implements ID {

	public static final I18NText EMPTY = of("");

	private final String key;
	private String value;

	public static I18NText of(String msg) {
		return new I18NText(msg) {
			@Override
			public String i18nd() {
				return msg;
			}

			@Override
			public I18NText set(Object... o) {
				throw new UnsupportedOperationException("set has already been executed.");
			}

		};
	}

	public I18NText(String key) {
		this.key = key;
	}

	public I18NText(Enum<?> e) {
		this.key = e.toString();
	}

	public final String getKey() {
		return key;
	}

	public final boolean isEmpty() {
		return key == null || key.isEmpty();
	}

	@Override
	public final String getId() {
		return key;
	}

	@Override
	public final String toString() {
		return i18nd();
	}

	@NewInstance
	public final Text toText() {
		return Text.of(this);
	}

	@Virtual
	public String i18nd() {
		if (value == null) {
			value = I18N.get(getKey());
		}
		return value;
	}

	@Virtual
	@NewInstance
	public I18NText set(Object... o) {
		final String s = I18N.get(getKey(), o);
		return new I18NText(getKey()) {
			@Override
			public String i18nd() {
				return s;
			}

			@Override
			public I18NText set(Object... o) {
				throw new UnsupportedOperationException("set has already been executed.");
			}

		};
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 71 * hash + Objects.hashCode(this.i18nd());
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj.getClass() == String.class) {
			Objects.equals(this.i18nd(), obj.toString());
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final I18NText other = (I18NText) obj;
		return Objects.equals(this.i18nd(), other.i18nd());
	}

}
