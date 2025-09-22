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
package kinugasa.game;

import kinugasa.graphics.RenderingQuality;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import javax.swing.ImageIcon;

/**
 * ゲーム起動時の画面サイズなどの設定です。ゲーム起動後にこのクラスを変更しても、反映されません。
 *
 * @vesion 1.0.0 - 2021/08/17_5:42:55<br>
 * @author Shinacho<br>
 */
public class GameOption implements GameOptionValue {

	public enum Keys {

	}
	private final String title;
	private Color backColor = new Color(0, 132, 116);
	private Point windowLocation = new Point(0, 0);
	private Dimension windowSize = new Dimension(720, 480);
	private float drawSize = 1f;
	private boolean useMouse = true;
	private boolean useKeyboard = true;
	private boolean useGamePad = false;

	private boolean useLog = true;
	private String logPath = "./";
	private String logName = "log_" + new SimpleDateFormat("yyyyMMddHHmmssSSS")
			.format(Date.from(Instant.now())) + ".log";

	private int fps = 60;
	private RenderingQuality rq = RenderingQuality.SPEED;
	private I18NReader i18nReader = null;

	private boolean updateIfNotActive = false;

	private String[] args = new String[]{};
	private boolean debugMode = false;
	private boolean useLock = false;

	private ImageIcon icon = new ImageIcon(getClass().getResource("icon.png"));

	private CloseEvent closeEvent = null;

	public GameOption() {
		this("My Game");
	}

	public GameOption(String name) {
		this.title = name;
	}

	public GameOption setCenterOfScreen() {
		Point centerOfScreen = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		this.windowLocation.x = centerOfScreen.x - windowSize.width / 2;
		this.windowLocation.y = centerOfScreen.y - windowSize.height / 2;
		if (windowLocation.x < 0) {
			windowLocation.x = 0;
		}
		if (windowLocation.y < 0) {
			windowLocation.y = 0;
		}
		return this;
	}

	public GameOption setBackColor(Color backColor) {
		this.backColor = backColor;
		return this;
	}

	public GameOption setWindowLocation(Point windowLocation) {
		this.windowLocation = windowLocation;
		return this;
	}

	public GameOption setWindowSize(Dimension windowSize) {
		this.windowSize = windowSize;
		return this;
	}

	public GameOption setDrawSize(float drawSize) {
		this.drawSize = drawSize;
		return this;
	}

	public GameOption setUseMouse(boolean useMouse) {
		this.useMouse = useMouse;
		return this;
	}

	public GameOption setUseKeyboard(boolean useKeyboard) {
		this.useKeyboard = useKeyboard;
		return this;
	}

	public GameOption setUseGamePad(boolean useGamePad) {
		this.useGamePad = useGamePad;
		return this;
	}

	public GameOption setUseLog(boolean useLog) {
		this.useLog = useLog;
		return this;
	}

	public GameOption setLogPath(String logPath) {
		this.logPath = logPath;
		return this;
	}

	public GameOption setLogName(String logName) {
		this.logName = logName;
		return this;
	}

	public GameOption setFps(int fps) {
		this.fps = fps;
		return this;
	}

	public GameOption setRenderingQuality(RenderingQuality rq) {
		this.rq = rq;
		return this;
	}

	public GameOption setI18nReader(I18NReader i18nReader) {
		this.i18nReader = i18nReader;
		return this;
	}

	public GameOption setUpdateIfNotActive(boolean updateIfNotActive) {
		this.updateIfNotActive = updateIfNotActive;
		return this;
	}

	public GameOption setArgs(String[] args) {
		this.args = args;
		return this;
	}

	public GameOption setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
		return this;
	}

	public GameOption setUseLock(boolean useLock) {
		this.useLock = useLock;
		return this;
	}

	public GameOption setIcon(ImageIcon icon) {
		this.icon = icon;
		return this;
	}

	public GameOption setCloseEvent(CloseEvent closeEvent) {
		this.closeEvent = closeEvent;
		return this;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Color getBackColor() {
		return backColor;
	}

	@Override
	public Point getWindowLocation() {
		return windowLocation;
	}

	@Override
	public Dimension getWindowSize() {
		return windowSize;
	}

	@Override
	public float getDrawSize() {
		return drawSize;
	}

	@Override
	public boolean isUseMouse() {
		return useMouse;
	}

	@Override
	public boolean isUseKeyboard() {
		return useKeyboard;
	}

	@Override
	public boolean isUseGamePad() {
		return useGamePad;
	}

	@Override
	public boolean isUseLog() {
		return useLog;
	}

	@Override
	public String getLogPath() {
		return logPath;
	}

	@Override
	public String getLogName() {
		return logName;
	}

	@Override
	public int getFps() {
		return fps;
	}

	@Override
	public RenderingQuality getRenderingQuality() {
		return rq;
	}

	@Override
	public I18NReader getI18nReader() {
		return i18nReader;
	}

	@Override
	public boolean isUpdateIfNotActive() {
		return updateIfNotActive;
	}

	@Override
	public String[] getArgs() {
		return args;
	}

	@Override
	public boolean isDebugMode() {
		return debugMode;
	}

	@Override
	public boolean isUseLock() {
		return useLock;
	}

	@Override
	public ImageIcon getIcon() {
		return icon;
	}

	@Override
	public CloseEvent getCloseEvent() {
		return closeEvent;
	}

	@Override
	public String toString() {
		return "GameOption{" + "title=" + title + ", backColor=" + backColor + ", windowLocation=" + windowLocation + ", windowSize=" + windowSize + ", drawSize=" + drawSize + ", useMouse=" + useMouse + ", useKeyboard=" + useKeyboard + ", useGamePad=" + useGamePad + ", useLog=" + useLog + ", logPath=" + logPath + ", logName=" + logName + ", fps=" + fps + ", rq=" + rq + ", i18nReader=" + i18nReader + ", updateIfNotActive=" + updateIfNotActive + ", args=" + args + ", debugMode=" + debugMode + ", useLock=" + useLock + ", icon=" + icon + ", closeEvent=" + closeEvent + '}';
	}

}
