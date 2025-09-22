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
package kinugasa.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.List;
import javax.swing.ImageIcon;
import kinugasa.graphics.RenderingQuality;

/**
 * gameOptionValue.<br>
 *
 * @vesion 1.0.0 - 2025/07/20_11:22:03<br>
 * @author Shinacho.<br>
 */
public interface GameOptionValue {

	RenderingQuality getRenderingQuality();

	String[] getArgs();

	Color getBackColor();

	CloseEvent getCloseEvent();

	float getDrawSize();

	int getFps();

	I18NReader getI18nReader();

	ImageIcon getIcon();

	String getLogName();

	String getLogPath();

	String getTitle();

	Point getWindowLocation();

	Dimension getWindowSize();

	boolean isDebugMode();

	boolean isUpdateIfNotActive();

	boolean isUseGamePad();

	boolean isUseKeyboard();

	boolean isUseLock();

	boolean isUseLog();

	boolean isUseMouse();

}
