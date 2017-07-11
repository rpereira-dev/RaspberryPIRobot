package org.rpereira.robot.modules;

import org.rpereira.robot.Robot;

/** a robot module which can be added to the robot */
public interface IModule {

	/** called when this module has to be initialized */
	public void initialize();

	/** called when this module has to be deinitialized */
	public void deinitialize();

	/** module uuid */
	public String getName();

	/** update the module */
	public void update(Robot robot);

}
