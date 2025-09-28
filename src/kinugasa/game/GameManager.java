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

import kinugasa.game.annotation.LoopCall;
import kinugasa.game.annotation.OneceTime;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import kinugasa.game.input.GamePadConnection;
import kinugasa.game.input.InputState;
import kinugasa.game.input.KeyConnection;
import kinugasa.game.input.MouseConnection;
import kinugasa.system.GameSystem;
import kinugasa.graphics.ImageUtil;
import kinugasa.graphics.RenderingQuality;
import kinugasa.resource.TempFileStorage;
import kinugasa.resource.sound.SoundSystem;
import kinugasa.util.MathUtil;

/**
 * ゲームのスーパークラスです.
 *
 * @vesion 1.0.0 - 2021/08/17_5:41:45<br>
 * @author Shinacho<br>
 */
public abstract class GameManager {

	private static GameManager instance;

	public static GameManager getInstance() {
		return instance;
	}

	/**
	 * 起動時設定.
	 */
	private GameOptionValue option;
	/**
	 * ウインドウ.
	 */
	private AWTGameWindow window;
	private GameLoop loop;
	private GameTimeManager gameTimeManager;
	private boolean updateIfNotActive;

	private Graphics2D g;
	private BufferStrategy graphicsBuffer;
	private Rectangle clippingRectangle;
	private RenderingHints renderingHints;
	private int fps;
	private float drawSize = 0;
	private BufferedImage image;
	private List<ScreenEffect> effects = new ArrayList<>();
	private List<UpdateLogicInjector> updateLogicInjectors = new ArrayList<>();

	protected GameManager(GameOptionValue option) throws IllegalStateException {
		if (instance != null) {
			throw new IllegalStateException("game instance is already exist");
		}
		try {
			//LnF切替
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException
				| InstantiationException
				| IllegalAccessException
				| UnsupportedLookAndFeelException ex) {
		}

		GameManager.instance = this;//safe
		this.option = option;
		updateOption();
	}

	@OneceTime
	protected final void updateOption() {
		MathUtil.init();
		if (option.isUseLog()) {
			GameLog.usingLog(option.getLogPath() + option.getLogName());
			try {
				FileHandler handler = new FileHandler(option.getLogPath() + option.getLogName());
				handler.setLevel(Level.ALL);
				handler.setFormatter(new Formatter() {

					final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

					@Override
					public String format(LogRecord record) {
						StringBuilder line = new StringBuilder();
						line.append(DATE_FORMAT.format(new Date(record.getMillis()))).append(' ');
						line.append(record.getLevel().getName()).append(' ');
						if (record.getThrown() != null) {
							line.append("(exception)").append(record.getThrown().getMessage());
						} else {
							line.append(record.getMessage());
						}
						line.append(System.lineSeparator());
						return line.toString();
					}
				});

				Logger.getGlobal().addHandler(handler);
			} catch (IOException | SecurityException ex) {
				GameLog.print(ex);
			}
			GameLog.print("this is " + option.getLogPath() + option.getLogName());
		}
		CMDargs.init(option.getArgs());

		if (I18N.getReader() == null && option.getI18nReader() != null) {
			I18N.init(option.getI18nReader());
		}

		if (LockUtil.isExistsLockFile() && option.isUseLock()) {
			throw new IllegalStateException(Arrays.toString(LockUtil.listLockFile()));
		}
		if (option.isUseLock()) {
			LockUtil.createLockFile();
		}
		window = new AWTGameWindow(this);
		if (option.getRenderingQuality() != null) {
			renderingHints = option.getRenderingQuality().getRenderingHints();
		}

		if (option.getCloseEvent() != null) {
			window.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					if (option.getCloseEvent().isClose()) {
						gameExit();
					}
				}
			});
		} else {
			window.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					gameExit();
				}
			});

		}

		window.setTitle(option.getTitle());
		window.setIconImage(option.getIcon().getImage());
		window.setBackground(option.getBackColor());
		window.setLocation(option.getWindowLocation());
		{
			int w = (int) (option.getWindowSize().width * option.getDrawSize());
			int h = (int) (option.getWindowSize().height * option.getDrawSize());
			window.setSize(new Dimension(w, h));
		}
		window.setResizable(false);

		this.fps = option.getFps();
		this.updateIfNotActive = option.isUpdateIfNotActive();
		this.drawSize = option.getDrawSize();

		PlayerConstants playerConstants = PlayerConstants.getInstance();

		playerConstants.setUsingKeyboard(option.isUseKeyboard());
		if (playerConstants.isUsingKeyboard()) {
			KeyConnection.setListener(window);
		}

		playerConstants.setUsingMouse(option.isUseMouse());
		if (playerConstants.isUsingMouse()) {
			MouseConnection.setListener(window);
		} else {
			BufferedImage cursorImage = ImageUtil.newImage(16, 16);
			window.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(), "game cursor"));
		}

		playerConstants.setUsingGamePad(option.isUseGamePad());
		if (playerConstants.isUsingGamePad()) {
			GamePadConnection.init();
		}

		GameSystem.setDebugMode(option.isDebugMode());

	}

	public List<ScreenEffect> getEffects() {
		return effects;
	}

	public List<UpdateLogicInjector> getUpdateLogicInjectors() {
		return updateLogicInjectors;
	}

	public void setEffects(List<ScreenEffect> effects) {
		this.effects = effects;
	}

	public void clearEffects() {
		effects.clear();
	}

	public void addEffect(ScreenEffect e) {
		effects.add(e);
	}

	public void clearEndedEffects() {
		List<ScreenEffect> remove = new ArrayList<>();
		for (ScreenEffect e : effects) {
			if (e.isEnded()) {
				remove.add(e);
			}
		}
		effects.removeAll(remove);
	}

	public GameWindow getWindow() {
		return window;
	}

	public Component getAWTComponent() {
		return window;
	}

	public final GameOptionValue getOption() {
		return option;
	}

	private boolean started = false;

	@OneceTime
	public final void gameStart() throws IllegalStateException {
		GameLog.print("GAME START");
		if (option == null) {
			throw new IllegalStateException("game option is null");
		}
		if (started) {
			throw new IllegalStateException("game is alredy started");
		}
		loop = new GameLoop(this, gameTimeManager = new GameTimeManager(fps), updateIfNotActive);
		gameTimeManager.setStartTime(System.currentTimeMillis());
		startUp();
		EventQueue.invokeLater(() -> {
			window.setVisible(true);
			window.createBufferStrategy(drawSize == 1 ? 2 : 1);
			graphicsBuffer = window.getBufferStrategy();
			clippingRectangle = window.getInternalBounds();
			started = true;
			loop.start();
			image = ImageUtil.newImage((int) (window.getInternalBounds().getWidth() / drawSize), (int) (window.getInternalBounds().getHeight() / drawSize));
		});
		GameLog.print(getWindow().getTitle() + " is start");
	}

	@OneceTime
	public final void gameExit() throws IllegalStateException {
		if (!started) {
			throw new IllegalStateException("game is not started");
		}
		if (loop != null && loop.isStarted()) {
			loop.end();
		}
		try {
			dispose();
		} catch (Throwable ex) {
			GameLog.print("ERROR : " + ex);
			System.exit(1);
		}
		//サウンド
		SoundSystem.getInstance().free();
		
		if (GameSystem.isDebugMode()) {
			GameLog.print("--not found i18n keys");
			if (I18N.getNotFoundKeySet().isEmpty()) {
				GameLog.print("->NOTHING 👍");
			} else {
				I18N.getNotFoundKeySet().forEach(GameLog::print);
			}
		}
		LockUtil.deleteLockFile();
		GameLog.close();
		TempFileStorage.getInstance().deleteAll();
		window.dispose();
		System.exit(0);
	}

	/**
	 * ゲームを開始する手順を記述します.
	 */
	@OneceTime
	protected abstract void startUp();

	/**
	 * ゲームを破棄する手順を記述します.
	 */
	@OneceTime
	protected abstract void dispose();

	/**
	 * ゲームワールドを更新する処理を記述します.
	 *
	 * @param gtm ゲームタイムマネージャーの唯一のインスタンス.
	 */
	@LoopCall
	protected abstract void update(GameTimeManager gtm, InputState is);

	/**
	 * 画面を描画する処理を記述します.
	 *
	 * @param gc ウインドウの内部領域に対応するグラフィックスコンテキスト.
	 */
	@LoopCall
	protected abstract void draw(GraphicsContext g2);

	/**
	 * 画面をリペイントします. このメソッドは内部用です。呼び出さないでください。
	 */
	@LoopCall
	final void repaint() {
		g = ImageUtil.createGraphics2D(image, RenderingQuality.NOT_USE);
		g.setBackground(window.getBackground());
		g.setClip(clippingRectangle);
		g.clearRect(clippingRectangle.x, clippingRectangle.y,
				clippingRectangle.width, clippingRectangle.height);
		if (renderingHints != null) {
			g.setRenderingHints(renderingHints);
		}
		draw(new GraphicsContext(g));
		g.dispose();

		final Dimension imageSize = new Dimension(image.getWidth(), image.getHeight());
		for (ScreenEffect e : effects) {
			image = e.doIt(image);
			if (!imageSize.equals(new Dimension(image.getWidth(), image.getHeight()))) {
				throw new ScreenEffectException("screen effect " + e + " s size is missmatch");
			}
		}

		Graphics2D g2 = (Graphics2D) graphicsBuffer.getDrawGraphics();
		g2.drawImage(image, 0, 0, (int) (image.getWidth() * drawSize), (int) (image.getHeight() * drawSize), null);
		g2.dispose();

		if (graphicsBuffer.contentsRestored()) {
			repaint();
		}
		graphicsBuffer.show();
		if (graphicsBuffer.contentsLost()) {
			repaint();
		}

	}
	
}
