package com.juan.sistema.controllers;

import com.juan.sistema.dao.UsuarioDAO;
import com.juan.sistema.models.Usuario;
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

        // 1. Llama al método modificado que devuelve el Usuario
        Usuario usuarioLogueado = usuarioDAO.comprobarLogin(username, password);

        if (usuarioLogueado != null) {
            // Login exitoso
            mostrarAlerta("Login exitoso", "Bienvenido, " + usuarioLogueado.getNombre() + "!");

            // 2. Muestra la ventana principal
            cargarVentanaPrincipal(usuarioLogueado.getId());

        } else {
            // Login fallido
            mostrarAlerta("Error de login", "Usuario o contrasenia incorrectos");
        }
    }

    /**
     * Carga la ventana principal (main-view.fxml) y cierra la ventana de login.
     * @param usuarioId El ID del usuario que inició sesión.
     */
    private void cargarVentanaPrincipal(int usuarioId) {
        try {
            // Cargar el FXML de la vista principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/juan/sistema/main-view.fxml"));
            Parent root = loader.load();

            // 3. Obtener el controlador de la vista principal
            CuentaController cuentaController = loader.getController();

            // 4. Establecer el ID del usuario en el controlador de la cuenta
            cuentaController.setUsuarioLogueadoId(usuarioId);

            // 5. Mostrar la nueva ventana
            Stage mainStage = new Stage();
            mainStage.setTitle("Página Principal - Cuentas");
            mainStage.setScene(new Scene(root));
            mainStage.show();

            // 6. Cerrar la ventana actual (login)
            Stage loginStage = (Stage) usernameField.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de carga", "No se pudo cargar la vista principal.");
        }
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
