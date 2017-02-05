/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Task with a text file.
 *
 * Read/write text files.
 *
 * @author Fernando Correa da Conceição
 */
public class TextFile {

    /**
     * The name of the file on disk.
     */
    private String filename = new String();

    /**
     * Charset used to read/write files, defaults to UTF-8
     */
    private Charset charset = Charset.forName("UTF-8");

    /**
     * Default constructor
     */
    public TextFile() {
    }

    /**
     * Build and set the filename.
     *
     * @param file The name of file to use in te next operations.
     */
    public TextFile(String file) {
        this.filename = file;
    }

    /**
     * Write the string to text file.
     *
     * This will not overwrite the current content, but instead, will append at
     * end.
     *
     * @param text The content to append at end of file.
     *
     * @throws IOException Error while writting the file.
     */
    public void append(String text) throws IOException {
        this.write(text, true);
    }

    /**
     * Write the string to text file.
     *
     * This is not an append, all previous content from file will be lost.
     *
     * @param text The new content of the file.
     *
     * @throws IOException Error while writting the file.
     */
    public void write(String text) throws IOException {
        this.write(text, false);
    }

    /**
     * Write the string to text file.
     *
     * This is not an append, all previous content from file will be lost.
     *
     * @param text The new content of the file.
     * @param append true to append the content, false to overwrite.
     *
     * @throws IOException Error while writting the file.
     */
    public void write(String text, boolean append) throws IOException {
        File file = new File(this.filename);

        if (this.filename == null) {
            throw new IOException("File should not be null.");
        }
        // Create file 
        FileWriter fstream = new FileWriter(this.filename, append);
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(text);
        //Close the output stream
        out.close();
    }

    /**
     * Read the content of the file.
     *
     * @return The content of the file.
     *
     * @throws FileNotFoundException If the file is not found.
     * @throws IOException If there is an error while reading the file.
     */
    public String read() throws FileNotFoundException, IOException {
        StringBuilder answer = new StringBuilder(1000);
        // Open the file that is the first 
        // command line parameter
        FileInputStream fstream = new FileInputStream(this.filename);
        // Get the object of DataInputStream
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        //Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            answer.append(strLine);
        }
        //Close the input stream
        in.close();
        return answer.toString();
    }

    /**
     * Read a file and return an array of lines.
     *
     * @return An array with each line of the file.
     *
     * @throws FileNotFoundException Exception if the file is not found.
     * @throws IOException Exception while reading the file.
     */
    public ArrayList<String> readLines() throws FileNotFoundException, IOException {
        ArrayList<String> answer = new ArrayList<String>();
        // Open the file that is the first 
        // command line parameter
        FileInputStream fstream = new FileInputStream(this.filename);
        // Get the object of DataInputStream
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        //Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            answer.add(strLine);
        }
        //Close the input stream
        in.close();
        return answer;
    }

    /**
     * Get the filename.
     *
     * @return The filename.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Set the filename.
     *
     * @param filename The filename to set.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Get the charset.
     *
     * @return The charset.
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * Set the charset.
     *
     * @param charset The charset to set.
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }
}
