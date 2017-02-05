/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.common;

import jargs.gnu.CmdLineParser;
import java.util.ArrayList;

/**
 * Improves CmdLineParser.
 *
 * Current improves with functions to show a usage help.
 *
 * @author Fernando Correa da Conceição
 */
public class CommandLineParser extends CmdLineParser {

    /**
     * Contains a list with all help messages.
     */
    private ArrayList<String> optionDesc = new ArrayList<String>();

    /**
     * Constructor.
     */
    public CommandLineParser() {
        super();
    }

    /**
     * Add the help message for an option.
     *
     * @param option The option.
     * @param helpString The string with help for that option.
     */
    public void addHelp(Option option, String helpString) {
        optionDesc.add(" -" + option.shortForm() + "/--" + option.longForm() + ": " + helpString);
    }

    /**
     * Add the message for default option.
     *
     * Use this to add help message for a value that do not have a options, as
     * example, when you allow a file name in the options to work with it.
     *
     * @param helpString The message.
     */
    public void addHelpDefault(String helpString) {
        optionDesc.add(helpString);
    }

    /**
     * Show the help message
     */
    public void printUsage() {
        System.err.println("usage: prog [options]");
        for (String line : optionDesc) {
            System.err.println(line);
        }
    }
}
