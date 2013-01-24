package de.jmaicher.osc;

import java.util.ArrayList;

import com.illposed.osc.OSCPacket;

/**
 * Dispatches OSCPackets to registered listeners
 * 
 * @author jmaicher
 */
public class RawOSCPacketDispatcher {
	
	/** stores the registered listener */
	private ArrayList<RawOSCPacketListener> listeners
			= new ArrayList<>();

	/**
	 * Dispatches the given OSCPacket to all registered listener
	 * 
	 * @param oscPacket	The OSCPacket to dispatch
	 */
	public void dispatchPacket(OSCPacket oscPacket) {
		for(RawOSCPacketListener listener : listeners) {
			listener.acceptPacket(oscPacket);
		}
	}
	
	/**
	 * Adds the given listener to the registered listeners
	 * 
	 * @param listener	The object interested in OSCPackets
	 */
	public void addListener(RawOSCPacketListener listener) {
		listeners.add(listener);
	}
	
}
