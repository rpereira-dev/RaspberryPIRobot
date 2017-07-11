package org.rpereira.robot.voice;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.sound.sampled.AudioInputStream;

import org.rpereira.robot.Robot;
import org.rpereira.robot.modules.IModule;
import org.rpereira.robot.utils.Logger;
import org.rpereira.robot.utils.Logger.Level;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.signalproc.effects.AudioEffect;
import marytts.signalproc.effects.AudioEffects;
import marytts.util.data.audio.AudioPlayer;

/** main class for speech */
public class SpeakerModule implements IModule {

	private AudioPlayer audioPlayer;
	private MaryInterface maryInterface;

	public SpeakerModule() {
	}

	/**
	 * Transform text to speech
	 * 
	 * @param text
	 *            The text that will be transformed to speech
	 * @param daemon
	 *            <br>
	 *            <b>True</b> The thread that will start the text to speech
	 *            Player will be a daemon Thread <br>
	 *            <b>False</b> The thread that will start the text to speech
	 *            Player will be a normal non daemon Thread
	 * @param join
	 *            <br>
	 *            <b>True</b> The current Thread calling this method will
	 *            wait(blocked) until the Thread which is playing the Speech
	 *            finish <br>
	 *            <b>False</b> The current Thread calling this method will
	 *            continue freely after calling this method
	 */
	public void say(String text, boolean join) {

		// Stop the previous player
		this.shutUp();

		try {
			AudioInputStream audio = this.maryInterface.generateAudio(text);
			this.audioPlayer = new AudioPlayer();
			this.audioPlayer.setAudio(audio);
			this.audioPlayer.start();
			if (join) {
				this.audioPlayer.join();
			}
		} catch (SynthesisException ex) {
			Logger.get().log(Level.WARNING, "Error saying phrase.", ex);
		} catch (InterruptedException ex) {
			Logger.get().log(Level.WARNING, "Interrupted ", ex);
			this.audioPlayer.interrupt();
		}
	}

	/**
	 * Stop the MaryTTS from Speaking
	 */
	public final void shutUp() {
		if (this.audioPlayer != null) {
			this.audioPlayer.cancel();
		}
	}

	/**
	 * Available voices in String representation
	 * 
	 * @return The available voices for MaryTTS
	 */
	public static final Collection<marytts.modules.synthesis.Voice> getAvailableVoices() {
		return (marytts.modules.synthesis.Voice.getAvailableVoices());
	}

	/**
	 * @return the maryInterface
	 */
	public final MaryInterface getMaryaudioPlayer() {
		return (this.maryInterface);
	}

	/**
	 * @return list of available audio effects for MaryTTS
	 */
	public static final List<AudioEffect> getAudioEffects() {
		return (StreamSupport.stream(AudioEffects.getEffects().spliterator(), false).collect(Collectors.toList()));
	}

	/**
	 * Change the default voice of the MaryTTS
	 * 
	 * @param voice
	 */
	public void setVoice(String voiceName) {
		this.maryInterface.setVoice(voiceName);
	}

	@Override
	public void initialize() {
		try {
			this.maryInterface = new LocalMaryInterface();
		} catch (MaryConfigurationException ex) {
			ex.printStackTrace();
		}

		// Setting the Voice
		//
		// upmc-pierre-hsmm
		// upmc-jessica-hsmm
		// enst-camille-hsmm
		// cmu-slt-hsmm

		this.setVoice("upmc-pierre-hsmm");
	}

	@Override
	public void deinitialize() {
	}

	@Override
	public String getName() {
		return (this.getClass().getSimpleName());
	}

	@Override
	public void update(Robot robot) {
		// TODO Auto-generated method stub

	}
}
