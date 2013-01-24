package de.jmaicher.osc;

import com.illposed.osc.OSCPacket;

/**
 * Interface for objects that listen to OSC packets
 * 
 * @author jmaicher
 */
public interface RawOSCPacketListener {

	/**
	 * Accept an incoming OSC Packet
	 * 
	 * @param packet	The packet to execute
	 */
	public void acceptPacket(OSCPacket packet);
	
}
