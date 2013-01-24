package de.jmaicher.osc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import com.illposed.osc.OSCPacket;
import com.illposed.osc.OSCPort;
import com.illposed.osc.utility.OSCByteArrayToJavaConverter;


/**
 * RawOSCPortIn receives raw OSC packets.
 * 
 * It is similar to OSCPortIn, but it doesn't use a
 * dispatcher which dispatches OSC messages. Instead,
 * it uses a dispatcher which dispatches raw OSC packets.
 * 
 * @author jmaicher
 */
public class RawOSCPortIn extends OSCPort {
	private RawOSCPacketDispatcher dispatcher = new RawOSCPacketDispatcher();
	
	/** indicates current listening status */
	private boolean listening;
	
	/** @see com.illposed.osc.OSCPortIn */
	private static final int BUFFER_SIZE = 1536;
	
	/**
	 * Worker thread that receives OSC data and converts them
	 * to OSC packets. It invokes RawOSCPortIn#onOSCPacket when
	 * a new OSC packet has been received. It stops receiving
	 * when the RawOSCPortIn instance stops listening.
	 * 
	 * The run method is largely similar to OSCPortIn#run.
	 */
	private class OSCPacketReceiver extends Thread {
		private OSCByteArrayToJavaConverter converter = new OSCByteArrayToJavaConverter();
		
		/** @see com.illposed.osc.OSCPortIn */ 
		@Override
		public void run() {
			byte[] buffer = new byte[BUFFER_SIZE];
			DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);
			DatagramSocket socket = getSocket();
			while (listening) {
				try {
					try {
						socket.receive(packet);
					} catch (SocketException ex) {
						if (listening) {
							throw ex;
						} else {
							// if we closed the socket while receiving data,
							// the exception is expected/normal, so we hide it
							continue;
						}
					}
					OSCPacket oscPacket = converter.convert(buffer,
							packet.getLength());
					onOSCPacket(oscPacket);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Create an RawOSCPortIn that listens in the specified port. 
	 * @param port	UDP port to listen to
	 * @throws SocketException
	 */
	public RawOSCPortIn(int port) throws SocketException {
		super(new DatagramSocket(new InetSocketAddress("localhost", port)), port);
	}
	
	/**
	 * Start listening for incoming OSCPackets
	 */
	public void startListening() {
		if(listening) {
			return;
		}
		
		listening = true;
		new OSCPacketReceiver().start();
	}
	
	/**
	 * Stop listening for incoming OSCPackets
	 */
	public void stopListening() {
		listening = false;
	}
	
	/**
	 * Indicates whether the port is currently receiving
	 */
	public boolean isListening() {
		return listening;
	}
	
	/**
	 * Register the listener for incoming OSCPackets
	 * 
	 * @param listener	The object to invoke when a messages comes in
	 */
	public void addListener(RawOSCPacketListener listener) {
		dispatcher.addListener(listener);
	}
	
	/**
	 * This method will be invoked when the OSCPacketReceiver thread
	 * has received a new OSC packet.
	 * 
	 * @param oscPacket	The received OSC packet
	 */
	private void onOSCPacket(OSCPacket oscPacket) {
		dispatcher.dispatchPacket(oscPacket);
	}

}
