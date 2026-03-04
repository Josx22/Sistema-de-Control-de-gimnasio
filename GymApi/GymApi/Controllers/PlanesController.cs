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
    public class PlanesController : ControllerBase
    {
        private readonly GymContext _db;
        public PlanesController(GymContext db) => _db = db;

        [HttpGet]
        public async Task<ActionResult> Get(bool verInactivos = false) // Por defecto es 'false'
        {
            var query = _db.Planes.AsQueryable();

            if (!verInactivos)
            {
                query = query.Where(p => !p.nombre_plan.Contains("[INACTIVO]"));
            }

            return Ok(await query.ToListAsync());
        }

        [HttpPut("{id}")] // EDITAR Plan completo
        public async Task<ActionResult> Put(int id, PlanMembresia p)
        {
            var plan = await _db.Planes.FindAsync(id);
            if (plan == null) return NotFound();

            plan.nombre_plan = p.nombre_plan;
            plan.precio_simulado = p.precio_simulado;
            plan.duracion_dias = p.duracion_dias;
            plan.monto_penalizacion = p.monto_penalizacion;

            await _db.SaveChangesAsync();
            return Ok(plan);
        }

        [HttpPost]
        public async Task<ActionResult> Post(PlanMembresia p)
        {
            _db.Planes.Add(p);
            await _db.SaveChangesAsync();
            return StatusCode(201, p);
        }
        [HttpPatch("{id}/desactivar")] 
        public async Task<ActionResult> Desactivar(int id)
        {
            var plan = await _db.Planes.FindAsync(id);
            if (plan == null) return NotFound();

            plan.nombre_plan = "[INACTIVO] " + plan.nombre_plan;
            await _db.SaveChangesAsync();
            return Ok();
        }

        [HttpDelete("{id}")]
        public async Task<ActionResult> Delete(int id)
        {
            var p = await _db.Planes.FindAsync(id);
            if (p == null) return NotFound();
            _db.Planes.Remove(p);
            await _db.SaveChangesAsync();
            return Ok();
        }
    }
}