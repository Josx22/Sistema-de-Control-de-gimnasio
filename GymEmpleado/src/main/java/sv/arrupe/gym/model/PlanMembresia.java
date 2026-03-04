package sv.arrupe.gym.model;

public class PlanMembresia {
    private int id_plan;
    private String nombre_plan;

    private double precio_simulado;

    private int duracion_dias;

    // --- GETTERS Y SETTERS ---
    public int getId_plan() { return id_plan; }
    public void setId_plan(int id_plan) { this.id_plan = id_plan; }

    public String getNombre_plan() { return nombre_plan; }
    public void setNombre_plan(String nombre_plan) { this.nombre_plan = nombre_plan; }

    public double getPrecio_simulado() { return precio_simulado; }
    public void setPrecio_simulado(double precio_simulado) { this.precio_simulado = precio_simulado; }

    public int getDuracion_dias() { return duracion_dias; }
    public void setDuracion_dias(int duracion_dias) { this.duracion_dias = duracion_dias; }

    // Este método ayuda a que el ComboBox no muestre basura, sino el nombre del plan
    @Override
    public String toString() {
        return nombre_plan + " ($" + precio_simulado + ")";
    }
}