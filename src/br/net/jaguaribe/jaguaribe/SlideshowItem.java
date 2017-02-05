/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.jaguaribe;

/**
 * Represents an item in a slideshow.
 *
 * @author Fernando Correa da Conceição.
 */
public class SlideshowItem {

    /**
     * The absolute path of this item.
     */
    private String path;

    /**
     * If this item is a directory.
     */
    private Boolean directory = false;

    /**
     * When add the other files from same folder if this is a file.
     */
    private Boolean addFiles = false;

    /**
     * Get the absolute path.
     *
     * @return The path to ietm.
     */
    public String getPath() {
        return path;
    }

    /**
     * Set the absolute path to item.
     *
     * @param path The path to set.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Return if is a directory.
     *
     * @return true if is directory
     */
    public Boolean getDirectory() {
        return directory;
    }

    /**
     * Set if the item is a directory.
     *
     * @param directory true if this item is a directory
     */
    public void setDirectory(Boolean directory) {
        this.directory = directory;
    }

    /**
     * Return if is to add all files from same folder.
     *
     * @return true if is to add all files from same folder
     */
    public Boolean getAddFiles() {
        return addFiles;
    }

    /**
     * Set if is to add all files from same folder.
     *
     * @param addFiles true if is to add all files from same folder.
     */
    public void setAddFiles(Boolean addFiles) {
        this.addFiles = addFiles;
    }
}
