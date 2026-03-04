package sv.arrupe.gym.model;

public class PlanMembresia {
    private int id_plan;
    private String nombre_plan;
    private double precio_simulado;
    private int duracion_dias;
    private double monto_penalizacion;

    public void setMonto_penalizacion(double monto_penalizacion) {
        this.monto_penalizacion = monto_penalizacion;
    }

    public double getMonto_penalizacion() {
        return monto_penalizacion;
    }
    private int id_usuario_admin;

    // Getters y Setters
    public int getId_plan() { return id_plan; }
    public void setId_plan(int id_plan) { this.id_plan = id_plan; }

    public String getNombre_plan() { return nombre_plan; }
    public void setNombre_plan(String nombre_plan) { this.nombre_plan = nombre_plan; }

    public double getPrecio_simulado() { return precio_simulado; }
    public void setPrecio_simulado(double precio_simulado) { this.precio_simulado = precio_simulado; }

    public int getDuracion_dias() { return duracion_dias; }
    public void setDuracion_dias(int duracion_dias) { this.duracion_dias = duracion_dias; }
    
    public int getId_usuario_admin() {
    return id_usuario_admin;
}

public void setId_usuario_admin(int id_usuario_admin) {
    this.id_usuario_admin = id_usuario_admin;
}
    
}