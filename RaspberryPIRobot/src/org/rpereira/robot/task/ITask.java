package org.rpereira.robot.task;

import org.rpereira.robot.Robot;

/** a generic task interface */
public interface ITask {

	/** run the task */
	public void run(Robot robot, Object... arguments);
}
