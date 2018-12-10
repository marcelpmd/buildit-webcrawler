package com.buildit;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig extends Properties {
    private static Logger log = LoggerFactory.getLogger(AppConfig.class);
    private static AppConfig instance = null;

    private AppConfig() { }

    public static AppConfig getInstance() {
        if (instance == null) {
            String configRoot = null;
            try {
                instance = new AppConfig();
                String rootDirectory = System.getProperty("ext.properties.dir");
                if (rootDirectory == null) {
                    throw new IllegalStateException("ext.properties.dir must be set");
                }
                String environment = System.getProperty("spring.profiles.active");
                if (environment == null) {
                    throw new IllegalStateException("spring.profiles.active must be set");
                }
                configRoot = rootDirectory + "/" + environment;
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                FileInputStream resourceStream = new FileInputStream(configRoot + "/app.properties");
                instance.load(resourceStream);
                resourceStream.close();
            } catch (Exception e) {
                log.error("Unable to initialize application configuration {} ",configRoot ,e.getMessage());
                return null;
            }
        }
        return instance;
    }
}
