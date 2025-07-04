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

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import kinugasa.game.GraphicsContext;
import kinugasa.graphics.ImageUtil;
import kinugasa.graphics.KImage;
import kinugasa.util.FrameTimeCounter;
import kinugasa.util.TimeCounter;
import kinugasa.util.TimeCounterState;

/**
 *
 * @vesion 1.0.0 - 2021/11/25_15:42:51<br>
 * @author Shinacho<br>
 */
public class SimpleMessageWindowModel extends MessageWindowModel {

	private String nextIcon = ">";
	private boolean iconVisible = false;

	private static String selectIcon = ">";
	private static boolean selectIconVisible = false;

	public SimpleMessageWindowModel setNextIcon(String nextIcon) {
		this.nextIcon = nextIcon;
		return this;
	}

	public String getNextIcon() {
		return nextIcon;
	}

	public static String getSelectIcon() {
		return selectIcon;
	}

	public static void setSelectIcon(String selectIcon) {
		SimpleMessageWindowModel.selectIcon = selectIcon;
	}

	private static TimeCounter tc = new FrameTimeCounter(30);

	private Color border1 = Color.WHITE;
	private Color border2 = Color.BLACK;
	private Color inner1 = new Color(9, 18, 32);
	private Color inner2 = new Color(24, 32, 66);
	private FontModel font;
	public final static int BORDER_SIZE = 2;
	private static final float CHARA_IMAGE_W = 235;
	private static final float CHARA_IMAGE_H = 235;
	private KImage charaImage;

	public SimpleMessageWindowModel() {
		font = FontModel.DEFAULT.clone();
	}

	public SimpleMessageWindowModel(String nextIcon) {
		this();
		setNextIcon(nextIcon);
	}

	@Override
	public SimpleMessageWindowModel clone() {
		return (SimpleMessageWindowModel) super.clone(); //To change body of generated methods, choose Tools | Templates.
	}

	public Color getBorder1() {
		return border1;
	}

	public void setBorder1(Color border1) {
		this.border1 = border1;
	}

	public Color getBorder2() {
		return border2;
	}

	public void setBorder2(Color border2) {
		this.border2 = border2;
	}
//
//	public Color getInner() {
//		return inner;
//	}
//
//	public void setInner(Color inner) {
//		this.inner = inner;
//	}

	public FontModel getFont() {
		return font;
	}

	public void setFont(FontModel font) {
		this.font = font;
	}

	@Override
	public void draw(GraphicsContext g, MessageWindow mw) {
		if (!mw.isVisible() || !mw.isExist()) {
			return;
		}
		int x = (int) mw.getX();
		int y = (int) mw.getY();
		int w = (int) mw.getWidth();
		int h = (int) mw.getHeight();
		if (w <= 0 || h <= 0) {
			return;
		}
		Graphics2D g2 = g.create();
		g2.setColor(border1);
		g2.fillRect(x, y, w, h);
		g2.setColor(border2);
		g2.fillRect(x + BORDER_SIZE, y + BORDER_SIZE, w - BORDER_SIZE * 2, h - BORDER_SIZE * 2);
		g2.setColor(border1);
		g2.fillRect(x + BORDER_SIZE * 2, y + BORDER_SIZE * 2, w - BORDER_SIZE * 4, h - BORDER_SIZE * 4);

		GradientPaint paint = new GradientPaint(
				x + BORDER_SIZE * 3,
				y + BORDER_SIZE * 3,
				inner1,
				x + w - BORDER_SIZE * 6,
				y + h - BORDER_SIZE * 6,
				inner2);
		Paint p = g2.getPaint();
		//グラデーションでインナー描画
		g2.setPaint(paint);
		g2.fillRect(x + BORDER_SIZE * 3, y + BORDER_SIZE * 3, w - BORDER_SIZE * 6, h - BORDER_SIZE * 6);

		//ペイントをもとに戻す
		g2.setPaint(p);

		g2.setColor(font.getColor());
		x += BORDER_SIZE * 5;
		float size = font == null || font.getFont() == null ? g2.getFont().getSize() : font.getFont().getSize();
		y += BORDER_SIZE * 5 + size;

		String visibleText = mw.getVisibleText();
		String[] text = visibleText.contains(Text.getLineSep()) ? visibleText.split(Text.getLineSep()) : new String[]{visibleText};
		if (font != null) {
			g2.setFont(font.getFont());
		}
		for (String t : text) {
			g2.drawString(t, x, y);
			y += size + BORDER_SIZE * 2;
		}
		y += size;
		// オプションと選択の表示
		if (mw.isAllVisible()) {
			if (mw.getText() instanceof Choice) {
				y -= size / 2;
				for (int i = 0; i < mw.getChoice().getOptions().size(); i++) {
					if (i == mw.getSelect()) {
						g2.drawString(selectIcon, x, y);
					}
					String optionVal = mw.getChoice().getOptions().get(i).getText();
					g2.drawString(optionVal, x + (size * 2), y);
					y += BORDER_SIZE * 3 + size;
				}
			}
		}

		if (mw.isAllVisible()) {
			if (tc.update().is(TimeCounterState.ACTIVE)) {
				iconVisible = !iconVisible;
			}
			if (iconVisible) {
				x = (int) (mw.getX() + mw.getWidth() - 18);
				if (mw.getText().hasImage()) {
					x -= CHARA_IMAGE_W;
				}
				y = (int) (mw.getY() + mw.getHeight() - 12);
				g2.drawString(nextIcon, x, y);
			}
		}

		if (mw.getText().hasImage()) {
			if (charaImage == null) {
				if (mw.getText().getImage().getWidth() == CHARA_IMAGE_W && mw.getText().getImage().getHeight() == CHARA_IMAGE_H) {
					charaImage = mw.getText().getImage();
				} else {

					charaImage = charaImage.resizeTo(
							(CHARA_IMAGE_W / mw.getText().getImage().getWidth()),
							(CHARA_IMAGE_H / mw.getText().getImage().getHeight()));
				}
			}
			x = (int) (mw.getX() + mw.getWidth() - CHARA_IMAGE_W - (BORDER_SIZE * 3));
			y = (int) (mw.getY() + mw.getHeight() - CHARA_IMAGE_H - (BORDER_SIZE * 3));
			g2.drawImage(charaImage.asAWTImage(), x, y, null);
		} else {
			charaImage = null;
		}

		g2.dispose();
	}

}
