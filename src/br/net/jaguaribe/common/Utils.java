/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 *
 */
package br.net.jaguaribe.common;

import java.io.*;
import java.util.Arrays;

/**
 * Usefull static functions.
 *
 * @author Fernando Correa da Conceição
 */
public class Utils {

    /**
     * Private constructor to do not alow creation of objects of this class.
     */
    private Utils() {
    }

    /**
     * Add an item as first in an array of File[]
     *
     * @param source The array
     * @param item The item
     *
     * @return The array with the item
     */
    public static File[] fileArrayPrepend(File[] source, File item) {
        int count = source.length;
        File result[] = new File[count + 1];
        result[0] = item;
        System.arraycopy(source, 0, result, 1, count);
        return result;
    }

    /**
     * Sort an array of File[] by the name of the files
     *
     * @param source The array to sort
     *
     * @return The same array sorted by the file name
     */
    public static File[] sortFileNames(File[] source) {
        File[] result = new File[source.length];
        String[] names = new String[source.length];
        int i = 0;
        for (File f : source) {
            names[i] = f.getAbsolutePath();
            i++;
        }
        Arrays.sort(names, new StringComparator());
        i = 0;
        for (String a : names) {
            result[i] = new File(a);
            i++;
        }
        return result;
    }
}
