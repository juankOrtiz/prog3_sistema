package com.juan.sistema.dao;

import com.juan.sistema.models.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public boolean registrarUsuario(String nombre, String password) throws SQLException {
        // TODO
        return false;
    }

    public Usuario obtenerUsuario(String nombre) {
        // TODO
        return new Usuario(1, "", "");
    }

    public boolean comprobarLogin(String nombre, String password) {
        // TODO
        return false;
    }
}
