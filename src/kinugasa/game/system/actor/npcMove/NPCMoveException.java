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

package kinugasa.game.system.actor.npcMove;

/**
 * NPCMoveException.<br>
 * @vesion 1.0.0 - 2025/09/23_21:29:41<br>
 * @author Shinacho.<br>
 */
public class NPCMoveException extends RuntimeException {

    /**
     * Creates a new instance of <code>NPCMoveException</code> without detail message.
     */
    public NPCMoveException() {
    }


    /**
     * Constructs an instance of <code>NPCMoveException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public NPCMoveException(String msg) {
        super(msg);
    }

    public NPCMoveException(Exception ex){
        super(ex);
    }
}
