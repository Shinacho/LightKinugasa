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

package kinugasa.script.exception;

/**
 * ScriptNoSuchClassException.<br>
 * @vesion 1.0.0 - 2025/09/27_23:22:50<br>
 * @author Shinacho.<br>
 */
public class ScriptNoSuchClassException extends ScriptException {

    /**
     * Creates a new instance of <code>ScriptNoSuchClassException</code> without detail message.
     */
    public ScriptNoSuchClassException() {
    }


    /**
     * Constructs an instance of <code>ScriptNoSuchClassException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ScriptNoSuchClassException(String msg) {
        super(msg);
    }

    public ScriptNoSuchClassException(Exception ex){
        super(ex);
    }
}
