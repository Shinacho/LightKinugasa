/*
 * Copyright (C) 2024 Shinacho
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
package kinugasa.graphics;

/**
 * グラフィック例外は、主に画像の異常な状態を表すための例外です。
 *
 * @vesion 1.0.0 - 2024/08/24_20:03:05<br>
 * @author Shinacho<br>
 */
public class GraphicsException extends RuntimeException {

	/**
	 * Creates a new instance of <code>GraphicsException</code> without detail
	 * message.
	 */
	public GraphicsException() {
	}

	public GraphicsException(Throwable cs) {
		super(cs);
	}

	/**
	 * Constructs an instance of <code>GraphicsException</code> with the
	 * specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public GraphicsException(String msg) {
		super(msg);
	}
}
