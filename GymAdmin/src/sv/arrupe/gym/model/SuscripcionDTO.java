package sv.arrupe.gym.model;

public class SuscripcionDTO {
    public int id_suscripcion;
    public int id_usuario_cliente;
    public int id_plan_fk;
    public int id_usuario_empleado;
    
    // Estos nombres deben coincidir con tu API en C#
    public String fecha_inicio;
    public String fecha_vencimiento;
    public String estado_acceso;
    
    // Este campo lo usaremos para que el Admin vea el nombre del plan (ej: "Mensual")
    // Recuerda que en C# agregamos el .Include(s => s.Plan)
    public String nombrePlan; 
}