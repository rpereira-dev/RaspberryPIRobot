package org.rpereira.robot.ear;

import org.rpereira.robot.Robot;
import org.rpereira.robot.modules.IModule;
import org.rpereira.robot.utils.Logger;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

/** main class for voice recognition */
public class EarModule implements IModule {

	// TODO : make this path cleaner
	private static final String SPHINX4_PATH = "../res/sphinx4/fr/";

	public EarModule() {
	}

	@Override
	public void initialize() {
		Logger.get().log(Logger.Level.FINE, "initializing ear module");
		try {
			Configuration configuration = new Configuration();

			configuration.setAcousticModelPath(SPHINX4_PATH + "model");
			configuration.setDictionaryPath(SPHINX4_PATH + "fr.dict");
			configuration.setUseGrammar(true);
			configuration.setGrammarPath(SPHINX4_PATH);
			configuration.setGrammarName("fr");

			LiveSpeechRecognizer jsgfRecognizer = new LiveSpeechRecognizer(configuration);
			jsgfRecognizer.startRecognition(true);
			while (true) {
				String utterance = jsgfRecognizer.getResult().getHypothesis();
				System.out.println(utterance);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deinitialize() {
		// TODO Auto-generated method stub

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
