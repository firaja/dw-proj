package it.unumib.disco.dw.etl.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class ConfigLoader
{
    private final Properties properties;

    private final static Logger LOG = LogManager.getLogger(ConfigLoader.class);

    public ConfigLoader()
    {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            LOG.error("Error reading file 'application.properties'");
            LOG.error("The following exception has been thrown: " + e.getMessage());
        }
    }

    public String getProperty(String name)
    {
        return getProperty(name, "");
    }

    public String getProperty(String name, String defaultValue)
    {
        return properties.getProperty(name, defaultValue);
    }
}
