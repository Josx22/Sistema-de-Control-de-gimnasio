package sv.arrupe.gym.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.*;
import java.util.ArrayList;
import java.util.List;
import sv.arrupe.gym.model.*;

public class GymService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private final String BASE_URL = "http://localhost:5195/api";

    // LOGIN
    public boolean login(String user, String pass) throws Exception {
        String jsonEnvio = "{\"username\":\"" + user + "\", \"password\":\"" + pass + "\", \"dui\":\"none\", \"nombre\":\"none\", \"apellido\":\"none\"}";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/Auth/login")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(jsonEnvio)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            JsonObject data = gson.fromJson(response.body(), JsonObject.class);
            Session.token = data.get("token").getAsString();
            Session.idUsuario = data.get("id_usuario").getAsInt();
            Session.idRol = data.get("id_rol").getAsInt();
            return true;
        }
        return false;
    }

    // BUSCAR
    public UsuarioStatus buscarSocio(String dui) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/Usuarios/buscar/" + dui))
                .header("Authorization", "Bearer " + Session.token)
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return (response.statusCode() == 200) ? gson.fromJson(response.body(), UsuarioStatus.class) : null;
    }

    // REGISTRAR
    public boolean registrarSocio(Usuario u) throws Exception {
        u.setid_rol_fk(3); u.setEstado_usuario("Activo");
        String json = gson.toJson(u);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/Usuarios")).header("Content-Type", "application/json").header("Authorization", "Bearer " + Session.token).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).statusCode() == 201;
    }
    // Traer TODOS los clientes
    public List<Usuario> getTodosLosClientes() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(BASE_URL + "/Usuarios/clientes-total"))
                .header("Authorization", "Bearer " + Session.token)
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), new com.google.gson.reflect.TypeToken<ArrayList<Usuario>>(){}.getType());
    }

    // Traer historial de membresías de UN socio
    public List<SuscripcionDTO> getHistorialSocio(int idUsuario) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/Suscripcion/historial/" + idUsuario))
                .header("Authorization", "Bearer " + Session.token)
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        java.lang.reflect.Type listType = new TypeToken<ArrayList<SuscripcionDTO>>(){}.getType();
        return gson.fromJson(response.body(), listType);
    }

    // ASIGNAR MEMBRESIA - SOLVENCIA INMEDIATA
    public void asignarMembresia(int idSocio, int idPlan) throws Exception {
        SuscripcionDTO dto = new SuscripcionDTO();
        dto.id_usuario_cliente = idSocio;
        dto.id_plan_fk = idPlan;
        dto.id_usuario_empleado = Session.idUsuario;
        String json = gson.toJson(dto);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/Suscripcion/asignar")).header("Content-Type", "application/json").header("Authorization", "Bearer " + Session.token).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // CAMBIAR ESTADO - SUSPENDER/ACTIVAR
    public void cambiarEstadoSocio(int id, String nuevoEstado) throws Exception {
        String json = "\"" + nuevoEstado + "\"";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/Usuarios/" + id + "/estado")).header("Content-Type", "application/json").header("Authorization", "Bearer " + Session.token).method("PATCH", HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() != 200) throw new Exception("Error API: " + res.statusCode());
    }
    public void cancelarMembresiaPresencial(int idSocio) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(BASE_URL + "/Suscripcion/cancelar-manual/" + idSocio))
                .header("Authorization", "Bearer " + Session.token)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) throw new Exception("Error");
    }

    public List<PlanMembresia> getPlanes() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/Planes")).header("Authorization", "Bearer " + Session.token).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), new TypeToken<ArrayList<PlanMembresia>>(){}.getType());
    }
    public List<UsuarioStatus> getEstatusSocios() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/Usuarios/estatus-socios"))
                .header("Authorization", "Bearer " + Session.token)
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        java.lang.reflect.Type listType = new TypeToken<ArrayList<UsuarioStatus>>(){}.getType();
        return gson.fromJson(response.body(), listType);
    }
}