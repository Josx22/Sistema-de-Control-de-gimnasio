using GymCliente.Models;
using Newtonsoft.Json;
using System.Net.Http.Headers;
using System.Text;
using System.Text.Json.Serialization;

namespace GymCliente.Service
{
    public class ApiService
    {
        private readonly HttpClient _http = new HttpClient();
        private readonly string API_URL = "http://localhost:5195/api";

        private void AgregarToken(string token)
        {
            if (!string.IsNullOrEmpty(token))
                _http.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", token);
        }

        // --- MÉTODO PARA LOGIN ---
        public async Task<SocioStatusModel> LoginAsync(string user, string pass)
        {
            var data = new { username = user, password = pass, dui = "N/A", nombre = "N/A", apellido = "N/A" };

            var jsonString = JsonConvert.SerializeObject(data);
            var content = new StringContent(jsonString, Encoding.UTF8, "application/json");

            var res = await _http.PostAsync($"{API_URL}/Auth/login", content);

            if (res.IsSuccessStatusCode)
            {
                
                string respuestaBody = await res.Content.ReadAsStringAsync();
                return JsonConvert.DeserializeObject<SocioStatusModel>(respuestaBody);
            }
            return null;
        }

        // --- CONSULTA DE MÁQUINAS 
        public async Task<List<MaquinariaModel>> GetMaquinas(string token)
        {
            AgregarToken(token);
            var res = await _http.GetAsync($"{API_URL}/Maquinaria");
            if (res.IsSuccessStatusCode)
            {
                var json = await res.Content.ReadAsStringAsync();
                var lista = JsonConvert.DeserializeObject<List<MaquinariaModel>>(json);
                return lista.Where(m => m.estado == "En Orden").ToList();
            }
            return new List<MaquinariaModel>();
        }

        // --- CONSULTA ESTATUS / DÍAS RESTANTES ---
        public async Task<SocioStatusModel> GetEstatus(string dui, string token)
        {
            AgregarToken(token);
            try
            {
                var res = await _http.GetAsync($"{API_URL}/Usuarios/buscar/{dui}");

                if (res.IsSuccessStatusCode)
                {
                    var json = await res.Content.ReadAsStringAsync();
                    return JsonConvert.DeserializeObject<SocioStatusModel>(json);
                }
                return null;
            }
            catch
            {
                return null;
            }
        }
        public async Task<bool> PagarMembresia(int idSub, string token)
        {
            _http.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", token);
            var res = await _http.PostAsync($"{API_URL}/Suscripcion/{idSub}/pagar", null);
            return res.IsSuccessStatusCode;
        }

        // --- SELECCIONAR PLAN 
        public async Task<bool> AsignarPlan(int idUsuario, int idPlan, string token)
        {
            AgregarToken(token);
            var data = new { id_usuario_cliente = idUsuario, id_plan_fk = idPlan, id_usuario_empleado = idUsuario };
            var content = new StringContent(JsonConvert.SerializeObject(data), Encoding.UTF8, "application/json");
            var res = await _http.PostAsync($"{API_URL}/Suscripcion/asignar", content);
            return res.IsSuccessStatusCode;
        }

        // --- CANCELAR MEMBRESÍA 
        public async Task<bool> PostAsync(string path, object data, string token)
        {
            AgregarToken(token);
            var res = await _http.PostAsync($"{API_URL}{path}", null);
            return res.IsSuccessStatusCode;
        }

        public async Task<List<PlanWebModel>> GetPlanes(string token)
        {
            AgregarToken(token);
            var res = await _http.GetAsync($"{API_URL}/Planes");
            var json = await res.Content.ReadAsStringAsync();
            return JsonConvert.DeserializeObject<List<PlanWebModel>>(json);
        }
    }
}