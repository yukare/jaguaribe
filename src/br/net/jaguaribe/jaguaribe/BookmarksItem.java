/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.jaguaribe;

/**
 * This class is one item from bookmarks.
 *
 * @author Fernando Correa da Conceição
 */
public class BookmarksItem {

    /**
     * Name of bookmark.
     */
    private String name;

    /**
     * The path on filesystem.
     */
    private String path;

    /**
     * Get the name.
     *
     * @return The name of bookmark.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name.
     *
     * @param name The new value for name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the absolute path.
     *
     * @return The absolute path of bookmark.
     */
    public String getPath() {
        return path;
    }

    /**
     * Set the path to bookmark.
     *
     * @param path The absolute path of bookmark
     */
    public void setPath(String path) {
        this.path = path;
    }
}
