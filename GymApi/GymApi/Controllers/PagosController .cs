
using GymApi.Data;
using GymApi.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace Gimnasio.Controllers
{
    [Authorize]
    [ApiController]
    [Route("api/[controller]")]
    public class PagosController : ControllerBase
    {
        private readonly GymContext _db;
        public PagosController(GymContext db) => _db = db;

        [HttpGet("total-mensual")]
        public async Task<ActionResult> GetGanancias()
        {
            var total = await _db.Pagos
                .Where(p => p.fecha.Month == DateTime.Now.Month)
                .SumAsync(p => p.monto);
            return Ok(new { recaudacion = total });
        }
        [HttpPost("registrar-gastos-fijos")]
        public async Task<ActionResult> registrarGastosFijos()
        {
            var suscripcionCualquiera = await _db.Suscripciones.FirstOrDefaultAsync();

            if (suscripcionCualquiera == null)
            {
                return BadRequest("Debe existir al menos un socio con suscripción para registrar gastos.");
            }

            // Registro de Gasto de LUZ
            _db.Pagos.Add(new PagoSimulado
            {
                id_suscripcion_fk = suscripcionCualquiera.id_suscripcion,
                monto = -100.00m,
                fecha = DateTime.Now,
                tipo = "Gasto Eléctrico (Mensual)"
            });

            // Registro de Gasto de LIMPIEZA
            _db.Pagos.Add(new PagoSimulado
            {
                id_suscripcion_fk = suscripcionCualquiera.id_suscripcion,
                monto = -80.00m,
                fecha = DateTime.Now,
                tipo = "Insumos de Limpieza (Fijo)"
            });

            await _db.SaveChangesAsync(); 
            return Ok(new { m = "Gastos aplicados" });
        }
        [HttpGet("reporte-contable")]
        public async Task<ActionResult> GetReporteFinanciero()
        {
            var hoy = DateTime.Now;
            var pagos = await _db.Pagos.Where(p => p.fecha.Month == hoy.Month).ToListAsync();
  
            var ingresos = pagos.Where(p => p.monto > 0).Sum(p => p.monto);
            var gastos = pagos.Where(p => p.monto < 0).Sum(p => p.monto);
            var balance = ingresos + gastos;

            return Ok(new
            {
                ingresosSimulados = ingresos,
                gastosOperativos = gastos,
                balanceTotal = balance
            });
        }

    }
}