package com.proyecto;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mindrot.jbcrypt.BCrypt;

public class Main {
    private Properties properties;
    private static Logger logger = LoggerFactory.getLogger(Main.class.getName());

    public Main(String filePath) {
        properties = new Properties();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static void main(String[] args) {
        Main configReader = new Main("config.properties");
        String password = configReader.getProperty("password");

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try {
            if (BCrypt.checkpw(password, hashedPassword)) {
                logger.info("Contraseña válida");
            } else {
                logger.info("Contraseña inválida");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
