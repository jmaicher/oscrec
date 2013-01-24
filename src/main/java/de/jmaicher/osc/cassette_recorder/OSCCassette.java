package de.jmaicher.osc.cassette_recorder;

import java.util.ArrayList;

public class OSCCassette {

	private final ArrayList<RecordedOSCPacket> recordedPackets;
	
	public OSCCassette(ArrayList<RecordedOSCPacket> _recordedPackets) {
		recordedPackets = _recordedPackets;
	}
	
	public ArrayList<RecordedOSCPacket> getRecordedPackets() {
		return recordedPackets;
	}
	
}