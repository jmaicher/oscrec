package de.jmaicher.osc.cassette_recorder.cli;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import de.jmaicher.osc.cassette_recorder.CLI;
import de.jmaicher.osc.cassette_recorder.OSCCassette;
import de.jmaicher.osc.cassette_recorder.OSCCassettePlayer;
import de.jmaicher.osc.cassette_recorder.OSCCassetteStorage;
import de.jmaicher.utils.ProgressObserver;

public final class PlaybackCommand {
	
	public static final String COMMAND_STRING = "play";
	
	private static final String HELP_OPTION = "help";
	private static final String PORT_OPTION = "port";
	
	/**
	 * Playback command of the command line interface
	 * 
	 * @param args
	 */
	public static void run(final String[] args) {
		final Options options = getCLIOptions();
		final CommandLine commandLine = CLI.parseCLIOptions(options, args, false);
		
		OSCCassette cassette = null;
		try {
			String inputFilePath = args[0];
			cassette = OSCCassetteStorage.load(new File(inputFilePath));
		} catch (IOException e) {
			CLI.exitWithError(String.format("Couldn't read input file `%s`", args[0]));
		}
		
		int port = 0;
		try {
			port = Integer.parseInt(commandLine.getOptionValue(PORT_OPTION));
		} catch (NumberFormatException e) {
			CLI.exitWithError(String.format("The port `%s` is invalid", commandLine.getOptionValue(PORT_OPTION)));
		}
		
		OSCCassettePlayer player = null;
		try {
			player = new OSCCassettePlayer(cassette, port, new ProgressObserver() {
				
				@Override
				public void onProgress() {
					System.out.print(".");
				}
				
				@Override
				public void onDone() {
					System.out.print(String.format("done.%n"));
				}
				
				@Override
				public void onError(Exception e) {
					e.printStackTrace();
				}
				
			});
		} catch (Exception e) {
			CLI.exitWithError(String.format("Couldn't connect to port `%d` on localhost", port));
		}

		System.out.print(String.format("Playing OSC cassette on port `%d`", port));
		player.play();
	}
	
	
	/**
	 * Prints help information to System.out
	 * 
	 * @param options CLI options shown in the help information
	 */
	public static void printHelp() {
		CLI.printCommandHelp(COMMAND_STRING + " FILE", "Plays an existing OSC cassette.", getCLIOptions());
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
	 * @return Options expected from CLI in GNU-format
	 */
	@SuppressWarnings("static-access")
	private static Options constructCLIOptions() {
		final Options options = new Options();
		
		options.addOption(OptionBuilder.withLongOpt(HELP_OPTION)
				.withDescription("Print help information")
				.hasArg(false)
				.create()	);
		
		options.addOption(OptionBuilder.withLongOpt(PORT_OPTION)
				.withArgName("port")
				.withDescription("Port used to playback OSC cassette")
				.hasArg()
				.isRequired()
				.create()	);
		
		return options;
	}
	
}
