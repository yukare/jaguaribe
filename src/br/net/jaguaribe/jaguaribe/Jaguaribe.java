/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.jaguaribe;

import br.net.jaguaribe.common.CommandLineParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Main class from the application.
 *
 * @author Fernando Correa da Conceição
 */
public class Jaguaribe {

    /**
     * The constant for the application version
     */
    public static final String VERSION = "0.1";

    /**
     * Static instance of this class, used in singleton.
     */
    private static Jaguaribe instance = new Jaguaribe();

    /**
     * Logger used in all application.
     */
    private static final Logger logger = null;

    /**
     * Singleton, get a instance of this class.
     *
     * @return The only instance of this class.
     */
    public static Jaguaribe getInstance() {
        return instance;
    }

    /**
     * Properties for the application
     */
    private Properties appProp = new Properties();

    /**
     * Files/Folder to open passed as argument in command line.
     */
    private String[] files;

    /**
     * Main frame of application.
     */
    private JaguaribeFrame frame;

    /**
     * Resource with the messages for i18n
     */
    private ResourceBundle messages;

    /**
     * Folder where store configuration and log files for user.
     */
    private String userConfigFolder = null;

    /**
     * Path where config/data for the user is stored.
     */
    private String userConfigPath;

    /**
     * Properties for the user.
     */
    private Properties userProp = new Properties();

    /**
     * Private constructor used in singleton
     */
    private Jaguaribe() {
    }

    /**
     * Get the Properties object with configuration from the application.
     *
     * @return The Properties object with configuration from the application.
     */
    public Properties getAppProp() {
        return appProp;
    }

    /**
     * Get the files passed on command line as aguments to open.
     *
     * @return The files.
     */
    public String[] getFiles() {
        return files;
    }

    /**
     * Get the main frame of application.
     *
     * @return The main frame of application.
     */
    public JaguaribeFrame getFrame() {
        return frame;
    }

    /**
     * Get the resource blunde used in translation.
     *
     * @return The resource blunde for translations.
     */
    public ResourceBundle getMessages() {
        return messages;
    }

    /**
     * Get the path to store configuration file for user.
     *
     * @return The path to store configuration file for user.
     */
    public String getUserConfigPath() {
        return this.userConfigPath;
    }

    /**
     * Get the Properties object with configuration from the user.
     *
     * @return The Properties object with configuration from the user.
     */
    public Properties getUserProp() {
        return userProp;
    }

    /**
     * Setup the messages translation.
     */
    private void i18n() {
        Locale currentLocale = Locale.getDefault();
        this.messages = ResourceBundle.getBundle("br/net/jaguaribe/jaguaribe/Bundle", currentLocale);
    }

    /**
     * Parse the arguments from command line.
     *
     * Do the apropriate actions depending on the arguments from command line.
     *
     * @param args The arguments from command line, same value as used in
     * main().
     */
    private void parseCommandLine(String[] args) {
        CommandLineParser parser = new CommandLineParser();
        CommandLineParser.Option help = parser.addBooleanOption('h', "help");
        parser.addHelp(help, "View This help");
        CommandLineParser.Option version = parser.addBooleanOption('v',
                "version");
        parser.addHelp(version, "Show the version from application");

        try {
            parser.parse(args);
        } catch (CommandLineParser.OptionException e) {
            System.err.println(e.getMessage());
            parser.printUsage();
            System.exit(2);
        }

        // parse help option
        Boolean helpValue = (Boolean) parser
                .getOptionValue(help, Boolean.FALSE);
        if (helpValue) {
            parser.printUsage();
            System.exit(0);
        }

        // parse version option
        Boolean versionValue = (Boolean) parser.getOptionValue(version,
                Boolean.FALSE);
        if (versionValue) {
            System.out.println("Version " + Jaguaribe.VERSION);
            System.exit(0);
        }

        // parse undefined values, like file names at end of options
        this.files = parser.getRemainingArgs();
    }

    /**
     * Read the file with configuration.
     */
    private void readConfig() {
        InputStream inputApp = this.getClass().getClassLoader().getResourceAsStream("config/config.properties");
        try {
            this.getAppProp().load(inputApp);
        } catch (IOException ex) {
            Logger.getLogger(Jaguaribe.class.getName()).log(Level.SEVERE, null, ex);
        }
        String userConfigDir = System.getProperty("user.home") + "/"
                + getAppProp().getProperty("config_dir");
        File path = new File(userConfigDir);
        if (!path.isDirectory()) {
            path.mkdir();
        }
        File file = new File(userConfigDir + "/config.properties");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Jaguaribe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            FileInputStream inputUser = new FileInputStream(userConfigDir + "/config.properties");
            try {
                this.getUserProp().load(inputUser);
            } catch (IOException ex) {
                Logger.getLogger(Jaguaribe.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Jaguaribe.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.userConfigPath = userConfigDir;
    }

    /**
     * Set the look and feel.
     */
    private void setLookFeel() {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
    }

    /**
     * Start the application.
     *
     * This function do all basic tasks to start the application.
     *
     * @param args This is the same args from main().
     */
    public void start(String[] args) {
        this.readConfig();
        this.parseCommandLine(args);
        this.setLookFeel();
        this.i18n();
        frame = new JaguaribeFrame();
        frame.buildUI();
        if (this.files.length > 0) {
            frame.open(this.files);
        }
        frame.setVisible(true);
    }
}
