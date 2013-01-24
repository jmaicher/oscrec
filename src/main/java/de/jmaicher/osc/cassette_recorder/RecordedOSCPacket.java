package de.jmaicher.osc.cassette_recorder;

import com.illposed.osc.OSCPacket;

public class RecordedOSCPacket {

	private final OSCPacket oscPacket;
	private final long time;
	
	public RecordedOSCPacket(OSCPacket oscPacket, long time) {
		this.oscPacket = oscPacket;
		this.time = time;
	}

	public long getTime() {
		return time;
	}

	public OSCPacket getOSCPacket() {
		return oscPacket;
	}
	
}