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


package kinugasa.resource.sound;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import kinugasa.game.DescSupport;
import kinugasa.game.GameLog;
import kinugasa.game.I18NText;
import kinugasa.game.NotNewInstance;
import kinugasa.game.NotNull;
import kinugasa.game.Nullable;
import kinugasa.game.UnneedTranslate;
import kinugasa.game.VisibleNameSupport;
import kinugasa.game.system.GameSystem;
import kinugasa.object.UIDKey;
import kinugasa.object.UIDSupport;
import kinugasa.object.UIDValue;
import kinugasa.resource.FileIOException;
import kinugasa.resource.Input;
import kinugasa.resource.InputStatus;
import kinugasa.resource.Updateable;
import kinugasa.util.StopWatch;

/**
 *
 * @vesion 1.0.0 - 2024/08/14_22:10:39<br>
 * @author Shinacho<br>
 */
public final class Sound
		implements Input<Sound>, UIDSupport, VisibleNameSupport, DescSupport, Updateable<Sound.State>, Comparable<Sound> {

	@UnneedTranslate
	public enum State {
		NOT_YET_LOADED,
		LOADED_STOP,
		PLAYING,
		PAUSING,;
	}

	@UnneedTranslate
	public enum Type {
		BGM,
		SE,;
	}

	//-------------------------------------
	private final UIDKey uidk;
	private final UIDValue uidv;
	private final File file;
	private final Type type;
	private int trackNo;
	private State soundStatus;
	private InputStatus inputStatus;
	private MasterGain masterGain;
	private FadeOutModel fadeOutModel;
	private LoopPoint loopPoint;
	private Clip clip;
	private I18NText visibleName, desc;
	private int pauseLocation = -1;
	//-------------------------------------

	public Sound(Type t, String fileName) {
		this(fileName, t, new File(fileName));
	}

	public Sound(String id, Type t, String fileName) {
		this(id, t, new File(fileName));
	}

	public Sound(String id, Type t, File f) {
		this.uidk = UIDKey.of(id);
		this.type = t;
		this.file = f;
		this.soundStatus = State.NOT_YET_LOADED;
		this.inputStatus = InputStatus.NOT_LOADED;
		this.uidv = createSoftUIDValue();
		this.masterGain = new MasterGain(1f);
		this.fadeOutModel = null;
		this.loopPoint = null;
		this.clip = null;
	}

	@Override
	public Sound.State update() {
		if (soundStatus == State.PLAYING) {
			if (fadeOutModel != null) {
				fadeOutModel.set(this);
				if (fadeOutModel.update() == FadeOutResult.END) {
					stop();
				}
			}
		}
		return soundStatus;
	}

	@Override
	public boolean isRunning() {
		return soundStatus != State.NOT_YET_LOADED && soundStatus != State.LOADED_STOP;
	}

	@Override
	public boolean isEnded() {
		return soundStatus == State.NOT_YET_LOADED || soundStatus == State.LOADED_STOP;
	}

	public Sound play() {
		if (inputStatus == InputStatus.NOT_LOADED) {
			throw new IllegalStateException("sound " + this.getId() + " is not yet loaded");
		}
		if (soundStatus == State.PLAYING) {
			return this;
		}
		if (loopPoint != null) {
			if (pauseLocation != -1) {
				clip.setFramePosition(pauseLocation);
				pauseLocation = -1;
			}
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
			if (pauseLocation != -1) {
				clip.setFramePosition(pauseLocation);
				pauseLocation = -1;
			}
			clip.start();
		}
		soundStatus = State.PLAYING;
		return this;
	}

	public Sound pause() {
		if (inputStatus == InputStatus.NOT_LOADED) {
			throw new IllegalStateException("sound " + this.getId() + " is not yet loaded");
		}
		if (soundStatus == State.LOADED_STOP) {
			return this;
		}
		pauseLocation = clip.getFramePosition();
		clip.stop();
		soundStatus = State.PAUSING;
		return this;
	}

	public Sound stop() {
		if (inputStatus == InputStatus.NOT_LOADED) {
			return this;
		}
		if (soundStatus == State.LOADED_STOP) {
			return this;
		}
		clip.stop();
		pauseLocation = -1;
		soundStatus = State.LOADED_STOP;
		return this;
	}

	public Sound stopAndPlay() {
		stop();
		play();
		return this;
	}

	public int getTrackNo() {
		return trackNo;
	}

	public Sound setTrackNo(int trackNo) {
		this.trackNo = trackNo;
		return this;
	}

	public State getSoundStatus() {
		return soundStatus;
	}

	@NotNull
	public MasterGain getMasterGain() {
		return masterGain;
	}

	public Sound setMasterGain(MasterGain masterGain) {
		if (!this.masterGain.equals(masterGain) && masterGain != null) {
			this.masterGain = masterGain;
		}
		return this;
	}

	public int getPauseLocation() {
		return pauseLocation;
	}

	public Sound setMasterGainNow() {
		if (getInputStatus() == InputStatus.NOT_LOADED) {
			return this;
		}
		try {
			float val = this.masterGain.value();
			((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float) Math.log10(val) * 20);
		} catch (IllegalArgumentException e) {
			GameLog.print("! > Sound : [" + getId() + "] : unsupported control[master gain]");
		}
		return this;
	}

	@Nullable
	public FadeOutModel getFadeOut() {
		return fadeOutModel;
	}

	public Sound setFadeOutNow(FadeOutModel fadeOutModel) {
		this.fadeOutModel = fadeOutModel;
		return this;
	}

	public Sound setFadeOutNow(int frame) {
		this.setFadeOutNow(new FadeOutModel(frame));
		return this;
	}

	public LoopPoint getLoopPoint() {
		return loopPoint;
	}

	public Sound setLoopPoint(LoopPoint loopPoint) {
		this.loopPoint = loopPoint;
		return this;
	}

	public Sound setLoopPointNow(LoopPoint loopPoint) {
		setLoopPoint(loopPoint);
		stop();
		play();
		return this;
	}

	@Deprecated
	@NotNewInstance
	public Clip getClip() {
		return clip;
	}

	public Type getType() {
		return type;
	}

	public Sound setVisibleName(I18NText visibleName) {
		this.visibleName = visibleName;
		return this;
	}

	public Sound setDesc(I18NText desc) {
		this.desc = desc;
		return this;
	}

	//-------------------------------------
	//-------------------------------------------------------------------------
	@Override
	@NotNewInstance
	@Deprecated
	public File getFile() {
		return file;
	}

	@Override
	public Sound load() throws FileIOException {
		if (getInputStatus() == InputStatus.LOADED) {
			return this;
		}
		StopWatch watch = new StopWatch().start();
		try (AudioInputStream stream = AudioSystem.getAudioInputStream(getFile())) {
			DataLine.Info dInfo = new DataLine.Info(Clip.class, stream.getFormat());
			clip = (Clip) AudioSystem.getLine(dInfo);
			clip.open(stream);
			setMasterGainNow();
			if (loopPoint != null) {
				clip.setLoopPoints(loopPoint.getTo().VALUE, loopPoint.getFrom().VALUE);
			}
		} catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
			throw new FileIOException(ex);
		}
		watch.stop();
		inputStatus = InputStatus.LOADED;
		soundStatus = State.LOADED_STOP;
		if (GameSystem.isDebugMode()) {
			GameLog.print("Sound is loaded id=[" + getId() + "](" + watch.getTime() + " ms)");
		}
		return this;
	}

	@Override
	public void dispose() {
		if (getInputStatus() == InputStatus.NOT_LOADED) {
			return;
		}
		stop();
		clip.flush();
		clip.close();
		clip = null;
		inputStatus = InputStatus.NOT_LOADED;
		soundStatus = State.NOT_YET_LOADED;
		if (GameSystem.isDebugMode()) {
			GameLog.print("Sound : [" + getId() + "] : is disposed");
		}
	}

	@Override
	public InputStatus getInputStatus() {
		return inputStatus;
	}

	@Override
	public UIDKey getUIDKey() {
		return uidk;
	}

	@Override
	public UIDValue getUIDValue() {
		return uidv;
	}

	@Override
	public I18NText getVisibleName() {
		return visibleName;
	}

	@Override
	public I18NText getDesc() {
		return desc;
	}

	@Override
	public int compareTo(Sound o) {
		if (this.trackNo != o.trackNo) {
			return Integer.compare(trackNo, o.trackNo);
		}
		return getUIDKey().getId().compareTo(o.getUIDKey().getId());
	}
	//--------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Sound{" + "uidv=" + uidv + ", file=" + file + ", type=" + type + ", trackNo=" + trackNo + ", soundStatus=" + soundStatus + ", inputStatus=" + inputStatus + ", masterGain=" + masterGain + ", fadeOutModel=" + fadeOutModel + ", loopPoint=" + loopPoint + ", visibleName=" + visibleName + ", desc=" + desc + '}';
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 79 * hash + Objects.hashCode(this.uidv);
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
		final Sound other = (Sound) obj;
		return Objects.equals(this.uidv, other.uidv);
	}

}
