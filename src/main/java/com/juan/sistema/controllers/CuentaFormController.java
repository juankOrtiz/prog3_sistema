package com.juan.sistema.controllers;

import com.juan.sistema.models.Cuenta;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CuentaFormController {

    @FXML private Label tituloLabel;
    @FXML private TextField idField;
    @FXML private TextField descripcionField;
    @FXML private TextField saldoField;

    private Stage dialogStage;
    private Cuenta cuenta; // La cuenta que se está editando o la nueva cuenta
    private boolean isGuardado = false;
    private boolean esCreacion = false; // Bandera para saber si estamos creando o editando

    /**
     * Inicializa los campos con los datos de la cuenta (usado para la edicion).
     * Si cuenta es null, prepara el formulario para la creacion.
     */
    public void setCuenta(Cuenta cuenta, int usuarioId) {
        this.cuenta = cuenta;

        if (cuenta == null) {
            // Modo CREAR
            this.esCreacion = true;
            this.cuenta = new Cuenta(-1, usuarioId, "", 0.0); // Crear objeto temporal
            tituloLabel.setText("Crear Nueva Cuenta");
            idField.setText("-");
            saldoField.setDisable(false); // Permitir ingresar saldo inicial
        } else {
            // Modo EDITAR
            this.esCreacion = false;
            tituloLabel.setText("Editar Descripción de Cuenta");
            idField.setText(String.valueOf(cuenta.getId()));
            descripcionField.setText(cuenta.getDescripcion());
            saldoField.setText(String.format("%.2f", cuenta.getSaldo()));
            saldoField.setDisable(true); // NO se puede editar el saldo
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isGuardado() {
        return isGuardado;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    @FXML
    private void manejarBotonGuardar() {
        if (esCreacion) {
            // Validacion y asignacion para CREAR
            try {
                double saldoInicial = Double.parseDouble(saldoField.getText().replace(',', '.'));
                if (descripcionField.getText().trim().isEmpty()) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Descripción requerida.");
                    return;
                }

                // Asignar los valores al objeto Cuenta temporal
                this.cuenta.setDescripcion(descripcionField.getText().trim());
                this.cuenta.setSaldo(saldoInicial);

            } catch (NumberFormatException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Saldo debe ser un número válido.");
                return;
            }
        } else {
            // Validacion y asignacion para EDITAR
            if (descripcionField.getText().trim().isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Descripción requerida.");
                return;
            }
            this.cuenta.setDescripcion(descripcionField.getText().trim());
        }

        isGuardado = true;
        dialogStage.close();
    }

    @FXML
    private void manejarBotonCancelar() {
        dialogStage.close();
    }

    private void mostrarAlerta(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Error de Validación");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}