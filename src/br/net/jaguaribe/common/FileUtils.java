/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Static functions to read contents of files.
 *
 * @author Fernando Correa da Conceição
 *
 * @todo Merge those function in TextFile.
 */
public class FileUtils {

    /**
     * Return a list with each line of a text file.
     *
     * Important: this code comes from
     * http://www.rgagnon.com/javadetails/java-0077.html .
     *
     * @param fileName The name of the file to open in jar file
     *
     * @return A List of String with all lines from text file.
     */
    public static List<String> readTextFromJarList(String fileName) {
        InputStream is = null;
        BufferedReader br = null;
        String line;
        ArrayList<String> list = new ArrayList<String>();

        try {
            is = FileUtils.class.getResourceAsStream(fileName);
            br = new BufferedReader(new InputStreamReader(is));
            while (null != (line = br.readLine())) {
                list.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * Get all content from a text file inside a jar file.
     *
     * @param fileName The name of the file to get the contents.
     *
     * @return The contents of the file.
     */
    public static String readFromJarFile(String fileName) {
        List<String> list = FileUtils.readTextFromJarList(fileName);
        Iterator<String> it = list.iterator();
        StringBuilder contents = new StringBuilder(10000);
        while (it.hasNext()) {
            contents.append(it.next()).append("\n");
        }
        return contents.toString();
    }
}
