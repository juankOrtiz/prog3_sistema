package com.juan.sistema.controllers;

import com.juan.sistema.dao.CuentaDAO;
import com.juan.sistema.models.Cuenta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class CuentaController {

    private int usuarioLogueadoId;

    @FXML private TableView<Cuenta> tablaCuentas;
    @FXML private TableColumn<Cuenta, Integer> colId;
    @FXML private TableColumn<Cuenta, String> colDescripcion;
    @FXML private TableColumn<Cuenta, Double> colSaldo;

    private final CuentaDAO cuentaDAO = new CuentaDAO();
    private ObservableList<Cuenta> listaCuentas;

    @FXML
    public void initialize() {
        // Inicializar la tabla y el binding de datos
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
    }

    public void setUsuarioLogueadoId(int id) {
        this.usuarioLogueadoId = id;
        // Iniciar la carga de datos una vez que se conoce el ID
        cargarCuentas();
    }

    private void cargarCuentas() {
        // Solo cargar si el ID ya fue establecido
        if (usuarioLogueadoId > 0) {
            try {
                // Usa la variable de instancia
                listaCuentas = FXCollections.observableArrayList(
                        cuentaDAO.obtenerCuentasPorUsuario(usuarioLogueadoId)
                );
                tablaCuentas.setItems(listaCuentas);
            } catch (SQLException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de BD", "No se pudieron cargar las cuentas.");
                e.printStackTrace();
            }
        }
    }

    private Cuenta abrirVentanaModal(Cuenta cuentaAEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/juan/sistema/cuenta-form-view.fxml"));
            Parent root = loader.load();

            // Obtenemos el controlador de la ventana modal
            CuentaFormController modalController = loader.getController();

            // Creamos el Stage de la ventana modal
            Stage modalStage = new Stage();
            modalStage.setTitle(cuentaAEditar == null ? "Crear Cuenta" : "Editar Cuenta");
            modalStage.initModality(Modality.WINDOW_MODAL);

            // Obtenemos la Stage actual (padre) y la asignamos como dueña
            Stage parentStage = (Stage) tablaCuentas.getScene().getWindow();
            modalStage.initOwner(parentStage);

            Scene scene = new Scene(root);
            modalStage.setScene(scene);

            // Configuramos el controlador de la modal
            modalController.setDialogStage(modalStage);
            modalController.setCuenta(cuentaAEditar, this.usuarioLogueadoId);

            // Muestra la ventana y espera hasta que se cierre (MODAL)
            modalStage.showAndWait();

            // Verificamos si el usuario hizo clic en "Guardar"
            if (modalController.isGuardado()) {
                return modalController.getCuenta();
            }
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Interfaz", "No se pudo cargar la ventana modal.");
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    private void manejarBotonCrear() {
        // Llama al auxiliar, pasando null para indicar que es CREACIÓN
        Cuenta nuevaCuenta = abrirVentanaModal(null);

        if (nuevaCuenta != null) {
            try {
                // El objeto 'nuevaCuenta' ya tiene la descripción, saldo y usuarioId
                int newId = cuentaDAO.crearCuenta(nuevaCuenta);
                if (newId != -1) {
                    listaCuentas.add(nuevaCuenta);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cuenta creada con ID: " + newId + "!");
                }
            } catch (SQLException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Creación", "Fallo al crear la cuenta en la base de datos.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void manejarBotonEditar() {
        Cuenta seleccionada = tablaCuentas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Selecciona una cuenta para editar.");
            return;
        }

        // Llama al auxiliar, pasando la cuenta seleccionada para EDITAR
        Cuenta cuentaEditada = abrirVentanaModal(seleccionada);

        if (cuentaEditada != null) {
            // El objeto 'seleccionada' y 'cuentaEditada' son el mismo, pero el DAO debe confirmarlo
            try {
                if (cuentaDAO.actualizarDescripcion(cuentaEditada)) {
                    tablaCuentas.refresh();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Descripción actualizada!");
                }
            } catch (SQLException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Edición", "Fallo al actualizar la descripción.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void manejarBotonEliminar() {
        Cuenta seleccionada = tablaCuentas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Selecciona una cuenta para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar Eliminación");
        confirm.setHeaderText("Estás a punto de eliminar la cuenta: " + seleccionada.getDescripcion());
        confirm.setContentText("¿Estás seguro de que quieres eliminar la cuenta ID " + seleccionada.getId() + "? Esta acción es irreversible.");

        Optional<ButtonType> resultado = confirm.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                if (cuentaDAO.eliminarCuenta(seleccionada.getId())) {
                    // Eliminar de la lista observable para actualizar la UI
                    listaCuentas.remove(seleccionada);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cuenta eliminada correctamente.");
                }
            } catch (SQLException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Eliminación", "Fallo al eliminar la cuenta.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void manejarBotonTransferencia() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/juan/sistema/transferencia-view.fxml"));
            Parent root = loader.load();

            TransferenciaController modalController = loader.getController();

            Stage modalStage = new Stage();
            modalStage.setTitle("Realizar Transferencia");
            modalStage.initModality(Modality.WINDOW_MODAL);

            Stage parentStage = (Stage) tablaCuentas.getScene().getWindow();
            modalStage.initOwner(parentStage);

            Scene scene = new Scene(root);
            modalStage.setScene(scene);

            // Pasamos el ID del usuario logueado y la lista de cuentas (origen)
            modalController.setDialogStage(modalStage, this.usuarioLogueadoId, listaCuentas);

            modalStage.showAndWait();

            // Si la transferencia fue exitosa, recargamos la lista para actualizar saldos
            if (modalController.fueExitosa()) {
                cargarCuentas(); // Recarga los datos de la BD para reflejar los nuevos saldos
            }

        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Interfaz", "No se pudo cargar la ventana de transferencia.");
            e.printStackTrace();
        }
    }

    // Método auxiliar para crear el diálogo de nueva cuenta
    private Dialog<Cuenta> crearDialogoCuenta(String title) {
        Dialog<Cuenta> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(null);

        // Configurar los botones
        ButtonType botonAceptar = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(botonAceptar, ButtonType.CANCEL);

        // Crear grid para los campos
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField descripcionField = new TextField();
        descripcionField.setPromptText("Ej: Ahorros Viaje");
        TextField saldoField = new TextField("0.00"); // Saldo inicial por defecto

        grid.add(new Label("Descripción:"), 0, 0);
        grid.add(descripcionField, 1, 0);
        grid.add(new Label("Saldo Inicial:"), 0, 1);
        grid.add(saldoField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convertir el resultado a un objeto Cuenta
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == botonAceptar) {
                try {
                    String desc = descripcionField.getText();
                    double saldo = Double.parseDouble(saldoField.getText());
                    // Asumimos el usuario logueado (ID_USUARIO_LOGUEADO)
                    return new Cuenta(this.usuarioLogueadoId, desc, saldo);
                } catch (NumberFormatException e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Entrada Inválida", "El saldo debe ser un número válido.");
                    return null;
                }
            }
            return null;
        });

        return dialog;
    }

    // Método auxiliar para mostrar alertas
    private void mostrarAlerta(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
