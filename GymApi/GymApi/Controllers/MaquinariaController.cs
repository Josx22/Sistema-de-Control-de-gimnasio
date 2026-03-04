
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
    public class MaquinariaController : ControllerBase
    {
        private readonly GymContext _db;
        public MaquinariaController(GymContext db) => _db = db;

        [HttpGet] public async Task<ActionResult> Get() => Ok(await _db.Maquinarias.ToListAsync());

        [HttpPost]
        public async Task<ActionResult> Post(Maquinaria m)
        {
            _db.Maquinarias.Add(m);
            await _db.SaveChangesAsync();
            return StatusCode(201, m);
        }

        [HttpPut("{id}")]
        public async Task<ActionResult> Put(int id, Maquinaria m)
        {
            var eq = await _db.Maquinarias.FindAsync(id);
            if (eq == null) return NotFound();

            eq.estado = m.estado;
            eq.descripcion = m.descripcion;
            eq.id_usuario_admin = m.id_usuario_admin;

            // regisrtar el gasto si existe una suscripción 
            if (m.estado == "Dañada")
            {
                var dummySus = await _db.Suscripciones.FirstOrDefaultAsync();
                if (dummySus != null)
                {
                    _db.Pagos.Add(new PagoSimulado
                    {
                        id_suscripcion_fk = dummySus.id_suscripcion,
                        monto = -25.00m,
                        fecha = DateTime.Now,
                        tipo = "Reparación: " + m.nombre
                    });
                }
            }

            _db.Entry(eq).State = EntityState.Modified;
            await _db.SaveChangesAsync(); 
            return Ok(eq);
        }
        [HttpPost("subir-imagen")]
        public async Task<ActionResult> SubirImagen(IFormFile archivo)
        {
            if (archivo == null || archivo.Length == 0) return BadRequest("Sin archivo");

            // Crear la carpeta si no existe
            string carpeta = Path.Combine(Directory.GetCurrentDirectory(), "wwwroot", "uploads");
            if (!Directory.Exists(carpeta)) Directory.CreateDirectory(carpeta);

            // Generar nombre único para la imagen
            string nombreUnico = Guid.NewGuid().ToString() + Path.GetExtension(archivo.FileName);
            string rutaFisica = Path.Combine(carpeta, nombreUnico);

            using (var stream = new FileStream(rutaFisica, FileMode.Create))
            {
                await archivo.CopyToAsync(stream);
            }

            // Devolvemos la ruta relativa para guardarla en la tabla Maquinaria
            return Ok(new { ruta = "/uploads/" + nombreUnico });
        }

        [HttpDelete("{id}")]
        public async Task<ActionResult> Delete(int id)
        {
            var m = await _db.Maquinarias.FindAsync(id);
            if (m == null) return NotFound();
            _db.Maquinarias.Remove(m);
            await _db.SaveChangesAsync();
            return Ok();
        }
    }
}