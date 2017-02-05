/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.xml;

import java.util.ArrayList;

/**
 * A node from a xml file.
 *
 * @author Fernando Correa da Conceição.
 */
public class XMLItem {

    /**
     * Name of this tag.
     */
    private String name;

    /**
     * Text from this tag.
     */
    private String text;

    /**
     * Array with all childs from this node.
     */
    private ArrayList<XMLItem> childs = new ArrayList<XMLItem>();

    /**
     * Associative array with attributes from this node.
     */
    private ArrayList<XMLAttr> attr = new ArrayList<XMLAttr>();

    /**
     * Constructor.
     */
    public XMLItem() {
    }

    /**
     * Constructor.
     *
     * @param tag The name of this tag.
     */
    public XMLItem(String tag) {
        this.name = tag;
    }

    /**
     * Get the name(tag) from this node.
     *
     * @return The name of this tag.
     */
    public String getName() {
        return name;
    }

    /**
     * Add a new atributte to this node.
     *
     * @param key The attribute name.
     * @param value The attribute value.
     */
    public void addAttr(String key, String value) {
        XMLAttr attribute = new XMLAttr(key, value);
        this.attr.add(attribute);
    }

    /**
     * Get the value of an attribute.
     *
     * @param key The name of the attribute
     *
     * @return The String with the value of the attribute or null if the
     * attribute does not exists.
     */
    public String getAttr(String key) {
        for (XMLAttr at : this.attr) {
            if (at.getKey().equalsIgnoreCase(key)) {
                return at.getValue();
            }
        }
        return null;
    }

    /**
     * Get all attributes from this node.
     *
     * @return All attributes from this node.
     */
    public ArrayList<XMLAttr> getAllAttr() {
        return this.attr;
    }

    /**
     * Add an item as child from this node.
     *
     * @param item The item to add.
     */
    public void addChild(XMLItem item) {
        this.childs.add(item);

    }

    /**
     * Set the name of this node.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the text from this node.
     *
     * @return The text from node.
     */
    public String getText() {
        return text;
    }

    /**
     * Set the text of this node.
     *
     * @param text The text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get all children from this nodes.
     *
     * @return An array with all childrens.
     */
    public ArrayList<XMLItem> getChildren() {
        return this.childs;
    }

    /**
     * Get a child using its name.
     *
     * Return the frist child with the given name
     *
     * @param name The name to search for.
     *
     * @return An XMLItem where the name is the given name.
     */
    public XMLItem getChild(String name) {
        for (XMLItem child : this.childs) {
            if (child.getName().equalsIgnoreCase(name)) {
                return child;
            }
        }
        return null;
    }
}
