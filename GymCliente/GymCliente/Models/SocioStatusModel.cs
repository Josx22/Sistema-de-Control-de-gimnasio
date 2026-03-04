namespace GymCliente.Models
{
    public class SocioStatusModel
    {
        public int id_usuario { get; set; }
        public string nombre { get; set; }
        public string dui { get; set; }
        public string token { get; set; }
        public string nombrePlan { get; set; }
        public string ultimaMembresia { get; set; }
        public string estadoMembresia { get; set; }
        public string estado_usuario { get; set; }
        public int id_suscripcion_actual { get; set; }
        public bool esta_pagado { get; set; }

        // --- AGREGAR ESTAS DOS LÍNEAS ---
        public decimal precio_plan { get; set; }
        public decimal monto_penalizacion { get; set; }
    }
}