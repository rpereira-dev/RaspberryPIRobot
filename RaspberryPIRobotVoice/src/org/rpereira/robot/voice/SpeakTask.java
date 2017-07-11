package org.rpereira.robot.voice;

import org.rpereira.robot.Robot;
import org.rpereira.robot.task.ITask;

public class SpeakTask implements ITask {

	private static final String DEFAULT_STRING_TO_SAY = "Je ne sais pas quoi dire";

	@Override
	public void run(Robot robot, Object... arguments) {
		String stringToSay;

		if (arguments == null || arguments.length == 0) {
			stringToSay = DEFAULT_STRING_TO_SAY;
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			for (Object object : arguments) {
				stringBuilder.append(object.toString());
			}
			stringToSay = stringBuilder.toString();
		}

		SpeakerModule speaker = (SpeakerModule) robot.getModule(SpeakerModule.class);
		speaker.say(stringToSay, true);
	}
}
