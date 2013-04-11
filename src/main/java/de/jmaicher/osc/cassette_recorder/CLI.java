package de.jmaicher.osc.cassette_recorder;

import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.jmaicher.commons.cli.GraciousGnuParser;
import de.jmaicher.osc.cassette_recorder.cli.PlaybackCommand;
import de.jmaicher.osc.cassette_recorder.cli.RecordCommand;

/**
 * This class provides the entry point for the command-line interface
 * (@see {@link CLI#main}.
 * 
 * @author jmaicher

 */
public final class CLI {
	public static final String VERSION = "0.0.1";
	
	public static final int DEFAULT_EXIT_STATUS_SUCCESS = 0; 
	public static final int DEFAULT_EXIT_STATUS_ERROR = 1;
	
	public static final String GENERAL_COMMAND_LINE_SYNTAX = "java -jar [..].jar ["
			+ RecordCommand.COMMAND_STRING + "|" + PlaybackCommand.COMMAND_STRING + "] OPTIONS";
	
	public static final String HELP_OPTION = "help";
	public static final String VERSION_OPTION = "version";
	
	/**
	 * Main entry point for the command line interface
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		final Options generalOptions = getGeneralCLIOptions();
		
		if(args.length < 1) {
			// no args => print general help
			printGeneralHelp(generalOptions);
		} else {
			final CommandLine generalCommand = parseCLIOptions(generalOptions, args, true);
			
			if(generalCommand.hasOption(VERSION_OPTION)) {
				printVersion();
				exit();
			}
			
			final String commandString = args[0];
			// commandArgs = args without commandString
			final String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);
			
			if(commandString.equals(RecordCommand.COMMAND_STRING)) {
				if(commandArgs.length >= 1 && !generalCommand.hasOption(HELP_OPTION)) {
					RecordCommand.run(commandArgs);
				} else {
					RecordCommand.printHelp();
				}
			} else if(commandString.equals(PlaybackCommand.COMMAND_STRING)) {
				if(commandArgs.length >= 1 && !generalCommand.hasOption(HELP_OPTION)) {
					PlaybackCommand.run(commandArgs);
				} else {
					PlaybackCommand.printHelp();
				}
			} else {
				printGeneralHelp(generalOptions);
			}
		}
	}
		
	
	/**
	 * Prints usage information
	 * 
	 * @param cmdLineSyntax	command line syntax
	 * @param header		header of the usage information
	 * @param options		CLI options shown in the help information
	 * @param footer		footer of the usage information
	 */
	public static void printHelp(final String cmdLineSyntax, final String header, final Options options, final String footer) {
		final HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(cmdLineSyntax, header, options, footer);
	}
	
	
	/**
	 * Prints general usage information
	 * 
	 * @param options CLI options shown in the help information
	 */
	public static void printGeneralHelp(final Options options) {
		final String eol = System.getProperty("line.separator");
		printHelp(GENERAL_COMMAND_LINE_SYNTAX, eol
				+ "The OSC Cassette Recorder lets you record and playback OSC packets."
				+ eol + "OPTIONS:", options, eol
				+ "For more help consult the specific command help with `[..] record --help` or `[..] play --help`.");
	}
	
	
	/**
	 * Prints usage information for a CLI command
	 * 
	 * @param commandString		identifier for the command
	 * @param header			information displayed over the options
	 * @param options			options for the command
	 */
	public static void printCommandHelp(final String commandString, final String commandDescription, final Options options) {
		final String eol = System.getProperty("line.separator");
		printHelp(getCommandLineSyntax(commandString), eol
				+ commandDescription + eol + "OPTIONS:", options, "");
	}
	
	
	/**
	 * Prints version information
	 */
	public static void printVersion() {
		System.out.println("OSC Cassette Recorder v" + VERSION);
	}
	
	
	/**
	 * Terminates execution with status 0
	 */
	public static void exit() {
		exit(DEFAULT_EXIT_STATUS_SUCCESS);
	}
	
	
	/**
	 * Terminates execution with the given status
	 * 
	 * @param status	exit status code
	 */
	public static void exit(int status) {
		System.exit(0);
	}
	
	
	/**
	 * Terminates the execution with the given error message
	 * 
	 * @param errorMessage	error message shown to the user
	 */
	public static void exitWithError(final String errorMessage) {
		exitWithError(DEFAULT_EXIT_STATUS_ERROR, errorMessage);
	}
	
	
	/**
	 * Terminates the execution with the given status and error message
	 * 
	 * @param status		exit status
	 * @param errorMessage	error message shown to the user
	 */
	public static void exitWithError(final int status, final String errorMessage) {
		System.out.println(errorMessage);
		System.exit(status);
	}
	
	
	/**
	 * Returns command line syntax for a CLI command
	 * 
	 * @param commandString		identifier for the command
	 * @return 					command line syntax for the CLI command
	 */
	private static String getCommandLineSyntax(final String commandString) {
		final String commandLineSyntax = "java -jar [..].jar " + commandString + " OPTIONS";
		return commandLineSyntax;
	}
	
	
	// ############################################
	// ## CLI Options #############################
	// ############################################
	
	private static Options generalCLIOptions = null;
	
	/**
	 * Returns GNU-compatible CLI options
	 * 
	 * @return	GNU-compatible CLI options
	 */
	public static Options getGeneralCLIOptions() {
		if(generalCLIOptions == null) {
			generalCLIOptions = constructGeneralCLIOptions();
		}
		
		return generalCLIOptions;
	}

	
	/**
	 * Constructs general GNU-compatible CLI options
	 * 
	 * @return	general CLI options in GNU-format
	 */
	@SuppressWarnings("static-access")
	private static Options constructGeneralCLIOptions() {
		final Options options = new Options();
		
		options.addOption(OptionBuilder.withLongOpt(VERSION_OPTION)
				.withDescription("Print version information")
				.hasArg(false)
				.create()	);
		
		options.addOption(OptionBuilder.withLongOpt(HELP_OPTION)
				.withDescription("Print help information")
				.hasArg(false)
				.create()	);
		
		return options;
	}

	
	/**
	 * Parse GNU-compatible CLI options
	 * 
	 * NOTE: When there is a parse error, this function will print the
	 * exception message and terminates the execution with status code 1.
	 * This occurs typically when there is an required option not provided
	 * in the given list of arguments.
	 * 
	 * @param options	GNU-compatible CLI options
	 * @param args		list of arguments
	 * @param gracious	if true, unknown arguments are ignored by the parser
	 * @return			list of arguments parsed against options
	 */
	public static CommandLine parseCLIOptions(Options options, String[] args, boolean gracious) {
		final CommandLineParser parser;
		
		if(gracious) {
			parser = new GraciousGnuParser();
		} else {
			parser = new GnuParser();
		}
		
		try {
			return parser.parse(options, args);
		} catch (ParseException e) {
			exitWithError(e.getMessage());
		}
		
		// make the compiler happy
		return null;
	}
	
}
