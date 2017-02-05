/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.common;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * Filter the files based on extension.
 *
 * Use this class to filter a list of files based on their extension. This
 * filter blocks all directories.
 *
 * @author Fernando Correa da Conceição.
 */
public class FileTypeFilter implements FileFilter {

    /**
     * The description of this filer, used with JFileChooser.
     */
    private String description = "";

    /**
     * List of alowed extensions.
     */
    private ArrayList<String> extensions = new ArrayList<String>();

    /**
     * When accept(true) or not(false) directories in the result, default is
     * false.
     */
    public boolean directories = false;

    /**
     * Filter the files.
     *
     * Filter that let only files from the extensions in the list pass, block
     * all other files and all directories.
     *
     * @param file The file object to filter.
     *
     * @return true if the file have a valid extension.
     */
    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            if (directories) {
                return true;
            } else {
                return false;
            }
        }
        if (file.isFile()) {
            String path = file.getAbsolutePath().toLowerCase();
            for (String ext : this.getExtensions()) {
                if (path.endsWith(ext)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Add the extensions from image files.
     *
     * This function add the extensions for common formats of images.
     */
    public void addImage() {
        this.addExtension("jpg jpeg png");
    }

    /**
     * Add one or more extension to the list.
     *
     * Add the extensions to the list. Can add one extension or many at same
     * time(separated by a space). You must not add the dot(.) to the
     * extensions.
     *
     * @param extension The extension(s) to add.
     */
    public void addExtension(String extension) {
        String[] list = extension.toLowerCase().split(" ");
        for (String item : list) {
            this.getExtensions().add("." + item);
        }
    }

    /**
     * Get the description of this filter.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description for this filter.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get all extensions.
     *
     * @return The extensions.
     */
    public ArrayList<String> getExtensions() {
        return extensions;
    }
}
