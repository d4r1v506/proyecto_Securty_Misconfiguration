package com.proyecto;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Main {
    private Properties properties;

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

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Main configReader = new Main("config.properties");
        String username = configReader.getProperty("username");
        String password = configReader.getProperty("password");

        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        // Encriptar la contraseña
        String encryptedPassword = encryptPassword(password, "P4S");
        System.out.println("contraseña ecriptada: " + encryptedPassword);

        try {
            String desencryptedPassword = decryptWithPBKDF2(encryptedPassword, password, "P4S");
            System.out.println("contraseña desencriptado: " + desencryptedPassword);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static String encryptPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Implementar lógica para encriptar la contraseña aquí
        // Por ejemplo, se puede utilizar bcrypt o PBKDF2
        int iterations = 10000; // Número de iteraciones
        int keyLength = 256; // Longitud de la clave en bits

        char[] pass = password.toCharArray(); // Convierte la variable en un arreglo de caracteres

        byte[] saltBytes = Base64.getDecoder().decode(salt); // Decodifica la sal en bytes

        KeySpec spec = new PBEKeySpec(pass, saltBytes, iterations, keyLength); // Crea la especificación de la clave
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); // Crea una instancia de la
                                                                                       // fábrica de claves
        byte[] keyBytes = factory.generateSecret(spec).getEncoded(); // Genera la clave en bytes

        String encryptedPassword = Base64.getEncoder().encodeToString(keyBytes); // Codifica la clave en Base64

        return encryptedPassword;
    }

    public static String decryptWithPBKDF2(String encryptedVariable, String password, String salt) throws Exception {
        int iterations = 10000; // Número de iteraciones
        int keyLength = 256; // Longitud de la clave en bits

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedVariable); // Decodifica la contraseña encriptada de
                                                                               // Base64 a bytes
        byte[] saltBytes = Base64.getDecoder().decode(salt); // Decodifica la sal en bytes

        char[] pass = password.toCharArray(); // Convierte la variable en un arreglo de caracteres

        KeySpec spec = new PBEKeySpec(pass, saltBytes, iterations, keyLength); // Crea la especificación de la clave
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); // Crea una instancia de la
                                                                                       // fábrica de claves
        byte[] keyBytes = factory.generateSecret(spec).getEncoded(); // Genera la clave en bytes

        // Compara los bytes de la clave generada con los bytes de la contraseña
        // encriptada
        if (Arrays.equals(encryptedBytes, keyBytes)) {
            return password; // Si coinciden, devuelve la contraseña original
        } else {
            return null; // Si no coinciden, devuelve null o maneja el caso de error según tus
                         // necesidades
        }
    }

}