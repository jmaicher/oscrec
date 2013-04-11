package de.jmaicher.osc.cassette_recorder;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.illposed.osc.OSCPacket;

import de.jmaicher.osc.RawOSCPacketListener;
import de.jmaicher.osc.RawOSCPortIn;

/**
 * This class is responsible for recording an {@link OSCCassette}.
 * 
 * @author jmaicher
 */
public class OSCCassetteRecorder {
	private int port;
	
	private RawOSCPortIn oscReceiver;
	private long startTime;
	private List<RecordedOSCPacket> recordedPackets = null;
	
	private OSCCassette recordedCassette = null;
	private boolean recording = false;
	
	
	/**
	 * An instance of this class observes an {@link RawOSCPortIn}
	 * and acts as mediator between incoming {@link OSCPacket}s and
	 * the {@link OSCCassetteRecorder}.
	 * 
	 * @author jmaicher
	 */
	private class OSCObserver implements RawOSCPacketListener {

		@Override
		public void acceptPacket(OSCPacket oscPacket) {
			recordOSCPacket(oscPacket);
		}
		
	}
	
	/**
	 * Creates an {@link OSCCassetteRecorder} which will record incoming
	 * {@link OSCPacket}s on the given port.
	 * 
	 * @param port
	 */
	public OSCCassetteRecorder(int port) {
		this.port = port;
	}
	
	/**
	 * @return the recording state
	 */
	public boolean isRecording() {
		return recording;
	}
	
	/**
	 * @return the recorded {@link OSCCassette}
	 */
	public OSCCassette getRecordedCassette() {
		return recordedCassette;
	}
	
	/**
	 * Starts recording a new {@link OSCCassette}.
	 * 
	 * @throws SocketException
	 */
	public void record() throws SocketException {
		oscReceiver = new RawOSCPortIn(port);
		OSCObserver oscObserver = new OSCObserver();
		oscReceiver.addListener(oscObserver);
		
		// (re)set initial state
		recordedPackets = new ArrayList<>();
		startTime = new Date().getTime();
		// start recording
		recording = true;
		oscReceiver.startListening();
	}

	/**
	 * Stops recording an {@link OSCCassette}.
	 */
	public void stop() {
		if(oscReceiver == null || !oscReceiver.isListening()) {
			return;
		}
		
		oscReceiver.stopListening();
		oscReceiver.close();
		recording = false;
		recordedCassette = new OSCCassette(recordedPackets);
	}
	
	/**
	 * This method will be invoked by an {@link OSCObserver}
	 * when a new {@link OSCPacket} arrives.
	 * 
	 * @param oscPacket
	 */
	private void recordOSCPacket(OSCPacket oscPacket) {
		recordedPackets.add(new RecordedOSCPacket(oscPacket, getCurrentRecordingTime()));
	}
	
	/**
	 * @return time elapsed since the recording begin in milliseconds
	 */
	private long getCurrentRecordingTime() {
		return new Date().getTime() - startTime;
	}
	
}