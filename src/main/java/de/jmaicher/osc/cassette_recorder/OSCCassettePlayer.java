package de.jmaicher.osc.cassette_recorder;

import java.net.SocketException;
import java.net.UnknownHostException;

import de.jmaicher.utils.ProgressObserver;

public class OSCCassettePlayer {
	private ScheduledRecordedOSCPacketSender scheduledSender;
	
	public OSCCassettePlayer(OSCCassette cassette, int port, ProgressObserver observer) throws SocketException, UnknownHostException {
		scheduledSender = new ScheduledRecordedOSCPacketSender(cassette.getRecordedPackets(), port, observer);
	}
	
	public void play() {
		scheduledSender.start();
	}
	
	public void stop() {
		if(!scheduledSender.isSending()) {
			return;
		}
		
		scheduledSender.stop();
	}
	
	public boolean isPlaying() {
		return scheduledSender.isSending();
	}
	
}
