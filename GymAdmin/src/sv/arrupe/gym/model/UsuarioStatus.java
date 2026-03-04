package sv.arrupe.gym.model;

public class UsuarioStatus {
    public int id_usuario;
    public String nombre;
    public String dui;
    public String ultimaMembresia; 
    public UsuarioStatus() {}
    
    public int getId_usuario() {
        return id_usuario;
    }
    
    // También asegúrate de tener el del nombre para evitar futuros errores
    public String getNombre() {
        return nombre;
    }
}