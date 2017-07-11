package org.rpereira.robot.ear;

import org.rpereira.robot.Robot;
import org.rpereira.robot.RobotTest;

/** launch class */
public class RobotEarModule extends RobotTest {

	public static void main(String[] args) {
		new RobotEarModule().run();
	}

	@Override
	protected void postRobotInitialization(Robot robot) {
		EarModule ear = new EarModule();
		ear.initialize();
		robot.registerModule(ear);
	}
}
