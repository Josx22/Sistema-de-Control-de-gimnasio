package sv.arrupe.gym.model;
import com.google.gson.annotations.SerializedName;

public class Usuario {
    public int id_usuario;
    public String nombre;
    public String apellido;
    public String dui;
    public String username;
    public String password;
    public int id_rol_fk = 3;
    public String estado_usuario = "Activo";

    public void setid_rol_fk(int id_rol_fk) {
        this.id_rol_fk = id_rol_fk;
    }

    public void setEstado_usuario(String estado_usuario) {
        this.estado_usuario = estado_usuario;
    }
}