package de.jmaicher.commons.cli;

import java.util.ListIterator;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;

/**
 * This parser ignores unknown options.
 * 
 * @author jmaicher
 *
 */
public class GraciousGnuParser extends GnuParser {

	@SuppressWarnings("rawtypes")
	@Override
	protected void processOption(String arg, ListIterator iter)
			throws ParseException {
		if(getOptions().hasOption(arg)) {
			super.processOption(arg, iter);
		}
	}
	
}
