using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GymApi.Models
{
    [Table("Planes_Membresia")]
    public class PlanMembresia
    {
        [Key]
        public int id_plan { get; set; }
        public string nombre_plan { get; set; } = null!;
        public decimal precio_simulado { get; set; }
        public int duracion_dias { get; set; }
        public decimal monto_penalizacion { get; set; }

        [ForeignKey("Admin")]
        public int? id_usuario_admin { get; set; }
        public virtual Usuario? Admin { get; set; }
    }
}