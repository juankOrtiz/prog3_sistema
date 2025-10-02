package com.juan.sistema.dao;

import com.juan.sistema.models.Movimiento;
import com.juan.sistema.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransferenciaDAO {

    /**
     * Ejecuta una transferencia de fondos como una transacción completa:
     * 1. Debita de la cuenta de origen (con chequeo de saldo).
     * 2. Acredita a la cuenta de destino.
     * 3. Registra el movimiento en la tabla 'movimientos'.
     *
     * @return true si la transacción fue exitosa (COMMIT), false si falló (ROLLBACK).
     */
    public boolean realizarTransferencia(int idOrigen, int idDestino, double monto) {

        // Sentencias SQL
        String sqlDebitar = "UPDATE cuentas SET saldo = saldo - ? WHERE id = ? AND saldo >= ?";
        String sqlAcreditar = "UPDATE cuentas SET saldo = saldo + ? WHERE id = ?";
        String sqlRegistrarMov = "INSERT INTO movimientos (cuenta_origen_id, cuenta_destino_id, monto, tipo_movimiento) VALUES (?, ?, ?, 'TRANSFERENCIA')";

        Connection conn = null; // Declaramos aquí para usarlo en el bloque finally

        try {
            conn = DatabaseConnection.getConexion();

            // 1. INICIO DE LA TRANSACCIÓN: Desactivar Auto-Commit
            conn.setAutoCommit(false);

            // =========================================================
            // PASO 1: Debitar de la cuenta de origen (chequeando saldo)
            // =========================================================
            try (PreparedStatement stmtDebitar = conn.prepareStatement(sqlDebitar)) {
                stmtDebitar.setDouble(1, monto);
                stmtDebitar.setInt(2, idOrigen);
                stmtDebitar.setDouble(3, monto); // El saldo debe ser >= monto

                int filasAfectadas = stmtDebitar.executeUpdate();

                if (filasAfectadas != 1) {
                    // Si no afecta una fila, es porque la cuenta no existe O EL SALDO ES INSUFICIENTE.
                    throw new SQLException("Error al debitar. Saldo insuficiente o cuenta de origen inválida.");
                }
            }

            // =========================================================
            // PASO 2: Acreditar a la cuenta de destino
            // =========================================================
            try (PreparedStatement stmtAcreditar = conn.prepareStatement(sqlAcreditar)) {
                stmtAcreditar.setDouble(1, monto);
                stmtAcreditar.setInt(2, idDestino);

                int filasAfectadas = stmtAcreditar.executeUpdate();

                if (filasAfectadas != 1) {
                    // Aunque improbable si la cuenta destino existe, es vital.
                    throw new SQLException("Error al acreditar. Cuenta de destino inválida.");
                }
            }

            // =========================================================
            // PASO 3: Registrar el movimiento
            // =========================================================
            try (PreparedStatement stmtRegistrar = conn.prepareStatement(sqlRegistrarMov)) {
                stmtRegistrar.setInt(1, idOrigen);
                stmtRegistrar.setInt(2, idDestino);
                stmtRegistrar.setDouble(3, monto);

                stmtRegistrar.executeUpdate();
            }

            // =========================================================
            // FINALIZACIÓN EXITOSA: COMMIT
            // =========================================================
            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Transacción fallida. Iniciando ROLLBACK...");
            e.printStackTrace();

            // Si hay un error, deshacer todos los cambios
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("✅ ROLLBACK exitoso.");
                } catch (SQLException exRollback) {
                    System.err.println("⚠️ Error al intentar ROLLBACK: " + exRollback.getMessage());
                }
            }
            return false;

        } finally {
            // Es buena práctica devolver la conexión a su estado inicial
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close(); // Asegurarse de cerrar la conexión
                } catch (SQLException e) {
                    System.err.println("Error al restaurar autoCommit/cerrar conexión: " + e.getMessage());
                }
            }
        }
    }
}
