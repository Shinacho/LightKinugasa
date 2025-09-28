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
package kinugasa.system.actor;

import java.io.File;
import java.util.Iterator;
import kinugasa.game.I18NText;
import kinugasa.game.VisibleNameSupport;
import kinugasa.game.annotation.Nullable;
import kinugasa.game.annotation.RequiresReturnTypeChange;
import kinugasa.game.annotation.Virtual;
import kinugasa.field4.D2Idx;
import kinugasa.field4.FieldMap;
import kinugasa.field4.FieldMapSystem;
import kinugasa.system.BagItem;
import kinugasa.system.UniversalValue;
import kinugasa.system.actor.book.Book;
import kinugasa.system.grimoire.Greimoire;
import kinugasa.system.item.Item;
import kinugasa.graphics.Animation;
import kinugasa.graphics.KImage;
import kinugasa.object.FileObject;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.text.DataFile;
import kinugasa.resource.text.FileFormatException;
import kinugasa.util.FrameTimeCounter;
import kinugasa.ui.MWSpeaker;

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
		this(f, FieldMapSystem.getInstance().getCurrent(), FieldMapSystem.getInstance().getCamera().getPcLocation());
	}

	public Actor(File f, FieldMap fm, D2Idx initial) {
		super(f);
		this.fieldMap = fm;
		this.initialIdx = initial;
	}

	//------------------------------------------------
	private D2Idx initialIdx;
	//
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
	//
	private FieldMap fieldMap;

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

	public FieldMap getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(FieldMap fieldMap) {
		this.fieldMap = fieldMap;
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
			this.sprite.setCurrentLocationOnMap(initialIdx);
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
		//NPC MoveModel
		{
			if (pi.has("moveModel")) {
				UniversalValue moveModel = pi.get("moveModel").getValue();
				switch (moveModel.trim().safeSplit(",")[0].toUpperCase()) {
					case "LOCKED" -> {
						new FieldMapNPCMoveModelSetter(this).locked();
					}
					case "PROWL" -> {
						int d = moveModel.trim().safeSplitUV(",")[1].asInt();
						float wt = moveModel.trim().safeSplitUV(",")[2].asFloat();
						new FieldMapNPCMoveModelSetter(this).prowl(d, wt);
					}
					case "TRIP" -> {
						UniversalValue[] val = moveModel.safeSplitUV(",");
						float tt = val[1].asFloat();
						float ti = val[2].asFloat();
						int sx = val[3].asInt();
						int sy = val[4].asInt();
						new FieldMapNPCMoveModelSetter(this).trip(tt, ti, new D2Idx(sx, sy));
					}
					case "PATROL" -> {

					}
					case "RANDOM_TGT" -> {
						UniversalValue[] val = moveModel.safeSplitUV(",");
						float w1 = val[1].asFloat();
						float w2 = val[2].asFloat();
						new FieldMapNPCMoveModelSetter(this).randomTgt(w1, w2);
					}
					case "GOTO_AND_STOP" -> {
						UniversalValue[] val = moveModel.safeSplitUV(",");
						float w = val[1].asFloat();
						int x = val[2].asInt();
						int y = val[3].asInt();
						new FieldMapNPCMoveModelSetter(this).gotoAndStop(w, new D2Idx(x, y));
					}
					case "FOLLOW" -> {
						new FieldMapNPCMoveModelSetter(this).follow(this.sprite.getCurrentLocationOnMap());
					}
					default -> {
						throw new FileFormatException("Actor : undefined move model : " + moveModel);
					}
				}
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

	public FieldMapNPCMoveModelSetter setMoveModelTo() {
		return new FieldMapNPCMoveModelSetter(this);
	}

}
