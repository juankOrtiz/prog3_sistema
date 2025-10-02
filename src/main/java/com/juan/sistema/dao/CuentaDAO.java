package com.juan.sistema.dao;

import com.juan.sistema.models.Cuenta;
import com.juan.sistema.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaDAO {

    public int crearCuenta(Cuenta cuenta) throws SQLException {
        String sql = "INSERT INTO cuentas (usuario_id, descripcion, saldo) VALUES (?, ?, ?)";
        int idGenerado = -1;

        try (Connection conn = DatabaseConnection.getConexion();
             // Solicitamos las claves generadas
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, cuenta.getUsuarioId());
            stmt.setString(2, cuenta.getDescripcion());
            stmt.setDouble(3, cuenta.getSaldo());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                // Obtener Generated Keys (el ID)
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                        // Actualizamos el modelo con el ID
                        cuenta.setId(idGenerado);
                    }
                }
            }
        }
        return idGenerado;
    }

    public List<Cuenta> obtenerCuentasPorUsuario(int usuarioId) throws SQLException {
        List<Cuenta> cuentas = new ArrayList<>();
        String sql = "SELECT id, descripcion, saldo FROM cuentas WHERE usuario_id = ?";

        try (Connection conn = DatabaseConnection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String descripcion = rs.getString("descripcion");
                    double saldo = rs.getDouble("saldo");

                    cuentas.add(new Cuenta(id, usuarioId, descripcion, saldo));
                }
            }
        }
        return cuentas;
    }

    public boolean actualizarDescripcion(Cuenta cuenta) throws SQLException {
        String sql = "UPDATE cuentas SET descripcion = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cuenta.getDescripcion());
            stmt.setInt(2, cuenta.getId());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    public boolean eliminarCuenta(int cuentaId) throws SQLException {
        String sql = "DELETE FROM cuentas WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cuentaId);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }
}