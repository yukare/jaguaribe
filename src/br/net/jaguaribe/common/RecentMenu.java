/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Manage a ordered list of recent itens.
 *
 * @author Fernando Correa da Conceição
 */
public class RecentMenu {

    /**
     * Array with the itens.
     */
    private ArrayList<RecentItem> itens = new ArrayList<RecentItem>();

    /**
     * Filename where to store the itens.
     */
    private String filename = new String();

    /**
     * The maximun number of itens.
     */
    private int limit = 0;

    /**
     * The menu where the itens are.
     */
    private JMenu menu;

    /**
     * This is the class which receive the clicks on menu.
     */
    private RecentMenuInterface observer = null;

    /**
     * Add a file name at the end of the list.
     *
     * @param file The name of file to add.
     *
     * @todo Trown an exception if can not add the file name.
     */
    public void add(String file) {
        final RecentItem item = new RecentItem(file);
        item.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (observer != null) {
                    observer.recentMenuItemClicked(item, event);
                }
            }
        });
        this.itens.add(item);
        this.limitItens();
    }

    /**
     * Count the number of itens in list.
     *
     * @return Th number of itens in list.
     */
    public int count() {
        return (this.itens.size());
    }

    /**
     * Return the item from list.
     *
     * Return the item at given position,
     *
     * @param index The index of item to get
     * @return The item from the index.
     *
     * @todo Return null if the index is invalid.
     * @todo Trown an exception if the index is invalid.
     */
    public RecentItem get(int index) {
        return (this.itens.get(index));
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Set the filename.
     * 
     * @param filename The filename to set.
     * 
     * @return This class.
     */
    public RecentMenu setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    /**
     * Get the limit of items.
     * 
     * @return The limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Set the limit of items.
     * 
     * @param limit The limit to set.
     * 
     * @return This class.
     */
    public RecentMenu setLimit(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Limit must be 0 or greather, given: " + limit);
        }
        this.limit = limit;
        return this;
    }

    /**
     * Get the menu.
     * 
     * @return The menu.
     */
    public JMenuItem getMenu() {
        return menu;
    }

    /**
     * Set the menu.
     * 
     * @param menu The menu to set.
     * 
     * @return This class.
     */
    public RecentMenu setMenu(JMenu menu) {
        this.menu = menu;
        return this;
    }

    /**
     * Update the menu.
     */
    public void update() {
        if (this.itens.isEmpty()) {
            TextFile text = new TextFile(this.filename);
            try {
                ArrayList<String> lines = text.readLines();
                RecentItem i;
                for (String line : lines) {
                    this.add(line);
                }
                this.addToMenu();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(RecentMenu.class.getName()).log(Level.SEVERE,
                        "The file with the menu itens is not available: " + this.filename, ex);
            } catch (IOException ex) {
                Logger.getLogger(RecentMenu.class.getName()).log(Level.SEVERE,
                        "Error while loading the file with the menu itens: " + this.filename, ex);
            }
        } else {
            this.removeFromMenu();
            this.addToMenu();
        }
    }

    /**
     * Add the current itens to menu.
     */
    private void addToMenu() {
        int len = this.itens.size();
        for (int count = len - 1; count > -1; count--) {
            this.menu.add(this.itens.get(count));
        }
        this.menu.validate();
    }

    /**
     * Remove the current itens from menu.
     */
    private void removeFromMenu() {
        this.menu.removeAll();
    }

    /**
     * Save the current recent itens in the file
     * 
     * @throws java.io.IOException Exception if can not save the file.
     */
    public void save() throws IOException {
        StringBuilder lines = new StringBuilder(1000);
        for (RecentItem item : this.itens) {
            lines.append(item.getContent()).append("\n");
        }
        TextFile text = new TextFile(this.filename);
        text.write(lines.toString());
    }

    /**
     * Get the observer.
     * 
     * @return The observer.
     */
    public RecentMenuInterface getObserver() {
        return observer;
    }

    /**
     * Set the observer.
     * 
     * @param observer The observer to set.
     * 
     * @return This class.
     */
    public RecentMenu setObserver(RecentMenuInterface observer) {
        this.observer = observer;
        return this;
    }

    /**
     * Remove the itens until there is only items enought to fit in the limit.
     */
    public void limitItens() {
        int count = this.itens.size();
        while (count > this.limit) {
            this.itens.remove(0);
            count = this.itens.size();
        }
    }
}
