/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.log;

import java.io.File;

/**
 * Static functions to help with debug.
 *
 * @author Fernando Correa da Conceição
 */
public class Debug {

    /**
     * Show some information about a File object.
     *
     * @param file The file object.
     *
     * @return Information about file object.
     */
    public static String format(File file) {
        String output = "Absolute path: ".concat(file.getAbsolutePath());
        return output;
    }
}
