package controller.util;

import exceptions.PropertyParserInitException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This is utility class used to work with properties
 */

public class PropertyParser {
    private static final String PATH_TO_PROPERTIES = "staff/properties/file.properties", EX_STR = "Can't init file ";
    private Properties properties;

    public PropertyParser() throws PropertyParserInitException {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(PATH_TO_PROPERTIES)) {
            properties.load(fis);
        } catch (IOException ex) {
            throw new PropertyParserInitException(EX_STR + ex.getMessage());
        }
    }


    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
