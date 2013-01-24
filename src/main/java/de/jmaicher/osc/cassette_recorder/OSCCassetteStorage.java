package de.jmaicher.osc.cassette_recorder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;

import com.illposed.osc.OSCPacket;
import com.illposed.osc.utility.OSCByteArrayToJavaConverter;

public class OSCCassetteStorage {

	private static char SEPARATOR = ',';
	
	public static void save(File file, OSCCassette cassette) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		
		for(RecordedOSCPacket recordedPacket: cassette.getRecordedPackets()) {
			writer.write(Long.toString(recordedPacket.getTime()));
			writer.write(",");
			writer.write(Base64.encodeBase64String(recordedPacket.getOSCPacket().getByteArray()));
			writer.newLine();
		}
		
		writer.close();
	}
	
	public static OSCCassette load(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		ArrayList<RecordedOSCPacket> recordedPackets = new ArrayList<>();
		
		String line; while((line = reader.readLine()) != null) {
			int seperatorIndex = line.indexOf(SEPARATOR);
			long time = Long.parseLong(line.substring(0, seperatorIndex));
			byte[] oscPacketBytes = Base64.decodeBase64(line.substring(seperatorIndex + 1));
			OSCPacket oscPacket = new OSCByteArrayToJavaConverter().convert(oscPacketBytes, oscPacketBytes.length);
			
			recordedPackets.add(new RecordedOSCPacket(oscPacket, time));
		}
		
		reader.close();
		return new OSCCassette(recordedPackets);
	}
	
}
