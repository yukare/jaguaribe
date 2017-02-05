/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.jaguaribe;

import br.net.jaguaribe.common.FileTypeFilter;
import br.net.jaguaribe.common.Utils;
import java.io.File;

/**
 * Manages a list of directories and files in an ordered way.
 *
 * Allow the navegation of the files inside a tree of directory in a way like
 * next/previous, this is used in the slideshow.
 *
 * @author Fernando Correa da Conceição
 */
public class DirectoryArray {

    /**
     * Array with current directories.
     */
    private File dirs[];

    /**
     * Array with files from current directory.
     */
    private File files[];

    /**
     * The numeric index of current directory
     */
    private int curDir;

    /**
     * The numeric index of current file.
     */
    private int curFile;

    /**
     * Constructor
     */
    public DirectoryArray() {
    }

    /**
     * Get the files and folders from a directory.
     *
     * This function reads a directory, parse it for sub dirs and files, after
     * this all navegation functions like next/prev are available.
     *
     * @param path This is the directory where to scan for files/folders
     */
    public void OpenDir(File path) {
        DirectoryFilter filter = new DirectoryFilter();
        this.dirs = path.listFiles(filter);
        this.dirs = Utils.fileArrayPrepend(this.dirs, path);
        this.dirs = Utils.sortFileNames(this.dirs);
        this.curDir = 0;
        this.curFile = 0;
        this.loadDir(0, 1);
    }

    /**
     * Get the files and parent folder from a file.
     *
     * This function is like OpenDir(File file) but it uses a file as parameter.
     * It add all the files from the same directory as the file, and adds all
     * directories from the same directory as the file.
     *
     * @param file The file that is in the folder which we want all the files.
     */
    public void OpenFile(File file) {
        File parent = file.getParentFile();
        DirectoryFilter filter = new DirectoryFilter();
        this.dirs = parent.listFiles(filter);
        this.dirs = Utils.fileArrayPrepend(this.dirs, parent);
        this.dirs = Utils.sortFileNames(this.dirs);
        this.curDir = 0;
        this.loadDir(0, 1);
        // Search for the index of the open file in the current directory
        int i;
        for (i = 0; i < this.files.length; i++) {
            if (this.files[i].getAbsolutePath().equals(file.getAbsolutePath())) {
                break;
            }
        }
        this.curFile = i;
    }

    /**
     * Get the current file.
     *
     * @return The File object of current file.
     */
    public File getFile() {
        return this.files[this.curFile];
    }

    /**
     * Change the current loaded dir.
     *
     * This function is used to change the current loaded dir. The dirNumber is
     * the index of directory to load, so when a directory do not have any file,
     * this function calls itself(recurse) with a new dirNumber modified by
     * change.
     *
     * @param dirNumber The index of directory to load.
     * @param change +1 mean go to next dir, -1 go to previous dir if the
     * current directory do not have files.
     */
    protected void loadDir(int dirNumber, int change) {
        FileTypeFilter filter = new FileTypeFilter();
        filter.addImage();
        if ((dirNumber > -1) && (dirNumber < this.dirs.length)) {
            File[] curFiles = this.dirs[dirNumber].listFiles(filter);
            if (curFiles.length == 0) {
                this.loadDir(dirNumber + change, change);
            } else {
                this.curFile = 0;
                this.files = Utils.sortFileNames(curFiles);
                this.curDir = dirNumber;
            }
        }
    }

    /**
     * Move to the next directory in the list.
     */
    protected void nextDir() {
        if (this.curDir == this.dirs.length - 1) {
            // last dir
        } else {
            this.loadDir(this.curDir + 1, 1);
        }
    }

    /**
     * Move to the next file in the list.
     */
    protected void nextFile() {
        if (this.files.length - 1 == this.curFile) {
            this.nextDir();
        } else {
            this.curFile++;
        }
    }

    /**
     * Move to previous directory in the list.
     */
    protected void previousDir() {
        if (this.curDir > 0) {
            this.loadDir(this.curDir - 1, -1);
        }
    }

    /**
     * Move to previous directory in the list.
     */
    protected void previousFile() {
        // store the number of current directory
        int dirNumber = this.curDir;
        if (this.curFile == 0) {
            this.previousDir();
            if (dirNumber > this.curDir) {
                this.curFile = this.files.length - 1;
            }
        } else {
            this.curFile--;
        }
    }

    /**
     * Get the index of the current file.
     *
     * @return The index of current file.
     */
    public int getCurrentFile() {
        return this.curFile + 1;
    }

    /**
     * Get the name of current file.
     *
     * @return The absolute file name of the current file.
     */
    public String getCurrentFileName() {
        return this.files[this.curFile].getAbsolutePath();
    }

    /**
     * Get the number of current directory.
     *
     * Get the current directory. The count starts on 1, to use something like
     * "This is the directory 1 of 5", not to use as an array index.
     *
     * @return The number of current directory.
     */
    public int getCurrentDir() {
        return this.curDir + 1;
    }

    /**
     * Get the number of directories.
     *
     * @return The number of directories.
     */
    public int getTotalDirs() {
        return this.dirs.length;
    }

    /**
     * Get the number of files.
     *
     * Get how many files have in the current directory, not the sun of all
     * directories.
     *
     * @return The number of files in current directory.
     */
    public int getTotalFiles() {
        return this.files.length;
    }
}
