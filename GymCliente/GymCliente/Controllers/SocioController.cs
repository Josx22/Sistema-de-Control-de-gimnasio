using Microsoft.AspNetCore.Mvc;
using GymCliente.Service;
using GymCliente.Models;

namespace GymCliente.Controllers
{
    public class SocioController : Controller
    {
        private readonly ApiService _api = new ApiService();

        // Login Web
        public IActionResult Login() => View();

        [HttpPost]
        public async Task<IActionResult> Login(string username, string password)
        {
            var socio = await _api.LoginAsync(username, password);
            if (socio != null)
            {
                // Guardamos en la Sesión de la web
                HttpContext.Session.SetString("TokenJWT", socio.token);
                HttpContext.Session.SetString("DUI", socio.dui);
                HttpContext.Session.SetInt32("IdUsuario", socio.id_usuario);
                return RedirectToAction("Dashboard");
            }
            ViewBag.Error = "Credenciales inválidas";
            return View();
        }

        public async Task<IActionResult> Dashboard()
        {
            string token = HttpContext.Session.GetString("TokenJWT");
            string dui = HttpContext.Session.GetString("DUI");

            if (string.IsNullOrEmpty(token)) return RedirectToAction("Login");

            var statusSocio = await _api.GetEstatus(dui, token);
            var planes = await _api.GetPlanes(token);
            var maquinas = await _api.GetMaquinas(token);

            if (statusSocio != null && !string.IsNullOrEmpty(statusSocio.nombrePlan))
            {
                var miPlan = planes.FirstOrDefault(p => p.nombre_plan == statusSocio.nombrePlan);
                if (miPlan != null)
                {
                    statusSocio.precio_plan = miPlan.precio_simulado;
                    statusSocio.monto_penalizacion = miPlan.monto_penalizacion;
                }
            }

            ViewBag.Socio = statusSocio;
            ViewBag.Planes = planes;

            return View(maquinas);
        }

        //Selección autónoma de plan
        public async Task<IActionResult> ComprarPlan(int idPlan)
        {
            string token = HttpContext.Session.GetString("TokenJWT");
            string dui = HttpContext.Session.GetString("DUI");

            var status = await _api.GetEstatus(dui, token);

            if (status.estadoMembresia == "Vigente")
            {
                return RedirectToAction("Dashboard");
            }

            int idUser = HttpContext.Session.GetInt32("IdUsuario") ?? 0;
            await _api.AsignarPlan(idUser, idPlan, token);
            return RedirectToAction("Dashboard");
        }


        //Cancelar con Multa
        public async Task<IActionResult> Cancelar()
        {
            string token = HttpContext.Session.GetString("TokenJWT");
            int idUser = HttpContext.Session.GetInt32("IdUsuario") ?? 0;

            // Llamamos a la ruta de cancelación manual (La API le aplica la multa por nosotros)
            await _api.PostAsync($"/Suscripcion/cancelar-manual/{idUser}", null, token);
            return RedirectToAction("Dashboard");
        }

        public async Task<IActionResult> PagarYa(int idSub)
        {
            string token = HttpContext.Session.GetString("TokenJWT");
            var exito = await _api.PagarMembresia(idSub, token);

            return RedirectToAction("Dashboard");
        }

        // penalización automática
        public async Task<IActionResult> CancelarWeb(int idSub)
        {
            string token = HttpContext.Session.GetString("TokenJWT");
            var res = await _api.PostAsync($"/Suscripcion/{idSub}/cancelar", null, token);

            return RedirectToAction("Dashboard");
        }
    }
}