package de.jmaicher.osc.cassette_recorder;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;

import com.illposed.osc.OSCPacket;

import de.jmaicher.osc.RawOSCPacketListener;
import de.jmaicher.osc.RawOSCPortIn;

public class OSCCassetteRecorder {
	private int port;
	
	private RawOSCPortIn oscReceiver;
	private long startTime;
	private ArrayList<RecordedOSCPacket> recordedPackets = null;
	
	// initial state
	private OSCCassette recordedCassette = null;
	private boolean recording = false;
	
	private class OSCObserver implements RawOSCPacketListener {

		@Override
		public void acceptPacket(OSCPacket oscPacket) {
			recordOSCPacket(oscPacket);
		}
		
	}
	
	public OSCCassetteRecorder(int _port) {
		port = _port;
	}
	
	public boolean isRecording() {
		return recording;
	}
	
	public OSCCassette getRecordedCassette() {
		return recordedCassette;
	}
	
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

	public void stop() {
		if(oscReceiver == null || !oscReceiver.isListening()) {
			return;
		}
		
		oscReceiver.stopListening();
		oscReceiver.close();
		recording = false;
		recordedCassette = new OSCCassette(recordedPackets);
	}
	
	private void recordOSCPacket(OSCPacket oscPacket) {
		recordedPackets.add(new RecordedOSCPacket(oscPacket, getCurrentRecordingTime()));
	}
	
	private long getCurrentRecordingTime() {
		return new Date().getTime() - startTime;
	}
	
}
