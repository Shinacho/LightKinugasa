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

import java.util.Set;
import kinugasa.field4.FieldMap;
import kinugasa.field4.LayeredTile;
import kinugasa.game.I18NText;
import kinugasa.object.IDdCloneableObject;
import kinugasa.resource.ID;
import kinugasa.script.ScriptAccessMethod;
import kinugasa.system.UniversalValue;
import kinugasa.system.actor.Actor;
import kinugasa.system.actor.status.cnd.Condition;
import kinugasa.system.item.EqipSlot;
import kinugasa.system.item.Item;

/**
 * StatusEffect<T>はパッシブ、状態異常、装備効果などの発生中の効果のインスタンスです.<br>
 * 効果はActorに紐づき、その型パラメータはアイテムや状態異常等があります。<br>
 * 効果は効果時間を持つ場合があります。<br>
 *
 * @vesion 1.0.0 - 2025/10/05_22:27:59<br>
 * @author Shinacho.<br>
 */
public abstract class StatusEffect<T extends ID> extends IDdCloneableObject implements Comparable<StatusEffect<T>> {

	public static enum ExpireMode {
		NON,
		TURN_AND_STEP,
		EVENT,;
	}

	public static enum ManualCountMode {
		NON,
		ENABLE,
	}
	public static final int TIME_NOT_USER = -1;

	private final T key;
	private final Actor tgt;
	private final ExpireMode expireMode;
	private int time = TIME_NOT_USER;
	private final ManualCountMode countMode;
	private int count = TIME_NOT_USER;
	private boolean exists = true;
	private int priority;

	public StatusEffect(T key, Actor tgt) {
		this(key, tgt, ExpireMode.NON, TIME_NOT_USER, ManualCountMode.NON, TIME_NOT_USER);
	}

	public StatusEffect(T key, Actor tgt, ExpireMode mode, int time) {
		this(key, tgt, mode, time, ManualCountMode.NON, TIME_NOT_USER);
	}

	public StatusEffect(T key, Actor tgt, ManualCountMode mode, int c) {
		this(key, tgt, ExpireMode.NON, TIME_NOT_USER, mode, c);
	}

	public StatusEffect(T key, Actor tgt, ExpireMode em, int time, ManualCountMode mm, int c) {
		super(key.getId() + "_of_" + tgt.getId() + "_" + em + "_" + mm);
		this.tgt = tgt;
		this.key = key;
		this.expireMode = em;
		this.time = time;
		this.countMode = mm;
		this.count = c;
	}

	public StatusEffect(T key, Object extendId, Actor tgt) {
		this(key, extendId, tgt, ExpireMode.NON, TIME_NOT_USER, ManualCountMode.NON, TIME_NOT_USER);
	}

	public StatusEffect(T key, Object extendId, Actor tgt, ExpireMode mode, int time) {
		this(key, extendId, tgt, mode, time, ManualCountMode.NON, TIME_NOT_USER);
	}

	public StatusEffect(T key, Object extendId, Actor tgt, ManualCountMode mode, int c) {
		this(key, extendId, tgt, ExpireMode.NON, TIME_NOT_USER, mode, c);
	}

	public StatusEffect(T key, Object extendId, Actor tgt, ExpireMode em, int time, ManualCountMode mm, int c) {
		super(key.getId() + "_" + extendId.toString() + "_of_" + tgt.getId() + "_" + em + "_" + mm);
		this.tgt = tgt;
		this.key = key;
		this.expireMode = em;
		this.time = time;
		this.countMode = mm;
		this.count = c;
	}

	public <T extends ID> boolean is(Class<?> t) {
		return key.getClass() == t;
	}

	public T getKey() {
		return key;
	}

	public Actor getTgt() {
		return tgt;
	}

	public ExpireMode getExpireMode() {
		return expireMode;
	}

	public int getTime() {
		return time;
	}

	public StatusEffect<T> setTime(int time) {
		this.time = time;
		return this;
	}

	public StatusEffect<T> setCount(int c) {
		this.count = c;
		return this;
	}

	public StatusEffect<T> addTime(int t) {
		return setTime(this.time + t);
	}

	public StatusEffect<T> addCount(int c) {
		return setCount(this.count - 1);
	}

	public StatusEffect<T> setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public int getCount() {
		return count;
	}

	public ManualCountMode getCountMode() {
		return countMode;
	}

	@ScriptAccessMethod
	public boolean timeModeIs(UniversalValue v) {
		return this.expireMode == v.of(ExpireMode.class);
	}

	@ScriptAccessMethod
	public boolean timeValueIsOver(UniversalValue v) {
		return time > v.asInt();
	}

	@ScriptAccessMethod
	public boolean timeValueIsUnder(UniversalValue v) {
		return time <= v.asInt();
	}

	public boolean isExists() {
		return exists;
	}

	public int getPriority() {
		return priority;
	}

	public void timeCountStep() {
		timeCountTurn();
	}

	public void timeCountTurn() {
		if (this.expireMode == ExpireMode.TURN_AND_STEP) {
			time--;
			if (time <= 0) {
				exists = false;
			}
		}
	}

	public void timeCountEvent() {
		if (this.expireMode == ExpireMode.EVENT) {
			time--;
			if (time <= 0) {
				exists = false;
			}
		}
	}

	public void nCount() {
		if (this.countMode == ManualCountMode.ENABLE) {
			count--;
			if (count <= 0) {
				exists = false;
			}
		}
	}

	@Override
	public StatusEffect<T> clone() {
		return (StatusEffect<T>) super.clone();
	}

	@Override
	public String toString() {
		return "ActorEffect{" + "id=" + getId() + ", tgt=" + tgt + ", expireMode=" + expireMode + ", time=" + time + ", exists=" + exists + ", priority=" + priority + '}';
	}

	@Override
	public int compareTo(StatusEffect<T> o) {
		return Integer.compare(priority, o.priority);
	}

//	@Now
//	public abstract StatusEffectResult whenBattleStart();
//
//	@Now
//	public abstract StatusEffectResult whenBattleEnd();
//
//	@Now
//	public abstract StatusEffectResult whenTurnStart();
//
//	@Lazy("canExecがtrueの場合にeventが実行される")
//	public abstract StatusEffectResult whenBattleEventStart();
//
//	@Now
//	public abstract StatusEffectResult whenMyCmdSelected();
//
//	@Now
//	public abstract StatusEffectResult whenBattleEventEnd();
//
//	@Now
//	public abstract StatusEffectResult whenDcsStartFromMe();
//
//	@Now
//	public abstract StatusEffectResult whenDcsEndFromMe();
//
//	@Now
//	public abstract StatusEffectResult whenDcsStartToMe();
//
//	@Now
//	public abstract StatusEffectResult whenDcsEndToMe();
	//
	/**
	 * このEffectの説明文を作成します。<br>
	 * 例えば、「体力上昇：体力が500上昇する」といった文章です。<br>
	 *
	 * @return このEffectの説明文。
	 */
	public abstract I18NText text();

	/**
	 * Actorが死亡しました。ただしActorが持っているEffectのResurrectイベント以外でです。<br>
	 * canExecをfalseにするとこれはキャンセルされます。<br>
	 *
	 * @param deadCnd 追加される予定の死亡状態異常
	 * @return 処理結果。
	 */
	public abstract StatusUpdateResult whenIMDead(Set<Condition> deadCnd);

	/**
	 * Actorが復活しました。ただしActorが持っているEffectのDeadイベント以外でです。<br>
	 * canExecをfalseにするとこれはキャンセルされます。<br>
	 *
	 * @param deadCnd 削除される予定の死亡状態異常
	 * @return 処理結果。
	 */
	public abstract StatusUpdateResult whenIMResurrect(Set<Condition> deadCnd);

	/**
	 * ActorがFM上で移動し、1タイル移動しました。<br>
	 * すなわち、前のフレームとは違うタイルに乗りました。<br>
	 *
	 * @param fm 今いるFM。
	 * @param current 移動後のタイル。
	 * @return 処理結果。
	 */
	public abstract StatusUpdateResult whenFMWalk(FieldMap fm, LayeredTile current);

	/**
	 * Actorがアイテムiを入手しました。<br>
	 * canExecをfalseにするとこれはキャンセルされます。<br>
	 *
	 * @param i 入手したアイテム
	 * @return 処理結果。
	 */
	public abstract StatusUpdateResult whenIGetItem(Item i);

	/**
	 * Actorがアイテムiを損失しました。<br>
	 * canExecをfalseにするとこれはキャンセルされます。<br>
	 *
	 * @param i ドロップするアイテム。
	 * @return 処理結果。
	 */
	public abstract StatusUpdateResult whenIDropItem(Item i);

	/**
	 * Actorがiをslotに装備しました。<br>
	 * canExecをfalseにするとこれはキャンセルされます。<br>
	 *
	 * @param i 装備したアイテム
	 * @param slot 装備したスロット
	 * @return 処理結果。
	 */
	public abstract StatusUpdateResult whenIEqipItem(Item i, EqipSlot slot);

	/**
	 * Actorがiをslotに装備解除しました。<br>
	 * canExecをfalseにするとこれはキャンセルされます。<br>
	 *
	 * @param i 装備解除したアイテム
	 * @param slot 装備解除したスロット
	 * @return 処理結果。
	 */
	public abstract StatusUpdateResult whenIUnEqipItem(Item i, EqipSlot slot);

	/**
	 * ActorにtgtCndが追加されました。すでにEffectに入っています。
	 *
	 * @param tgtCnd 追加されたCnd。
	 * @return 処理結果。
	 */
	public abstract StatusUpdateResult whenAddCondition(StatusEffect<?> tgtCnd);

	/**
	 * ActorからtgtCndが削除されました。すでにEffectから削除されています。
	 *
	 * @param tgtCnd 削除されたCnd。
	 * @return 処理結果。
	 */
	public abstract StatusUpdateResult whenRemoveCondition(StatusEffect<?> tgtCnd);

	/**
	 * このEffectによるステータス効果を適用します。
	 *
	 * @param val このインスタンスに加算します。
	 * @return 処理結果。
	 */
	public abstract StatusUpdateResult setEffectedStatus(Status.Value val);

	public abstract boolean effectedRecalcRequire();
}
