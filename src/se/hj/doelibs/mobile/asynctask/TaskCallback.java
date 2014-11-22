package se.hj.doelibs.mobile.asynctask;

/**
 * Abstract class to handle objects after a tasks completed
 *
 * I.e to set up the view so it doesn't have to be done in the asyncTask class
 *
 * @author Christoph
 */
public abstract class TaskCallback<T> {

	public void onTaskCompleted(T objectOnComplete) {}

	public void beforeTaskRun() {}
}
