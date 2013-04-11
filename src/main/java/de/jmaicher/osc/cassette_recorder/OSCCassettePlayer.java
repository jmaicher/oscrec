package de.jmaicher.osc.cassette_recorder;

import java.net.SocketException;
import java.net.UnknownHostException;

import de.jmaicher.utils.ProgressObserver;

/**
 * This class is responsible for playing an {@link OSCCassette}.
 * 
 * @author jmaicher
 */
public class OSCCassettePlayer {
	private ScheduledRecordedOSCPacketSender scheduledSender;
	
	/**
	 * Creates an {@link OSCCassettePlayer} which will play the given
	 * {@link OSCCassette} on the given port. The given {@link ProgressObserver}
	 * will be kept informed about the playback state.
	 * 
	 * @param cassette 	the {@link OSCCassette} to play
	 * @param port		the port to replay the {@link RecordedOSCPacket}s to
	 * @param observer	a {@link ProgressObserver} which will be kept informed about the current playback state
	 * 
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public OSCCassettePlayer(OSCCassette cassette, int port, ProgressObserver observer) throws SocketException, UnknownHostException {
		scheduledSender = new ScheduledRecordedOSCPacketSender(cassette.getRecordedPackets(), port, observer);
	}
	
	/**
	 * Starts the playback
	 */
	public void play() {
		scheduledSender.start();
	}
	
	/**
	 * Stops the playback
	 */
	public void stop() {
		if(!scheduledSender.isSending()) {
			return;
		}
		
		scheduledSender.stop();
	}
	
	/**
	 * @return the playback state
	 */
	public boolean isPlaying() {
		return scheduledSender.isSending();
	}
	
}
