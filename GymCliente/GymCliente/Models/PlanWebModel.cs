namespace GymCliente.Models
{
    public class PlanWebModel
    {
        public int id_plan { get; set; }
        public string nombre_plan { get; set; }
        public decimal precio_simulado { get; set; }
        public int duracion_dias { get; set; }
        public decimal monto_penalizacion { get; set; }
    }
}
