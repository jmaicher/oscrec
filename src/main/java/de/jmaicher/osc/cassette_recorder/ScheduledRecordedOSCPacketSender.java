package de.jmaicher.osc.cassette_recorder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.illposed.osc.OSCPacket;
import com.illposed.osc.OSCPortOut;

import de.jmaicher.utils.ProgressObserver;

public class ScheduledRecordedOSCPacketSender implements Runnable {
	private static int THREAD_POOL_SIZE = 3;
	private static int POLL_INTERVAL = 200;
	
	private ArrayList<RecordedOSCPacket> recordedOSCPackets;
	private int port;
	private ProgressObserver observer;
	
	private ScheduledThreadPoolExecutor scheduledSender = new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE);
	private OSCPortOut oscSender;
	
	private boolean sending = false;
	
	
	public ScheduledRecordedOSCPacketSender(ArrayList<RecordedOSCPacket> _recordedPackets, int _port, ProgressObserver _observer) throws SocketException, UnknownHostException {
		recordedOSCPackets = _recordedPackets;
		port = _port;
		observer = _observer;
		
		oscSender = new OSCPortOut(InetAddress.getByName("localhost"), port);
	}

	@Override
	public void run() {
		// schedule packets
		for(RecordedOSCPacket recordedPacket: recordedOSCPackets) {
			ScheduledOSCPacket scheduledOSCPacket = new ScheduledOSCPacket(recordedPacket.getOSCPacket());
			scheduledSender.schedule(scheduledOSCPacket, recordedPacket.getTime(), TimeUnit.MILLISECONDS);
		}
		
		// send packets until done or stopped
		while (sending) {
			// is done?
			if (scheduledSender.getCompletedTaskCount() == recordedOSCPackets.size()) {
				observer.onDone();
				scheduledSender.shutdown();
				cleanup();
				return;
			}
			
			observer.onProgress();
			
			try {
				Thread.sleep(POLL_INTERVAL);
			} catch (InterruptedException e) {}
		}
		
		scheduledSender.shutdownNow();
		cleanup();
	}
	
	public void start() {
		sending = true;
		new Thread(this).start();
	}
	
	public void stop() {
		if(!sending) {
			return;
		}
		
		sending = false;
	}
	
	
	public boolean isSending() {
		return sending;
	}
	
	
	// private
	
	private void cleanup() {
		oscSender.close();
	}
	
	private synchronized void sendOSCPacket(OSCPacket oscPacket) {
		try {
			oscSender.send(oscPacket);
		} catch (IOException e) {
			// Something's wrong here => notify user
			observer.onError(e);
		}
	}
	
	private class ScheduledOSCPacket implements Runnable {

		private OSCPacket oscPacket;
		
		public ScheduledOSCPacket(OSCPacket _oscPacket) {
			oscPacket = _oscPacket;
		}
		
		@Override
		public void run() {
			sendOSCPacket(oscPacket);
		}
		
	}
}