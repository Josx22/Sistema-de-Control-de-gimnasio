package sv.arrupe.gym.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import sv.arrupe.gym.model.*;

public class GymService {

    
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private final String BASE_URL = "http://localhost:5195/api";

    // Función auxiliar para no repetir el Token en cada método
    private HttpRequest.Builder peticionBase(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Authorization", "Bearer " + Session.token);
    }

    // --- 1. MÓDULO DE AUTENTICACIÓN ---
    public boolean login(String user, String pass) throws Exception {
        // Formato simple para no dar error 400 en la API
        String json = "{\"username\":\"" + user + "\", \"password\":\"" + pass + "\", \"dui\":\"none\", \"nombre\":\"none\", \"apellido\":\"none\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/Auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonObject data = gson.fromJson(response.body(), JsonObject.class);
            Session.token = data.get("token").getAsString();
            Session.idUsuario = data.get("id_usuario").getAsInt();
            Session.idRol = data.get("id_rol").getAsInt();
            Session.nombre = data.get("nombre").getAsString();
            return true;
        }
        return false;
    }

    // --- 2. GESTIÓN DE MAQUINARIA (ESTILO GUÍA 10 - URL SIMPLE) ---
    public List<Maquinaria> getMaquinaria() throws Exception {
        HttpRequest request = peticionBase("/Maquinaria").GET().build();
        HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        java.lang.reflect.Type listType = new TypeToken<ArrayList<Maquinaria>>() {}.getType();
        return gson.fromJson(res.body(), listType);
    }

    public void crearMaquinaria(Maquinaria m) throws Exception {
        m.setId_usuario_admin(Session.idUsuario);
        String json = gson.toJson(m); // La imagen va como String URL en el JSON

        HttpRequest request = peticionBase("/Maquinaria")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();
        
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void actualizarMaquina(int id, Maquinaria m) throws Exception {
        m.setId_usuario_admin(Session.idUsuario);
        String json = gson.toJson(m);
        
        HttpRequest request = peticionBase("/Maquinaria/" + id)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json)).build();
        
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void eliminarMaquinaria(int id) throws Exception {
        HttpRequest request = peticionBase("/Maquinaria/" + id).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // --- 3. GESTIÓN DE MEMBRESÍAS ---
    public List<PlanMembresia> getPlanes() throws Exception {
        HttpRequest request = peticionBase("/Planes?verInactivos=true").GET().build();
        HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        java.lang.reflect.Type listType = new TypeToken<ArrayList<PlanMembresia>>() {}.getType();
        return gson.fromJson(res.body(), listType);
    }

    public void crearPlanMembresia(PlanMembresia plan) throws Exception {
        plan.setId_usuario_admin(Session.idUsuario);
        String json = gson.toJson(plan);

        HttpRequest request = peticionBase("/Planes")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();
        
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void editarPlan(int id, PlanMembresia p) throws Exception {
        p.setId_usuario_admin(Session.idUsuario);
        String json = gson.toJson(p);

        HttpRequest request = peticionBase("/Planes/" + id)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json)).build();
        
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void activarPlan(int id, PlanMembresia p) throws Exception {
        p.setNombre_plan(p.getNombre_plan().replace("[INACTIVO] ", ""));
        editarPlan(id, p);
    }

    public void desactivarPlan(int id, PlanMembresia p) throws Exception {
        p.setNombre_plan("[INACTIVO] " + p.getNombre_plan());
        editarPlan(id, p);
    }

    // --- 4. GESTIÓN DE PERSONAL Y SOCIOS ---
    public List<Usuario> getPersonal() throws Exception {
        HttpRequest request = peticionBase("/Usuarios/rol/2").GET().build();
        HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        java.lang.reflect.Type listType = new TypeToken<ArrayList<Usuario>>() {}.getType();
        return gson.fromJson(res.body(), listType);
    }

    public void registrarEmpleado(Usuario u) throws Exception {
        u.setId_rol_fk(2); // Rol empleado
        String json = gson.toJson(u);
        HttpRequest request = peticionBase("/Usuarios")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();
        
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public List<UsuarioStatus> getEstatusSocios() throws Exception {
        HttpRequest request = peticionBase("/Usuarios/estatus-socios").GET().build();
        HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        java.lang.reflect.Type listType = new TypeToken<ArrayList<UsuarioStatus>>() {}.getType();
        return gson.fromJson(res.body(), listType);
    }

    public List<Usuario> getTodosLosClientes() throws Exception {
        HttpRequest request = peticionBase("/Usuarios/clientes-total").GET().build();
        HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        java.lang.reflect.Type listType = new TypeToken<ArrayList<Usuario>>() {}.getType();
        return gson.fromJson(res.body(), listType);
    }

    public UsuarioStatus buscarSocio(String dui) throws Exception {
        HttpRequest request = peticionBase("/Usuarios/buscar/" + dui).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return (response.statusCode() == 200) ? gson.fromJson(response.body(), UsuarioStatus.class) : null;
    }

    // --- 5. FINANZAS Y GASTOS (CON RUMBO LÓGICO DEL DÍA 4) ---
    public JsonObject getReporteContable() throws Exception {
        HttpRequest request = peticionBase("/Pagos/reporte-contable").GET().build();
        return gson.fromJson(client.send(request, HttpResponse.BodyHandlers.ofString()).body(), JsonObject.class);
    }

    public double getGananciasTotales() throws Exception {
        HttpRequest request = peticionBase("/Pagos/total-mensual").GET().build();
        JsonObject obj = gson.fromJson(client.send(request, HttpResponse.BodyHandlers.ofString()).body(), JsonObject.class);
        return obj.get("recaudacion").getAsDouble();
    }

    public void aplicarGastosFijos() throws Exception {
        HttpRequest request = peticionBase("/Pagos/registrar-gastos-fijos").POST(HttpRequest.BodyPublishers.noBody()).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public List<SuscripcionDTO> getHistorialSocio(int idUsuario) throws Exception {
        HttpRequest request = peticionBase("/Suscripcion/historial/" + idUsuario).GET().build();
        HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        java.lang.reflect.Type listType = new TypeToken<ArrayList<SuscripcionDTO>>() {}.getType();
        return (res.statusCode() == 200) ? gson.fromJson(res.body(), listType) : new ArrayList<>();
    }

    // --- 6. OPERACIONES DE EMPLEADO (RECOPILACIÓN FINAL) ---
    public void asignarMembresia(int idSocio, int idPlan) throws Exception {
        SuscripcionDTO dto = new SuscripcionDTO();
        dto.id_usuario_cliente = idSocio;
        dto.id_plan_fk = idPlan;
        dto.id_usuario_empleado = Session.idUsuario;

        HttpRequest request = peticionBase("/Suscripcion/asignar")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(dto))).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void cambiarEstadoSocio(int id, String estado) throws Exception {
        String json = "\"" + estado + "\"";
        HttpRequest request = peticionBase("/Usuarios/" + id + "/estado")
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json)).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void cancelarMembresiaPresencial(int idSocio) throws Exception {
        HttpRequest request = peticionBase("/Suscripcion/cancelar-manual/" + idSocio)
                .POST(HttpRequest.BodyPublishers.noBody()).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
    
    public void desactivarUsuario(int id) throws Exception {
    String json = "\"Inactivo\"";
    HttpRequest request = peticionBase("/Usuarios/" + id + "/estado")
            .header("Content-Type", "application/json")
            .method("PATCH", HttpRequest.BodyPublishers.ofString(json)).build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() != 200) {
        throw new Exception("No se pudo desactivar. Error: " + response.statusCode());
    }
}
}
    
