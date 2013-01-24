package de.jmaicher.utils;

public interface ProgressObserver {
	
	public void onProgress();
	
	public void onDone();
	
	public void onError(Exception e);
	
}
