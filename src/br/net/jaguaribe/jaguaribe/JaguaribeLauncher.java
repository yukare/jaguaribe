/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.jaguaribe;

import javax.swing.SwingUtilities;

/**
 * Launch the application.
 *
 * This class contains the method main(), it simple create a instance of the
 * application class(Jaguaribe) that does all the work.
 */
public class JaguaribeLauncher {

    /**
     * Hold the args to use inside runnable.
     */
    private static String[] args;

    /**
     * Start of application.
     *
     * This is the start of application, where all begins ...
     *
     * @param args The arguments from command line.
     */
    public static void main(String[] args) {
        JaguaribeLauncher.args = args;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Jaguaribe jaguaribe = Jaguaribe.getInstance();
                jaguaribe.start(JaguaribeLauncher.getArgs());
            }
        });
    }

    /**
     * Get the arguments from command line.
     *
     * @return The arguments from command line.
     */
    public static String[] getArgs() {
        return args;
    }
}
