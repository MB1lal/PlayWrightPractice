package core;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    private static final Properties prop = new Properties();

    public static String readPropertyFile(String propertyName)
    {
        String propertyFile= System.getProperty("user.dir") + "/src/test/resources/config.properties";
        try
        {
            InputStream input = new FileInputStream(propertyFile);
            prop.load(input);
            return prop.getProperty(propertyName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
