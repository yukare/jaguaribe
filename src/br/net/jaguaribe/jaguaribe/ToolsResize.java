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
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

/**
 * Tool for resizing images.
 *
 * @author Fernando Correa da Conceição
 */
public class ToolsResize {

    /**
     * Menu item used in JaguaribeFrame for this tool.
     */
    private JMenuItem menuItem;

    /**
     * List with the files to resize.
     */
    private ArrayList<String> list;

    /**
     * The new height for files.
     */
    private int sizeH;

    /**
     * The new width for files.
     */
    private int sizeW;

    /**
     * Current file being processed.
     */
    private int current;

    /**
     * Name of current file.
     */
    private String currentFile = "";

    /**
     * The number of files to resize.
     */
    private int amount;

    /**
     * Dialog that show the progress of task.
     */
    private ProgressMonitor progressMonitor;

    /**
     * Constructor.
     */
    ToolsResize() {
        ResourceBundle msg = Jaguaribe.getInstance().getMessages();
        this.menuItem = new JMenuItem(msg.getString("ToolsResize.MenuItem"));
        this.menuItem.setToolTipText(msg.getString("ToolsResize.MenuItem.Tooltip"));
        this.menuItem.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                class ToDoTask extends TimerTask {

                    public void run() {
                        progressMonitor.setNote(currentFile);
                        progressMonitor.setProgress(current);
                    }
                }
                ToolsResizeDialog window = new ToolsResizeDialog(Jaguaribe.getInstance().getFrame(), true);
                window.setVisible(true);
                if (window.status) {
                    class Task implements Runnable {

                        public void run() {
                            for (String item : list) {
                                resize(item, sizeW, sizeH);
                                current++;
                                if (progressMonitor.isCanceled()) {
                                    break;
                                }
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressMonitor.setNote(current + " of " + amount);
                                        progressMonitor.setProgress(current);
                                    }
                                });
                            }
                        }
                    }
                    String path = window.getPath();
                    sizeH = window.getImageHeight();
                    sizeW = window.getImageWidth();
                    list = new ArrayList<String>();
                    current = 0;
                    File fileObj = new File(path);
                    if (fileObj.isDirectory()) {
                        listPath(fileObj.getAbsolutePath());
                    } else if (fileObj.isFile()) {
                        list.add(fileObj.getAbsolutePath());
                    }
                    amount = list.size();
                    progressMonitor = new ProgressMonitor(
                            Jaguaribe.getInstance().getFrame(), "Resizing Images",
                            "", 0, amount);
                    progressMonitor.setProgress(0);
                    Task r = new Task();
                    Thread thr = new Thread(r);
                    thr.start();
                }
            }
        });
    }

    /**
     * List files and folders.
     *
     * Create a list will all files from the folder.
     *
     * @param path The path to list.
     */
    private void listPath(String path) {
        File fileObj = new File(path);
        if (fileObj.isDirectory()) {
            FileTypeFilter filter = new FileTypeFilter();
            filter.addImage();
            filter.directories = true;
            File[] directories = fileObj.listFiles(filter);
            for (File directory : directories) {
                if (directory.isDirectory()) {
                    listPath(directory.getAbsolutePath());
                } else if (directory.isFile()) {
                    list.add(directory.getAbsolutePath());
                }
            }
        }
    }

    /**
     * Resize a file.
     *
     * @param path The file to resize.
     * @param sizeW The new weight.
     * @param sizeH The new height.
     */
    private void resize(String path, int sizeW, int sizeH) {
        System.out.println(path);
        ConvertCmd cmd = new ConvertCmd(true);

        // create the operation, add images and operators/options
        IMOperation op = new IMOperation();
        op.addImage(path);
        op.resize(sizeW, sizeH, ">");
        op.addImage(path);
        try {
            cmd.run(op);
        } catch (IOException ex) {
            Logger.getLogger(ToolsResize.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ToolsResize.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IM4JavaException ex) {
            Logger.getLogger(ToolsResize.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get the menu item for this tool.
     *
     * @return The menu item for this tool.
     */
    public JMenuItem getMenuItem() {
        return this.menuItem;
    }
}
