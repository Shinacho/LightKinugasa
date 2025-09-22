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
package kinugasa.game.system.actor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import kinugasa.game.I18N;
import kinugasa.game.I18NText;
import kinugasa.game.VisibleNameIDInjector;
import kinugasa.game.VisibleNameSupport;
import kinugasa.game.annotation.Nullable;
import kinugasa.game.annotation.RequiresReturnTypeChange;
import kinugasa.game.annotation.Virtual;
import kinugasa.game.system.BagItem;
import kinugasa.game.system.UniversalValue;
import kinugasa.game.system.actor.book.Book;
import kinugasa.game.system.grimoire.Greimoire;
import kinugasa.game.system.item.Item;
import kinugasa.graphics.Animation;
import kinugasa.graphics.KImage;
import kinugasa.object.FileObject;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.text.DataFile;
import kinugasa.resource.text.FileFormatException;
import kinugasa.util.FrameTimeCounter;
import kinugasa.game.ui.MWSpeaker;

/**
 * Actor.<br>
 *
 * @vesion 1.0.0 - 2025/08/13_2:57:31<br>
 * @author Shinacho.<br>
 */
public class Actor extends FileObject implements MWSpeaker, VisibleNameSupport {

	public class Status {
	}

	public class Bag<T extends BagItem> implements Iterable<T> {

		@Override
		public Iterator<T> iterator() {
			throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
		}
	}

	//------------------------------------------------
	public Actor(File f) {
		super(f);
	}

	//------------------------------------------------
	private Status status;
	private CharaSprite sprite;
	private Bag<Item> itemBag;
	private Bag<Book> bookBag;
	private Bag<Greimoire> scriptBag;
	//
	private KImage speakerImage;
	private boolean named = false;
	//
	private boolean isLoaded = false;

	public Status getStatus() {
		return status;
	}

	public Bag<Item> getItemBag() {
		return itemBag;
	}

	public Bag<Book> getBookBag() {
		return bookBag;
	}

	public CharaSprite getSprite() {
		return sprite;
	}

	//------------------------------------------------
	public Bag<Greimoire> getScriptBag() {
		return scriptBag;
	}

	@Override
	@RequiresReturnTypeChange
	public Actor load() throws FileNotFoundException, FileFormatException, ContentsIOException {
		DataFile f = super.asDataFile().load();

		//PI		
		var pi = f.get("PI");

		//WAni
		{
			this.sprite = loadSprite(pi);
			this.sprite.setId(getId());
			this.sprite.setSizeByImage();
		}
		//speaker
		{
			if (pi.has("speakerImage")) {
				this.speakerImage = pi.get("speakerImage").getValue().asKImageFile();
			}
			if (pi.has("named")) {
				this.named = pi.get("named").getValue().asBoolean();
			}
		}
		loadPI(pi);

		f.free();
		isLoaded = true;
		return this;
	}

	@Virtual
	protected CharaSprite loadSprite(DataFile.Element pi) {
		int waTime = pi.get("waTime").getValue().asInt();
		KImage image = pi.get("sprite").getValue().asKImageFile();
		Animation south = new Animation(new FrameTimeCounter(waTime), image.splitRows(0, 16, 16));
		Animation north = new Animation(new FrameTimeCounter(waTime), image.splitRows(16, 16, 16));
		Animation east = new Animation(new FrameTimeCounter(waTime), image.splitRows(32, 16, 16));
		Animation west = new Animation(new FrameTimeCounter(waTime), image.splitRows(48, 16, 16));
		WalkAnimation wa = new WalkAnimation(north, south, east, west);
		return new CharaSprite(wa);
	}

	@Virtual
	protected void loadPI(DataFile.Element pi) {
	}

	@Override
	public void free() {
		status = null;
		sprite = null;
		isLoaded = false;
	}

	@Override
	public boolean isLoaded() {
		return isLoaded;
	}

	public void update() {
		if (sprite.getMoveModel() != null) {
			sprite.getMoveModel().moveToTgt();
		}
	}

	@Override
	public KImage getMWSpeakerImage() {
		return speakerImage;
	}

	@Override
	public I18NText getVisibleName() {
		if (named) {
			return new I18NText(getId());
		}
		return I18NText.EMPTY;
	}

	@Override
	@Nullable
	public I18NText getMWSpeakerName() {
		return getVisibleName();
	}

}
