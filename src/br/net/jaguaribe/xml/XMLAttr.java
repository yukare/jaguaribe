/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.xml;

/**
 * Represents the key/value of one atribute in a node.
 *
 * @author fernando Correa da Conceição.
 */
public class XMLAttr {

    /**
     * The attribute.
     */
    private String keyAttr;

    /**
     * The value
     */
    private String valueAttr;

    /**
     * Constructor.
     */
    XMLAttr() {
    }

    /**
     * Constructor.
     *
     * @param key The key.
     * @param value The value.
     */
    XMLAttr(String key, String value) {
        this.keyAttr = key;
        this.valueAttr = value;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return this.keyAttr;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.keyAttr = key;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return this.valueAttr;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.valueAttr = value;
    }
}
