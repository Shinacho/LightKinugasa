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
package kinugasa.script;

import kinugasa.script.exception.ScriptSyntaxException;
import kinugasa.system.actor.status.StatusUpdateSAO;
import kinugasa.system.actor.ConditionChangeScriptAccessObject;
import kinugasa.system.actor.status.StatusEffectSAO;
import kinugasa.system.item.ItemEqipSAO;
import kinugasa.system.item.ItemGetSAO;

/**
 * ScriptBlockType.<br>
 *
 * @vesion 1.0.0 - 2025/08/01_12:41:12<br>
 * @author Shinacho.<br>
 */
public enum ScriptBlockType {
	//----------------------------------------GENERAL----------------------------------------
	DUMMY {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return new ScriptAccessObject();
		}

	},
	ONE_TIME {

		@Override
		public ScriptAccessObject getSAO(String name) {
			if (name.toUpperCase().equals("FieldScriptAccessObject".toUpperCase())) {
				return new FieldScriptAccessObject();
			}
			if (name.toUpperCase().equals("TextBuilderScriptAcccessObject".toUpperCase())) {
				return new TextBuilderScriptAcccessObject();
			}
			if (name.toUpperCase().equals("ActorUpdateScriptAccessObject".toUpperCase())) {
				return new ConditionChangeScriptAccessObject();
			}
			return new ScriptAccessObject();
		}
	},
	MAIN {
		@Override
		public ScriptAccessObject getSAO(String name) {
			if (name.toUpperCase().equals("FieldScriptAccessObject".toUpperCase())) {
				return new FieldScriptAccessObject();
			}
			if (name.toUpperCase().equals("TextBuilderScriptAcccessObject".toUpperCase())) {
				return new TextBuilderScriptAcccessObject();
			}
			if (name.toUpperCase().equals("ActorUpdateScriptAccessObject".toUpperCase())) {
				return new ConditionChangeScriptAccessObject();
			}
			return new ScriptAccessObject();
		}
	},
	MANUAL {
		@Override
		public ScriptAccessObject getSAO(String name) {
			if (name.toUpperCase().equals("FieldScriptAccessObject".toUpperCase())) {
				return new FieldScriptAccessObject();
			}
			if (name.toUpperCase().equals("TextBuilderScriptAcccessObject".toUpperCase())) {
				return new TextBuilderScriptAcccessObject();
			}
			if (name.toUpperCase().equals("ActorUpdateScriptAccessObject".toUpperCase())) {
				return new ConditionChangeScriptAccessObject();
			}
			return new ScriptAccessObject();
		}
	},
	TEXT {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return new TextBuilderScriptAcccessObject();
		}
	},
	//----------------------------------------FM----------------------------------------
	STEP_ON {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new FieldScriptAccessObject(), name);
		}

	},
	STEP_DOWN {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new FieldScriptAccessObject(), name);
		}

	},
	APPROACH {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new FieldScriptAccessObject(), name);
		}

	},
	LEAVE {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new FieldScriptAccessObject(), name);
		}

	},
	TOUCH {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new FieldScriptAccessObject(), name);
		}

	},
	TALK {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new FieldScriptAccessObject(), name);
		}

	},
	STEP {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new StatusUpdateSAO(), name);
		}

	},
	//----------------------------------------STATUS_EFFECT----------------------------------------
	IM_DEAD {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new StatusUpdateSAO(), name);
		}
	},
	IM_RESURRECT {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new StatusUpdateSAO(), name);
		}
	},
	FM_WALK {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new StatusUpdateSAO(), name);
		}
	},
	GET_ITEM {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new ItemGetSAO().setRequireEqip(true), name);
		}
	},
	DROP_ITEM {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new ItemGetSAO().setRequireEqip(false), name);
		}
	},
	EQIP_ITEM {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new ItemEqipSAO().setRequireEqip(true), name);
		}
	},
	UNEQIP_ITEM {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new ItemEqipSAO().setRequireEqip(false), name);
		}

	},
	ADD_CONDITION {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new ConditionChangeScriptAccessObject().setRequireAdd(true), name);
		}
	},
	REMOVE_CONDITION {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new ConditionChangeScriptAccessObject().setRequireAdd(false), name);
		}
	},
	EFFECT {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new StatusEffectSAO(), name);
		}
	},
	EFFECT2{
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new StatusEffectSAO(), name);
		}
	},
	//----------------------------------------THIS ITEM----------------------------------------
	GET_THIS_ITEM {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new ItemGetSAO().setRequireEqip(true), name);
		}
	},
	DROP_THIS_ITEM {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new ItemGetSAO().setRequireEqip(false), name);
		}
	},
	EQIP_THIS_ITEM {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new ItemEqipSAO().setRequireEqip(true), name);
		}
	},
	UNEQIP_THIS_ITEM {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new ItemEqipSAO().setRequireEqip(false), name);
		}
	},
	//----------------------------------------USE----------------------------------------
	USE_FIELD {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return check(new FieldScriptAccessObject(), name);
		}
	},
	USE_BATTLE {
		@Override
		public ScriptAccessObject getSAO(String name) {
			return null;
		}
	};

	public abstract ScriptAccessObject getSAO(String name);

	private static ScriptAccessObject check(ScriptAccessObject sao, String name) throws ScriptSyntaxException {
		if (!sao.getClass().getName().toUpperCase().endsWith(name.toUpperCase())) {
			throw new ScriptSyntaxException("SF : SAO missmatch : " + sao);
		}
		return sao;
	}

	public static boolean has(String n) {
		for (var v : values()) {
			if (n.toUpperCase().equals(v.toString())) {
				return true;
			}
		}
		return false;
	}

}
