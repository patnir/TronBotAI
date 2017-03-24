/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tron.properties;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author User
 */
public class TronProperties {
    private static String DEFAULT_PROPERTY_FILENAME = "config/defaults.properties";
    private static String OPTIONS_PROPERTY_FILENAME = "options.properties";

    private static TronProperties singleton;

    private Properties props;

    public static TronProperties getSingleton() {
        if(singleton == null) {
            singleton = new TronProperties();
        }

        return singleton;
    }

    private TronProperties() {
        Properties defaults = new Properties();
        
        try {
            defaults.load(this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROPERTY_FILENAME));

            props = new Properties(defaults);
            props.load(new FileInputStream(new File(OPTIONS_PROPERTY_FILENAME)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public void setProperty(String key, String value) {
        props.setProperty(key, value);
    }

}
