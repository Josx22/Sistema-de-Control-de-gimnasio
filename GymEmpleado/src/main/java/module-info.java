module sv.arrupe.gym.gymempleado {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http; // Necesario para HttpClient
    requires com.google.gson;
    // Permite que JavaFX vea tus controladores
    opens sv.arrupe.gym.gymempleado to javafx.fxml;

    // PERMITE QUE GSON ENTRE A TUS MODELOS (Fundamental para no tener NULLs)
    opens sv.arrupe.gym.model to com.google.gson, javafx.base;

    exports sv.arrupe.gym.gymempleado;
}