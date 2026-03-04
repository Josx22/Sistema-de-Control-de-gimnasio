using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GymApi.Models
{
    [Table("Suscripcion")]
    public class Suscripcion
    {
        [Key]
        public int id_suscripcion { get; set; }

        [ForeignKey("Cliente")]
        public int id_usuario_cliente { get; set; }

        [ForeignKey("Plan")]
        public int id_plan_fk { get; set; }

        [ForeignKey("Empleado")]
        public int id_usuario_empleado { get; set; }

        public DateTime fecha_inicio { get; set; }
        public DateTime fecha_vencimiento { get; set; }
        public string estado_acceso { get; set; } = "Vigente";
        public bool esta_pagado { get; set; } = false;

        // Propiedades de navegación
        public virtual Usuario? Cliente { get; set; }
        public virtual Usuario? Empleado { get; set; }
        public virtual PlanMembresia? Plan { get; set; }
    }
}