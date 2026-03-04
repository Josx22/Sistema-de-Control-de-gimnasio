using GymApi.Data;
using GymApi.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Security.Claims;
using System.IdentityModel.Tokens.Jwt;
using Microsoft.IdentityModel.Tokens;
using System.Text;

namespace Gimnasio.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class AuthController : ControllerBase
    {
        private readonly GymContext _db;
        private readonly IConfiguration _config;

        public AuthController(GymContext db, IConfiguration config)
        {
            _db = db;
            _config = config;
        }

        [HttpPost("login")]
        public async Task<ActionResult> Login([FromBody] Usuario credenciales)
        {
           
            var u = await _db.Usuarios.Include(u => u.Rol)
                .FirstOrDefaultAsync(u => u.username == credenciales.username && u.password == credenciales.password);

            // 2. Si no existe, error 401
            if (u == null) return Unauthorized(new { m = "Usuario o clave incorrecta" });

            if (u.estado_usuario == "Inactivo") 
            {
                return Forbid(); // 403: Cuenta dada de baja permanentemente
            }
            
            if (u.estado_usuario == "Suspendido") 
            {
                return Unauthorized(new { m = "Cuenta suspendida temporalmente. Consulte en recepción." });
            }

            // 4. Generación de Token JWT 
            var secretKey = _config.GetSection("Jwt").GetSection("Key").Value;
            var keyBytes = Encoding.ASCII.GetBytes(secretKey);

            var claims = new ClaimsIdentity();
            claims.AddClaim(new Claim(ClaimTypes.NameIdentifier, u.id_usuario.ToString()));
            claims.AddClaim(new Claim(ClaimTypes.Name, u.nombre));
            claims.AddClaim(new Claim(ClaimTypes.Role, u.Rol.nombre_rol));

            var tokenDescriptor = new SecurityTokenDescriptor
            {
                Subject = claims,
                Expires = DateTime.UtcNow.AddHours(8),
                SigningCredentials = new SigningCredentials(
                    new SymmetricSecurityKey(keyBytes),
                    SecurityAlgorithms.HmacSha256Signature)
            };

            var tokenHandler = new JwtSecurityTokenHandler();
            var tokenConfig = tokenHandler.CreateToken(tokenDescriptor);
            string tokenCreado = tokenHandler.WriteToken(tokenConfig);

            // 5. Devolver los datos necesarios para que Swing y FX controlen la navegación
            return Ok(new
            {
                id_usuario = u.id_usuario,
                nombre = u.nombre,
                rol_nombre = u.Rol.nombre_rol,
                id_rol = u.id_rol_fk,
                dui = u.dui,
                token = tokenCreado
            });
        }
    }
}