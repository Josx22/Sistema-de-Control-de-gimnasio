using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GymApi.Models
{
    [Table("Maquinaria")]
    public class Maquinaria
    {
        [Key]
        public int id_maquina { get; set; }
        public string nombre { get; set; } = null!;
        public string estado { get; set; } = "Orden"; // Orden, Dañada, Mantenimiento
        public string? descripcion { get; set; }
        public string? imagen_url { get; set; }
        [NotMapped]
        public string? imagen_data { get; set; }


        [ForeignKey("Admin")]
        public int? id_usuario_admin { get; set; }
        public virtual Usuario? Admin { get; set; }
    }
}