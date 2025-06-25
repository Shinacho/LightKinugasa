 /*
  * MIT License
  *
  * Copyright (c) 2025 しなちょ
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */


package kinugasa.game.ui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kinugasa.object.ImagePainter;
import kinugasa.object.ImageSprite;
import kinugasa.object.MovingModel;
import kinugasa.object.KVector;
import kinugasa.graphics.KImage;

/**
 * .
 * <br>
 *
 * @version 1.0.0 - 2015/09/27<br>
 * @author Shinacho<br>
 * <br>
 */
public class ActionImageSprite extends ImageSprite implements ActionSupport {

	private List<UIAction> actions;

	public ActionImageSprite(float x, float y, float w, float h, UIAction... actions) {
		super(x, y, w, h);
		this.actions = new ArrayList<UIAction>();
		this.actions.addAll(Arrays.asList(actions));
	}

	public ActionImageSprite(float x, float y, float w, float h, BufferedImage image, UIAction... actions) {
		super(x, y, w, h, image);
		this.actions = new ArrayList<UIAction>();
		this.actions.addAll(Arrays.asList(actions));
	}

	public ActionImageSprite(float x, float y, float w, float h, KImage image, UIAction... actions) {
		super(x, y, w, h, image);
		this.actions = new ArrayList<UIAction>();
		this.actions.addAll(Arrays.asList(actions));
	}

	public ActionImageSprite(float w, float h, KVector vector, MovingModel mm, BufferedImage image, ImagePainter dm, UIAction... actions) {
		super(w, h, vector, mm, image, dm);
		this.actions = new ArrayList<UIAction>();
		this.actions.addAll(Arrays.asList(actions));
	}

	public ActionImageSprite(float w, float h, KVector vector, MovingModel mm, KImage image, ImagePainter dm, UIAction... actions) {
		super(w, h, vector, mm, image, dm);
		this.actions = new ArrayList<UIAction>();
		this.actions.addAll(Arrays.asList(actions));
	}

	public ActionImageSprite(float x, float y, float w, float h, BufferedImage image, ImagePainter model, UIAction... actions) {
		super(x, y, w, h, image, model);
		this.actions = new ArrayList<UIAction>();
		this.actions.addAll(Arrays.asList(actions));
	}

	public ActionImageSprite(float x, float y, float w, float h, KImage image, ImagePainter model, UIAction... actions) {
		super(x, y, w, h, image, model);
		this.actions = new ArrayList<UIAction>();
		this.actions.addAll(Arrays.asList(actions));
	}

	public ActionImageSprite(float x, float y, float w, float h, KVector vector, MovingModel mm, BufferedImage image, ImagePainter dm, UIAction... actions) {
		super(x, y, w, h, vector, mm, image, dm);
		this.actions = new ArrayList<UIAction>();
		this.actions.addAll(Arrays.asList(actions));
	}

	public ActionImageSprite(float x, float y, float w, float h, KVector vector, MovingModel mm, KImage image, ImagePainter dm, UIAction... actions) {
		super(x, y, w, h, vector, mm, image, dm);
		this.actions = new ArrayList<UIAction>();
		this.actions.addAll(Arrays.asList(actions));
	}

	@Override
	public void addAction(UIAction... actions) {
		this.actions.addAll(Arrays.asList(actions));
	}

	@Override
	public UIAction[] getActions() {
		return (UIAction[]) actions.toArray();
	}

	@Override
	public UIAction getAction() {
		return actions.isEmpty() ? null : actions.get(0);
	}

	@Override
	public void setActions(UIAction... actions) {
		this.actions = new ArrayList<UIAction>();
		this.actions.addAll(Arrays.asList(actions));
	}

	@Override
	public void exec() {
		for (int i = 0, size = actions.size(); i < size; i++) {
			actions.get(i).exec();
		}
	}

	@Override
	public void clearActions() {
		this.actions.clear();
	}

	private boolean selected = false;

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public void selected() {
		selected = true;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

}
