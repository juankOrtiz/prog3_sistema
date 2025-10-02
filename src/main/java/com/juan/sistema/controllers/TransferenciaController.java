package com.juan.sistema.controllers;

import com.juan.sistema.dao.CuentaDAO;
import com.juan.sistema.dao.TransferenciaDAO;
import com.juan.sistema.dao.UsuarioDAO;
import com.juan.sistema.models.Cuenta;
import com.juan.sistema.models.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class TransferenciaController {

    @FXML private ComboBox<Cuenta> cbOrigen;
    @FXML private ComboBox<Usuario> cbUsuarioDestino;
    @FXML private ComboBox<Cuenta> cbDestino;
    @FXML private TextField montoField;
    @FXML private Label saldoLabel;

    private Stage dialogStage;
    private int usuarioLogueadoId;
    private boolean isTransferenciaExitosa = false;

    private final TransferenciaDAO transferenciaDAO = new TransferenciaDAO();
    private final CuentaDAO cuentaDAO = new CuentaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void setDialogStage(Stage dialogStage, int usuarioId, List<Cuenta> cuentasOrigen) {
        this.dialogStage = dialogStage;
        this.usuarioLogueadoId = usuarioId;

        // 1. Cargar Cuentas de Origen del Usuario logueado
        cbOrigen.setItems(FXCollections.observableArrayList(cuentasOrigen));
        cbOrigen.setConverter(new CuentaStringConverter()); // Para mostrar la descripción
        cbOrigen.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                saldoLabel.setText(String.format("Saldo: $%.2f", newVal.getSaldo()));
            } else {
                saldoLabel.setText("Saldo: $0.00");
            }
        });

        // 2. Cargar Usuarios Destino
        try {
            List<Usuario> otrosUsuarios = usuarioDAO.obtenerOtrosUsuarios(usuarioLogueadoId);
            cbUsuarioDestino.setItems(FXCollections.observableArrayList(otrosUsuarios));
            cbUsuarioDestino.setConverter(new UsuarioStringConverter()); // Para mostrar el nombre

            // 3. Listener para cargar cuentas de destino al seleccionar un usuario
            cbUsuarioDestino.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                cbDestino.getItems().clear();
                if (newVal != null) {
                    cargarCuentasDestino(newVal.getId());
                }
            });
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Carga", "No se pudieron cargar usuarios destino.");
            e.printStackTrace();
        }
    }

    private void cargarCuentasDestino(int usuarioId) {
        try {
            List<Cuenta> cuentasDestino = cuentaDAO.obtenerCuentasPorUsuario(usuarioId);
            cbDestino.setItems(FXCollections.observableArrayList(cuentasDestino));
            cbDestino.setConverter(new CuentaStringConverter());
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Carga", "No se pudieron cargar las cuentas del usuario destino.");
            e.printStackTrace();
        }
    }

    @FXML
    private void manejarBotonTransferir() {
        Cuenta origen = cbOrigen.getSelectionModel().getSelectedItem();
        Cuenta destino = cbDestino.getSelectionModel().getSelectedItem();
        double monto;

        // 1. Validaciones
        if (origen == null || destino == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Error", "Selecciona cuentas de origen y destino.");
            return;
        }

        try {
            monto = Double.parseDouble(montoField.getText().replace(',', '.'));
            if (monto <= 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Error", "El monto debe ser positivo.");
                return;
            }
            if (monto > origen.getSaldo()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Error", "Saldo insuficiente en la cuenta de origen.");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Monto no válido.");
            return;
        }

        // 2. Ejecutar la Transacción
        boolean exito = transferenciaDAO.realizarTransferencia(
                origen.getId(),
                destino.getId(),
                monto
        );

        if (exito) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Transferencia Exitosa!", String.format("Se transfirió $%.2f.", monto));
            isTransferenciaExitosa = true;
            dialogStage.close();
        } else {
            // El DAO ya maneja el rollback y lanza la excepción si hay un problema (como saldo insuficiente)
            mostrarAlerta(Alert.AlertType.ERROR, "Transferencia Fallida", "No se pudo completar la transacción. Verifique el saldo y las cuentas.");
        }
    }

    @FXML
    private void manejarBotonCancelar() {
        dialogStage.close();
    }

    public boolean fueExitosa() {
        return isTransferenciaExitosa;
    }

    private void mostrarAlerta(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Clases auxiliares para mostrar la descripción/nombre en el ComboBox
    private static class CuentaStringConverter extends javafx.util.StringConverter<Cuenta> {
        @Override
        public String toString(Cuenta cuenta) {
            return cuenta != null ? cuenta.getDescripcion() + " (ID: " + cuenta.getId() + ")" : "";
        }
        @Override
        public Cuenta fromString(String string) { return null; }
    }

    private static class UsuarioStringConverter extends javafx.util.StringConverter<Usuario> {
        @Override
        public String toString(Usuario usuario) {
            return usuario != null ? usuario.getNombre() : "";
        }
        @Override
        public Usuario fromString(String string) { return null; }
    }
}