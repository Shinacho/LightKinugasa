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
package kinugasa.system.actor.status.skill;

import java.util.EnumMap;
import kinugasa.object.CloneableObject;
import kinugasa.system.action.MagicType;
import kinugasa.system.actor.Actor;
import kinugasa.system.actor.status.StatusKey;
import kinugasa.system.item.ArmorType;
import kinugasa.system.item.WeaponType;

/**
 * SkillSet.<br>
 *
 * @vesion 1.0.0 - 2025/10/19_23:16:24<br>
 * @author Shinacho.<br>
 */
public class SkillSet extends CloneableObject {

	/**
	 * スキルの最大値：<br>
	 * 99
	 *
	 * スキルが上がる条件：<br>
	 * ・何かの道具を使ったり魔法を使う <br>
	 * ・戦闘後に身に着けたり、使ったりしたものについて<br>
	 * 1d100が現在のスキル値以上になったら確率で上がる。<br>
	 *
	 * スキルの効果：<br>
	 * ・スキルが一定の値に上がったとき、パッシブが得られる。
	 */
	private final Actor a;
	private EnumMap<StatusKey, Integer> general = new EnumMap<>(StatusKey.class);
	private EnumMap<WeaponType, Integer> weapon = new EnumMap<>(WeaponType.class);
	private EnumMap<ArmorType, Integer> armor = new EnumMap<>(ArmorType.class);
	private EnumMap<MagicType, Integer> magic = new EnumMap<>(MagicType.class);

	public SkillSet(Actor a) {
		this.a = a;
	}

	public EnumMap<StatusKey, Integer> getGeneral() {
		return general;
	}

	public EnumMap<WeaponType, Integer> getWeapon() {
		return weapon;
	}

	public EnumMap<ArmorType, Integer> getArmor() {
		return armor;
	}

	public EnumMap<MagicType, Integer> getMagic() {
		return magic;
	}

	@Override
	public SkillSet clone() {
		SkillSet o = (SkillSet) super.clone();
		o.general = this.general.clone();
		o.weapon = this.weapon.clone();
		o.armor = this.armor.clone();
		o.magic = this.magic.clone();
		return o;
	}

	@Override
	public String toString() {
		return "SkillSet{" + "general=" + general + ", weapon=" + weapon + ", armor=" + armor + ", magic=" + magic + '}';
	}

}
