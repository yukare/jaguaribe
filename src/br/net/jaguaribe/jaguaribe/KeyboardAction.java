/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.jaguaribe;

import javax.swing.ImageIcon;

/**
 * A action in keyboard
 *
 * @author Fernando Correa da Conceição
 */
public class KeyboardAction {

    /**
     * User readable name of the action, as show in menu and preferences.
     */
    private String name = "";

    /**
     * Keystroke combination used for action, like: control s.
     */
    private String keyStroke = "";

    /**
     * Default value for keystroke.
     */
    private String defaultValue = "";

    /**
     * Used for tooltips.
     */
    private String description = "";

    /**
     * The name of the file with the icon.
     */
    private String iconName = "";

    /**
     * Unique id for the action, same name as the action key in hashtable.
     */
    private String id = "";

    /**
     * Icon object for the action, can use an icon or set a name, in this case,
     * this object is updated with the icon name given.
     */
    private ImageIcon icon = null;

    /**
     * Set the Id.
     *
     * @param Id Unique id for the action, same name as the action key in
     * hashtable.
     *
     * @return This object.
     */
    public KeyboardAction setId(String Id) {
        this.id = Id;
        return this;
    }

    /**
     * Set the name of this action
     *
     * @param actionName User readable name of the action, as show in menu and
     * preferences.
     *
     * @return This object.
     */
    public KeyboardAction setName(String actionName) {
        this.name = actionName;
        return this;
    }

    /**
     * Get the name.
     *
     * @return User readable name of the action, as show in menu and
     * preferences.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the Id.
     *
     * @return Unique id for the action, same name as the action key in
     * hashtable.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Set the image for the action.
     *
     * This image is used in toolbar and menu.
     *
     * @param ico The icon to use.
     *
     * @return This object.
     */
    public KeyboardAction setIcon(ImageIcon ico) {
        this.icon = ico;
        return this;
    }

    /**
     * Set the image for the action.
     *
     * This image is used in toolbar and menu.
     *
     * @param name The name of the file with the icon, the file must be one of
     * the stored in jar.
     *
     * @return This object.
     */
    public KeyboardAction setIcon(String name) {
        this.iconName = name;
        this.icon = new ImageIcon(getClass().getResource(name));
        return this;
    }

    /**
     * Get the icon.
     *
     * @return Get the icon used in toolbal/menu.
     */
    public ImageIcon getIcon() {
        return this.icon;
    }

    /**
     * Get the icon name.
     *
     * @return Get the name of the icon from jar file.
     */
    public String getIconName() {
        return this.iconName;
    }

    /**
     * Get the keystroke.
     *
     * @return The keyStroke.
     */
    public String getKeyStroke() {
        return keyStroke;
    }

    /**
     * Set the keystroke.
     *
     * @param keyStroke The keyStroke to set
     *
     * @return This object.
     */
    public KeyboardAction setKeyStroke(String keyStroke) {
        this.keyStroke = keyStroke;
        return this;
    }

    /**
     * Get the default value for keystroke.
     *
     * @return The default value for keystroke.
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Set the default value for keystroke.
     *
     * @param defaultValue The default value to set.
     *
     * @return This object.
     */
    public KeyboardAction setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    /**
     * Get the description.
     *
     * This description is used as tooltips.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description.
     *
     * This description is used as tooltips.
     *
     * @param description The description to set.
     *
     * @return This object.
     */
    public KeyboardAction setDescription(String description) {
        this.description = description;
        return this;
    }
}
