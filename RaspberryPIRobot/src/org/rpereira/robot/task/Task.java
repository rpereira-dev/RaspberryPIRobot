package org.rpereira.robot.task;

public class Task {

	/** the ITask instance */
	private final Class<? extends ITask> itask;
	private final Object[] arguments;

	public Task(Class<? extends ITask> itask, Object... arguments) {
		this.itask = itask;
		this.arguments = arguments;
	}

	/** get the task */
	public final Class<? extends ITask> getITaskClass() {
		return (this.itask);
	}

	/** get the argument for the next task */
	public final Object[] getArguments() {
		return (this.arguments);
	}

}
