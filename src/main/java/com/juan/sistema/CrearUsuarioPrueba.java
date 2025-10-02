package com.juan.sistema;

import com.juan.sistema.util.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrearUsuarioPrueba {
    public static void main(String[] args) {
        String nombre = "otro";
        String password = "otro";
        String password_encriptada = BCrypt.hashpw(password, BCrypt.gensalt());
        String consulta = "INSERT INTO usuarios(nombre, password) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConexion()) {
            PreparedStatement stmt = conn.prepareStatement(consulta);
            stmt.setString(1, nombre);
            stmt.setString(2, password_encriptada);
            int filas = stmt.executeUpdate();
            System.out.println(filas > 0);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}
