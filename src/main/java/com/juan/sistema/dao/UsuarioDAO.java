package com.juan.sistema.dao;

import com.juan.sistema.models.Usuario;
import com.juan.sistema.util.DatabaseConnection;
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
        // Definir consulta
        String consulta = "SELECT id, password FROM usuarios WHERE nombre = ? LIMIT 1";
        try(Connection conn = DatabaseConnection.getConexion()) {
            // Preparar la consulta definida
            PreparedStatement stmt = conn.prepareStatement(consulta);
            // Reemplazando el primer comodin por la variable nombre que recibe el metodo
            stmt.setString(1, nombre);
            // Ejecutas la consulta y guardas el resultado en la variable rs
            ResultSet rs = stmt.executeQuery();
            // Si hay un siguiente resultado (hay un usuario)..
            if(rs.next()) {
                // Leemos el id y lo asignamos a una variable
                int id = rs.getInt("id");
                // Leemos la contrasenia y la asignamos a una variable
                String passwordEncriptada = rs.getString("password");
                // Creamos un nuevo usuario (modelo) con el id de la BD, el nombre y la contrasenia encriptada de la BD
                // Y lo devolvemos para que lo use comprobarLogin
                return new Usuario(id, nombre, passwordEncriptada);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        // Si no se encontro el usuario se devuelve null
        return null;
    }

    public boolean comprobarLogin(String nombre, String password) {
        // Obtener el usuario de la BD por el nombre buscado
        Usuario usuario = obtenerUsuario(nombre);
        // Si el usuario existe en la BD..
        if(usuario != null) {
            // Comparamos la contrasenia ingresada con la contrasenia encriptada en la BD
            return BCrypt.checkpw(password, usuario.getPassword());
        }
        // Por defecto, devolvemos false si no existe el usuario
        return false;
    }
}
