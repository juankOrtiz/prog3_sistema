package com.juan.sistema.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();
    static {
        try(InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if(input == null) {
                System.out.println("No existe contenido en el archivo de configuracion");
            }
            properties.load(input);
        } catch(IOException ex) {
            System.out.println("No existe el archivo");
        }
    }

    public static String getPropiedad(String clave) {
        return properties.getProperty(clave);
    }
}

