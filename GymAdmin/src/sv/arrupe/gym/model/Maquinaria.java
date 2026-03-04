package sv.arrupe.gym.model;

public class Maquinaria {
    private int id_maquina;
    private String nombre;
    private String estado;
    private String descripcion;
    private int id_usuario_admin;
    private String imagen_url;
    private String imagen_data;

    

   

    // Getters y Setters (clic derecho -> Insert Code -> Getter and Setter)
    public int getId_maquina() { return id_maquina; }
    public String getNombre() { return nombre; }
    public String getEstado() { return estado; }
    public String getDescripcion() { return descripcion; }
    public String getImagen_url() {return imagen_url;}

    public String getImagen_data() {
        return imagen_data;
    }
    
    
    // SETTERS (Añadir estos para que no den error las lineas 302-304 de tu Admin)
    public void setId_maquina(int id_maquina) { this.id_maquina = id_maquina; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getId_usuario_admin() { return id_usuario_admin; }
    public void setId_usuario_admin(int id_usuario_admin) { this.id_usuario_admin = id_usuario_admin; }
    public void setImagen_url(String imagen_url) {this.imagen_url = imagen_url;}

    public void setImagen_data(String imagen_data) {
        this.imagen_data = imagen_data;
    }
    
}
