package de.jmaicher.osc.cassette_recorder;

import com.illposed.osc.OSCPacket;

/**
 * This class wraps an {@link OSCPacket} with the relative
 * record time in milliseconds.
 * 
 * @author jmaicher
 */
public class RecordedOSCPacket {

	private final OSCPacket oscPacket;
	private final long relativeRecordTime;
	
	/**
	 * Wraps the given recorded {@link OSCPacket} and the relative record
	 * time in a new {@link RecordedOSCPacket}.
	 * 
	 * @param oscPacket	recorded {@link OSCPacket}
	 * @param relativeRecordTime in milliseconds
	 */
	public RecordedOSCPacket(OSCPacket oscPacket, long relativeRecordTime) {
		this.oscPacket = oscPacket;
		this.relativeRecordTime = relativeRecordTime;
	}

	/**
	 * @return the relative record time in milliseconds
	 */
	public long getRelativeRecordTime() {
		return relativeRecordTime;
	}

	/**
	 * @return the recorded {@link OSCPacket}
	 */
	public OSCPacket getOSCPacket() {
		return oscPacket;
	}
	
}