package de.jmaicher.utils;

/**
 * A {@link ProgressObserver} can be used whenever an object is
 * interested in the progress of some process.
 * 
 * @author jmaicher
 */
public interface ProgressObserver {
	
	public void onProgress();
	
	public void onDone();
	
	public void onError(Exception e);
	
}
