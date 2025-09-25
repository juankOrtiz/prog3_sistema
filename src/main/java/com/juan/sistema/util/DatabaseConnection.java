package com.juan.sistema.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConexion() throws SQLException {
        try {
            // Carga explícita del driver JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Manejar la excepción si el driver no se encuentra
            throw new SQLException("MySQL JDBC Driver no encontrado", e);
        }
        String url = Config.getPropiedad("db.url");
        String username = Config.getPropiedad("db.username");
        String password = Config.getPropiedad("db.password");
        return DriverManager.getConnection(url, username, password);
    }
}
