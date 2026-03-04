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
    public class UsuariosController : ControllerBase
    {
        private readonly GymContext _db;
        public UsuariosController(GymContext db) => _db = db;

        [HttpPost]
        public async Task<ActionResult> Post(Usuario u)
        {
            // 1. Validar que no se repitan datos
            if (await _db.Usuarios.AnyAsync(x => x.dui == u.dui))
                return Conflict(new { m = "El DUI ya existe" });

            if (await _db.Usuarios.AnyAsync(x => x.username == u.username))
                return Conflict(new { m = "El nombre de usuario ya está ocupado" });

            // 2. Valores por defecto por si Java no los envía
            if (u.id_rol_fk == 0) u.id_rol_fk = 3; // Socio
            if (string.IsNullOrEmpty(u.estado_usuario)) u.estado_usuario = "Activo";

            try
            {
                _db.Usuarios.Add(u);
                await _db.SaveChangesAsync();

                // RETORNA 201: Esto es lo que GymService en Java espera para devolver "true"
                return StatusCode(201, u);
            }
            catch (Exception ex)
            {
                return StatusCode(500, "Error interno al guardar en SQL");
            }
        }

        

        [HttpGet]
        public async Task<ActionResult> Get() => Ok(await _db.Usuarios.Include(u => u.Rol).ToListAsync());

        [HttpGet("rol/{idRol}")]
        public async Task<ActionResult> GetByRol(int idRol)
        {
            var lista = await _db.Usuarios.Include(u => u.Rol).Where(u => u.id_rol_fk == idRol).ToListAsync();
            return Ok(lista);
        }

        [HttpGet("estatus-socios")]
        public async Task<ActionResult> GetEstatusSocios()
        {
            var socios = await _db.Usuarios
                .Where(u => u.id_rol_fk == 3)
                .Select(u => new {
                    id_usuario = u.id_usuario,
                    nombre = u.nombre + " " + u.apellido,
                    dui = u.dui,
                    estado_usuario = u.estado_usuario,
                    nombrePlan = _db.Suscripciones
                                    .Where(s => s.id_usuario_cliente == u.id_usuario)
                                    .OrderByDescending(s => s.id_suscripcion)
                                    .Select(s => s.Plan.nombre_plan).FirstOrDefault(),
                    ultimaMembresia = _db.Suscripciones
                                        .Where(s => s.id_usuario_cliente == u.id_usuario)
                                        .OrderByDescending(s => s.id_suscripcion)
                                        .Select(s => s.fecha_vencimiento.ToString("yyyy-MM-dd")).FirstOrDefault(),
                    estadoMembresia = _db.Suscripciones
                                        .Where(s => s.id_usuario_cliente == u.id_usuario)
                                        .OrderByDescending(s => s.id_suscripcion)
                                        .Select(s => s.estado_acceso).FirstOrDefault(),

                    id_suscripcion_actual = _db.Suscripciones
                                     .Where(s => s.id_usuario_cliente == u.id_usuario && s.estado_acceso == "Vigente")
                                     .OrderByDescending(s => s.id_suscripcion)
                                     .Select(s => s.id_suscripcion).FirstOrDefault(),
                    esta_pagado = _db.Suscripciones
                                    .Where(s => s.id_usuario_cliente == u.id_usuario && s.estado_acceso == "Vigente")
                                    .OrderByDescending(s => s.id_suscripcion)
                                    .Select(s => s.esta_pagado).FirstOrDefault()

                }).ToListAsync();
            return Ok(socios);
        }

        [HttpGet("buscar/{dui}")]
        public async Task<ActionResult> GetByDui(string dui)
        {
            var u = await _db.Usuarios.Include(u => u.Rol).FirstOrDefaultAsync(x => x.dui == dui);
            if (u == null) return NotFound(new { m = "Socio no encontrado" });

         
            var resultado = new
            {
                id_usuario = u.id_usuario,
                nombre = u.nombre + " " + u.apellido,
                dui = u.dui,
                estado_usuario = u.estado_usuario,
                nombrePlan = _db.Suscripciones.Where(s => s.id_usuario_cliente == u.id_usuario && s.estado_acceso == "Vigente").OrderByDescending(s => s.id_suscripcion).Select(s => s.Plan.nombre_plan).FirstOrDefault(),
                ultimaMembresia = _db.Suscripciones.Where(s => s.id_usuario_cliente == u.id_usuario && s.estado_acceso == "Vigente").OrderByDescending(s => s.id_suscripcion).Select(s => s.fecha_vencimiento.ToString("yyyy-MM-dd")).FirstOrDefault(),
                estadoMembresia = _db.Suscripciones.Where(s => s.id_usuario_cliente == u.id_usuario).OrderByDescending(s => s.id_suscripcion).Select(s => s.estado_acceso).FirstOrDefault(),

        
                id_suscripcion_actual = _db.Suscripciones
                                         .Where(s => s.id_usuario_cliente == u.id_usuario && s.estado_acceso == "Vigente")
                                         .OrderByDescending(s => s.id_suscripcion)
                                         .Select(s => s.id_suscripcion).FirstOrDefault(),
                esta_pagado = _db.Suscripciones
                                .Where(s => s.id_usuario_cliente == u.id_usuario && s.estado_acceso == "Vigente")
                                .OrderByDescending(s => s.id_suscripcion)
                                .Select(s => s.esta_pagado).FirstOrDefault()
            };

            return Ok(resultado);
        }

        [HttpPut("{id}")]
        public async Task<ActionResult> Put(int id, Usuario u)
        {
            if (id != u.id_usuario) return BadRequest();
            _db.Entry(u).State = EntityState.Modified;
            await _db.SaveChangesAsync();
            return Ok(u);
        }

        [HttpPatch("{id}/estado")]
        public async Task<ActionResult> PatchEstado(int id, [FromBody] string nuevoEstado)
        {
            var u = await _db.Usuarios.FindAsync(id);
            if (u == null) return NotFound();
            u.estado_usuario = nuevoEstado;
            _db.Entry(u).State = EntityState.Modified;
            await _db.SaveChangesAsync();
            return Ok(new { m = "Estado cambiado" });
        }

        [HttpGet("clientes-total")]
        public async Task<ActionResult> GetSoloClientes()
        {
            var clientes = await _db.Usuarios.Where(u => u.id_rol_fk == 3).ToListAsync();
            return Ok(clientes);
        }
    }
}