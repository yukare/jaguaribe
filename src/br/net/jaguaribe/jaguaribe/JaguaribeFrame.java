/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.jaguaribe;

import br.net.jaguaribe.about.AboutDialog;
import br.net.jaguaribe.common.FileTypeFilter;
import br.net.jaguaribe.common.RecentItem;
import br.net.jaguaribe.common.RecentMenu;
import br.net.jaguaribe.common.RecentMenuInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import java.awt.event.MouseAdapter;
import javax.swing.ToolTipManager;
import java.util.concurrent.TimeUnit;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main frame of the application.
 *
 * This class is the main frame of the GUI.
 *
 * @author Fernando Correa da Conceição
 */
public class JaguaribeFrame extends JFrame implements RecentMenuInterface {

    /**
     * Class that hold all directories and files.
     */
    private Slideshow slideshow = new Slideshow();

    /**
     * Control with the image show.
     */
    private ImageIcon image;

    /**
     * This is the label that contains the image.
     */
    private ImageLabel label;

    /**
     * This the menu bar.
     */
    private JMenuBar menubar;

    /**
     * This is the toolbar.
     */
    private JToolBar toolbar;

    /**
     * This is the label with some info.
     */
    private JLabel statusbar;

    /**
     * If we are in full screen mode.
     */
    private boolean fullscreen;

    /**
     * The current state of frame before maximizing it.
     */
    private int frameStatus;

    /**
     * The menu with the bookmarks.
     */
    private BookmarksMenu bookmarksMenu;

    /**
     * The object with the user preferences.
     */
    private UserPreferences preferences;

    /**
     * Serial used for class serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Class with the recent itens in menu File.
     */
    private RecentMenu recentMenu;

    /**
     * Base class for other actions with the keyboard
     */
    abstract class BaseAction extends AbstractAction {

        BaseAction(KeyboardAction action) {
            super(action.getName(), action.getIcon());
            putValue(SHORT_DESCRIPTION, action.getDescription());
            if (!action.getKeyStroke().isEmpty()) {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(action.getKeyStroke()));
            }
        }

        void setActions(Action action, KeyboardAction keyboard) {
            KeyStroke keyStroke = KeyStroke.getKeyStroke(keyboard.getKeyStroke());
            getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                    put(keyStroke, keyboard.getId());
            getRootPane().getActionMap().put(keyboard.getId(), action);
        }
    }

    /**
     * Action to move to next image
     */
    class ActionMoveNextImage extends BaseAction {

        ActionMoveNextImage(KeyboardAction action) {
            super(action);
            setActions(this, action);
        }

        public void actionPerformed(ActionEvent ae) {
            if (slideshow.getTotalFiles() > 0) {
                slideshow.nextFile();
                updateImage();
            }
        }
    }

    /**
     * Action to move to previous image
     */
    class ActionMovePreviousImage extends BaseAction {

        ActionMovePreviousImage(KeyboardAction action) {
            super(action);
            setActions(this, action);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (slideshow.getTotalFiles() > 0) {
                slideshow.previousFile();
                updateImage();
            }
        }
    }

    /**
     * Action to move to previous folder
     */
    class ActionMovePreviousFolder extends BaseAction {

        ActionMovePreviousFolder(KeyboardAction action) {
            super(action);
            setActions(this, action);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (slideshow.getTotalFiles() > 0) {
                slideshow.previousDir();
                updateImage();
            }
        }
    }

    /**
     * Action to move to next folder
     */
    class ActionMoveNextFolder extends BaseAction {

        ActionMoveNextFolder(KeyboardAction action) {
            super(action);
            setActions(this, action);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (slideshow.getTotalFiles() > 0) {
                slideshow.nextDir();
                updateImage();
            }
        }
    }

    /**
     * Action to fullscreen
     */
    class ActionFullscreen extends BaseAction {

        ActionFullscreen(KeyboardAction action) {
            super(action);
            setActions(this, action);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (fullscreen) {
                setFullscreen(false);
                fullscreen = false;
                menubar.setVisible(true);
            } else {
                setFullscreen(true);
                fullscreen = true;
                menubar.setVisible(false);
            }
        }
    }

    /**
     * Build the UI.
     *
     * This function create all components from UI.
     */
    public void buildUI() {
        //Resource for translation
        ResourceBundle msg = Jaguaribe.getInstance().getMessages();

        //Preferences from user
        preferences = UserPreferences.getInstance();

        //properties for the frame
        this.setTitle(msg.getString("JaguaribeFrame.Title"));
        this.setSize(300, 500);
        this.setLocationRelativeTo(null);

        // Add event on window close, save the current slideshow info
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainWindowClosing(e);

            }
        });

        //Actions
        KeyStroke keyStroke;
        KeyboardAction action;

        //Next Image
        action = preferences.keyboard.actions.get("move_next_image");
        ActionMoveNextImage actionMoveNextImage = new ActionMoveNextImage(action);

        //Previous Image
        action = preferences.keyboard.actions.get("move_previous_image");
        ActionMovePreviousImage actionMovePreviousImage = new ActionMovePreviousImage(action);

        //Next Folder
        action = preferences.keyboard.actions.get("move_next_folder");
        ActionMoveNextFolder actionMoveNextFolder = new ActionMoveNextFolder(action);

        //Previous Folder
        action = preferences.keyboard.actions.get("move_previous_folder");
        ActionMovePreviousFolder actionMovePreviousFolder = new ActionMovePreviousFolder(action);

        //Fullscreen
        action = preferences.keyboard.actions.get("fullscreen");
        ActionFullscreen actionFullscreen = new ActionFullscreen(action);

        //Create the main panel
        JPanel panel = new JPanel(new BorderLayout());
        this.add(panel);

        //Create the top panel
        JPanel topPanel = new JPanel(new GridBagLayout());
        panel.add(topPanel, BorderLayout.PAGE_START);

        // create the menu bar
        this.menubar = new JMenuBar();

        // create the menu file
        JMenu fileMenu = new JMenu(msg.getString("JaguaribeFrame.FileMenu"));
        fileMenu.setMnemonic(KeyEvent.VK_F);
        this.menubar.add(fileMenu);

        // create the item open dir
        JMenuItem openMenuItem = new JMenuItem(msg.getString("JaguaribeFrame.FileMenu.Open"));
        openMenuItem.setMnemonic(KeyEvent.VK_A);
        openMenuItem.setToolTipText(msg.getString("JaguaribeFrame.FileMenu.Open.Tooltip"));
        openMenuItem.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                openMenuItemActionPerformed(event);
            }
        });
        fileMenu.add(openMenuItem);

        // Create the recent itens in menu file
        fileMenu.addSeparator();
        JMenu recentMenu = new JMenu(msg.getString("JaguaribeFrame.FileMenu.Recent"));
        fileMenu.add(recentMenu);
        this.recentMenu = new RecentMenu();
        this.recentMenu.setFilename(System.getProperty("user.home") + "/.jaguaribe" + "/" + "recentMenu.properties")
                .setMenu(recentMenu)
                .setLimit(5)
                .setObserver(this)
                .update();
        fileMenu.addSeparator();

        // create the item exit
        JMenuItem exitMenuItem = new JMenuItem(msg.getString("JaguaribeFrame.FileMenu.Exit"));
        exitMenuItem.setMnemonic(KeyEvent.VK_X);

        exitMenuItem.setToolTipText(msg.getString("JaguaribeFrame.FileMenu.Exit.Tooltip"));
        exitMenuItem.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                slideshow.saveState();
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        // A
        // Create the menu navigate
        JMenu navigateMenu = new JMenu(msg.getString("JaguaribeFrame.NavigateMenu"));

        // Create the menu navigate/previous image
        JMenuItem navigatePreviousImageMenuItem = new JMenuItem(actionMovePreviousImage);
        navigateMenu.add(navigatePreviousImageMenuItem);

        // create the menu navigate/next image
        JMenuItem navigateNextImageMenuItem = new JMenuItem(actionMoveNextImage);
        navigateMenu.add(navigateNextImageMenuItem);

        // Create the menu navigate/previous folder
        JMenuItem navigatePreviousFolderMenuItem = new JMenuItem(actionMovePreviousFolder);
        navigateMenu.add(navigatePreviousFolderMenuItem);

        // Create the menu navigate/previous folder
        JMenuItem navigateNextFolderMenuItem = new JMenuItem(actionMoveNextFolder);
        navigateMenu.add(navigateNextFolderMenuItem);

        this.menubar.add(navigateMenu);

        // Add the bookmarks menu
        this.bookmarksMenu = new BookmarksMenu(msg.getString("JaguaribeFrame.BookmarksMenu"));
        this.menubar.add(this.bookmarksMenu);

        // Create the menu slideshow the menu file
        SlideshowMenu slideshowMenu = new SlideshowMenu(msg.getString("JaguaribeFrame.SlideshowMenu"));
        this.menubar.add(slideshowMenu);

        // Create the menu tools
        JMenu toolsMenu = new JMenu(msg.getString("JaguaribeFrame.ToolsMenu"));

        // create the menu Tools/Fix empty folder
        ToolsEmptyFolder toolsEmptyFolder = new ToolsEmptyFolder();
        toolsMenu.add(toolsEmptyFolder.getMenuItem());

        // Create the item Tools/Resize
        ToolsResize toolsResize = new ToolsResize();
        toolsMenu.add(toolsResize.getMenuItem());

        // create the menu Tools/Rename
        ToolsRename toolsRename = new ToolsRename();
        toolsMenu.add(toolsRename.getMenuItem());

        // create the menu tools/preferences
        JMenuItem toolsPreferencesMenuItem = new JMenuItem(msg.getString("JaguaribeFrame.ToolsMenu.Preferences"));
        toolsPreferencesMenuItem.setToolTipText(msg.getString("JaguaribeFrame.ToolsMenu.Preferences.Tooltip"));
        toolsPreferencesMenuItem.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                PreferencesFrame window = new PreferencesFrame();
                window.setVisible(true);
            }
        });
        toolsMenu.add(toolsPreferencesMenuItem);

        this.menubar.add(toolsMenu);

        // Create the menu help
        JMenu helpMenu = new JMenu(msg.getString("JaguaribeFrame.HelpMenu"));

        // create the menu help/about
        JMenuItem helpAboutMenuItem = new JMenuItem(msg.getString("JaguaribeFrame.HelpMenu.About"));
        helpAboutMenuItem.setToolTipText(msg.getString("JaguaribeFrame.HelpMenu.About.Tooltip"));
        helpAboutMenuItem.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                helpAboutMenuItemActionPerformed(event);
            }
        });
        helpMenu.add(helpAboutMenuItem);
        this.menubar.add(helpMenu);

        // Create the toolbar and the items.
        this.toolbar = new JToolBar();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 0;
        c.anchor = GridBagConstraints.PAGE_START;
        c.weightx = 1;
        topPanel.add(this.toolbar, c);

        JButton previousImageButton = new JButton(actionMovePreviousImage);
        this.toolbar.add(previousImageButton);

        JButton nextImageButton = new JButton(actionMoveNextImage);
        this.toolbar.add(nextImageButton);

        JButton previousFolderButton = new JButton(actionMovePreviousFolder);
        this.toolbar.add(previousFolderButton);

        JButton nextFolderButton = new JButton(actionMoveNextFolder);
        this.toolbar.add(nextFolderButton);

        // Prevent the toolbar buttons getting focus and hide the text
        for (int i = 0; i < this.toolbar.getComponentCount(); i++) {
            if (this.toolbar.getComponent(i) instanceof JButton) {
                ((JButton) this.toolbar.getComponent(i)).setRequestFocusEnabled(false);
                ((JButton) this.toolbar.getComponent(i)).setFocusable(false);
                ((JButton) this.toolbar.getComponent(i)).setHideActionText(true);
            }
        }

        //Create the label with information about the file
        c.gridy = 1;
        this.statusbar = new JLabel(msg.getString("JaguaribeFrame.Statusbar"));
        this.statusbar.setBackground(Color.BLACK);
        this.statusbar.setForeground(Color.WHITE);
        this.statusbar.setOpaque(true);

        // Make the tooltip on label last longer.
        // This tooltip will contain information about the image.
        this.statusbar.addMouseListener(new MouseAdapter() {
            final int defaultDismissTimeout = ToolTipManager.sharedInstance().getDismissDelay();

            final int dismissDelayMinutes = (int) TimeUnit.MINUTES.toMillis(10); // 10 minutes

            @Override
            public void mouseEntered(MouseEvent me) {
                ToolTipManager.sharedInstance().setDismissDelay(dismissDelayMinutes);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                ToolTipManager.sharedInstance().setDismissDelay(defaultDismissTimeout);
            }
        });
        topPanel.add(this.statusbar, c);

        //Create the label with the image
        this.label = new ImageLabel();
        this.label.setFocusable(true);
        this.label.requestFocus();

        panel.add(this.label, BorderLayout.CENTER);

        this.setJMenuBar(menubar);

        panel.setBackground(Color.BLACK);

        // this.setExtendedState(JaguaribeFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Event. When the menu Help / About is clicked.
     *
     * @param event The event.
     */
    private void helpAboutMenuItemActionPerformed(ActionEvent event) {
        ResourceBundle msg = Jaguaribe.getInstance().getMessages();
        Properties appProp = Jaguaribe.getInstance().getAppProp();

        AboutDialog about = new AboutDialog(this, msg.getString("JaguaribeFrame.HelpMenu.About.Dialog.Title"));
        StringBuilder message = new StringBuilder(1000);
        message.append("Jaguaribe is an image browser written in java\n")
                .append("Version:").append(appProp.getProperty("app.build.version"))
                .append("\nBuild date:").append(appProp.getProperty("app.build.date"));
        about.addTabText(msg.getString("JaguaribeFrame.HelpMenu.About.Dialog.Tab.About"), message.toString());
        about.addTab(msg.getString("JaguaribeFrame.HelpMenu.About.Dialog.Tab.Readme"), "/text/README.txt");
        about.addTab(msg.getString("JaguaribeFrame.HelpMenu.About.Dialog.Tab.License"), "/text/LICENSE.txt");
        about.addTab(msg.getString("JaguaribeFrame.HelpMenu.About.Dialog.Tab.Authors"), "/text/AUTHORS.txt");
        about.setVisible(true);
    }

    /**
     * Event. When the window is closing.
     *
     * @param event The event.
     */
    private void mainWindowClosing(WindowEvent event) {
        slideshow.saveState();
        try {
            this.recentMenu.save();
        } catch (IOException ex) {
            Logger.getLogger(JaguaribeFrame.class.getName()).log(Level.SEVERE,
                    "Error while writting the file with recent menu itens: "
                    + this.recentMenu.getFilename(), ex);
        }
        System.exit(0);
    }

    /**
     * Open a file or directory.
     *
     * This function is used when the user pass a file or folder from the
     * command line.
     *
     * @param listFiles Array with names of files or folders to 0pen.
     */
    protected void open(String[] listFiles) {
        if (listFiles.length > 0) {
            this.open(listFiles[0]);
        }
    }

    /**
     * Open a file or directory.
     *
     * This function is used when the user pass a file or folder from the
     * command line.
     *
     * @param fileName The name of file or directory
     */
    public void open(String fileName) {
        this.slideshow = new Slideshow();
        // If the file is a valid slideshow file, load it and continue from
        // where it was
        if (this.slideshow.isValidFile(fileName)) {
            this.slideshow.setFile(fileName);
            this.slideshow.loadState();
            // If is not, create a new slideshow
        } else {
            this.slideshow.createFromPath(fileName);
        }
        this.updateImage();
    }

    /**
     * Event. When the menu File / Open is clicked.
     *
     * Promp the user for a file or folder and show it.
     *
     * @param event The event.
     */
    private void openMenuItemActionPerformed(ActionEvent event) {
        File path;
        FileTypeFilter filter = new FileTypeFilter();
        filter.addImage();
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setAcceptAllFileFilterUsed(false);
        //fc.setFileFilter(filter);
        int returnVal = fc.showDialog(this, "Ok");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            path = fc.getSelectedFile();
            this.slideshow = new Slideshow();
            this.slideshow.createFromPath(path.getAbsolutePath());
            File file = this.slideshow.getCurrentFile();
            this.recentMenu.add(file.getAbsolutePath());
            this.recentMenu.update();
            this.updateImage();
        }
    }

    /**
     * Change the GUI to/from full screen mode.
     *
     * Since the same code to full screen do not work in windows and linux, this
     * function simple test which so is in use and call the function for it.
     *
     * @param fullscreen When use or not the full screen mode
     */
    public void setFullscreen(boolean fullscreen) {
        String os = System.getProperty("os.name");
        String version = System.getProperty("os.version");
        if (os.equalsIgnoreCase("windows xp")) {
            this.setFullscreenWindows(fullscreen);
        } else {
            this.setFullscreenLinux(fullscreen);
        }
        // Dimension tamanho = this.label.getSize();
        // System.out.println("altura:"+tamanho.height);
        // System.out.println("largura:"+tamanho.width);
    }

    /**
     * Change the GUI to/from full screen mode on Windows.
     *
     * @param fullscreen When use or not the full screen mode
     */
    public void setFullscreenWindows(boolean fullscreen) {
        if (this.fullscreen != fullscreen) {
            // are we actually changing modes.
            // change modes.
            this.fullscreen = fullscreen;
            // toggle fullscreen mode
            if (!fullscreen) {
                this.setVisible(false);
                this.toolbar.setVisible(true);
                this.dispose();
                this.setUndecorated(false);
                this.setExtendedState(this.frameStatus);
                this.setVisible(true);
            } else { // change to full screen.
                this.setVisible(false);
                this.toolbar.setVisible(false);
                this.dispose();
                this.frameStatus = this.getExtendedState();
                this.setExtendedState(MAXIMIZED_BOTH);
                this.setUndecorated(true);
                this.setVisible(true);
            }
            // make sure that the screen is refreshed.
            repaint();
        }
    }

    /**
     * Change the GUI to/from full screen mode on Linux.
     *
     * @param fullscreen When use or not the full screen mode
     */
    public void setFullscreenLinux(boolean fullscreen) {
        // get a reference to the device.
        GraphicsDevice device = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode dispMode = device.getDisplayMode();

        // save the old display mode before changing it.
        DisplayMode dispModeOld = device.getDisplayMode();

        if (this.fullscreen != fullscreen) { // are we actually changing modes.
            // change modes.
            this.fullscreen = fullscreen;
            // toggle fullscreen mode
            if (!fullscreen) {
                // change to windowed mode.
                // set the display mode back to the what it was when
                // the program was launched.
                device.setDisplayMode(dispModeOld);
                // hide the frame so we can change it.
                setVisible(false);
                this.toolbar.setVisible(true);
                // remove the frame from being displayable.
                dispose();
                // put the borders back on the frame.
                setUndecorated(false);
                // needed to unset this window as the fullscreen window.
                device.setFullScreenWindow(null);
                // recenter window
                setLocationRelativeTo(null);
                setResizable(true);

                // reset the display mode to what it was before
                // we changed it.
                setVisible(true);

            } else { // change to full screen.
                // hide everything
                setVisible(false);
                this.toolbar.setVisible(false);
                // remove the frame from being displayable.
                dispose();
                // remove borders around the frame
                setUndecorated(true);
                // make the window full screen.
                device.setFullScreenWindow(this);
                // attempt to change the screen resolution.
                device.setDisplayMode(dispMode);
                setResizable(false);
                setAlwaysOnTop(false);
                // show the frame
                setVisible(true);
            }
            // make sure that the screen is refreshed.
            repaint();
        }
    }

    /**
     * Get the current slideshow.
     *
     * @return The slideshow.
     */
    public Slideshow getSlideshow() {
        return slideshow;
    }

    /**
     * Add a bookmark.
     *
     * @param item The bookmark to add.
     */
    protected void addBookmark(BookmarksItem item) {
        this.bookmarksMenu.addBookmark(item);
    }

    /**
     * Change the current slideshow.
     *
     * @param slideshow The slideshow to set.
     */
    public void setSlideshow(Slideshow slideshow) {
        this.slideshow = slideshow;
        File curFile = this.slideshow.getCurrentFile();
        this.label.setIcon(new ImageIcon(curFile.getAbsolutePath()));
        this.updateStatus();
    }

    /**
     * Update the status message.
     *
     * Update the message with the filename and the number of files.
     */
    private void updateStatus() {
        ResourceBundle res = Jaguaribe.getInstance().getMessages();
        String msg = res.getString("JaguaribeFrame.Statusbar") + this.slideshow.getCurrentFileName()
                + " (" + this.slideshow.getCurrentFileIndex() + "/"
                + this.slideshow.getTotalFiles() + ")";
        this.statusbar.setText(msg);
        ImageInfo info = new ImageInfo(this.slideshow.getCurrentFileName());
        info.showInfo();
        this.statusbar.setToolTipText(info.getRawInfo());
    }

    /**
     * Event. When an item in recent menu is clicked.
     *
     * This implements from RecentMenuInterface.
     *
     * @param item The item clicked.
     * @param event The event.
     */
    @Override
    public void recentMenuItemClicked(RecentItem item, ActionEvent event) {
        this.open(item.getContent());
    }

    protected void updateImage() {
        File file = slideshow.getCurrentFile();
        if (file != null) {
            label.setImageFile(file.getAbsolutePath());
        }
        updateStatus();
    }
}
