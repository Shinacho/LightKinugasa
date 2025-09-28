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
package kinugasa.field4;

import kinugasa.game.VisibleNameEnumInjector;
import kinugasa.resource.IDEnumInjector;

/**
 * MapChipAttribute.<br>
 *
 * @vesion 1.0.0 - 2025/07/19_7:56:09<br>
 * @author Shinacho.<br>
 */
public enum MapChipAttribute implements IDEnumInjector<MapChipAttribute>, VisibleNameEnumInjector<MapChipAttribute> {
	//
	VOID(0),//VOID
	CLOSE(0),//CLOSE
	//外装----------------------------------------------------
	PLAIN(2),//平地
	GRASSLAND(2),//草原
	CITY(0),//都市
	TOWN(0),//町
	VILLAGE(0),//村
	HUT(0),//小屋
	DESERT(2),//砂漠
	SANDY_BEACH(2),//砂浜
	BUSH(4),//藪
	FOREST(6),//森林
	JUNGLE(8),//密林
	HILLS(6),//丘陵
	MOUNTAINS(8),//山岳
	HIGH_MOUNTAINS(8),//高山
	RIVER(1),//河川
	BRIDGE(1),//橋
	SHALLOW_WATER(1),//浅瀬
	SEA(1),//海
	CASTLE(0),//城
	TOWER(0),//塔
	CAVE(4),//洞窟
	ROAD(1),//道路
	WALL(0),//城壁
	PORT(0),//港
	MOUNTAIN_TRAIL(0),//登山道
	MOUNTAIN_HUT(0),//山小屋
	COLLAPSED_BRIDGE(0),//崩壊橋
	RUINS(0),//廃墟
	REEF(1),//岩礁
	FENCE(0),//柵
	ROCK(0),//岩
	FIELD(1),//畑
	CLIFF(0),//崖
	WASTELAND(1),//荒地
	ROCKBED(1),//岩盤
	//内装----------------------------------------------------
	WOOD(0),//木
	MAGIC_SQUARE(1),//魔方陣
	LADDER(0),//はしご
	STONE_STEPS(1),//石階段
	WOODEN_STEPS(1),//木階段
	WOODEN_BRIDGE(1),//木橋
	STONE_BRIDGE(1),//石橋
	WOODEN_FLOOR(1),//木床
	STONE_FLOOR(1),//石床
	PAVED_ROAD(1),//舗装道路
	WOODEN_WALL(0),//木壁
	STONE_WALL(0),//石壁
	IRON_WALL(0),//鉄壁
	DOOR(0),//ドア
	ROOF(0),//屋根
	SIGN(0),//看板
	DESK(0),//机
	CHAIR(0),//椅子
	SHELF(0),//棚
	BOOKSHELF(0),//本棚
	BED(0),//ベッド
	TREASURE_CHEST(0),//宝箱
	POT(0),//壷
	BARREL(0),//樽
	BOX(0),//箱
	BAG(0),//袋
	BOOK(0),//本
	WEAPON(0),//武器
	FIREARM(0),//火器
	SHIELD(0),//盾
	ARMOR(0),//鎧
	HELMET(0),//兜
	SWITCH(0),//スイッチ
	LEVER(0),//レバー
	BONE(0),//骨
	MACHINE(0),//機械
	TORCH(0),//松明
	;
	private int encountBaseValue;

	private MapChipAttribute(int encountBaseValue) {
		this.encountBaseValue = encountBaseValue;
	}

	public int getEncountBaseValue() {
		return encountBaseValue;
	}

	@Deprecated
	public void setEncountBaseValue(int encountBaseValue) {
		this.encountBaseValue = encountBaseValue;
	}

}
