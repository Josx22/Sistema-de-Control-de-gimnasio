using Gimnasio.DTOs;
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
    public class SuscripcionController : ControllerBase
    {
        private readonly GymContext _db;
        public SuscripcionController(GymContext db) => _db = db;

        [HttpPost("asignar")]
        public async Task<ActionResult> Asignar([FromBody] SuscripcionDTO dto)
        {
            var planAnterior = await _db.Suscripciones.Where(s => s.id_usuario_cliente == dto.id_usuario_cliente && s.estado_acceso == "Vigente").FirstOrDefaultAsync();
            if (planAnterior != null) { planAnterior.estado_acceso = "Cancelada (Cambio)"; }

            var planNuevoInfo = await _db.Planes.FindAsync(dto.id_plan_fk);
            if (planNuevoInfo == null) return BadRequest();

            var nueva = new Suscripcion
            {
                id_usuario_cliente = dto.id_usuario_cliente,
                id_plan_fk = dto.id_plan_fk,
                id_usuario_empleado = dto.id_usuario_empleado,
                fecha_inicio = DateTime.Now,
                fecha_vencimiento = DateTime.Now.AddDays(planNuevoInfo.duracion_dias),
                estado_acceso = "Vigente",
                esta_pagado = false // <--- SE CREA COMO DEUDA
            };

            _db.Suscripciones.Add(nueva);


            await _db.SaveChangesAsync();
            return Ok(nueva);
        }
        [HttpGet("pendientes")]
        public async Task<ActionResult> GetPendientes()
        {
            // Buscamos suscripciones que el cliente pidió por la web (Estado: Pendiente)
            var lista = await _db.Suscripciones
                        .Include(s => s.Cliente)
                        .Include(s => s.Plan)
                        .Where(s => s.estado_acceso == "Esperando Validación")
                        .ToListAsync();
            return Ok(lista);
        }

        [HttpPost("cancelar-manual/{idUsuario}")]
        public async Task<ActionResult> CancelarManual(int idUsuario)
        {
            // Buscamos la última membresía VIGENTE de ese usuario específico
            var suscripcion = await _db.Suscripciones
                                .Where(s => s.id_usuario_cliente == idUsuario && s.estado_acceso == "Vigente")
                                .OrderByDescending(s => s.id_suscripcion)
                                .FirstOrDefaultAsync();

            if (suscripcion == null)
            {
                // Si no hay membresía activa para ese usuario, devolvemos un 404
                return NotFound(new { m = "No hay plan activo para cancelar." });
            }

            // Cambiamos el estado a Cancelada
            suscripcion.estado_acceso = "Cancelada";
            _db.Entry(suscripcion).State = EntityState.Modified;

            await _db.SaveChangesAsync(); 

            return Ok(new { m = "Plan cancelado exitosamente." });
        }
        [HttpGet("historial/{idUsuario}")] // Lo usará el Admin en Swing
        public async Task<ActionResult> GetHistorialPorUsuario(int idUsuario)
        {
            var historial = await _db.Suscripciones
                            .Include(s => s.Plan) // Para ver el nombre del plan
                            .Where(s => s.id_usuario_cliente == idUsuario)
                            .OrderByDescending(s => s.fecha_inicio)
                            .ToListAsync();
            return Ok(historial);
        }
        [HttpPost("{id}/pagar")]
        public async Task<ActionResult> Pagar(int id)
        {
            var s = await _db.Suscripciones.Include(x => x.Plan).FirstOrDefaultAsync(x => x.id_suscripcion == id);
            if (s == null) return NotFound("Suscripción no encontrada.");

            s.esta_pagado = true; 

            _db.Pagos.Add(new PagoSimulado
            {
                id_suscripcion_fk = s.id_suscripcion,
                monto = s.Plan.precio_simulado,
                fecha = DateTime.Now,
                tipo = "Cobro Mensual Web"
            });

            await _db.SaveChangesAsync();
            return Ok(new { m = "¡Membresía pagada con éxito!" });
        }
        [HttpPost("{id}/cancelar")] // Endpoint que usa el SOCIO en la WEB
        public async Task<ActionResult> CancelarSocio(int id)
        {
            var s = await _db.Suscripciones.Include(x => x.Plan).FirstOrDefaultAsync(x => x.id_suscripcion == id);
            if (s == null) return NotFound();

            // 1. Cambiamos el estado a Cancelada de inmediato
            s.estado_acceso = "Cancelada";

            // 2. Lógica de Cobro de "Castigo" (Toda la membresía + Multa)
            if (s.esta_pagado == false)
            {
                // NO PAGÓ NADA: Le cobramos el PLAN COMPLETO y la PENALIZACIÓN juntas
                decimal cargoTotal = s.Plan.precio_simulado + s.Plan.monto_penalizacion;

                _db.Pagos.Add(new PagoSimulado
                {
                    id_suscripcion_fk = s.id_suscripcion,
                    monto = cargoTotal,
                    fecha = DateTime.Now,
                    tipo = "CANCELACIÓN WEB (Cobro Plan + Multa)"
                });

                s.esta_pagado = true;
            }
            else
            {
                _db.Pagos.Add(new PagoSimulado
                {
                    id_suscripcion_fk = s.id_suscripcion,
                    monto = s.Plan.monto_penalizacion,
                    fecha = DateTime.Now,
                    tipo = "MULTA por Cancelación Anticipada (Socio Web)"
                });
            }

            await _db.SaveChangesAsync();
            return Ok();
        }

    }
}