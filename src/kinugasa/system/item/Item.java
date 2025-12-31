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

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import kinugasa.game.VisibleNameIDInjector;
import kinugasa.game.annotation.NewInstance;
import kinugasa.system.actor.PersonalBagItem;
import kinugasa.script.ScriptBlockType;
import kinugasa.system.actor.Actor;
import kinugasa.system.actor.status.StatusEffect;
import kinugasa.system.actor.status.StatusEffectable;
import kinugasa.system.actor.status.StatusUpdateResult;
import kinugasa.system.actor.status.attr.AttributeKey;

/**
 * Item.<br>
 *
 * @vesion 1.0.0 - 2025/08/13_14:55:00<br>
 * @author Shinacho.<br>
 */
public final class Item 
//		extends PersonalBagItem implements StatusEffectable<Item> 
{
//
//	protected Item(File f) {
//		super(f);
//	}
//
//	//--------------------------------------------------------------------------
//	private ItemType itemType;
//	private ItemRarity Rarity;
//	private EnumSet<ItemTags> itemTags;
//	private boolean unique;
//	private boolean baseItem;
//	private boolean saleable;
//	private int basePrice;
//	private boolean dropable;
//	private float weight;
//	//
//	private boolean itemBagSizeAdd;
//	private int itemBagSize;
//	//
//	private boolean bookBagSizeAdd;
//	private int bookBagSize;
//	//
//	private boolean scriptBagSizeAdd;
//	private int scriptBagSize;
//	//
//	private boolean disssasseEnable;
//	private MaterialSet dissasseMaterials;
//	//
//	private boolean upgradeEnable;
//	private ItemUpgradeSet upgradeSet;
//	//
//	private EnumSet<EqipSlot> eqipSlot;
//	private WeaponType weaponType;
//	private ArmorType armorType;
//	private boolean weaponTypeActionEnable;
//	private WeaponTwoHandMode twoHandMode;
//	private AttributeKey atkAttr;
//	private int atkCount;
//	//
//	private ItemSet itemSet;
//	//
//	private boolean enchantEnable;
//	private ItemEnchant defaultEnchant;
//	private ItemEnchant currentEnchant;
//	//
//	private boolean styleEnable;
//	private ItemStyle defaultStyle;
//	private ItemStyle currentStyle;
//
//	//--------------------------------------------------------------------------
//	@Override
//	public boolean effectedRecalcRequire() {
//		return !getBlockOf(ScriptBlockType.HAVING_EFFECT).isEmpty() || !getBlockOf(ScriptBlockType.EQIP_EFFECT).isEmpty();
//	}
//
//	@Override
//	public boolean getThisEventRequire() {
//	}
//
//	@Override
//	public StatusUpdateResult whenGetThis(Actor a) {
//	}
//
//	@Override
//	public boolean dropThisEventRequire() {
//	}
//
//	@Override
//	public StatusUpdateResult whenDropThis(Actor a) {
//	}
//
//	@Override
//	public StatusEffect<Item> createEffect(Actor tgt, StatusEffect.ExpireMode eMode, int time, StatusEffect.ManualCountMode mMode, int count) {
//	}
//
//	private boolean eqipEffectRequire() {
//
//	}
//
//	public StatusUpdateResult whenEqipThis(Actor a) {
//
//	}
//
//	public StatusUpdateResult whenUnEqipThis(Actor a) {
//
//	}
//
//	@Override
//	public Item clone() {
//		return super.clone();
//	}
//
//	@Override
//	public StatusEffectable<Item> effect1() {
//		return super.effect1(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
//	}
//	
//	
//	public StatusEffectable<Item> effect2(){
//	
//	}
//	
//
//	@NewInstance
//	public List<Item> nCopies(int n) {
//		List<Item> r = new ArrayList<>();
//		for (int i = 0; i < n; i++) {
//			r.add(clone());
//		}
//		return r;
//	}
//	//--------------------------------------------------------------------------
//
//	public ItemType getItemType() {
//		return itemType;
//	}
//
//	public ItemRarity getRarity() {
//		return Rarity;
//	}
//
//	public EnumSet<ItemTags> getItemTags() {
//		return itemTags;
//	}
//
//	public boolean isUnique() {
//		return unique;
//	}
//
//	public boolean isBaseItem() {
//		return baseItem;
//	}
//
//	public boolean isSaleable() {
//		return saleable;
//	}
//
//	public int getBasePrice() {
//		return basePrice;
//	}
//
//	public boolean isDropable() {
//		return dropable;
//	}
//
//	public float getWeight() {
//		return weight;
//	}
//
//	public boolean isDisssasseEnable() {
//		return disssasseEnable;
//	}
//
//	public MaterialSet getDissasseMaterials() {
//		return dissasseMaterials;
//	}
//
//	public boolean isUpgradeEnable() {
//		return upgradeEnable;
//	}
//
//	public ItemUpgradeSet getUpgradeSet() {
//		return upgradeSet;
//	}
//
//	public EnumSet<EqipSlot> getEqipSlot() {
//		return eqipSlot;
//	}
//
//	public WeaponType getWeaponType() {
//		return weaponType;
//	}
//
//	public boolean isWeaponTypeActionEnable() {
//		return weaponTypeActionEnable;
//	}
//
//	public WeaponTwoHandMode getTwoHandMode() {
//		return twoHandMode;
//	}
//
//	public AttributeKey getAtkAttr() {
//		return atkAttr;
//	}
//
//	public int getAtkCount() {
//		return atkCount;
//	}
//
//	public ItemSet getItemSet() {
//		return itemSet;
//	}
//
//	public boolean isEnchantEnable() {
//		return enchantEnable;
//	}
//
//	public ItemEnchant getDefaultEnchant() {
//		return defaultEnchant;
//	}
//
//	public ItemEnchant getCurrentEnchant() {
//		return currentEnchant;
//	}
//
//	public boolean isStyleEnable() {
//		return styleEnable;
//	}
//
//	public ItemStyle getDefaultStyle() {
//		return defaultStyle;
//	}
//
//	public ItemStyle getCurrentStyle() {
//		return currentStyle;
//	}
//
//	public void setItemType(ItemType itemType) {
//		this.itemType = itemType;
//	}
//
//	public void setRarity(ItemRarity Rarity) {
//		this.Rarity = Rarity;
//	}
//
//	public void setItemTags(EnumSet<ItemTags> itemTags) {
//		this.itemTags = itemTags;
//	}
//
//	public void setUnique(boolean unique) {
//		this.unique = unique;
//	}
//
//	public void setBaseItem(boolean baseItem) {
//		this.baseItem = baseItem;
//	}
//
//	public void setSaleable(boolean saleable) {
//		this.saleable = saleable;
//	}
//
//	public void setBasePrice(int basePrice) {
//		this.basePrice = basePrice;
//	}
//
//	public void setDropable(boolean dropable) {
//		this.dropable = dropable;
//	}
//
//	public void setWeight(float weight) {
//		this.weight = weight;
//	}
//
//	public void setDisssasseEnable(boolean disssasseEnable) {
//		this.disssasseEnable = disssasseEnable;
//	}
//
//	public void setDissasseMaterials(MaterialSet dissasseMaterials) {
//		this.dissasseMaterials = dissasseMaterials;
//	}
//
//	public void setUpgradeEnable(boolean upgradeEnable) {
//		this.upgradeEnable = upgradeEnable;
//	}
//
//	public void setUpgradeSet(ItemUpgradeSet upgradeSet) {
//		this.upgradeSet = upgradeSet;
//	}
//
//	public void setEqipSlot(EnumSet<EqipSlot> eqipSlot) {
//		this.eqipSlot = eqipSlot;
//	}
//
//	public void setWeaponType(WeaponType weaponType) {
//		this.weaponType = weaponType;
//	}
//
//	public void setWeaponTypeActionEnable(boolean weaponTypeActionEnable) {
//		this.weaponTypeActionEnable = weaponTypeActionEnable;
//	}
//
//	public void setTwoHandMode(WeaponTwoHandMode twoHandMode) {
//		this.twoHandMode = twoHandMode;
//	}
//
//	public void setAtkAttr(AttributeKey atkAttr) {
//		this.atkAttr = atkAttr;
//	}
//
//	public void setAtkCount(int atkCount) {
//		this.atkCount = atkCount;
//	}
//
//	public void setItemSet(ItemSet itemSet) {
//		this.itemSet = itemSet;
//	}
//
//	public void setEnchantEnable(boolean enchantEnable) {
//		this.enchantEnable = enchantEnable;
//	}
//
//	public void setCurrentEnchant(ItemEnchant currentEnchant) {
//		this.currentEnchant = currentEnchant;
//	}
//
//	public void setStyleEnable(boolean styleEnable) {
//		this.styleEnable = styleEnable;
//	}
//
//	public boolean isItemBagSizeAdd() {
//		return itemBagSizeAdd;
//	}
//
//	public void setItemBagSizeAdd(boolean itemBagSizeAdd) {
//		this.itemBagSizeAdd = itemBagSizeAdd;
//	}
//
//	public int getItemBagSize() {
//		return itemBagSize;
//	}
//
//	public void setItemBagSize(int itemBagSize) {
//		this.itemBagSize = itemBagSize;
//	}
//
//	public boolean isBookBagSizeAdd() {
//		return bookBagSizeAdd;
//	}
//
//	public void setBookBagSizeAdd(boolean bookBagSizeAdd) {
//		this.bookBagSizeAdd = bookBagSizeAdd;
//	}
//
//	public int getBookBagSize() {
//		return bookBagSize;
//	}
//
//	public void setBookBagSize(int bookBagSize) {
//		this.bookBagSize = bookBagSize;
//	}
//
//	public boolean isScriptBagSizeAdd() {
//		return scriptBagSizeAdd;
//	}
//
//	public void setScriptBagSizeAdd(boolean scriptBagSizeAdd) {
//		this.scriptBagSizeAdd = scriptBagSizeAdd;
//	}
//
//	public int getScriptBagSize() {
//		return scriptBagSize;
//	}
//
//	public void setScriptBagSize(int scriptBagSize) {
//		this.scriptBagSize = scriptBagSize;
//	}
//
//	public ArmorType getArmorType() {
//		return armorType;
//	}
//
//	public void setArmorType(ArmorType armorType) {
//		this.armorType = armorType;
//	}
//
//	@Override
//	public int hashCode() {
//		int hash = 3;
//		hash = 89 * hash + Objects.hashCode(this.upgradeSet);
//		hash = 89 * hash + Objects.hashCode(this.currentEnchant);
//		hash = 89 * hash + Objects.hashCode(this.currentStyle);
//		return hash;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj) {
//			return true;
//		}
//		if (obj == null) {
//			return false;
//		}
//		if (getClass() != obj.getClass()) {
//			return false;
//		}
//		final Item other = (Item) obj;
//		if (!Objects.equals(this.upgradeSet, other.upgradeSet)) {
//			return false;
//		}
//		if (!Objects.equals(this.currentEnchant, other.currentEnchant)) {
//			return false;
//		}
//		return Objects.equals(this.currentStyle, other.currentStyle);
//	}
//
//	@Override
//	public String toString() {
//		return getId();
//	}

}
