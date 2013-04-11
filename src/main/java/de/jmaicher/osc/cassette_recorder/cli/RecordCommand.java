package de.jmaicher.osc.cassette_recorder.cli;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import de.jmaicher.osc.cassette_recorder.CLI;
import de.jmaicher.osc.cassette_recorder.OSCCassette;
import de.jmaicher.osc.cassette_recorder.OSCCassetteRecorder;
import de.jmaicher.osc.cassette_recorder.OSCCassetteStorage;

/**
 * This class is responsible for the record command
 * of the command-line interface.
 * 
 * @author jmaicher
 */
public final class RecordCommand {

	public static final String COMMAND_STRING = "record";
	
	private static final String PORT_OPTION = "port";
	private static final String OUTPUT_FILE_PATH_OPTION = "file";
	
	private static final String DEFAULT_OUTPUT_FILE_PATH_FORMAT = "cassette-%s.osc";
	
	/**
	 * Record command of the command line interface
	 * 
	 * @param args
	 */
	public static void run(final String[] args) {
		final Options options = getCLIOptions();
		final CommandLine commandLine = CLI.parseCLIOptions(options, args, false);
		
		int port = 0;
		try {
			port = Integer.parseInt(commandLine.getOptionValue(PORT_OPTION));
		} catch (NumberFormatException e) {
			CLI.exitWithError(String.format("The port \"%s\" is invalid", commandLine.getOptionValue(PORT_OPTION)));
		}
		
		String outputFilePath;
		if(commandLine.hasOption(OUTPUT_FILE_PATH_OPTION)) {
			outputFilePath = commandLine.getOptionValue(OUTPUT_FILE_PATH_OPTION);
		} else {
			outputFilePath = getDefaultOutputFilePath();
		}
		
		OSCCassetteRecorder recorder = new OSCCassetteRecorder(port);
		try {
			recorder.record();
		} catch (SocketException e) {
			e.printStackTrace();
			CLI.exitWithError(String.format("Couldn't connect to port %d on localhost", port));
		}
		
		System.out.println("Recording OSC cassette..");
		System.out.println("(press `enter` to stop recording)");
		
		Scanner sc = new Scanner(System.in);
		while(!sc.nextLine().equals(""));
		sc.close();
		
		recorder.stop();
		System.out.println("Recording stopped.");
		
		OSCCassette cassette = recorder.getRecordedCassette();
		
		if(cassette.getRecordedPackets().size() > 0) {
			try {
				OSCCassetteStorage.save(new File(outputFilePath), cassette);
			} catch (IOException e) {
				CLI.exitWithError(String.format("Couldn't save the cassette to `%s`", outputFilePath));
			}
			
			System.out.println(String.format("The recorded OSC cassette has been saved to `%s`.", outputFilePath));
		} else {
			System.out.println("No packets have been recorded and therefore no cassette has been saved.");
		}
		
	}
	
	
	/**
	 * Returns a default file name
	 * 
	 * @return	default file name
	 */
	public static String getDefaultOutputFilePath() {
		long timestamp = System.currentTimeMillis();
		return String.format(DEFAULT_OUTPUT_FILE_PATH_FORMAT, Long.toString(timestamp));
	}

	
	/**
	 * Prints help information to System.out
	 * 
	 * @param options	CLI options shown in the help information
	 */
	public static void printHelp() {
		CLI.printCommandHelp(COMMAND_STRING, "Records a new OSC cassette.", getCLIOptions());
	}
	
	
	// ######################################
	// ## CLI Options #######################
	// ######################################
	
	private static Options cliOptions = null;
	
	/**
	 * Returns GNU-compatible CLI options
	 * 
	 * @return	GNU-compatible CLI options
	 */
	public static Options getCLIOptions() {
		if(cliOptions == null) {
			cliOptions = constructCLIOptions();
		}
		
		return cliOptions;
	}
	
	/**
	 * Constructs GNU-compatible CLI options
	 * 
	 * @return 	GNU-compatible CLI options
	 */
	@SuppressWarnings("static-access")
	private static Options constructCLIOptions() {
		final Options options = new Options();
		
		options.addOption(OptionBuilder.withLongOpt(PORT_OPTION)
				.withArgName("port")
				.withDescription("Port used to receive OSC messages on the local machine")
				.hasArg()
				.withType(Number.class)
				.isRequired()
				.create()	);
		
		options.addOption(OptionBuilder.withLongOpt(OUTPUT_FILE_PATH_OPTION)
				.withArgName("output file")
				.withDescription("Path to the output file")
				.hasArg()
				.create() );
		
		return options;
	}
	
}
