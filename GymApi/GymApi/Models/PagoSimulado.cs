using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GymApi.Models
{
    [Table("Pagos_Simulados")]
    public class PagoSimulado
    {
        [Key]
        public int id_pago { get; set; }

        [ForeignKey("Suscripcion")]
        public int id_suscripcion_fk { get; set; }

        public decimal monto { get; set; }
        public DateTime fecha { get; set; } = DateTime.Now;
        public string? tipo { get; set; } // 'Mensualidad' o 'Penalización'

        public virtual Suscripcion? Suscripcion { get; set; }
    }
}