package com.juan.sistema.controllers;

import com.juan.sistema.dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void manejarBotonLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // TODO
    }

    @FXML
    private void manejarLinkRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/juan/sistema/register-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Registro de Usuario");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
