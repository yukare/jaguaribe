/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.jaguaribe;

import br.net.jaguaribe.common.FileTypeFilter;
import br.net.jaguaribe.common.Utils;
import br.net.jaguaribe.xml.XMLItem;
import br.net.jaguaribe.xml.XMLTree;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class reprensent a slideshow.
 *
 * This contains all the metods to work with slideshow, as load/save the file
 * and the functions needed to navigate in the items.
 *
 * @author Fernando Correa da Conceição.
 */
public class Slideshow {

    /**
     * Contains the absolute file name for this slideshow.
     */
    private String file;

    /**
     * Contains the itens of the slideshow.
     */
    private ArrayList<String> paths = new ArrayList<String>();

    /**
     * Contains all the items from the slideshow.
     */
    private ArrayList<SlideshowItem> items = new ArrayList<SlideshowItem>();

    /**
     * Option: when recurse in the subfolder when a folder is added.
     */
    private Boolean recurse = true;

    /**
     * Array with files from current directory
     */
    private File files[] = null;

    /**
     * The numeric index of current directory
     */
    private Integer currentItem = -1;

    /**
     * The numeric index of current file
     */
    private Integer currentFile = -1;

    /**
     * When randomize the items after creation
     */
    public Boolean randomItems = false;

    /**
     * Save the slideshow on disk.
     *
     * Save the list of itens in the slideshow.
     *
     * Note: must use the function setFile() to set the filename on disk before
     * using this function.
     */
    public void save() {
        if (this.file.isEmpty()) {
            return;
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element slideshow = doc.createElement("slideshow");
            doc.appendChild(slideshow);
            Element pathsEl = doc.createElement("paths");
            slideshow.appendChild(pathsEl);
            for (String item : this.paths) {
                Element path = doc.createElement("path");
                path.setTextContent(item);
                pathsEl.appendChild(path);
            }
            Element state = doc.createElement("state");
            slideshow.appendChild(state);
            DOMSource source = new DOMSource(doc);
            PrintStream ps;
            try {
                ps = new PrintStream(this.file);
                StreamResult result = new StreamResult(ps);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer;
                try {
                    transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                    try {
                        transformer.transform(source, result);
                    } catch (TransformerException ex) {
                        Logger.getLogger(BookmarksData.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (TransformerConfigurationException ex) {
                    Logger.getLogger(BookmarksData.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BookmarksData.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(BookmarksData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Save the current state of slideshow.
     *
     * This function save much more info than save(), it includes a list with
     * all folders and store the current item/file in view.
     *
     * Note: must use the function setFile() to set the filename on disk before
     * using this function.
     */
    public void saveState() {
        if (this.file == null || this.file.isEmpty()) {
            return;
        }
        XMLTree tree = new XMLTree();
        XMLItem root = new XMLItem("slideshow");
        tree.setRoot(root);
        XMLItem pathsItem = new XMLItem("paths");
        root.addChild(pathsItem);
        for (String path : this.paths) {
            XMLItem it = new XMLItem("path");
            it.setText(path);
            pathsItem.addChild(it);
        }
        XMLItem state = new XMLItem("state");
        state.addAttr("lastFile", this.currentFile.toString());
        state.addAttr("lastItem", this.currentItem.toString());
        root.addChild(state);
        for (SlideshowItem sl : this.items) {
            XMLItem it = new XMLItem("path");
            it.setText(sl.getPath());
            it.addAttr("addFiles", sl.getAddFiles().toString());
            it.addAttr("directory", sl.getAddFiles().toString());
            state.addChild(it);
        }
        tree.save(this.file);

    }

    /**
     * Load the list of files/folders from disk.
     *
     * Note: must use the function setFile() to set the filename on disk before
     * using this function.
     */
    public void load() {
        if (this.file.isEmpty()) {
            return;
        }
        XMLTree tree = new XMLTree();
        tree.load(this.file);
        XMLItem root = tree.getRoot();
        XMLItem pathList = root.getChild("paths");
        if (paths == null) {
            return;
        }
        this.paths.clear();
        ArrayList<XMLItem> children = pathList.getChildren();
        for (XMLItem path : children) {
            this.paths.add(path.getText());
        }
    }

    /**
     * Load the current state of slideshow from disk.
     *
     * This is an improved version from load, it load all the items from disk,
     * and not only a list from files/folder as load.
     *
     * Note: must use the function setFile() to set the filename on disk before
     * using this function.
     */
    public void loadState() {
        if (this.file.isEmpty()) {
            return;
        }
        XMLTree tree = new XMLTree();
        tree.load(this.file);
        XMLItem root = tree.getRoot();
        XMLItem pathList = root.getChild("paths");
        if (pathList == null) {
            return;
        }
        this.paths.clear();
        ArrayList<XMLItem> children = pathList.getChildren();
        for (XMLItem path : children) {
            this.paths.add(path.getText());
        }
        XMLItem state = root.getChild("state");
        if (state == null) {
            return;
        }
        if (state.getAttr("lastItem") != null) {
            this.currentItem = Integer.parseInt(state.getAttr("lastItem"));
        } else {
            this.currentItem = -1;
        }
        if (state.getAttr("lastFile") != null) {
            this.currentFile = Integer.parseInt(state.getAttr("lastFile"));
        }
        ArrayList<XMLItem> states = state.getChildren();
        for (XMLItem path : states) {
            SlideshowItem it = new SlideshowItem();
            it.setPath(path.getText());
            if (path.getAttr("directory") != null) {
                it.setDirectory(Boolean.parseBoolean(path.getAttr("directory")));
            }
            if (path.getAttr("addFiles") != null) {
                it.setAddFiles(Boolean.parseBoolean(path.getAttr("addFiles")));
            }
            this.items.add(it);
        }
        if (this.currentItem == -1) {
            this.createItems();
            int provFile = this.currentFile;
            this.loadItem(this.currentItem, 1);
            this.currentFile = provFile;
        } else {
            int prov = this.currentFile;
            this.loadItem(this.currentItem, 1);
            this.currentFile = prov;
        }

    }

    /**
     * Get the filename.
     *
     * @return The filename.
     *
     */
    public String getFile() {
        return file;
    }

    /**
     * Set the name of the file.
     *
     * @param file The name of the file for this slideshow.
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * Return all the items from slideshow.
     *
     * @return The items.
     */
    public ArrayList<String> getItems() {
        return paths;
    }

    /**
     * Set all the items for slideshow.
     *
     * @param items The items to set.
     */
    public void setItems(ArrayList<String> items) {
        this.paths = items;
    }

    /**
     * Create all items used in slideshow.
     *
     * Create the items from the list of files/folders.
     */
    public void createItems() {
        SlideshowItem it;
        for (String path : this.paths) {
            File fileObj = new File(path);
            if (fileObj.isDirectory()) {
                it = new SlideshowItem();
                it.setPath(fileObj.getAbsolutePath());
                it.setAddFiles(true);
                it.setDirectory(true);
                this.items.add(it);
                if (this.recurse) {
                    DirectoryFilter filter = new DirectoryFilter();
                    File[] directories = fileObj.listFiles(filter);
                    directories = Utils.sortFileNames(directories);
                    for (File directory : directories) {
                        it = new SlideshowItem();
                        it.setPath(directory.getAbsolutePath());
                        System.out.println(directory.getAbsolutePath());
                        it.setAddFiles(true);
                        it.setDirectory(true);
                        this.items.add(it);
                    }
                }
            } else if (fileObj.isFile()) {
                it = new SlideshowItem();
                it.setPath(fileObj.getAbsolutePath());
                it.setAddFiles(false);
                it.setDirectory(false);
                this.items.add(it);
            }
        }
        if (this.randomItems) {
            Collections.shuffle(this.items);
        }
        this.loadItem(0, 1);
    }

    /**
     * Create the list of items from a path.
     *
     * Create the list from a path not from a list of files/folders. Used to
     * open a single file or folder.
     *
     * @param path The path to create the items.
     */
    public void createFromPath(String path) {
        File fileObj = new File(path);
        ArrayList<String> list = new ArrayList<String>();
        if (fileObj.isDirectory()) {
            list.add(path);
            this.setItems(list);
            this.createItems();
        } else if (fileObj.isFile()) {
            File parent = fileObj.getParentFile();
            list.add(parent.getAbsolutePath());
            this.setItems(list);
            this.createItems();
            this.loadItem(0, 1);
            // Search for the index of the open file in the current directory
            int i;
            for (i = 0; i < this.files.length; i++) {
                if (this.files[i].getAbsolutePath().equals(fileObj.getAbsolutePath())) {
                    break;
                }
            }
            this.currentFile = i;
        }
    }

    /**
     * Get the current file.
     *
     * @return The File object of current file.
     */
    public File getCurrentFile() {
        if ((this.files != null) && (this.files.length > 0)) {
            return this.files[this.currentFile];
        } else {
            return null;
        }
    }

    /**
     * Change the current loaded dir.
     *
     * This function is used to change the current loaded dir. The dirNumber is
     * the index of directory to load, so when a directory do not have any file,
     * this function calls itself(recurse) with a new dirNumber modified by
     * change.
     *
     * @param itemNumber The number current loaded.
     * @param change +1 mean go to next dir, -1 go to previous dir if the
     * current directory do not have files.
     */
    protected void loadItem(int itemNumber, int change) {
        FileTypeFilter filter = new FileTypeFilter();
        filter.addImage();
        if ((itemNumber > -1) && (itemNumber < this.items.size())) {
            SlideshowItem current = this.items.get(itemNumber);
            if (current.getDirectory()) {
                File[] curFiles = new File(current.getPath()).listFiles(filter);
                if (curFiles.length == 0) {
                    this.loadItem(itemNumber + change, change);
                } else {
                    this.currentFile = 0;
                    this.files = Utils.sortFileNames(curFiles);
                    this.currentItem = itemNumber;
                }
            } else {
                this.currentFile = 0;
                this.files = new File[1];
                this.files[0] = new File(current.getPath());
                this.currentItem = itemNumber;
            }
        }
    }

    /**
     * Move to the next directory in the list.
     */
    protected void nextDir() {
        if (this.currentItem == this.items.size() - 1) {
            // last dir
        } else {
            this.loadItem(this.currentItem + 1, 1);
        }
    }

    /**
     * Move to the next file in the list.
     */
    protected void nextFile() {
        if (this.files.length - 1 == this.currentFile) {
            this.nextDir();
        } else {
            this.currentFile++;
        }
    }

    /**
     * Move to previous directory in the list.
     */
    protected void previousDir() {
        if (this.currentItem > 0) {
            this.loadItem(this.currentItem - 1, -1);
        }
    }

    /**
     * Move to previous directory in the list.
     */
    protected void previousFile() {
        // store the number of current directory
        int dirNumber = this.currentItem;
        if (this.currentFile == 0) {
            this.previousDir();
            if (dirNumber > this.currentItem) {
                this.currentFile = this.files.length - 1;
            }
        } else {
            this.currentFile--;
        }
    }

    /**
     * Get the index of the current file.
     *
     * @return The index of current file.
     */
    public int getCurrentFileIndex() {
        return this.currentFile + 1;
    }

    /**
     * Get the name of current file.
     *
     * @return The absolute file name of the current file.
     */
    public String getCurrentFileName() {
        return this.files[this.currentFile].getAbsolutePath();
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
        return this.currentItem + 1;
    }

    /**
     * Get the number of directories.
     *
     * @return The number of directories.
     */
    public int getTotalItems() {
        return this.items.size();
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
        if (this.files != null) {
            return this.files.length;
        } else {
            return 0;
        }
    }

    /**
     * Test if a file is a valid slideshow
     *
     * @param filename The file with the slideshow
     * @return true if is a valid slideshow
     * @todo Implement a better verification based on the contents of the file,
     * and not only in the extension.
     */
    public boolean isValidFile(String filename) {
        if (filename.endsWith(".sld")) {
            return true;
        }
        return false;
    }

    /**
     * Get the array with all slideshow itens.
     *
     * @return Get the array with all slideshow itens.
     */
    public ArrayList<SlideshowItem> getChildItems() {
        return items;
    }
}
