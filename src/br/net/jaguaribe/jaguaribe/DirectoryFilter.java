/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.jaguaribe;

import java.io.*;

/**
 * Filter for directories.
 *
 * This class is a filter to block all files and let only directories to pass.
 *
 * @author Fernando Correa da Conceição
 */
public class DirectoryFilter implements FileFilter {

    /**
     * Filter the directories.
     *
     * Filter the directories using defined rules.
     *
     * @param file The name of diretory
     *
     * @return true if there is none rule to deny the directory
     */
    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        return false;
    }
}
