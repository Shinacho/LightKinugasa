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
package kinugasa.ui;

import kinugasa.game.I18NText;
import kinugasa.game.annotation.Immutable;
import kinugasa.game.annotation.Nullable;
import kinugasa.graphics.KImage;

/**
 * TextSpeaker.<br>
 *
 * @vesion 1.0.0 - 2025/08/10_2:08:38<br>
 * @author Shinacho.<br>
 */
@Immutable
public interface MWSpeaker {

	@Nullable
	public KImage getMWSpeakerImage();

	@Nullable
	public I18NText getMWSpeakerName();

	public static MWSpeaker of(I18NText name, KImage image) {
		return new MWSpeaker() {
			@Override
			public KImage getMWSpeakerImage() {
				return image;
			}

			@Override
			public I18NText getMWSpeakerName() {
				return name;
			}
		};
	}
}
