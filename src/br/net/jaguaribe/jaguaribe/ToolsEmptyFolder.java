/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.jaguaribe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

/**
 * Tool to fix empty folders
 *
 * @author Fernando Correa da Conceição
 */
public class ToolsEmptyFolder {

    /**
     * Menu item used in main frame
     */
    JMenuItem menuItem;

    /**
     * Constructor.
     */
    ToolsEmptyFolder() {
        ResourceBundle msg = Jaguaribe.getInstance().getMessages();
        this.menuItem = new JMenuItem(msg.getString("ToolsEmptyFolder.MenuItem"));
        this.menuItem.setToolTipText(msg.getString("ToolsEmptyFolder.MenuItem.Tooltip"));
        this.menuItem.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setAcceptAllFileFilterUsed(false);
                int returnVal = fc.showDialog(Jaguaribe.getInstance().getFrame(), "Ok");
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    listFiles(fc.getSelectedFile().getAbsolutePath());

                }
            }
        });
    }

    /**
     * List folders from given path.
     *
     * This function will get all folders from the given path (choosen before by
     * the user) and call moveFiles() on each one.
     *
     * @param path The path to search for folders.
     */
    private void listFiles(String path) {
        File fileObj = new File(path);
        if (fileObj.isDirectory()) {
            File[] directories = fileObj.listFiles();
            for (File item : directories) {
                moveFiles(item.getAbsolutePath());
            }
        }
    }

    /**
     * Move files from empty folders.
     *
     * This function will test if the given path is an "Empty Folder", which is
     * a folder that contains only a folder inside it, and move the files from
     * the subfolder to this folder.
     *
     * @param path The path to remove thye empty folder.
     */
    void moveFiles(String path) {
        File fileObj = new File(path);
        if (fileObj.isDirectory()) {
            File[] directories = fileObj.listFiles();
            if (directories.length == 1) {
                File[] itens = directories[0].listFiles();
                for (File item : itens) {
                    item.renameTo(new File(path.concat("/").concat(item.getName())));
                }
                if (directories[0].list().length == 0) {
                    directories[0].delete();
                }
            }
        }
    }

    /**
     * Get the JMenuItem necessary to add this tool in the main menu.
     *
     * @return The JMenuItem to add in main menu.
     */
    public JMenuItem getMenuItem() {
        return this.menuItem;
    }
}
