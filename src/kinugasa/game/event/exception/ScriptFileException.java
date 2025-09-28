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

package kinugasa.game.event.exception;

/**
 * ScriptFileException.<br>
 * @vesion 1.0.0 - 2025/09/27_22:06:28<br>
 * @author Shinacho.<br>
 */
public class ScriptFileException extends ScriptException {

    /**
     * Creates a new instance of <code>ScriptFileException</code> without detail message.
     */
    public ScriptFileException() {
    }


    /**
     * Constructs an instance of <code>ScriptFileException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ScriptFileException(String msg) {
        super(msg);
    }

    public ScriptFileException(Exception ex){
        super(ex);
    }
}
