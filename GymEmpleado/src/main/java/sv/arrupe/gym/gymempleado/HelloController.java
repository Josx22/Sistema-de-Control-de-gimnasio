package sv.arrupe.gym.gymempleado;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import sv.arrupe.gym.model.*;
import sv.arrupe.gym.service.GymService;
import java.util.List;

public class HelloController {

    private final GymService service = new GymService();

    private int idSocioActual = -1;

    @FXML private TextField txtBuscarDui, txtNom, txtApe, txtDuiReg, txtUser, txtPass;
    @FXML private Label lblNombre, lblVence, lblEstado;
    @FXML private ComboBox<PlanMembresia> cbPlanes;
    @FXML private VBox sectionAuditoria;
    @FXML private Button btnSuspender;
    @FXML private TableView<UsuarioStatus> tblSociosGlobal;
    @FXML private TableColumn<UsuarioStatus, String> colDui, colNombre, colStatus;

    @FXML
    public void initialize() {
        colDui.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dui"));
        colNombre.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nombre"));

        // LÓGICA DE LA TABLA
        colStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setStyle("");
                } else {
                    UsuarioStatus row = getTableRow().getItem();
                    String estadoUsu = row.getEstado_usuario(); // Activo / Suspendido
                    String estadoMem = (row.getEstadoMembresia() != null) ? row.getEstadoMembresia() : "S/P";
                    String planNom = (row.getNombrePlan() != null) ? row.getNombrePlan() : "Ninguno";
                    String fecha = (row.getUltimaMembresia() != null) ? row.getUltimaMembresia().split("T")[0] : "---";

                    setText(estadoUsu + " | Plan: " + planNom + " (" + fecha + ")");

                    if ("Suspendido".equals(estadoUsu)) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else if ("Cancelada".equals(estadoMem) || "Ninguno".equals(planNom)) {
                        setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;");
                    } else {
                        // Todo solvente
                        setStyle("-fx-text-fill: darkgreen; -fx-font-weight: normal;");
                    }
                }
            }
        });

        cargarPlanes();
        cargarTablaGlobal();
    }

    //BUSCADOR POR DUI
    @FXML
    private void onActionBuscar() {
        String dui = txtBuscarDui.getText().trim();
        if (dui.isEmpty()) {
            mostrarAlerta("Campo vacío", "Por favor ingresa un DUI para verificar.");
            return;
        }

        try {
            UsuarioStatus s = service.buscarSocio(dui);

            if (s != null) {
                this.idSocioActual = s.getId_usuario();

                lblNombre.setText(s.getNombre());

                String estadoUsuario = s.getEstado_usuario(); // Activo / Suspendido
                String estadoPlan = s.getEstadoMembresia();    // Vigente / Cancelada
                String planNombre = (s.getNombrePlan() != null) ? s.getNombrePlan() : "NINGUNO";

                String fechaVence = "---";
                if (s.getUltimaMembresia() != null && !s.getUltimaMembresia().contains("0001")) {
                    fechaVence = s.getUltimaMembresia().split("T")[0];
                }

                // --- LÓGICA VISUAL DE COLORES Y ESTATUS ---

                if ("Suspendido".equals(estadoUsuario)) {
                    // CASO 1: USUARIO BANEADO/SUSPENDIDO (Panel Rojo)
                    sectionAuditoria.setStyle("-fx-background-color: #ffcccc; -fx-border-color: #e74c3c; -fx-border-width: 2;");
                    lblEstado.setText("DENEGADO / CUENTA BANEADA");
                    lblEstado.setTextFill(Color.RED);

                    lblVence.setText("ESTADO DE MEMBRESÍA BLOQUEADO");
                    lblVence.setTextFill(Color.RED);

                    btnSuspender.setText("ACTIVAR ACCESO");
                    btnSuspender.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;"); // Se vuelve verde para habilitar
                }
                else if (planNombre.equals("NINGUNO") || "Cancelada".equals(estadoPlan)) {
                    // USUARIO ACTIVO PERO SIN PLAN
                    sectionAuditoria.setStyle("-fx-background-color: #fff3cd; -fx-border-color: #f39c12; -fx-border-width: 2;");
                    lblEstado.setText("ACTIVO: REQUIERE PAGO / PLAN");
                    lblEstado.setTextFill(Color.ORANGE);

                    String detallePlan = planNombre.equals("NINGUNO") ? "NUNCA HA TENIDO PLAN" : "PLAN " + planNombre + " [CANCELADO]";
                    lblVence.setText(detallePlan);
                    lblVence.setTextFill(Color.DARKRED);

                    btnSuspender.setText("SUSPENDER ACCESO MANUAL");
                    btnSuspender.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                }
                else {
                    // TODO CORRECTO / SOLVENTE
                    sectionAuditoria.setStyle("-fx-background-color: #c8e6c9; -fx-border-color: #2ecc71; -fx-border-width: 2;");
                    lblEstado.setText("SOCIO SOLVENTE / PUEDE PASAR");
                    lblEstado.setTextFill(Color.DARKGREEN);

                    lblVence.setText("MEMBRESÍA: " + planNombre + " | Vence: " + fechaVence);
                    lblVence.setTextFill(Color.BLACK);

                    btnSuspender.setText("SUSPENDER ACCESO MANUAL");
                    btnSuspender.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                }

            } else {
                mostrarAlerta("Sin registros", "El socio con DUI " + dui + " no existe en el sistema.");
                limpiarDatosBusqueda();
            }

        } catch (Exception e) {
            mostrarError("Falla de comunicación: Asegúrese de que la API en Visual Studio esté encendida.");
            e.printStackTrace();
        }
    }

    // ASIGNAR MEMBRESIA A SOCIO YA REGISTRADO
    @FXML
    private void onActionAsignarMembresiaSimple() {
        if (idSocioActual == -1) {
            mostrarError("Primero busca a un cliente en el panel izquierdo.");
            return;
        }
        PlanMembresia plan = cbPlanes.getValue();
        if (plan == null) {
            mostrarError("Selecciona una membresía de la lista.");
            return;
        }

        try {
            service.asignarMembresia(idSocioActual, plan.getId_plan());
            mostrarAlerta("Operación Exitosa", "Membresía activada correctamente.");
            onActionBuscar(); // Refresca panel visual
            cargarTablaGlobal(); // Refresca tabla
        } catch (Exception e) {
            mostrarError("No se pudo asignar la membresía.");
        }
    }

    // CANCELAR PLAN ACTUAL
    @FXML
    private void onActionCancelarPlan() {
        if (idSocioActual == -1) {
            mostrarError("Selecciona un socio en el buscador primero.");
            return;
        }

        if (confirmarAccion("¿Seguro que desea CANCELAR la membresía actual?\n(El usuario seguirá activo pero no tendrá días de acceso)")) {
            try {
                service.cancelarMembresiaPresencial(idSocioActual);
                mostrarAlerta("Éxito", "La membresía ha sido finalizada manualmente.");
                onActionBuscar();
                cargarTablaGlobal();
            } catch (Exception e) {
                mostrarError("La API respondió: " + e.getMessage());
            }
        }
    }

    //SUSPENDER/ACTIVAR CUENTA
    @FXML
    private void onActionSuspenderManual() {
        if (idSocioActual == -1) {
            mostrarError("Busca a un socio para cambiar su estado.");
            return;
        }

        try {
            String nuevoEstado = btnSuspender.getText().contains("ACTIVAR") ? "Activo" : "Suspendido";
            service.cambiarEstadoSocio(idSocioActual, nuevoEstado);

            mostrarAlerta("Estado Cambiado", "La cuenta ahora está: " + nuevoEstado);
            onActionBuscar();
            cargarTablaGlobal();
        } catch (Exception e) {
            mostrarError("Error al procesar el cambio de estado.");
        }
    }

    //REGISTRO + SOLVENCIA INMEDIATA
    @FXML
    private void onActionRegistrar() {
        if (txtNom.getText().isEmpty() || cbPlanes.getValue() == null) {
            mostrarError("Datos incompletos. Llena el nombre y elige una membresía.");
            return;
        }

        Usuario u = new Usuario();
        u.nombre = txtNom.getText();
        u.apellido = txtApe.getText();
        u.dui = txtDuiReg.getText();
        u.username = txtUser.getText();
        u.password = txtPass.getText();

        try {
            if (service.registrarSocio(u)) {
                UsuarioStatus creado = service.buscarSocio(u.dui);
                service.asignarMembresia(creado.getId_usuario(), cbPlanes.getValue().getId_plan());

                mostrarAlerta("Éxito", "Socio creado y Solvente de inmediato.");
                txtBuscarDui.setText(u.dui);
                onActionBuscar();
                cargarTablaGlobal();
                limpiarCampos();
            }
        } catch (Exception e) {
            mostrarError("DUI o Usuario ya existe en la base de datos.");
        }
    }

    // --- MÉTODOS DE APOYO Y CARGA ---

    private void cargarPlanes() {
        try {
            cbPlanes.setItems(FXCollections.observableArrayList(service.getPlanes()));
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void cargarTablaGlobal() {
        try {
            tblSociosGlobal.setItems(FXCollections.observableArrayList(service.getEstatusSocios()));
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void onTableClicked() {
        UsuarioStatus s = tblSociosGlobal.getSelectionModel().getSelectedItem();
        if (s != null) {
            txtBuscarDui.setText(s.getDui());
            onActionBuscar();
        }
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }

    private void mostrarError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error"); a.setHeaderText("Falla técnica"); a.setContentText(msg); a.showAndWait();
    }

    private boolean confirmarAccion(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Acción"); alert.setHeaderText(null); alert.setContentText(msg);
        return alert.showAndWait().get() == ButtonType.OK;
    }

    private void limpiarCampos() {
        txtNom.clear(); txtApe.clear(); txtDuiReg.clear(); txtUser.clear(); txtPass.clear();
    }

    private void limpiarDatosBusqueda() {
        lblNombre.setText("..."); lblVence.setText("..."); lblEstado.setText("...");
        sectionAuditoria.setStyle("-fx-border-color: #bdc3c7;");
        idSocioActual = -1;
    }
}