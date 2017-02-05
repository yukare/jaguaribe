/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.net.jaguaribe.jaguaribe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

class UserPreferences {

    // Instance of this class, used for singleton
    private static UserPreferences userPreferencesObject;
    // Preferences related to keyboard

    public KeyboardPreferences keyboard;

    Properties config = new Properties();

    private UserPreferences() {
        this.keyboard = new KeyboardPreferences();
        this.keyboard.parseValues(this.loadFile("config.keyboard.properties"));

        this.config = this.loadFile("config.properties");

        // Parse the properties
        /*Set states = config.keySet(); // get set-view of keys
         Iterator itr = states.iterator();
         String key;
         while (itr.hasNext()) {
         key = (String) itr.next();
         System.out.println("key: "
         + key + " value: "
         + config.getProperty(key)
         + ".");
         }*/
    }

    private Properties loadFile(String name) {
        Properties prop = new Properties();
        String userConfigDir = System.getProperty("user.home") + "/.jaguaribe";
        File path = new File(userConfigDir);
        if (!path.isDirectory()) {
            path.mkdir();
        }
        File file = new File(userConfigDir + "/" + name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Jaguaribe.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        try {
            FileInputStream inputUser = new FileInputStream(userConfigDir + "/" + name);
            try {
                prop.load(inputUser);
            } catch (IOException ex) {
                Logger.getLogger(Jaguaribe.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Jaguaribe.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return prop;
    }

    public static synchronized UserPreferences getInstance() {
        if (userPreferencesObject == null) {
            userPreferencesObject = new UserPreferences();
        }
        return userPreferencesObject;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void save() {
        this.saveContents("config.keyboard.properties", this.keyboard.save());
    }

    public void saveContents(String filename, Properties properties) {
        FileOutputStream out;
        try {
            out = new FileOutputStream(System.getProperty("user.home") + "/.jaguaribe" + "/" + filename);
            try {
                properties.store(out, "---No Comment---");
            } catch (IOException ex) {
                Logger.getLogger(UserPreferences.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(UserPreferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserPreferences.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
