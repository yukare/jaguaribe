/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php
 */
package br.net.jaguaribe.jaguaribe;

import javax.swing.JMenuItem;

/**
 * This class expands JMenuItem to hold more data.
 *
 * This class expands JMenuItem to hold more information, currenty only the
 * absolute file path for the item.
 *
 * @author Fernando Correa da Conceição
 */
public class BookmarksMenuItem extends JMenuItem {

    /**
     * Used for class serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Hold the absolute file name to item.
     */
    private String Path;

    /**
     * Constructor.
     *
     * @param label The text of the item.
     */
    public BookmarksMenuItem(String label) {
        super(label);
    }

    /**
     * Get the absolute file path for this item.
     *
     * @return The absolute file path to the item.
     */
    public String getPath() {
        return Path;
    }

    /**
     * Set the absolute file path to the item.
     *
     * @param Path The absolute file path to the item.
     */
    public void setPath(String Path) {
        this.Path = Path;
    }
}
