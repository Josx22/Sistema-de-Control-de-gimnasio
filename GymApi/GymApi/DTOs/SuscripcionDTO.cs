namespace Gimnasio.DTOs
{
    public class SuscripcionDTO
    {
        public int id_usuario_cliente { get; set; }
        public int id_plan_fk { get; set; }
        public int id_usuario_empleado { get; set; }
    }
}