/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.common;

import java.io.File;
import javax.swing.JMenuItem;

/**
 * One item in a list of recent itens.
 *
 * @author Fernando Correa da Conceição
 */
public class RecentItem extends JMenuItem {

    /**
     * The content of the item.
     */
    private String content = new String();

    /**
     * Constructor.
     */
    RecentItem() {
        super();
    }

    /**
     * Constructor.
     *
     * @param value The content of the item.
     */
    RecentItem(String value) {
        super();
        this.setContent(value);
    }

    /**
     * Get the string content of the item.
     *
     * @return The content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the content of the item.
     *
     * @param content The content to set.
     */
    public void setContent(String content) {
        this.content = content;
        File file = new File(content);
        this.setText(file.getName());
        this.setToolTipText(content);
    }
}
