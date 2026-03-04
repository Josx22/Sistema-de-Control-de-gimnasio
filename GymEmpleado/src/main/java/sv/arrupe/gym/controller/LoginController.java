package sv.arrupe.gym.gymempleado;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sv.arrupe.gym.service.GymService;
import sv.arrupe.gym.model.Session;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUser;
    @FXML private PasswordField txtPass;
    @FXML private Label lblError;

    private final GymService service = new GymService();

    @FXML
    protected void onLoginButtonClick() {
        String user = txtUser.getText();
        String pass = txtPass.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            lblError.setText("Por favor, llena todos los campos.");
            return;
        }

        try {
            if (service.login(user, pass)) {
                // Verificar que sea Empleado (Rol 2) o Admin (Rol 1)
                if (Session.idRol == 1 || Session.idRol == 2) {
                    cambiarEscena("hello-view.fxml"); // Abre el dashboard del empleado
                } else {
                    lblError.setText("No tienes permisos para usar esta App.");
                }
            } else {
                lblError.setText("Credenciales incorrectas.");
            }
        } catch (Exception e) {
            // ESTO TE MOSTRARÁ EL ERROR REAL (Diferente a offline)
            lblError.setText("Detalle Técnico: " + e.toString());
            e.printStackTrace(); // Revisa la consola (Output) de IntelliJ para ver el mensaje completo
        }
    }

    private void cambiarEscena(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) txtUser.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Recepción del Gimnasio");
    }
}