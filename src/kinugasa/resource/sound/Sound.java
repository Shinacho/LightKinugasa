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
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import kinugasa.game.GameLog;
import kinugasa.game.I18NText;
import kinugasa.game.annotation.NotNewInstance;
import kinugasa.game.annotation.NotNull;
import kinugasa.game.annotation.Nullable;
import kinugasa.game.annotation.UnneedTranslate;
import kinugasa.game.VisibleNameIDInjector;
import kinugasa.system.GameSystem;
import kinugasa.object.FileObject;
import kinugasa.resource.FileIOException;
import kinugasa.object.Updateable;
import kinugasa.resource.sound.tags.SoundTagSystem;
import kinugasa.resource.sound.tags.SoundTagData;
import kinugasa.resource.sound.tags.ID3V23TagKeys;
import kinugasa.resource.sound.tags.StandardTXXXValue;
import kinugasa.util.StopWatch;

/**
 *
 * @vesion 1.0.0 - 2024/08/14_22:10:39<br>
 * @author Shinacho<br>
 */
public final class Sound extends FileObject
		implements VisibleNameIDInjector<Sound>, Updateable<Sound.State>, Comparable<Sound> {//TrackNo順

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
	private Type type;
	private int trackNo;
	private State soundStatus;
	private MasterGain masterGain;
	private LoopPoint loopPoint;
	private Clip clip;
	private I18NText comment;
	private FadeOutModel fadeOutModel;
	private int pauseLocation = -1;
	private SoundTagData tags;
	//-------------------------------------

	Sound(String fileName) {
		this(new File(fileName));
	}

	Sound(File f) {
		super(f);
		this.type = null;
		this.soundStatus = State.NOT_YET_LOADED;;
		this.masterGain = new MasterGain(1f);
		this.fadeOutModel = null;
		this.loopPoint = null;
		this.clip = null;

	}

	@Override
	public String getId() {
		String res = super.getId();
		if (res.contains(".")) {
			res = res.substring(0, res.lastIndexOf("."));
		}
		return res;
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
		if (!isLoaded()) {
			throw new IllegalStateException("sound " + this.getId() + " is not yet loaded");
		}
		if (soundStatus == State.PLAYING) {
			return this;
		}
		if (pauseLocation == - 1) {
			clip.setFramePosition(0);
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
		if (!isLoaded()) {
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
		if (!isLoaded()) {
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
		if (!isLoaded()) {
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

	public Sound setComment(I18NText comment) {
		this.comment = comment;
		return this;
	}

	//-------------------------------------
	//-------------------------------------------------------------------------
	private boolean loaded = false;

	@Override
	public Sound load() throws FileIOException {
		if (isLoaded()) {
			return this;
		}
		StopWatch watch = new StopWatch().start();

		//ID3タグ
		setTagData();
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
		soundStatus = State.LOADED_STOP;
		GameLog.print("Sound is loaded id=[" + getId() + "](" + watch.getTime() + " ms)");
		loaded = true;
		return this;
	}

	@Override
	public void free() {
		if (!isLoaded()) {
			return;
		}
		stop();
		clip.flush();
		clip.close();
		clip = null;
		soundStatus = State.NOT_YET_LOADED;
		tags = null;
		setTag = false;
		loaded = false;
		GameLog.print("Sound : [" + getId() + "] : is disposed");
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public I18NText getComment() {
		return comment;
	}

	public Sound setAutoClose() {
		SoundSystem.getInstance().addAutoCloseSound(this);
		return this;
	}

	@Override
	public int compareTo(Sound o) {
		if (this.trackNo != o.trackNo) {
			return Integer.compare(trackNo, o.trackNo);
		}
		return getName().compareTo(o.getName());
	}

	@Nullable
	public SoundTagData getTags() {
		if (this.tags == null) {
			try {
				this.tags = SoundTagSystem.getTags(getFile());
				if (GameSystem.isDebugMode()) {
					GameLog.addIndent();
					GameLog.print("SOUND " + getId() + " / ID3Tag : " + tags.getV2Tags());
					GameLog.removeIndent();
				}
			} catch (Exception ex) {
				if (GameSystem.isDebugMode()) {
					GameLog.addIndent();
					GameLog.print("! > ID3Tag Error : " + ex);
					ex.printStackTrace();
					GameLog.removeIndent();
				}
				this.tags = null;
			}
		}
		return this.tags;
	}

	private boolean setTag = false;

	public void setTagData() {
		if (setTag) {
			return;
		}
		getTags();
		if (this.tags != null) {
			//comment
			if (this.tags.getV2Tags().containsKey(ID3V23TagKeys.COMM)) {
				setComment(this.tags.getV2Tags().get(ID3V23TagKeys.COMM).asI18N());
			}
			//TXXX
			StandardTXXXValue txxx = this.tags.getTXXXValue();
			if (GameSystem.isDebugMode()) {
				GameLog.addIndent();
				GameLog.addIndent();
				GameLog.print("TXXX : " + getId() + " / " + txxx);
				GameLog.removeIndent();
				GameLog.removeIndent();
			}
			setType(txxx.getType());
			setMasterGain(txxx.getMasterGain());
			setLoopPoint(txxx.getLoop());

			//track
			if (this.tags.getV2Tags().containsKey(ID3V23TagKeys.TRCK)) {
				setTrackNo(this.tags.getV2Tags().get(ID3V23TagKeys.TRCK).trim().asInt());
			}
			setTag = true;
		}
	}
	//--------------------------------------------------------------------------

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Sound{");
		sb.append("id=").append(getId());
		sb.append(", type=").append(type);
		sb.append(", trackNo=").append(trackNo);
		sb.append(", soundStatus=").append(soundStatus);
		sb.append(", masterGain=").append(masterGain);
		sb.append(", loopPoint=").append(loopPoint);
		sb.append(", comment=").append(comment);
		sb.append(", fadeOutModel=").append(fadeOutModel);
		sb.append(", pauseLocation=").append(pauseLocation);
		sb.append(", tags=").append(tags);
		sb.append(", loaded=").append(loaded);
		sb.append('}');
		return sb.toString();
	}

}
