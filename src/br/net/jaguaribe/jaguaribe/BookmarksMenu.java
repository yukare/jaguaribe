/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.jaguaribe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * The menu for bookmarks.
 *
 * This class is the menu for bookmarks, it does all the work of creating the
 * default itens (add/manage) and the itens from saved bookmarks.
 *
 * @author Fernando Correa da Conceição
 */
public class BookmarksMenu extends JMenu {

    /**
     * ID used for class serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Manage the data of bookmark itens.
     */
    private BookmarksData data = BookmarksData.getInstance();

    /**
     * The menu item to add a new bookmark.
     */
    private JMenuItem addNewItem;

    /**
     * Constructor.
     *
     * Call the private methods used to add the itens to menu.
     */
    public BookmarksMenu() {
        super();
        this.buildMenu();
    }

    /**
     * Constructor.
     *
     * @param s The string to use in menu.
     */
    public BookmarksMenu(String s) {
        super(s);
        this.buildMenu();
    }

    /**
     * Add the itens to menu.
     */
    private void buildMenu() {
        // Get the resource used in translation
        ResourceBundle msg = Jaguaribe.getInstance().getMessages();

        // Create the item add new
        this.addNewItem = new JMenuItem(msg.getString("BookmarksMenu.AddNew"));
        this.addNewItem.setToolTipText(msg.getString("BookmarksMenu.AddNew.Tooltip"));
        this.addNewItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                addNewItemActionPerfomed(event);
            }
        });
        this.add(this.addNewItem);

        ArrayList<BookmarksItem> items = data.getItems();
        BookmarksMenuItem menu;
        for (BookmarksItem item : items) {
            this.addBookmark(item);
        }
    }

    /**
     * Add a new bookmark on the menu.
     *
     * @param item The bookmark to add.
     */
    protected void addBookmark(BookmarksItem item) {
        BookmarksMenuItem menu;
        menu = new BookmarksMenuItem(item.getName());
        menu.setPath(item.getPath());
        menu.setToolTipText(item.getPath());
        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                bookmarkItemActionPerfomed(event);
            }
        });
        this.add(menu);
    }

    /**
     * Event - When the user clicks on menu add new bookmark
     *
     * @param event The MouseEvent
     */
    private void addNewItemActionPerfomed(ActionEvent event) {
        BookmarksAddFrame dialog = new BookmarksAddFrame(Jaguaribe.getInstance().getFrame(), true);
        dialog.setVisible(true);
    }

    /**
     * Event - When the user clicks on button "Ok"
     *
     * @param event The MouseEvent
     */
    private void bookmarkItemActionPerfomed(ActionEvent event) {
        BookmarksMenuItem item = (BookmarksMenuItem) event.getSource();
        Jaguaribe.getInstance().getFrame().open(item.getPath());
    }
}
