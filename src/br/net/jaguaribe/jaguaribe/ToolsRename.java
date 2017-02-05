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
import java.util.logging.Logger;
import javax.swing.JMenuItem;

/**
 * Tool to rename files/folder.
 *
 * @author Fernando Correa da Conceição
 */
public class ToolsRename {

    /**
     * Menu item used in main frame
     */
    JMenuItem menuItem;

    /**
     * Constant. Represent that we did not parse the item value
     */
    int START = 0;

    /**
     * Constant. We are current parsing the item value.
     */
    int ITEM = 1;

    /**
     * Constant. We are current parsing the volume value.
     */
    int VOLUME = 2;

    /**
     * Constant. We already parsed the item value and do not parse anything
     * more.
     */
    int END = 3;

    /**
     * Constructor.
     */
    ToolsRename() {
        ResourceBundle msg = Jaguaribe.getInstance().getMessages();
        this.menuItem = new JMenuItem(msg.getString("ToolsRename.MenuItem"));
        this.menuItem.setToolTipText(msg.getString("ToolsRename.MenuItem.Tooltip"));
        this.menuItem.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ToolsRenameDialog dialog = new ToolsRenameDialog(Jaguaribe.getInstance().getFrame(), true);
                dialog.setVisible(true);
                if (dialog.status = true) {
                    String path = dialog.getPath();
                    String folder = dialog.getFolder();
                    String file = dialog.getFile();
                    directoryRename(path, folder, file);
                }
            }
        });
    }

    /**
     * Get the menu item.
     *
     * Use this menu item to show a menu to run this class.
     *
     * @return The menu item.
     */
    public JMenuItem getMenuItem() {
        return this.menuItem;
    }

    /**
     * Rename a directory.
     *
     * @param path The folder to rename.
     * @param ruleDir The rule to use for directory.
     * @param ruleFile The rule to use for files.
     *
     * @todo Current files are not renamed, must make this, or change the
     * gui/function.
     */
    public void directoryRename(String path, String ruleDir, String ruleFile) {
        File fileObj = new File(path);
        String name;
        int status;
        String item;
        String volume = "";
        String previous = "";
        if (fileObj.isDirectory()) {
            DirectoryFilter filter = new DirectoryFilter();
            File[] directories = fileObj.listFiles(filter);
            for (File directory : directories) {
                name = directory.getName();
                status = START;
                item = "";
                volume = "";
                for (int index = 0; index < name.length(); index++) {
                    Character current = name.charAt(index);
                    if (Character.isDigit(current)) {
                        if (status == START) {
                            if (previous.equalsIgnoreCase("v")) {
                                status = VOLUME;
                                volume = volume.concat(current.toString());
                            } else {
                                status = ITEM;
                                item = item.concat(current.toString());
                            }
                        } else if (status == VOLUME) {
                            volume = volume.concat(current.toString());
                        } else if (status == ITEM) {
                            item = item.concat(current.toString());
                        }
                    } else {
                        if (status == ITEM) {
                            status = END;
                        } else {
                            status = START;
                        }
                    }
                    previous = current.toString();
                }
                boolean rename = false;
                int num;
                Integer len;
                // Need this "new" to make a clone
                String folder = new String(ruleDir);
                if (item.isEmpty()) {
                    folder = folder.replace("@item@", "");
                } else {
                    num = Integer.parseInt(item);
                    len = item.length();
                    folder = folder.replace("@item@", "%0".concat(String.valueOf(len)).concat("d"));
                    folder = String.format(folder, num);
                    rename = true;
                }
                if (volume.isEmpty()) {
                    folder = folder.replace("@volume@", "");
                } else {
                    num = Integer.parseInt(volume);
                    len = volume.length();
                    folder = folder.replace("@volume@", "%0".concat(String.valueOf(len)).concat("d"));
                    folder = String.format(folder, num);
                    rename = true;
                }
                if (rename) {
                    File newName = new File(path.concat("/").concat(folder));
                    directory.renameTo(newName);
                }
            }

        }
    }
}
