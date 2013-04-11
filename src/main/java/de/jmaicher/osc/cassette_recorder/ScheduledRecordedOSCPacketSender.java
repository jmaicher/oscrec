package de.jmaicher.osc.cassette_recorder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.illposed.osc.OSCPacket;
import com.illposed.osc.OSCPortOut;

import de.jmaicher.utils.ProgressObserver;

/**
 * An instance of {@link ScheduledRecordedOSCPacketSender} responsible
 * for the actual sending of the recorded {@link OSCPacket}s. Therefore,
 * it uses a pool of threads to send the packets which are scheduled at
 * the record time.
 * 
 * @author jmaicher
 */
public class ScheduledRecordedOSCPacketSender implements Runnable {
	private static int THREAD_POOL_SIZE = 3;
	private static int POLL_INTERVAL = 200;
	
	private List<RecordedOSCPacket> recordedOSCPackets;
	private ProgressObserver observer;
	
	private ScheduledThreadPoolExecutor scheduledSender = new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE);
	private OSCPortOut oscSender;
	
	private boolean sending = false;

	/**
	 * Creates a new {@link ScheduledRecordedOSCPacketSender} which will send the given
	 * list of {@link RecordedOSCPacket}s to the given port. It keeps the given {@link ProgressObserver}
	 * informed about the current progress.
	 * 
	 * @param recordedPackets
	 * @param port
	 * @param observer
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public ScheduledRecordedOSCPacketSender(List<RecordedOSCPacket> recordedPackets, int port, ProgressObserver observer) throws SocketException, UnknownHostException {
		this.recordedOSCPackets = recordedPackets;
		this.observer = observer;
		
		oscSender = new OSCPortOut(InetAddress.getByName("localhost"), port);
	}

	@Override
	public void run() {
		// schedule packets
		for(RecordedOSCPacket recordedPacket: recordedOSCPackets) {
			ScheduledOSCPacket scheduledOSCPacket = new ScheduledOSCPacket(recordedPacket.getOSCPacket());
			scheduledSender.schedule(scheduledOSCPacket, recordedPacket.getRelativeRecordTime(), TimeUnit.MILLISECONDS);
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
	
	/**
	 * Start sending the list of {@link RecordedOSCPacket}s
	 */
	public void start() {
		sending = true;
		new Thread(this).start();
	}
	
	/**
	 * Stop sending
	 */
	public void stop() {
		if(!sending) {
			return;
		}
		
		sending = false;
	}
	
	/**
	 * @return the sending state
	 */
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
		
		public ScheduledOSCPacket(OSCPacket oscPacket) {
			this.oscPacket = oscPacket;
		}
		
		@Override
		public void run() {
			sendOSCPacket(oscPacket);
		}
		
	}
}