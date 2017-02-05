/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.jaguaribe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Manage the preferences for keyboard.
 *
 * @author Fernando Correa da Conceição
 */
public class KeyboardPreferences {

    /**
     * Associative array with the actions.
     */
    public HashMap<String, KeyboardAction> actions = new HashMap<String, KeyboardAction>();

    /**
     * Constructor, set the default values.
     */
    KeyboardPreferences() {
        this.setDefaults();
    }

    /**
     * Set the default values.
     *
     * This functio set the default values for actions, including keyboard
     * shortcuts, names, images.
     */
    void setDefaults() {
        //Resource for translation
        ResourceBundle msg = Jaguaribe.getInstance().getMessages();

        KeyboardAction action;
        // next image, right arrow
        action = new KeyboardAction();
        action.setDefaultValue("RIGHT").setName(msg.getString("KeyboardPreferences.Actions.MoveNextImage")).
                setIcon("/images/next.png").setId("move_next_image").
                setDescription(msg.getString("KeyboardPreferences.Actions.MoveNextImage.Description"));
        this.actions.put("move_next_image", action);

        // previous image, right arrow
        action = new KeyboardAction();
        action.setDefaultValue("LEFT").setName(msg.getString("KeyboardPreferences.Actions.MovePreviousImage")).
                setIcon("/images/previous.png").setId("move_previous_image").
                setDescription(msg.getString("KeyboardPreferences.Actions.MovePreviousImage.Description"));
        this.actions.put("move_previous_image", action);

        // previous folder, up arrow
        action = new KeyboardAction();
        action.setDefaultValue("UP").setName(msg.getString("KeyboardPreferences.Actions.MovePreviousFolder")).
                setIcon("/images/previousFolder.png").setId("move_next_folder").
                setDescription(msg.getString("KeyboardPreferences.Actions.MovePreviousFolder.Description"));
        this.actions.put("move_previous_folder", action);

        // next folder, down arrow
        action = new KeyboardAction();
        action.setDefaultValue("DOWN").setName(msg.getString("KeyboardPreferences.Actions.MoveNextFolder")).
                setIcon("/images/nextFolder.png").setId("move_previous_folder").
                setDescription(msg.getString("KeyboardPreferences.Actions.MoveNextFolder.Description"));
        this.actions.put("move_next_folder", action);

        // fullscreen, F11
        action = new KeyboardAction();
        action.setDefaultValue("F11").setName("Fullscreen").
                setIcon("/images/nextFolder.png").setId("fullscreen").
                setDescription("Full screen mode");
        this.actions.put("fullscreen", action);
    }

    /**
     *
     * @todo Look at this and document it.
     */
    void loadDefaults() {
        //for (Map.Entry<String, Object> entry : map.entrySet()) {
        //String key = entry.getKey();
        //Object value = entry.getValue();
        // ...
        for (KeyboardAction action : this.actions.values()) {
            action.setKeyStroke(action.getDefaultValue());
        }

    }

    /**
     * Get the values from a properties file and put in keyboard actions.
     *
     * @param config The properties object.
     */
    void parseValues(Properties config) {
        if (config.isEmpty()) {
            this.loadDefaults();
        } else {
            Set states = config.keySet(); // get set-view of keys
            Iterator itr = states.iterator();
            String key;
            while (itr.hasNext()) {
                key = (String) itr.next();
                KeyboardAction action = actions.get(key);
                if (action != null) {
                    action.setKeyStroke(config.getProperty(key));
                    actions.put(key, action);
                }
            }
        }
    }

    /**
     * Save the keyboard shortcuts
     *
     * @return Return the properties object where shortcuts are saved.
     */
    public Properties save() {
        Properties answer = new Properties();
        String key;
        KeyboardAction action;
        for (Map.Entry<String, KeyboardAction> entry : actions.entrySet()) {
            key = entry.getKey();
            action = entry.getValue();
            if (!action.getKeyStroke().isEmpty()) {
                answer.setProperty(key, action.getKeyStroke());
            }
        }
        return answer;
    }
}
