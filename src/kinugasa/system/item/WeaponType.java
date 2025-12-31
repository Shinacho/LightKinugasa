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
package kinugasa.system.item;

import kinugasa.game.VisibleNameEnumInjector;

/**
 * WeaponType.<br>
 *
 * @vesion 1.0.0 - 2025/10/16_22:07:33<br>
 * @author Shinacho.<br>
 */
public enum WeaponType implements VisibleNameEnumInjector<WeaponType> {
	/**
	 * 小さい杖
	 */
	WAND,
	/**
	 * 中型の杖
	 */
	ROD,
	/**
	 * 大きい杖
	 */
	STAFF,
	/**
	 * ダガー
	 */
	DAGGER,
	/**
	 * ショートソード
	 */
	SHORT_SWORD,
	/**
	 * ロングソード
	 */
	LONG_SWORD,
	/**
	 * 刀
	 */
	KATANA,
	/**
	 * 突剣
	 */
	RAPIER,
	/**
	 * 大剣
	 */
	GREAT_SWORD,
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
	/**
	 * メイス
	 */
	MACE,
	/**
	 * 槍
	 */
	SPEAR,
	/**
	 * 斧
	 */
	AXE,
	/**
	 * フレイル
	 */
	FLAIL,
	/**
	 * 鞭
	 */
	WHIP,
	/**
	 * 棍棒
	 */
	CLUB,
	/**
	 * 弩
	 */
	CROSSBOW,
	/**
	 * 魔法銃
	 */
	MAGICAL_GUN,;

	public static boolean has(String name) {
		for (var v : values()) {
			if (v.toString().equals(name.toUpperCase())) {
				return true;
			}
		}
		return false;
	}
}
