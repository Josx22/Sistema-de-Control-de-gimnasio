using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GymApi.Models
{
    [Table("Usuarios")]
    public class Usuario
    {
        [Key]
        public int id_usuario { get; set; }

        [ForeignKey("Rol")]
        public int id_rol_fk { get; set; }

        public string nombre { get; set; } = null!;
        public string apellido { get; set; } = null!;
        public string dui { get; set; } = null!;
        public string username { get; set; } = null!;
        public string password { get; set; } = null!;
        public string estado_usuario { get; set; } = "Activo";
        public DateTime fecha_registro { get; set; } = DateTime.Now;

      
        public virtual Rol? Rol { get; set; }
    }
}