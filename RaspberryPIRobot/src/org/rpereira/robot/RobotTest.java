package org.rpereira.robot;

import org.rpereira.robot.utils.Keys;

/** launch class */
public class RobotTest {

	public static void main(String[] args) {
		new RobotTest().run();
	}

	public final void run() {

		// prepare
		Keys.init();

		// spawn a robot
		Robot robot = new Robot("./");
		this.preRobotInitialization(robot);
		robot.initialize();
		this.postRobotInitialization(robot);

		// loop
		while (robot.isRunning()) {
			robot.update();
			this.onRobotUpdate(robot);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}

		// free memory
		this.preRobotDeinitialization(robot);
		robot.deinitialize();
		this.postRobotDeinitialization(robot);

		Keys.deinit();
	}

	protected void preRobotInitialization(Robot robot) {
		// TODO Auto-generated method stub

	}

	protected void postRobotInitialization(Robot robot) {
		// TODO Auto-generated method stub

	}

	protected void onRobotUpdate(Robot robot) {
		// TODO Auto-generated method stub

	}

	protected void postRobotDeinitialization(Robot robot) {
		// TODO Auto-generated method stub

	}

	protected void preRobotDeinitialization(Robot robot) {
		// TODO Auto-generated method stub

	}

}
