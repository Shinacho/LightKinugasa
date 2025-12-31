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
package kinugasa.system.action;

import kinugasa.game.VisibleNameEnumInjector;

/**
 * MagicType.<br>
 *
 * @vesion 1.0.0 - 2025/10/19_23:20:45<br>
 * @author Shinacho.<br>
 */
public enum MagicType implements VisibleNameEnumInjector<MagicType> {
	//---------------------------------魔法武器-----------------------------------
	/**
	 * 小さい杖
	 */
	WAND,
	/**
	 * 中型の杖
	 */
	RPD,
	/**
	 * 大型の杖
	 */
	STAFF,
	/**
	 * 魔法剣
	 */
	MAGICAL_SWORD,
	/**
	 * 魔法弓
	 */
	MAGICAL_BOW,
	/**
	 * 魔法籠手
	 */
	MAGICAL_GAUNTLET,
	//-------------------------------------ATTR----------------------------------------
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
	//------------------------------その他の種別-------------------------------------
	/**
	 * 聖
	 */
	HOLY,
	/**
	 * 悪
	 */
	EVIL,
	/**
	 * 冥
	 */
	HELL,
	/**
	 * 変性
	 */
	ALTR,
	/**
	 * 召喚
	 */
	CONJ,
	/**
	 * エンチャント
	 */
	ENCH,
	/**
	 * 破壊
	 */
	DEST,
	/**
	 * 呪詛
	 */
	CURS,
	/**
	 * 謎術
	 */
	STRG,
	/**
	 * 念動
	 */
	TELK,
	/**
	 * 生活
	 */
	LIVI,
	/**
	 * 産業
	 */
	INDS,
	/**
	 * 転送
	 */
	WARP,
	/**
	 * 芸術
	 */
	APPE,
	/**
	 * 研究
	 */
	RESE,
	/**
	 * 空中
	 */
	FLYI,
	/**
	 * 樹木
	 */
	WOOD,
	/**
	 * 鮮血
	 */
	BLOD,
	/**
	 * 冒涜
	 */
	BLAS,
	/**
	 * 踏み台魔法
	 */
	STRT,
	/**
	 * 情報通信
	 */
	INFO,
	/**
	 * 神術
	 */
	DIVI,;

	public static boolean has(String name) {
		for (var v : values()) {
			if (v.toString().equals(name.toUpperCase())) {
				return true;
			}
		}
		return false;
	}
}
