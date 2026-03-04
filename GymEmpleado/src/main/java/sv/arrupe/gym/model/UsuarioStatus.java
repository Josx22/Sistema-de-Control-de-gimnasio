package sv.arrupe.gym.model;
import com.google.gson.annotations.SerializedName;

public class UsuarioStatus {
    public int id_usuario;
    public String nombre;
    public String dui;
    public String estado_usuario;
    public String ultimaMembresia;
    private String nombrePlan;
    private String estadoMembresia;

    public int getId_usuario() { return id_usuario; }
    public String getNombre() { return nombre; }
    public String getDui() { return dui; }
    public String getEstado_usuario() { return estado_usuario; }
    public String getUltimaMembresia() { return ultimaMembresia; }
    public String getNombrePlan() { return nombrePlan; }
    public String getEstadoMembresia() { return estadoMembresia; }

    // SETTERS
    public void setEstado_usuario(String estado_usuario) { this.estado_usuario = estado_usuario; }
    public void setUltimaMembresia(String ultimaMembresia) { this.ultimaMembresia = ultimaMembresia; }
    public void setNombrePlan(String nombrePlan) { this.nombrePlan = nombrePlan; }
    public void setEstadoMembresia(String estadoMembresia) { this.estadoMembresia = estadoMembresia; }

}
