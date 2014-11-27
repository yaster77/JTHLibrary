package se.hj.doelibs.mobile.asynctask;

/**
 * Abstract class to handle objects after a tasks completed
 *
 * I.e to set up the view so it doesn't have to be done in the asyncTask class
 *
 * @author Christoph
 */
public abstract class TaskCallback<T> {

	/**
	 * callback method after to call after the task completed
	 * @param objectOnComplete
	 */
	public void onTaskCompleted(T objectOnComplete) {}

	/**
	 * callback method to run before the actuall task runs
	 */
	public void beforeTaskRun() {}
}
