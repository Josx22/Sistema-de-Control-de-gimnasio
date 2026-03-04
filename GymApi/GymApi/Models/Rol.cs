using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GymApi.Models
{
    [Table("Roles")]
    public class Rol
    {
        [Key]
        public int id_rol { get; set; }
        public string nombre_rol { get; set; } = null!;
    }
}