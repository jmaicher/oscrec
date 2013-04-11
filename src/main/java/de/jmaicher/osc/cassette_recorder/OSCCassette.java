package de.jmaicher.osc.cassette_recorder;

import java.util.List;


/**
 * An instance of OSCCassette represents a recorded osc cassette
 * which can be played using an {@link OSCCassettePlayer}.
 * It is composed of many {@link RecordedOSCPacket}s and is usually
 * created by an {@link OSCCassetteRecorder}.
 * 
 * @author jmaicher
 */
public class OSCCassette {

	private final List<RecordedOSCPacket> recordedPackets;
	
	/**
	 * Creates an {@link OSCCassette} with the given list of
	 * {@link RecordedOSCPacket}s.
	 * 
	 * @param recordedPackets list of {@link RecordedOSCPacket}s
	 */
	public OSCCassette(final List<RecordedOSCPacket> recordedPackets) {
		this.recordedPackets = recordedPackets;
	}
	
	/**
	 * @return list of {@link RecordedOSCPacket}s
	 */
	public List<RecordedOSCPacket> getRecordedPackets() {
		return recordedPackets;
	}
	
}