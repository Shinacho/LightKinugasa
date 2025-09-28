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
package kinugasa.graphics;

import kinugasa.game.GraphicsContext;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import kinugasa.game.GameManager;
import kinugasa.game.annotation.Immutable;
import kinugasa.game.annotation.NewInstance;
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.game.annotation.Nullable;
import static kinugasa.graphics.ImageUtil.getPixel2D;
import static kinugasa.graphics.ImageUtil.setPixel2D;
import kinugasa.graphics.KImage.KRaster.KColor;
import kinugasa.object.CloneableObject;
import kinugasa.object.ImageSprite;
import kinugasa.object.Sprite;
import kinugasa.resource.ContentsIOException;
import kinugasa.util.StringUtil;

/**
 * これは編集可能な画像のインスタンスです。ファイルからロードすることもできますし、Graphics2Dを使って何かを描画することもできます。
 *
 * @vesion 1.0.0 - 2021/08/17_6:22:24<br>
 * @author Shinacho<br>
 */
@Immutable
public sealed class KImage extends CloneableObject permits KImage.MaskedArea {

	public final class KRaster implements Iterable<KImage.KRaster.KColor> {

		public final class KColor {

			private int value;

			private KColor(int value) {
				this.value = value;
			}

			@NewInstance
			public java.awt.Color asAWTColor() {
				return ARGBColor.toAWTColor(value);
			}

			public int asARGB() {
				return value;
			}

			@NotNewInstance
			public KColor to透明() {
				this.value = ARGBColor.toARGB(getR(), getG(), getB(), ARGBColor.ALPHA_TRANSPARENT);
				return this;
			}

			@NotNewInstance
			public KColor to不透明() {
				this.value = ARGBColor.toARGB(getR(), getG(), getB(), ARGBColor.ALPHA_OPAQUE);
				return this;
			}

			public int getA() {
				return ARGBColor.getAlpha(value);
			}

			public int getR() {
				return ARGBColor.getRed(value);
			}

			public int getG() {
				return ARGBColor.getGreen(value);
			}

			public int getB() {
				return ARGBColor.getBlue(value);
			}

			@NotNewInstance
			public KColor addA(int v) {
				int nv = getA() + v;
				return setA(nv);
			}

			@NotNewInstance
			public KColor mulA(float v) {
				int nv = (int) (getA() * v);
				return setA(nv);
			}

			@NotNewInstance
			public KColor addR(int v) {
				int nv = getR() + v;
				return setR(nv);
			}

			@NotNewInstance
			public KColor mulR(float v) {
				int nv = (int) (getR() * v);
				return setR(nv);
			}

			@NotNewInstance
			public KColor addG(int v) {
				int nv = getG() + v;
				return setG(nv);
			}

			@NotNewInstance
			public KColor mulG(float v) {
				int nv = (int) (getG() * v);
				return setG(nv);
			}

			@NotNewInstance
			public KColor addB(int v) {
				int nv = getB() + v;
				return setB(nv);
			}

			@NotNewInstance
			public KColor mulB(float v) {
				int nv = (int) (getB() * v);
				return setB(nv);
			}

			@NotNewInstance
			public KColor setA(int v) {
				if (v > 255) {
					v = 255;
				}
				if (v < 0) {
					v = 0;
				}
				this.value = ARGBColor.toARGB(v, getR(), getG(), getB());
				return this;
			}

			@NotNewInstance
			public KColor setR(int v) {
				if (v > 255) {
					v = 255;
				}
				if (v < 0) {
					v = 0;
				}
				this.value = ARGBColor.toARGB(getA(), v, getB(), getA());
				return this;
			}

			@NotNewInstance
			public KColor setG(int v) {
				if (v > 255) {
					v = 255;
				}
				if (v < 0) {
					v = 0;
				}
				this.value = ARGBColor.toARGB(getA(), getR(), v, getB());
				return this;
			}

			@NotNewInstance
			public KColor setB(int v) {
				if (v > 255) {
					v = 255;
				}
				if (v < 0) {
					v = 0;
				}
				this.value = ARGBColor.toARGB(getA(), getR(), getG(), v);
				return this;
			}

			@NotNewInstance
			public KColor to(int argb) {
				this.value = argb;
				return this;
			}

			@NotNewInstance
			public KColor to(int r, int g, int b, int a) {
				this.value = ARGBColor.toARGB(a, r, g, b);
				return this;
			}

			@NotNewInstance
			public KColor to(Color c) {
				this.value = ARGBColor.toARGB(c);
				return this;
			}

			@NotNewInstance
			public KColor to(KColor c) {
				this.value = c.value;
				return this;
			}

			@NotNewInstance
			public KColor reverse() {
				return new KColor(ARGBColor.reverse(value));
			}

			@NotNewInstance
			public KColor average() {
				int a = getR() + getG() + getB() / 3;
				return to(a, a, a, getA());
			}

			public boolean is完全透明() {
				return getA() == ARGBColor.ALPHA_TRANSPARENT;
			}

			public boolean is透明() {
				return getA() != ARGBColor.ALPHA_OPAQUE;
			}

			public boolean is不透明() {
				return getA() == ARGBColor.ALPHA_OPAQUE;
			}

			@NotNewInstance
			public KImage updateImage() {
				return KRaster.this.updateImage();
			}

			@Override
			public String toString() {
				return "KColor{" + "value=" + Integer.toHexString(value) + '}';
			}

			@Override
			public int hashCode() {
				int hash = 7;
				hash = 37 * hash + this.value;
				return hash;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj) {
					return true;
				}
				if (obj == null) {
					return false;
				}
				if (getClass() != obj.getClass()) {
					return false;
				}
				final KColor other = (KColor) obj;
				return this.value == other.value;
			}

		}
		private KColor[][] data;

		private KRaster() {
		}

		private void setData(KColor[][] data) {
			this.data = data;
		}

		public KColor of(int x, int y) throws GraphicsException {
			return data[y][x];
		}

		@NotNewInstance
		@Deprecated
		public KColor[][] all() {
			return data;
		}

		@NewInstance
		public KRaster set(UnaryOperator<KColor> u) {
			KColor[][] res = new KColor[getHeight()][];
			KRaster r = new KRaster();
			for (int y = 0; y < getHeight(); y++) {
				res[y] = new KColor[getWidth()];
				for (int x = 0; x < getWidth(); x++) {
					res[y][x] = u.apply(this.data[y][x]);
				}
			}
			r.setData(res);
			return r;
		}

		@NewInstance
		public KRaster fill(Supplier<KColor> s) {
			KColor[][] res = new KColor[getHeight()][];
			KRaster r = new KRaster();
			for (int y = 0; y < getHeight(); y++) {
				for (int x = 0; x < getWidth(); x++) {
					res[y][x] = s.get();
				}
			}
			r.setData(res);
			return r;
		}

		@NotNewInstance
		public KImage updateImage() {
			return KImage.this.updateImage(this);
		}

		@NewInstance
		public KImage newImage() {
			return KImage.this.clone().updateImage(this);
		}

		public KRaster rotate(float angle) {
			while (angle >= 360.0f) {
				angle -= 360.0f;
			}
			while (angle < 0.0f) {
				angle += 360.0f;
			}
			if (angle == 0f) {
				return this;
			}

			if (angle % 90 == 0) {
				for (int i = 0; i < (int) (angle / 90); i++) {
					data = rotate(data);
				}
				return this;
			} else {
				this.data = KImage.this.rotate(angle).asRaster().data;
				return this;
			}

		}

		private static KColor[][] rotate(KColor[][] p) {
			int n = p.length;
			int m = p[0].length;
			KColor[][] res = new KColor[m][n];

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					res[j][n - 1 - i] = p[i][j];
				}
			}
			return res;
		}

		public Stream<KColor> stream() {
			return Arrays.stream(data).flatMap(Arrays::stream);
		}

		@Override
		public Iterator<KColor> iterator() {
			return Arrays.stream(data).flatMap(Arrays::stream).toList().iterator();
		}

		private int[][] getPixel2D() {
			int[][] res = new int[data.length][];
			for (int y = 0; y < data.length; y++) {
				res[y] = new int[data[y].length];
				for (int x = 0; x < data[y].length; x++) {
					res[y][x] = data[y][x].value;
				}
			}
			return res;
		}

		@Override
		public int hashCode() {
			int hash = 5;
			hash = 29 * hash + Arrays.deepHashCode(this.data);
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final KRaster other = (KRaster) obj;
			return Arrays.deepEquals(this.data, other.data);
		}

	}

	@NotNewInstance
	public KImage updateImage(KRaster r) {
		ImageUtil.setPixel2D(image, r.getPixel2D());
		return this;
	}

	protected BufferedImage image;

	public KImage(BufferedImage image) {
		this.image = image;
	}

	public KImage(int w, int h) {
		this(ImageUtil.newImage(w, h));
	}

	public KImage(File f) {
		this(f.getAbsolutePath());
	}

	public KImage(String path) {
		this(ImageUtil.load(path));
	}

	protected KImage(KImage i) {
		this(i.image);
	}

	@NewInstance
	public KImage drawOnThisImage(List<? extends Sprite> d) {
		KImage r = clone();
		Graphics2D g = r.createGraphics2D(RenderingQuality.QUALITY);
		for (var v : d) {
			v.draw(g);
		}
		g.dispose();
		return r;
	}

	@NewInstance
	public KImage drawOnThisImage(Sprite... d) {
		return drawOnThisImage(Arrays.asList(d));
	}

	@NewInstance
	public KColor of(int x, int y) throws IndexOutOfBoundsException {
		return asRaster().of(x, y);
	}

	@NewInstance
	public KRaster asRaster() {
		int[][] pix = ImageUtil.getPixel2D(image);
		KColor[][] data = new KColor[getHeight()][];
		KRaster r = new KRaster();
		for (int y = 0; y < getHeight(); y++) {
			data[y] = new KColor[getWidth()];
			for (int x = 0; x < getWidth(); x++) {
				data[y][x] = r.new KColor(pix[y][x]);
			}
		}
		r.setData(data);
		return r;
	}

	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}

	@NotNewInstance
	@Deprecated
	public Image asAWTImage() {
		return image;
	}

	@NewInstance
	public ImageIcon asImageIcon() {
		return new ImageIcon(image);
	}

	@NotNewInstance
	@Deprecated
	public BufferedImage asBufferedImage() {
		return image;
	}

	@Override
	@NewInstance
	public KImage clone() {
		var r = (KImage) super.clone();
		r.image = ImageUtil.copy(image);
		return r;
	}

	@Override
	public String toString() {
		return "KImage{" + "image=" + image + '}';
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 47 * hash + Objects.hashCode(this.image);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final KImage other = (KImage) obj;
		return Objects.equals(this.image, other.image);
	}

	@NewInstance
	public Dimension getSize() {
		return new Dimension(getWidth(), getHeight());
	}

	public boolean sizeIs(KImage i) {
		return sizeIs(getSize());
	}

	public boolean sizeIs(Shape i) {
		return sizeIs(i.getBounds().getSize());
	}

	public boolean sizeIs(Dimension2D i) {
		int w = (int) i.getWidth();
		int h = (int) i.getHeight();
		return this.getWidth() == w && this.getHeight() == h;
	}

	public boolean sizeIs(Sprite i) {
		return sizeIs(i.getSize());
	}

	public void saveTo(String filePath) throws ContentsIOException {
		saveTo(new File(filePath));
	}

	public void saveTo(File f) throws ContentsIOException {
		try {
			ImageIO.write(image, "PNG", f);
		} catch (IOException ex) {
			throw new ContentsIOException(ex);
		}
	}

	public static void saveTo(String filePath, BufferedImage image) throws ContentsIOException {
		saveTo(new File(filePath), image);
	}

	public static void saveTo(File f, BufferedImage image) throws ContentsIOException {
		try {
			ImageIO.write(image, "PNG", f);
		} catch (IOException ex) {
			throw new ContentsIOException(ex);
		}
	}

	public GraphicsContext createGraphicsContext() {
		return new GraphicsContext(createGraphics2D());
	}

	public Graphics2D createGraphics2D() {
		return createGraphics2D(RenderingQuality.DEFAULT);
	}

	public Graphics2D createGraphics2D(RenderingQuality renderingPolicy) {
		Graphics2D g = image.createGraphics();
		if (renderingPolicy != null) {
			g.setRenderingHints(renderingPolicy.getRenderingHints());
		}
		g.setClip(0, 0, image.getWidth(), image.getHeight());
		return g;
	}

	@NewInstance
	public KImage fillBy(Color c) {
		KImage res = clone();
		Graphics2D g = res.createGraphics2D();
		g.setColor(c);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.dispose();
		return res;
	}

	@NewInstance
	public KImage fillBy(int c) {
		return fillBy(ARGBColor.toAWTColor(c));
	}

	@NewInstance
	public KImage fillBy(int r, int g, int b, int a) {
		return fillBy(ARGBColor.toARGB(a, r, g, b));
	}

	@NewInstance
	public List<KImage> splitX(int y, int w, int h) throws GraphicsException {
		try {
			BufferedImage[] dst = new BufferedImage[image.getWidth() / w];
			for (int i = 0, x = 0; i < dst.length; i++, x += w) {
				dst[i] = image.getSubimage(x, y, w, h);
			}
			return Arrays.asList(dst).stream().map(p -> new KImage(p)).toList();
		} catch (RasterFormatException e) {
			throw new GraphicsException(e);
		}
	}

	@NewInstance
	public List<KImage> splitY(int x, int w, int h) throws GraphicsException {
		try {
			BufferedImage[] dst = new BufferedImage[image.getHeight() / h];
			for (int i = 0, y = 0; i < dst.length; i++, y += h) {
				dst[i] = image.getSubimage(x, y, w, h);
			}
			return Arrays.asList(dst).stream().map(p -> new KImage(p)).toList();
		} catch (RasterFormatException e) {
			throw new GraphicsException(e);
		}
	}

	@NewInstance
	public List<KImage> splitX(int w, int h) throws GraphicsException {
		return splitY(0, w, h);
	}

	@NewInstance
	public List<List<KImage>> split2D(int w, int h) throws GraphicsException {
		List<List<KImage>> result = new ArrayList<>();
		for (int i = 0, y = 0; i < image.getHeight() / h; i++, y += h) {
			result.add(new ArrayList<>(splitX(y, w, h)));
		}
		return result;
	}

	@NewInstance
	public Map<String, KImage> splitAsMap(int w, int h, BiFunction<Integer, Integer, String> nameMapper)
			throws GraphicsException {
		Map<String, KImage> res = new HashMap<>();
		var list2d = split2D(w, h);
		for (int y = 0; y < list2d.size(); y++) {
			var list = list2d.get(y);
			for (int x = 0; x < list.size(); x++) {
				res.put(nameMapper.apply(x, y), list.get(x));
			}
		}

		return res;
	}

	@NewInstance
	public Map<String, KImage> splitAsMap(int w, int h) throws GraphicsException {
		return splitAsMap(w, h, DEFAULT_NAME_MAPPER);
	}

	public static final BiFunction<Integer, Integer, String> DEFAULT_NAME_MAPPER = (x, y) -> {
		String ys = StringUtil.zeroUme(y, 3);
		String xs = StringUtil.zeroUme(x, 3);
		if (ys.length() == 1) {
			ys = "0" + y;
		}
		if (xs.length() == 1) {
			xs = "0" + x;
		}
		return ys + xs;
	};

	@NewInstance
	public static KImage screenShot() throws GraphicsException {
		try {
			return new KImage(new Robot().createScreenCapture(GameManager.getInstance().getWindow().getBounds()));
		} catch (AWTException ex) {
			throw new GraphicsException(ex);
		}
	}

	@NewInstance
	public KImage tiling(int xNum, int yNum) {
		int w = (int) (this.image.getWidth() * xNum);
		int h = (int) (this.image.getHeight() * yNum);
		KImage res = new KImage(w, h);

		GraphicsContext g = res.createGraphicsContext();
		for (int y = 0; y < yNum; y++) {
			for (int x = 0; x < xNum; x++) {
				int locationX = x * getWidth();
				int locationY = y * getHeight();
				g.drawImage(this.image, locationX, locationY);
			}
		}

		g.dispose();
		return res;
	}

	@NewInstance
	public KImage tilingX(int xNum) {
		return tiling(xNum, 1, getWidth(), getHeight());
	}

	@NewInstance
	public KImage tiling(int xNum, int yNum, float drawW, float drawH) {
		int w = (int) (this.image.getWidth() * drawW * xNum);
		int h = (int) (this.image.getHeight() * drawH * yNum);
		KImage res = new KImage(w, h);

		GraphicsContext g = res.createGraphicsContext();
		int tileW = (int) (this.image.getWidth() * drawW);
		int tileH = (int) (this.image.getHeight() * drawH);
		for (int y = 0; y < yNum; y++) {
			for (int x = 0; x < xNum; x++) {
				int locationX = x * tileW;
				int locationY = y * tileH;
				g.drawImage(this.image, locationX, locationY, tileW, tileH);
			}
		}

		g.dispose();
		return res;
	}

	@NewInstance
	public KImage subImage(int x, int y, int w, int h) {
		return new KImage(image.getSubimage(x, y, w, h));
	}

	@NewInstance
	public KImage subImage(Point2D p, Dimension2D d) {
		return subImage((int) p.getX(), (int) p.getY(), (int) d.getWidth(), (int) d.getHeight());
	}

	@NewInstance
	public KImage subImage(Shape r) {
		return subImage(r.getBounds().x, r.getBounds().y,
				r.getBounds().width, r.getBounds().height);
	}

	//pがtrueになる最も左上から最も右下が返される。
	@NewInstance
	@Nullable
	public KImage autoTrimming(Predicate<KColor> p) {
		Point start = start(this, p);
		Point end = end(this, p);
		int w = end.x - start.x;
		int h = end.y - start.y;
		if (w <= 0 || h <= 0) {
			return null;
		}
		return subImage(start.x, start.y, w, h);
	}

	public static final Predicate<KColor> ALPHA_IS_TRANSPARENT = (v) -> v.getA() == ARGBColor.ALPHA_TRANSPARENT;
	public static final Predicate<KColor> ALPHA_IS_OPAQUE = (v) -> v.getA() == ARGBColor.ALPHA_OPAQUE;
	public static final Predicate<KColor> ALPHA_IS_NOT_TRANSPARENT = (v) -> v.getA() != ARGBColor.ALPHA_TRANSPARENT;
	public static final Predicate<KColor> ALPHA_IS_NOT_OPAQUE = (v) -> v.getA() != ARGBColor.ALPHA_OPAQUE;

	private static Point start(KImage src, Predicate<KColor> p) {
		KRaster raster = src.asRaster();
		KColor[][] pix = raster.all();
		Point res = new Point();
		for (int y = 0; y < pix.length; y++) {
			if (Stream.of(pix[y]).anyMatch(p)) {
				res.y = y;
				break;
			}
		}
		pix = raster.rotate(90).all();
		for (int y = 0; y < pix.length; y++) {
			if (Stream.of(pix[y]).anyMatch(p)) {
				res.x = y;
				break;
			}
		}
		return res;
	}

	private static Point end(KImage src, Predicate<KColor> p) {
		KRaster raster = src.asRaster();
		KColor[][] pix = raster.all();
		Point res = new Point();
		for (int y = pix.length - 1; y >= 0; y--) {
			if (Stream.of(pix[y]).anyMatch(p)) {
				res.y = y;
				break;
			}
		}
		pix = raster.rotate(90).all();
		for (int y = pix.length - 1; y >= 0; y--) {
			if (Stream.of(pix[y]).anyMatch(p)) {
				res.x = y;
				break;
			}
		}
		return res;
	}

	@NewInstance
	public List<KImage> fadeAnimation(float addTp) {
		List<KImage> res = new ArrayList<>();
		for (float t = 1f; t > 0 && t <= 1; t += addTp) {
			float v = t;
			res.add(this.clone().asRaster().set(p -> p.mulA(v)).updateImage());
		}
		return res;
	}

	@NewInstance
	public static KImage concatX(KImage... i) {
		return concatX(Arrays.asList(i));
	}

	@NewInstance
	public static KImage concatX(List<KImage> i) {
		if (i.isEmpty()) {
			throw new IllegalArgumentException("images is empty : images.length=[" + i.size() + "]");
		}
		int maxHeight = i.stream().mapToInt((p -> p.getHeight())).max().getAsInt();
		int width = i.stream().mapToInt(p -> p.getWidth()).sum();

		KImage res = new KImage(width, maxHeight);
		Graphics2D g = res.createGraphics2D(RenderingQuality.QUALITY);
		for (int n = 0, x = 0; n < i.size(); n++) {
			g.drawImage(i.get(n).image, x, 0, null);
			x += i.get(n).getWidth();
		}
		return res;
	}

	public boolean hasClaerPixel() {
		return asRaster().stream().anyMatch(p -> p.is完全透明());
	}

	public boolean hasOpauePixel() {
		return asRaster().stream().anyMatch(p -> p.is不透明());
	}

	@NewInstance
	public KImage replaceColor(Predicate<KColor> b, UnaryOperator<KColor> converter) {
		return asRaster().set(p -> {
			if (b.test(p)) {
				return converter.apply(p);
			}
			return p;
		}).newImage();
	}

	@NewInstance
	public KImage grayScale() {
		return asRaster().set(p -> p.average()).newImage();
	}

	@NewInstance
	public KImage weightedGrayScale() {
		KRaster rs = asRaster();
		return rs.set(p -> {
			int r = (int) (p.getR() * 0.298912f);
			int g = (int) (p.getG() * 0.586611f);
			int b = (int) (p.getB() * 0.114478f);
			return rs.new KColor(ARGBColor.toARGB(p.getA(), r, g, b));
		}).newImage();
	}

	@NewInstance
	public KImage sepia() {
		KImage i = grayScale();
		KRaster rs = i.asRaster();
		return rs.set(p -> {
			int r = (int) (p.getR() * 1.12f);
			if (r > 255) {
				r = 255;
			}
			int g = (int) (p.getG() * 0.66f);
			int b = (int) (p.getB() * 0.20f);
			return rs.new KColor(ARGBColor.toARGB(p.getA(), r, g, b));
		}).updateImage();
	}

	@NewInstance
	public KImage monochrome(int center) {
		KRaster rs = asRaster();
		return rs.set(p -> {
			int a = p.average().value;
			if (a > center) {
				return rs.new KColor(ARGBColor.WHITE);
			} else {
				return rs.new KColor(ARGBColor.BLACK);
			}
		}).newImage();
	}

	@NewInstance
	public KImage mulBrightness(float mul) {
		if (mul == 1f) {
			return clone();
		}
		if (mul < 0) {
			mul = 0;
		}
		KRaster rs = asRaster();
		float m = mul;
		return rs.set(p -> {
			int r = (int) (p.getR() * m);
			if (r > 255) {
				r = 255;
			}
			if (r < 0) {
				r = 0;
			}
			int g = (int) (p.getG() * m);
			if (g > 255) {
				g = 255;
			}
			if (g < 0) {
				g = 0;
			}
			int b = (int) (p.getB() * m);
			if (b > 255) {
				b = 255;
			}
			if (b < 0) {
				b = 0;
			}
			return p.to(r, g, b, p.getA());
		}).newImage();
	}

	@NewInstance
	public KImage reverseColor() {
		return asRaster().set(p -> p.reverse()).newImage();
	}

	@NewInstance
	public KImage mosaic(int size) throws GraphicsException {
		BufferedImage src = this.image;
		if (size < 1) {
			throw new IllegalArgumentException("size < 1 : size=[" + size + "]");
		}
		if (size > src.getWidth() || size > src.getHeight()) {
			throw new GraphicsException("size is over image bounds : size=[" + size + "]");
		}
		BufferedImage dst = ImageUtil.copy(src);
		int[][] pix = getPixel2D(src);
		for (int y = 0, imageHeight = src.getHeight(); y < imageHeight; y += size) {
			for (int x = 0, imageWidth = src.getWidth(); x < imageWidth; x += size) {
				int argb = pix[y][x];
				for (int mosaicY = y, i = 0; i < size; mosaicY++, i++) {
					for (int mosaicX = x, j = 0; j < size; mosaicX++, j++) {
						if (mosaicX <= imageWidth && mosaicY <= imageHeight) {
							pix[mosaicY][mosaicX] = argb;
						}
					}
				}
			}
		}
		ImageUtil.setPixel2D(dst, pix);
		return new KImage(dst);
	}

	@NewInstance
	public KImage rotate(float angle) {
		while (angle > 360) {
			angle -= 360;
		}
		while (angle < 0) {
			angle += 360;
		}
		if (angle == 0) {
			return clone();
		}
		KImage dst = clone();
		Graphics2D g = dst.createGraphics2D(RenderingQuality.QUALITY);
		g.setClip(0, 0, dst.getWidth(), dst.getHeight());
		g.setColor(new Color(ARGBColor.CLEAR_BLACK, true));
		g.fillRect(0, 0, dst.getWidth(), dst.getHeight());
		g.rotate(Math.toRadians(angle), dst.getWidth() / 2, dst.getHeight() / 2);
		g.drawImage(this.image, 0, 0, null);
		g.dispose();
		return dst;
	}

	@NewInstance
	public KImage setAlpha(int a) {
		return asRaster().set(p -> p.setA(a)).newImage();
	}

	@NewInstance
	public KImage addAlpha(int a) {
		return asRaster().set(p -> p.addA(a)).newImage();
	}

	@NewInstance
	public KImage mulAlpha(float t) {
		return asRaster().set(p -> p.mulA(t)).newImage();
	}

	@NewInstance
	public KImage rasterScroll(int[] shiftPixNum, int insertARGB) {
		BufferedImage dst = ImageUtil.copy(this.image);
		int[] sPix = shiftPixNum;
		if (shiftPixNum.length != dst.getHeight()) {
			sPix = new int[dst.getHeight()];
			for (int i = 0, spi = 0; i < sPix.length; i++) {
				sPix[i] = shiftPixNum[spi];
				spi = (spi < shiftPixNum.length - 1) ? spi + 1 : 0;
			}
		}
		int[][] pix = getPixel2D(dst);
		for (int y = 0; y < pix.length; y++) {
			if (sPix[y] == 0) {
				continue;
			}
			final int[] ROW = new int[pix[y].length];
			System.arraycopy(pix[y], 0, ROW, 0, ROW.length);
			int x = 0, lineIdx = 0;
			if (sPix[y] > 0) {
				Arrays.fill(pix[y], 0, sPix[y], insertARGB);
				x += sPix[y];
			} else {
				lineIdx += Math.abs(sPix[y]);
			}
			System.arraycopy(ROW, lineIdx, pix[y], x, ROW.length - Math.abs(sPix[y]));
			x += ROW.length - Math.abs(sPix[y]);
			Arrays.fill(pix[y], x, pix[y].length, insertARGB);
		}
		setPixel2D(dst, pix);
		return new KImage(dst);
	}

	@NewInstance
	public KImage resize(float scale) {
		int w = (int) (getWidth() * scale);
		int h = (int) (getHeight() * scale);
		return resize(w, h);
	}

	@NewInstance
	public KImage resize(float wScale, float hScale) {
		int w = (int) (getWidth() * wScale);
		int h = (int) (getHeight() * hScale);
		return resize(w, h);
	}

	@NewInstance
	public KImage resize(Dimension2D d) {
		return resize((int) d.getWidth(), (int) d.getHeight());
	}

	@NewInstance
	public KImage resize(Sprite s) {
		return resize(s.getSize());
	}

	@NewInstance
	public KImage resize(KImage i) {
		return resize(i.getWidth(), i.getHeight());
	}

	@NewInstance
	public KImage resize(int w, int h) {
		if (w == 0 || h == 0) {
			throw new IllegalArgumentException("resize image : size is 0");
		}
		if (getWidth() == w && getHeight() == h) {
			return clone();
		}
		KImage dst = new KImage(w, h);
		Graphics2D g2 = dst.createGraphics2D(RenderingQuality.QUALITY);
		g2.drawImage(this.image, 0, 0, w, h, null);
		g2.dispose();
		return dst;
	}

	@NewInstance
	public static List<KImage> resizeAll(float scale, KImage... images) {
		return resizeAll(scale, Arrays.asList(images));
	}

	@NewInstance
	public static List<KImage> resizeAll(float scale, List<KImage> images) {
		return resizeAll(scale, scale, images);
	}

	@NewInstance
	public static List<KImage> resizeAll(float wScale, float hScale, KImage... images) {
		return resizeAll(wScale, hScale, Arrays.asList(images));
	}

	@NewInstance
	public static List<KImage> resizeAll(float wScale, float hScale, List<KImage> images) {
		List<KImage> res = new ArrayList<>();
		for (var v : images) {
			res.add(v.resize(wScale, hScale));
		}
		return res;
	}

	@NewInstance
	public static List<KImage> resizeAll(Dimension2D d, KImage... images) {
		return resizeAll(d, Arrays.asList(images));
	}

	@NewInstance
	public static List<KImage> resizeAll(Dimension2D d, List<KImage> images) {
		return resizeAll((int) d.getWidth(), (int) d.getHeight(), images);
	}

	@NewInstance
	public static List<KImage> resizeAll(Sprite s, KImage... images) {
		return resizeAll(s.getSize(), images);
	}

	@NewInstance
	public static List<KImage> resizeAll(Sprite s, List<KImage> images) {
		return resizeAll(s.getSize(), images);
	}

	@NewInstance
	public static List<KImage> resizeAll(int w, int h, KImage... images) {
		return resizeAll(w, h, Arrays.asList(images));
	}

	@NewInstance
	public static List<KImage> resizeAll(int w, int h, List<KImage> images) {
		List<KImage> res = new ArrayList<>();
		for (var v : images) {
			res.add(v.resize(w, h));
		}
		return res;
	}

	@NewInstance
	public static List<KImage> listOf(List<? extends Sprite> s) {
		return s.stream().map(p -> p.drawOn00()).toList();
	}

	@NewInstance
	public static List<KImage> listOf(Sprite... s) {
		return listOf(Arrays.asList(s));
	}

	@NewInstance
	public List<KImage> nCopies(int n) {
		List<KImage> res = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			res.add(clone());
		}
		return res;
	}

	@NewInstance
	public static KImage composite(Sprite... s) {
		return composite(Arrays.asList(s));
	}

	@NewInstance
	public static KImage composite(List<? extends Sprite> s) {
		int w = s.stream().mapToInt(p -> (int) p.getWidth()).max().getAsInt();
		int h = s.stream().mapToInt(p -> (int) p.getHeight()).max().getAsInt();
		KImage r = new KImage(w, h);
		r.drawOnThisImage(s);
		return r;
	}

	public final class MaskedArea extends KImage {

		private final Point location;

		private MaskedArea(Point location, KImage i) {
			super(i);
			this.location = location;
		}

		@NewInstance
		public KImage commit() {
			//KImage.thisのcompositeを使ってthisをlocationに上書きする
			//resize対策として中心の座標に配置する
			int x = location.x + this.getWidth() / 2;
			int y = location.y + this.getHeight() / 2;
			return KImage.this.drawOnThisImage(new ImageSprite(x, y, this.getWidth(), this.getHeight(), this));
		}

	}

	@NewInstance
	public MaskedArea maskedAreaOf(KImage i, Predicate<KColor> p) {
		//領域算出して切り出してSubに渡す
		Point start = start(i, p);
		Point end = end(i, p);
		int x = start.x;
		int y = start.y;
		int w = end.x - start.x;
		int h = end.y - start.y;
		if (w == 0 || h == 0) {
			throw new GraphicsException("masked area size is zero");
		}
		return KImage.this.new MaskedArea(start, KImage.this.subImage(x, y, w, h));
	}

	@NewInstance
	public MaskedArea maskedAreaOf(Sprite s) {
		//spriteの位置のSubを返す
		int x = (int) s.getX();
		int y = (int) s.getY();
		int w = (int) s.getWidth();
		int h = (int) s.getHeight();
		if (w == 0 || h == 0) {
			throw new GraphicsException("masked area size is zero");
		}
		return KImage.this.new MaskedArea(new Point(x, y), KImage.this.subImage(x, y, w, h));
	}

	public void setTo(ImageSprite s) {
		s.setImage(this);
	}

	public Color averageColor() {
		int a, r, g, b;
		r = g = b = 0;
		a = 255;
		List<Integer> pix = asRaster().stream().map(p -> p.value).toList();
		for (int val : pix) {
			r += ARGBColor.getRed(val);
			g += ARGBColor.getGreen(val);
			b += ARGBColor.getBlue(val);
		}
		r /= pix.size();
		g /= pix.size();
		b /= pix.size();
		return new Color(r, g, b, a);
	}

	@NewInstance
	public Point centerLocation() {
		return new Point(getWidth() / 2, getHeight() / 2);
	}
}
