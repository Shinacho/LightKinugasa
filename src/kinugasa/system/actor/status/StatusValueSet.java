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
package kinugasa.system.actor.status;

import java.util.Arrays;
import java.util.Collection;
import kinugasa.game.annotation.CopyCtor;
import kinugasa.game.annotation.NewInstance;
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.object.CloneableObject;
import kinugasa.system.actor.EqipAttr;
import kinugasa.system.actor.MagicAptitude;

/**
 * StatusValueSet.<br>
 *
 * @vesion 1.0.0 - 2025/10/05_13:16:47<br>
 * @author Shinacho.<br>
 */
public class StatusValueSet extends CloneableObject {

	private static final StatusValueInt.Limit LIMIT_4_MINUS = new StatusValueInt.Limit(-9999, 9999);
	private static final StatusValueInt.Limit LIMIT_4_ZERO = new StatusValueInt.Limit(0, 9999);
	private static final StatusValueInt.Limit LIMIT_3 = new StatusValueInt.Limit(0, 999);
	private static final StatusValueInt.Limit LIMIT_2 = new StatusValueInt.Limit(0, 99);

	//base
	/**
	 * 体力
	 */
	public StatusValueInt HTP = new StatusValueInt(StatusKey.HTP, LIMIT_4_MINUS, new StatusValueInt.Limit(-9999, 1000), 1000);
	/**
	 * スタミナ
	 */
	public StatusValueInt MGP = new StatusValueInt(StatusKey.MGP, LIMIT_4_ZERO, new StatusValueInt.Limit(0, 1000), 1000);
	/**
	 * 魔力
	 */
	public StatusValueInt STA = new StatusValueInt(StatusKey.STA, LIMIT_4_ZERO, new StatusValueInt.Limit(0, 1000), 1000);
	/**
	 * 正気度
	 */
	public StatusValueInt SAN = new StatusValueInt(StatusKey.STA, LIMIT_2, LIMIT_2.clone(), 50);
	///status
	/**
	 * 筋力
	 */
	public StatusValueInt STR = new StatusValueInt(StatusKey.STR, LIMIT_2, LIMIT_2.clone(), 25).setAutoTrim(false);
	/**
	 * 器用
	 */
	public StatusValueInt DEX = new StatusValueInt(StatusKey.DEX, LIMIT_2, LIMIT_2.clone(), 25).setAutoTrim(false);
	/**
	 * 精神
	 */
	public StatusValueInt POW = new StatusValueInt(StatusKey.POW, LIMIT_2, LIMIT_2.clone(), 25).setAutoTrim(false);
	/**
	 * 信仰
	 */
	public StatusValueInt FAI = new StatusValueInt(StatusKey.FAI, LIMIT_2, LIMIT_2.clone(), 25).setAutoTrim(false);
	/**
	 * 速度
	 */
	public StatusValueInt SPD = new StatusValueInt(StatusKey.SPD, LIMIT_2, LIMIT_2.clone(), 25).setAutoTrim(false);
	/**
	 * 詠唱
	 */
	public StatusValueInt CNT = new StatusValueInt(StatusKey.CNT, LIMIT_2, LIMIT_2.clone(), 25).setAutoTrim(false);
	//physical
	/**
	 * 物理攻撃力
	 */
	public StatusValueInt PATK = new StatusValueInt(StatusKey.PATK, LIMIT_4_ZERO, LIMIT_4_ZERO.clone(), 500).setAutoTrim(false);
	/**
	 * 物理防御力
	 */
	public StatusValueInt PDEF = new StatusValueInt(StatusKey.PDEF, LIMIT_4_ZERO, LIMIT_4_ZERO.clone(), 500).setAutoTrim(false);
	/**
	 * 物理命中率
	 */
	public StatusValueChance PHTC = new StatusValueChance(StatusKey.PHTC, 0.95f);
	/**
	 * 物理回避率
	 */
	public StatusValueChance PAVC = new StatusValueChance(StatusKey.PAVC, 0.05f);
	/**
	 * 物理ブロック率
	 */
	public StatusValueChance PBKC = new StatusValueChance(StatusKey.PBKC, 0.02f);
	/**
	 * 物理ブロック倍率
	 */
	public StatusValueMul PBKM = new StatusValueMul(StatusKey.PBKM, 0.5f);
	/**
	 * 物理クリティカル率
	 */
	public StatusValueChance PCRC = new StatusValueChance(StatusKey.PCRC, 0.05f);
	/**
	 * 物理クリティカル倍率
	 */
	public StatusValueMul PCRM = new StatusValueMul(StatusKey.PCRM, 1.5f);
	//magical
	/**
	 * 魔法攻撃力
	 */
	public StatusValueInt MATK = new StatusValueInt(StatusKey.MATK, LIMIT_4_ZERO, LIMIT_4_ZERO.clone(), 500).setAutoTrim(false);
	/**
	 * 魔法防御力
	 */
	public StatusValueInt MDEF = new StatusValueInt(StatusKey.MDEF, LIMIT_4_ZERO, LIMIT_4_ZERO.clone(), 500).setAutoTrim(false);
	/**
	 * 魔法命中率
	 */
	public StatusValueChance MHTC = new StatusValueChance(StatusKey.MHTC, 0.95f);
	/**
	 * 魔法回避率
	 */
	public StatusValueChance MAVC = new StatusValueChance(StatusKey.MAVC, 0.05f);
	/**
	 * 魔法ブロック率
	 */
	public StatusValueChance MBKC = new StatusValueChance(StatusKey.MBKC, 0.02f);
	/**
	 * 魔法ブロック倍率
	 */
	public StatusValueMul MBKM = new StatusValueMul(StatusKey.MBKM, 0.5f);
	/**
	 * 魔法クリティカル率
	 */
	public StatusValueChance MCRC = new StatusValueChance(StatusKey.MCRC, 0.05f);
	/**
	 * 魔法クリティカル倍率
	 */
	public StatusValueMul MCRM = new StatusValueMul(StatusKey.MCRM, 1.5f);
	//move
	/**
	 * ベース行動力
	 */
	public StatusValueInt BINI = new StatusValueInt(StatusKey.BINI, LIMIT_3, LIMIT_3.clone(), 100).setAutoTrim(false);
	/**
	 * 現在の行動力
	 */
	public StatusValueInt CINI = new StatusValueInt(StatusKey.CINI, LIMIT_3, LIMIT_3.clone(), 100).setAutoTrim(false);
	//enum
	/**
	 * 装備属性
	 */
	public StatusValueEnum< EqipAttr> EQA = new StatusValueEnum<>(StatusKey.EQA, EqipAttr.MIDDLE_MAN);
	/**
	 * 魔術適性
	 */
	public StatusValueEnum< MagicAptitude> MGA = new StatusValueEnum<>(StatusKey.MGA, MagicAptitude.USABLE);
	//hidden
	/**
	 * 道徳
	 */
	public StatusValueInt HMOR = new StatusValueInt(StatusKey.HMOR, LIMIT_2, LIMIT_2.clone(), 50);
	/**
	 * 啓蒙
	 */
	public StatusValueInt HENL = new StatusValueInt(StatusKey.HENL, LIMIT_2, LIMIT_2.clone(), 0);

	public StatusValueSet() {
	}

	@CopyCtor
	public StatusValueSet(StatusValueSet vs) {
		this.HTP = vs.HTP.clone();
		this.MGP = vs.MGP.clone();
		this.STA = vs.STA.clone();
		this.SAN = vs.SAN.clone();
		this.STR = vs.STR.clone();
		this.DEX = vs.DEX.clone();
		this.SPD = vs.SPD.clone();
		this.POW = vs.POW.clone();
		this.FAI = vs.FAI.clone();
		this.CNT = vs.CNT.clone();
		this.PATK = vs.PATK.clone();
		this.PDEF = vs.PDEF.clone();
		this.PHTC = vs.PHTC.clone();
		this.PAVC = vs.PAVC.clone();
		this.PBKC = vs.PBKC.clone();
		this.PBKM = vs.PBKM.clone();
		this.PCRC = vs.PCRC.clone();
		this.PCRM = vs.PCRM.clone();
		this.MATK = vs.MATK.clone();
		this.MDEF = vs.MDEF.clone();
		this.MHTC = vs.MHTC.clone();
		this.MAVC = vs.MAVC.clone();
		this.MBKC = vs.MBKC.clone();
		this.MBKM = vs.MBKM.clone();
		this.MCRC = vs.MCRC.clone();
		this.MCRM = vs.MCRM.clone();
		this.BINI = vs.BINI.clone();
		this.CINI = vs.CINI.clone();
		this.EQA = vs.EQA.clone();
		this.MGA = vs.MGA.clone();
		this.HMOR = vs.HMOR.clone();
		this.HENL = vs.HENL.clone();
	}

	@NewInstance
	public static StatusValueSet zero() {
		StatusValueSet r = new StatusValueSet();
		r.HTP.setPersonalLimitToKeyLimit().toZero();
		r.MGP.setPersonalLimitToKeyLimit().toZero();
		r.STA.setPersonalLimitToKeyLimit().toZero();
		r.SAN.setPersonalLimitToKeyLimit().toZero();
		r.STR.setPersonalLimitToKeyLimit().toZero();
		r.DEX.setPersonalLimitToKeyLimit().toZero();
		r.POW.setPersonalLimitToKeyLimit().toZero();
		r.FAI.setPersonalLimitToKeyLimit().toZero();
		r.SPD.setPersonalLimitToKeyLimit().toZero();
		r.CNT.setPersonalLimitToKeyLimit().toZero();
		r.PATK.setPersonalLimitToKeyLimit().toZero();
		r.PDEF.setPersonalLimitToKeyLimit().toZero();
		r.PHTC.toZero();
		r.PAVC.toZero();
		r.PBKC.toZero();
		r.PBKM.toZero();
		r.PCRC.toZero();
		r.PCRM.toZero();
		r.MATK.setPersonalLimitToKeyLimit().toZero();
		r.MDEF.setPersonalLimitToKeyLimit().toZero();
		r.MHTC.toZero();
		r.MAVC.toZero();
		r.MBKC.toZero();
		r.MBKM.toZero();
		r.MCRC.toZero();
		r.MCRM.toZero();
		r.BINI.setPersonalLimitToKeyLimit().toZero();
		r.CINI.setPersonalLimitToKeyLimit().toZero();
//		r.EQA.setPersonalLimitToKeyLimit().toZero(); //処理なし
//		r.MGA.setPersonalLimitToKeyLimit().toZero(); //処理なし
		r.HMOR.setPersonalLimitToKeyLimit().toZero();
		r.HENL.setPersonalLimitToKeyLimit().toZero();
		return r;
	}

	@NewInstance
	public static StatusValueSet add(StatusValueSet... v) {
		return add(Arrays.asList(v));
	}

	@NewInstance
	public static StatusValueSet add(Collection<StatusValueSet> v) {
		StatusValueSet r = zero();
		for (var vv : v) {
			r.add(vv);
		}
		return r;
	}

	@NotNewInstance
	public StatusValueSet add(StatusValueSet vs) {
		this.HTP.addValue(vs.HTP);
		this.MGP.addValue(vs.MGP);
		this.STA.addValue(vs.STA);
		this.SAN.addValue(vs.SAN);
		this.STR.addValue(vs.STR);
		this.DEX.addValue(vs.DEX);
		this.POW.addValue(vs.POW);
		this.FAI.addValue(vs.FAI);
		this.SPD.addValue(vs.SPD);
		this.CNT.addValue(vs.CNT);
		this.PATK.addValue(vs.PATK);
		this.PDEF.addValue(vs.PDEF);
		this.PHTC.add(vs.PHTC);
		this.PAVC.add(vs.PAVC);
		this.PBKC.add(vs.PBKC);
		this.PBKM.add(vs.PBKM);
		this.PCRC.add(vs.PCRC);
		this.PCRM.add(vs.PCRM);
		this.MATK.addValue(vs.MATK);
		this.MDEF.addValue(vs.MDEF);
		this.MHTC.add(vs.MHTC);
		this.MAVC.add(vs.MAVC);
		this.MBKC.add(vs.MBKC);
		this.MBKM.add(vs.MBKM);
		this.MCRC.add(vs.MCRC);
		this.MCRM.add(vs.MCRM);
		this.BINI.addValue(vs.BINI);
		this.CINI.addValue(vs.CINI);
//		this.EQA.add(vs.EQA); //処理なし
//		this.MGA.add(vs.MGA); //処理なし
		this.HMOR.addValue(vs.HMOR);
		this.HENL.addValue(vs.HENL);
		return this;
	}

	//castable
	public StatusValue of(StatusKey k) {
		return switch (k) {
			case HTP ->
				HTP;
			case MGP ->
				MGP;
			case STA ->
				STA;
			case SAN ->
				SAN;
			case STR ->
				STR;
			case DEX ->
				DEX;
			case SPD ->
				SPD;
			case POW ->
				POW;
			case FAI ->
				FAI;
			case CNT ->
				CNT;
			case PATK ->
				PATK;
			case PDEF ->
				PDEF;
			case PHTC ->
				PHTC;
			case PAVC ->
				PAVC;
			case PBKC ->
				PBKC;
			case PBKM ->
				PBKM;
			case PCRC ->
				PCRC;
			case PCRM ->
				PCRM;
			case MATK ->
				MATK;
			case MDEF ->
				MDEF;
			case MHTC ->
				MHTC;
			case MAVC ->
				MAVC;
			case MBKC ->
				MBKC;
			case MBKM ->
				MBKM;
			case MCRC ->
				MCRC;
			case MCRM ->
				MCRM;
			case BINI ->
				BINI;
			case CINI ->
				CINI;
			case EQA ->
				EQA;
			case MGA ->
				MGA;
			case HMOR ->
				HMOR;
			case HENL ->
				HENL;
		};

	}

	@Override
	public StatusValueSet clone() {
		var r = (StatusValueSet) super.clone();
		r.HTP = this.HTP.clone();
		r.MGP = this.MGP.clone();
		r.STA = this.STA.clone();
		r.SAN = this.SAN.clone();
		r.STR = this.STR.clone();
		r.DEX = this.DEX.clone();
		r.SPD = this.SPD.clone();
		r.POW = this.POW.clone();
		r.FAI = this.FAI.clone();
		r.CNT = this.CNT.clone();
		r.PATK = this.PATK.clone();
		r.PDEF = this.PDEF.clone();
		r.PHTC = this.PHTC.clone();
		r.PAVC = this.PAVC.clone();
		r.PBKC = this.PBKC.clone();
		r.PBKM = this.PBKM.clone();
		r.PCRC = this.PCRC.clone();
		r.PCRM = this.PCRM.clone();
		r.MATK = this.MATK.clone();
		r.MDEF = this.MDEF.clone();
		r.MHTC = this.MHTC.clone();
		r.MAVC = this.MAVC.clone();
		r.MBKC = this.MBKC.clone();
		r.MBKM = this.MBKM.clone();
		r.MCRC = this.MCRC.clone();
		r.MCRM = this.MCRM.clone();
		r.BINI = this.BINI.clone();
		r.CINI = this.CINI.clone();
		r.EQA = this.EQA.clone();
		r.MGA = this.MGA.clone();
		r.HMOR = this.HMOR.clone();
		r.HENL = this.HENL.clone();
		return r;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("StatusValueSet{");
		sb.append("HTP=").append(HTP);
		sb.append(", MGP=").append(MGP);
		sb.append(", STA=").append(STA);
		sb.append(", SAN=").append(SAN);
		sb.append(", STR=").append(STR);
		sb.append(", DEX=").append(DEX);
		sb.append(", POW=").append(POW);
		sb.append(", FAI=").append(FAI);
		sb.append(", SPD=").append(SPD);
		sb.append(", CNT=").append(CNT);
		sb.append(", PATK=").append(PATK);
		sb.append(", PDEF=").append(PDEF);
		sb.append(", PHTC=").append(PHTC);
		sb.append(", PAVC=").append(PAVC);
		sb.append(", PBKC=").append(PBKC);
		sb.append(", PBKM=").append(PBKM);
		sb.append(", PCRC=").append(PCRC);
		sb.append(", PCRM=").append(PCRM);
		sb.append(", MATK=").append(MATK);
		sb.append(", MDEF=").append(MDEF);
		sb.append(", MHTC=").append(MHTC);
		sb.append(", MAVC=").append(MAVC);
		sb.append(", MBKC=").append(MBKC);
		sb.append(", MBKM=").append(MBKM);
		sb.append(", MCRC=").append(MCRC);
		sb.append(", MCRM=").append(MCRM);
		sb.append(", BINI=").append(BINI);
		sb.append(", CINI=").append(CINI);
		sb.append(", EQA=").append(EQA);
		sb.append(", MGA=").append(MGA);
		sb.append(", HMOR=").append(HMOR);
		sb.append(", HENL=").append(HENL);
		sb.append('}');
		return sb.toString();
	}

}
