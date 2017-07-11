package org.rpereira.robot.voice;

import org.rpereira.robot.Robot;
import org.rpereira.robot.RobotTest;

/** launch class */
public class RobotSpeakerModule extends RobotTest {

	public static void main(String[] args) {
		new RobotSpeakerModule().run();
	}

	@Override
	protected void postRobotInitialization(Robot robot) {
		SpeakerModule speaker = new SpeakerModule();
		speaker.initialize();
		robot.registerModule(speaker);
		speaker.say("Bonjour, ceci est un test afin de voir si le module 'Speaker' fonctionne.", true);
		speaker.say("Si tu m'entends bien, c'est que tout fonctionne.", true);
		speaker.say("Ceci signe donc la fin du test.", true);
		speaker.say("A plus en lanne", true);
	}
}
