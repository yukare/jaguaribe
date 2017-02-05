/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.jaguaribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Save/Load the data for bookmarks.
 *
 * @author Fernando Correa da Conceição
 */
public class BookmarksData {

    /**
     * An ArrayList with all bookmarks.
     */
    private ArrayList<BookmarksItem> items = new ArrayList<BookmarksItem>();

    /**
     * Instance of this class used for singleton.
     */
    private static BookmarksData instance = null;

    /**
     * Path to file with the bookmarks.
     */
    private String file;

    /**
     * Default Constructor.
     */
    private BookmarksData() {
        String userDir = Jaguaribe.getInstance().getUserConfigPath();
        File xmlBookmarks = new File(userDir + "/bookmarks.xml");
        this.file = userDir + "/bookmarks.xml";
        if (!xmlBookmarks.exists()) {
            this.createXML();
        }
        //The two lines below are just for getting an
        //instance of DocumentBuilder which we use
        //for parsing XML data
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            //Here we do the actual parsing
            Document doc;
            try {
                doc = builder.parse(xmlBookmarks);
                //Here we get the root element of XML and print out
                //the value of its 'testAttr' attribute
                Element rootEl = doc.getDocumentElement();
                //Here we get a list of all elements named 'child'
                NodeList list = rootEl.getElementsByTagName("bookmark");

                //Traversing all the elements from the list and printing
                //out its data
                for (int i = 0; i < list.getLength(); i++) {
                    //Getting one node from the list.
                    //BTW, we used method getElementsByTagName so every entry
                    //in the list is effectively of type 'Element', so you could
                    //cast it directly to 'Element' if you needed to.
                    BookmarksItem bookmark = new BookmarksItem();

                    Node childNode = list.item(i);
                    NodeList subNodes = childNode.getChildNodes();
                    for (int j = 0; j < subNodes.getLength(); j++) {
                        Node item = subNodes.item(j);
                        if (item.getNodeName().equalsIgnoreCase("name")) {
                            bookmark.setName(item.getTextContent());
                        } else if (item.getNodeName().equalsIgnoreCase("path")) {
                            bookmark.setPath(item.getTextContent());
                        }
                    }
                    this.items.add(bookmark);
                }
            } catch (SAXException ex) {
                Logger.getLogger(BookmarksData.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BookmarksData.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(BookmarksData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Create the bookmarks file for the current use if it do not exist.
     */
    private void createXML() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element el = doc.createElement("root");
            doc.appendChild(el);
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
     * Get all the bookmarks for the current use.
     *
     * @return An array with all bookmarks.
     */
    public ArrayList<BookmarksItem> getItems() {
        return items;
    }

    /**
     * Singleton.
     *
     * @return A instance of this class.
     */
    public static BookmarksData getInstance() {
        if (BookmarksData.instance == null) {
            BookmarksData.instance = new BookmarksData();
        }
        return (BookmarksData.instance);
    }

    /**
     * Add an item to bookmarks.
     *
     * @param item The item to add
     *
     * @return True if the item is added, false otherwise;
     */
    public boolean add(BookmarksItem item) {
        if (!this.search(item.getPath())) {
            this.items.add(item);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            try {
                builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();
                try {
                    doc = builder.parse(new File(this.file));
                } catch (SAXException ex) {
                    Logger.getLogger(BookmarksData.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(BookmarksData.class.getName()).log(Level.SEVERE, null, ex);
                }
                Element root = doc.getDocumentElement();
                Element bookmark = doc.createElement("bookmark");
                Element name = doc.createElement("name");
                name.setTextContent(item.getName());
                bookmark.appendChild(name);
                Element path = doc.createElement("path");
                path.setTextContent(item.getPath());
                bookmark.appendChild(path);
                root.appendChild(bookmark);
                DOMSource source = new DOMSource(doc);
                PrintStream ps;
                try {
                    ps = new PrintStream(file);
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
            return true;
        } else {
            return false;
        }

    }

    /**
     * Search for a bookmark based on his path.
     *
     * Compare all bookmarks and return true if one of then habe the same
     * absolute path as the path given.
     *
     * @param path The absolute path to search for.
     *
     * @return
     */
    private boolean search(String path) {
        for (BookmarksItem item : this.items) {
            if (item.getPath().trim().equals(path.trim())) {
                return true;
            }
        }
        return false;
    }
}
