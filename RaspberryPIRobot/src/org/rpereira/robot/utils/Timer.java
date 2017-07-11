package org.rpereira.robot.utils;

import java.util.Date;

public class Timer {

	/** start date */
	private final Date start;
	private long updates;
	private long lastUpdate;

	public Timer() {
		this.start = new Date();
		this.updates = 0;
		this.update();
	}

	/** update the timer */
	public void update() {
		++this.updates;
		this.lastUpdate = System.currentTimeMillis();
	}

	/** now */
	public final Date now() {
		return (new Date());
	}

	/** get the start date */
	public final Date getStartDate() {
		return (this.start);
	}

	/** get ms since the last update */
	public final long msSinceLastUpdate() {
		return (System.currentTimeMillis() - this.lastUpdate);
	}

	/** get the total number of updates */
	public final long getUpdatesCount() {
		return (this.updates);
	}

}
