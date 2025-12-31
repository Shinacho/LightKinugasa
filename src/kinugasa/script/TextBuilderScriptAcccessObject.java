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

import java.util.ArrayList;
import java.util.List;
import kinugasa.game.I18NText;
import kinugasa.system.UniversalValue;
import kinugasa.ui.Text;

/**
 * TextBuilderScriptAcccessObject.<br>
 *
 * @vesion 1.0.0 - 2025/10/03_22:33:36<br>
 * @author Shinacho.<br>
 */
public class TextBuilderScriptAcccessObject extends ScriptAccessObject {

	private final List<I18NText> list = new ArrayList<>();

	public TextBuilderScriptAcccessObject() {
	}

	@ScriptAccessMethod
	public TextBuilderScriptAcccessObject add(UniversalValue t) {
		list.add(t.asI18N());
		return this;
	}

	@ScriptAccessMethod
	public TextBuilderScriptAcccessObject add(UniversalValue t, UniversalValue o1) {
		list.add(t.asI18N().set(o1));
		return this;

	}

	@ScriptAccessMethod
	public TextBuilderScriptAcccessObject add(UniversalValue t, UniversalValue o1, UniversalValue o2) {
		list.add(t.asI18N().set(o1, o2));
		return this;

	}

	@ScriptAccessMethod
	public TextBuilderScriptAcccessObject add(UniversalValue t, UniversalValue o1, UniversalValue o2, UniversalValue o3) {
		list.add(t.asI18N().set(o1, o2, o3));
		return this;

	}

	@ScriptAccessMethod
	public TextBuilderScriptAcccessObject add(UniversalValue t, UniversalValue o1, UniversalValue o2, UniversalValue o3, UniversalValue o4) {
		list.add(t.asI18N().set(o1, o2, o3, o4));
		return this;

	}

	@ScriptAccessMethod
	public TextBuilderScriptAcccessObject add(UniversalValue t, UniversalValue o1, UniversalValue o2, UniversalValue o3, UniversalValue o4, UniversalValue o5) {
		list.add(t.asI18N().set(o1, o2, o3, o4, o5));
		return this;
	}

	@ScriptAccessMethod
	public TextBuilderScriptAcccessObject add(UniversalValue t, UniversalValue o1, UniversalValue o2, UniversalValue o3, UniversalValue o4, UniversalValue o5, UniversalValue o6) {
		list.add(t.asI18N().set(o1, o2, o3, o4, o5, o6));
		return this;
	}

	@ScriptAccessMethod
	public TextBuilderScriptAcccessObject of(UniversalValue v) {
		list.add(v.asI18Nd());
		return this;
	}

	@ScriptAccessMethod
	public TextBuilderScriptAcccessObject br() {
		list.add(I18NText.BR);
		return this;
	}

	@ScriptAccessMethod
	public TextBuilderScriptAcccessObject indent(UniversalValue n) {
		list.add(I18NText.indent(n.asInt()));
		return this;
	}

	public I18NText build() {
		return I18NText.of(list);
	}
}
