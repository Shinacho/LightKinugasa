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
package kinugasa.system.actor.status.attr;

import kinugasa.game.EnumUtilInjector;
import kinugasa.game.VisibleNameEnumInjector;

/**
 * AttributeKey.<br>
 *
 * @vesion 1.0.0 - 2025/10/05_16:09:54<br>
 * @author Shinacho.<br>
 */
public enum AttributeKey implements VisibleNameEnumInjector<AttributeKey>, EnumUtilInjector<AttributeKey> {
	//物理
	/**
	 * 切断
	 */
	CUTT,
	/**
	 * 刺突
	 */
	SPIK,
	/**
	 * 衝撃
	 */
	IMPC,
	//その他物理
	/**
	 * 生命
	 */
	VITA,
	/**
	 * 毒
	 */
	PISN,
	//基礎魔法
	/**
	 * 火
	 */
	FIRE,
	/**
	 * 水
	 */
	WATR,
	/**
	 * 風
	 */
	WIND,
	/**
	 * 地
	 */
	ERTH,
	/**
	 * 冷
	 */
	COLD,
	/**
	 * 雷
	 */
	THDR,
	//その他魔法
	/**
	 * 神秘
	 */
	MYST,
	/**
	 * 錬金
	 */
	ALCH,
	/**
	 * 精神
	 */
	MIND,
	/**
	 * 時空
	 */
	TIME,
}
