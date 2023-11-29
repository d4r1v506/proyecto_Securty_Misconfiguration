package com.proyecto;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    private Properties properties;

    public Main(String filePath) {
        System.out.println("ruta "+filePath);
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
        String username = configReader.getProperty("username");
        String password = configReader.getProperty("password");

        //solucion
         // Encriptar la contraseña
        //  String encryptedPassword = encryptPassword(password);

        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
    }

    /*private static String encryptPassword(String password) {
        // Implementar lógica para encriptar la contraseña aquí
        // Por ejemplo, se puede utilizar bcrypt o PBKDF2
        return encryptedPassword;
    }*/
}