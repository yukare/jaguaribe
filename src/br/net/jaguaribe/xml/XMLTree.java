/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.xml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A tree with nodes from a xml file.
 *
 * @author Fernando Correa da Conceição.
 */
public class XMLTree {

    /**
     * The item that is the root of the xml file.
     */
    private XMLItem rootItem;

    /**
     * Read a xml file.
     *
     * @param fileName The name of the xml file to read.
     */
    public void load(String fileName) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc;
            try {
                doc = builder.parse(fileName);
                Element root = doc.getDocumentElement();
                if (root != null) {
                    this.setRoot(new XMLItem());
                    this.rootItem.setName(root.getTagName());
                    this.rootItem.setText(root.getTextContent());
                    NodeList list = root.getChildNodes();
                    for (int i = 0; i < list.getLength(); i++) {
                        if (!list.item(i).getNodeName().equals("#text")) {
                            this.parseChild(list.item(i), this.rootItem);
                        }
                    }
                }
            } catch (SAXException ex) {
                Logger.getLogger(XMLTree.class.getName()).log(Level.SEVERE,
                        "There was an error while parsing the file.", ex);
            } catch (IOException ex) {
                Logger.getLogger(XMLTree.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLTree.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Parse a child node.
     *
     * This function get the tag name. the text, the attributes from the node
     * and recurse hinself for each child node.
     *
     * @param node The current node to parse.
     * @param parent The parent node.
     */
    private void parseChild(Node node, XMLItem parent) {
        XMLItem item = new XMLItem();
        item.setName(node.getNodeName());
        item.setText(node.getTextContent());
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            for (int a = 0; a < attributes.getLength(); a++) {
                Node attr = attributes.item(a);
                item.addAttr(attr.getNodeName(), attr.getTextContent());
            }
        }
        parent.addChild(item);

        NodeList subNodes = node.getChildNodes();
        for (int j = 0; j < subNodes.getLength(); j++) {
            Node child = subNodes.item(j);
            if (!child.getNodeName().equals("#text")) {
                this.parseChild(child, item);
            }
        }
    }

    /**
     * Get the root item.
     *
     * From the root item is possible to get all the child nodes.
     *
     * @return The root node.
     */
    public XMLItem getRoot() {
        return this.rootItem;
    }

    /**
     * Add itens in recursive way.
     *
     * @param item The item to add.
     * @param parent The parent item.
     * @param doc The xml document.
     */
    private void saveAddChild(XMLItem item, Element parent, Document doc) {
        Element el = doc.createElement(item.getName());
        el.setTextContent(item.getText());
        ArrayList<XMLAttr> attr = item.getAllAttr();
        for (XMLAttr entry : attr) {
            el.setAttribute(entry.getKey(), entry.getValue());
        }
        parent.appendChild(el);
        for (XMLItem it : item.getChildren()) {
            this.saveAddChild(it, el, doc);
        }
    }

    /**
     * Save the file.
     *
     * @param filename The file to save.
     */
    public void save(String filename) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element root = doc.createElement(this.rootItem.getName());
            ArrayList<XMLAttr> attr = this.rootItem.getAllAttr();
            for (XMLAttr entry : attr) {
                root.setAttribute(entry.getKey(), entry.getValue());
            }
            doc.appendChild(root);
            for (XMLItem item : this.rootItem.getChildren()) {
                this.saveAddChild(item, root, doc);
            }
            DOMSource source = new DOMSource(doc);
            PrintStream ps;
            try {
                ps = new PrintStream(filename);
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
                        Logger.getLogger(XMLTree.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (TransformerConfigurationException ex) {
                    Logger.getLogger(XMLTree.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(XMLTree.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLTree.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Set the root item.
     *
     * @param root the rootItem to set
     */
    public void setRoot(XMLItem root) {
        this.rootItem = root;
    }
}
