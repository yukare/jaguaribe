/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.jaguaribe;

import br.net.jaguaribe.common.FileTypeFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The menu with stuff related to slideshow.
 *
 * @author Fernando Correa da Conceição.
 */
class SlideshowMenu extends JMenu {

    // Id used for serialization
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    public SlideshowMenu() {
        super();
        this.buildMenu();
    }

    /**
     * Constructor.
     *
     * @param s The string to use in menu.
     */
    public SlideshowMenu(String s) {
        super(s);
        this.buildMenu();
    }

    /**
     * Create the menu, add the items.
     */
    private void buildMenu() {
        ResourceBundle msg = Jaguaribe.getInstance().getMessages();

        // create the new slideshow item
        JMenuItem newMenuItem = new JMenuItem(msg.getString("SlideshowMenu.New"));
        newMenuItem.setToolTipText(msg.getString("SlideshowMenu.New.Tooltip"));
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                newMenuItemActionPerformed(event);
            }
        });
        this.add(newMenuItem);

        // create the open slideshow item
        JMenuItem openMenuItem = new JMenuItem(msg.getString("SlideshowMenu.Open"));
        openMenuItem.setToolTipText(msg.getString("SlideshowMenu.Open.Tooltip"));
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                openMenuItemActionPerformed(event);
            }
        });
        this.add(openMenuItem);

        // create the save slideshow item
        JMenuItem saveMenuItem = new JMenuItem(msg.getString("SlideshowMenu.Save"));
        saveMenuItem.setToolTipText(msg.getString("SlideshowMenu.Save.Tooltip"));
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                saveMenuItemActionPerformed(event);
            }
        });
        this.add(saveMenuItem);

        // create the save as slideshow item
        JMenuItem saveAsMenuItem = new JMenuItem(msg.getString("SlideshowMenu.SaveAs"));
        saveAsMenuItem.setToolTipText(msg.getString("SlideshowMenu.SaveAs.Tooltip"));
        saveAsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                saveAsMenuItemActionPerformed(event);
            }
        });
        this.add(saveAsMenuItem);
    }

    /**
     * When the user clicks on menu item "New".
     *
     * @param event The event, current unused.
     */
    private void newMenuItemActionPerformed(ActionEvent event) {
        SlideshowFrame slideshow = new SlideshowFrame(Jaguaribe.getInstance().getFrame(), true);
        slideshow.setVisible(true);
    }

    /**
     * When the user clicks on menu item "Open".
     *
     * Let the user choose a file to open, after this, load the slideshow from
     * file and make it the current slideshow.
     *
     * @param event The event, current unused.
     */
    private void openMenuItemActionPerformed(ActionEvent event) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Slideshow Files", "sld");
        chooser.addChoosableFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            Slideshow slideshow = new Slideshow();
            slideshow.setFile(chooser.getSelectedFile().getAbsolutePath());
            slideshow.loadState();
            Jaguaribe.getInstance().getFrame().setSlideshow(slideshow);
        } else {
            chooser.setVisible(false);
        }
    }

    /**
     * When the user clicks on menu item "Save".
     *
     * Save the current slideshow.
     *
     * @param event The event, current unused.
     *
     * @todo Show a message when the file is save.
     */
    private void saveMenuItemActionPerformed(ActionEvent event) {
        Slideshow slideshow = Jaguaribe.getInstance().getFrame().getSlideshow();
        if (slideshow.getFile().isEmpty()) {
            this.saveAsMenuItemActionPerformed(event);
        } else {
            slideshow.saveState();
        }
    }

    /**
     * When the user clicks on menu item "Save As".
     *
     * Save the current slideshow with a new name.
     *
     * @param event The event, current unused.
     */
    private void saveAsMenuItemActionPerformed(ActionEvent event) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Slideshow Files", "sld");
        chooser.addChoosableFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            Slideshow slideshow = Jaguaribe.getInstance().getFrame().getSlideshow();
            slideshow.setFile(chooser.getSelectedFile().getAbsolutePath());
            slideshow.saveState();
            Jaguaribe.getInstance().getFrame().setSlideshow(slideshow);
        }
    }
}
