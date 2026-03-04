using Microsoft.EntityFrameworkCore;
using GymApi.Models; 

namespace GymApi.Data
{
    public class GymContext : DbContext
    {
        public GymContext(DbContextOptions<GymContext> options) : base(options) { }

        public DbSet<Usuario> Usuarios { get; set; }
        public DbSet<Rol> Roles { get; set; }
        public DbSet<Maquinaria> Maquinarias { get; set; }
        public DbSet<PlanMembresia> Planes { get; set; }
        public DbSet<Suscripcion> Suscripciones { get; set; }
        public DbSet<PagoSimulado> Pagos { get; set; }

        
        
    }
}