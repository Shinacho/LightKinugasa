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
package kinugasa.system.actor.status;

import kinugasa.game.EnumUtilInjector;
import kinugasa.game.VisibleNameEnumInjector;
import kinugasa.game.annotation.UnneedTranslate;

/**
 *
 * @vesion 1.0.0 - 2024/12/28_21:39:05<br>
 * @author Shinacho<br>
 */
public enum StatusKey implements VisibleNameEnumInjector<StatusKey>, EnumUtilInjector<StatusKey> {

	/**
	 * 体力
	 */
	HTP,
	/**
	 * スタミナ
	 */
	MGP,
	/**
	 * 魔力
	 */
	STA,
	/**
	 * 正気度
	 */
	SAN,
	/**
	 * 筋力
	 */
	STR,
	/**
	 * 器用
	 */
	DEX,
	/**
	 * 速度
	 */
	SPD,
	/**
	 * 精神
	 */
	POW,
	/**
	 * 信仰
	 */
	FAI,
	/**
	 * 詠唱
	 */
	CNT,
	/**
	 * 物理攻撃力
	 */
	PATK,
	/**
	 * 物理防御力
	 */
	PDEF,
	/**
	 * 物理命中率
	 */
	PHTC,
	/**
	 * 物理回避率
	 */
	PAVC,
	/**
	 * 物理ブロック率
	 */
	PBKC,
	/**
	 * 物理ブロック倍率
	 */
	PBKM,
	/**
	 * 物理クリティカル率
	 */
	PCRC,
	/**
	 * 物理クリティカル倍率
	 */
	PCRM,
	/**
	 * 魔法攻撃力
	 */
	MATK,
	/**
	 * 魔法防御力
	 */
	MDEF,
	/**
	 * 魔法命中率
	 */
	MHTC,
	/**
	 * 魔法回避率
	 */
	MAVC,
	/**
	 * 魔法ブロック率
	 */
	MBKC,
	/**
	 * 魔法ブロック倍率
	 */
	MBKM,
	/**
	 * 魔法クリティカル率
	 */
	MCRC,
	/**
	 * 魔法クリティカル倍率
	 */
	MCRM,
	/**
	 * ベース行動力
	 */
	BINI,
	/**
	 * 現在の行動力
	 */
	CINI,
	/**
	 * 装備属性
	 */
	EQA,
	/**
	 * 魔術適性
	 */
	MGA,
	/**
	 * 道徳
	 */
	HMOR,
	/**
	 * 啓蒙
	 */
	HENL,;

	@UnneedTranslate
	public static enum Type {
		INT,
		MUL,
		CHANCE,
		ENUM,;
	}

	public Type getType() {
		return switch (this) {
			case HTP ->
				Type.INT;
			case MGP ->
				Type.INT;
			case STA ->
				Type.INT;
			case SAN ->
				Type.INT;
			case STR ->
				Type.INT;
			case DEX ->
				Type.INT;
			case SPD ->
				Type.INT;
			case POW ->
				Type.INT;
			case FAI ->
				Type.INT;
			case CNT ->
				Type.INT;
			case PATK ->
				Type.INT;
			case PDEF ->
				Type.INT;
			case PHTC ->
				Type.CHANCE;
			case PAVC ->
				Type.CHANCE;
			case PBKC ->
				Type.CHANCE;
			case PBKM ->
				Type.MUL;
			case PCRC ->
				Type.CHANCE;
			case PCRM ->
				Type.MUL;
			case MATK ->
				Type.INT;
			case MDEF ->
				Type.INT;
			case MHTC ->
				Type.CHANCE;
			case MAVC ->
				Type.CHANCE;
			case MBKC ->
				Type.CHANCE;
			case MBKM ->
				Type.MUL;
			case MCRC ->
				Type.CHANCE;
			case MCRM ->
				Type.MUL;
			case BINI ->
				Type.INT;
			case CINI ->
				Type.INT;
			case EQA ->
				Type.ENUM;
			case MGA ->
				Type.ENUM;
			case HMOR ->
				Type.INT;
			case HENL ->
				Type.INT;

		};
	}

	public static void main(String... args) {
		for (var v : values()) {
			System.out.println("r." + v + " = this." + v + ".clone();");
		}
	}
}
