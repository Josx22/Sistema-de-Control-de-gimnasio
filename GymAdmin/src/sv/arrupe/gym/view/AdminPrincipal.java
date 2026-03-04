package sv.arrupe.gym.view;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sv.arrupe.gym.model.Maquinaria;
import sv.arrupe.gym.model.PlanMembresia;
import sv.arrupe.gym.model.SuscripcionDTO;
import sv.arrupe.gym.model.Usuario;
import sv.arrupe.gym.model.UsuarioStatus;
import sv.arrupe.gym.service.GymService;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author josue
 */

public class AdminPrincipal extends javax.swing.JFrame {

     private final GymService service = new GymService();
    private int idPlanSeleccionado = -1; 
    private java.io.File archivoImagenSeleccionado = null;

    public AdminPrincipal() {
        initComponents();
        this.setLocationRelativeTo(null); 
        inicializarDatos();
    } 

    private void inicializarDatos() {
        actualizarTablaMaquinaria();
        actualizarTablaPlanes();
        actualizarTablaPersonal();
        actualizarTablaStatusSocios();
        actualizarReporte();
    }
    private void actualizarTablaMaquinaria() {
        try {
            List<Maquinaria> lista = service.getMaquinaria();
            DefaultTableModel model = (DefaultTableModel) tblMaquinaria.getModel();
            model.setRowCount(0);
            
            for (Maquinaria m : lista) {
                model.addRow(new Object[]{ m.getId_maquina(), m.getNombre(), m.getEstado(), m.getDescripcion() });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar maquinaria.");
        }
    }
    private void actualizarReporte() {
    try {
        com.google.gson.JsonObject data = service.getReporteContable();
        double balance = data.get("balanceTotal").getAsDouble();
        
        String t = "<html>Ingresos: <font color='blue'>$ " + data.get("ingresosSimulados").getAsString() + "</font><br>" +
                   "Gastos: <font color='red'>$ " + data.get("gastosOperativos").getAsString() + "</font><br>" +
                   "<b>BALANCE NETO: $ " + String.format("%.2f", balance) + "</b></html>";
        
        lblMontoGanancias.setText(t);
        
        if(balance < 0) lblMontoGanancias.setForeground(java.awt.Color.RED);
        else lblMontoGanancias.setForeground(new java.awt.Color(0,102,0));
        
    } catch (Exception e) { 
        lblMontoGanancias.setText("Sin datos de API"); 
    }
}
    private void limpiarCamposPlanes() {
        txtNombrePlan.setText("");
        txtPrecioMembresia.setText("");
        txtDiasDuracion.setText("");
        jTextField1.setText(""); 
        idPlanSeleccionado = -1;
        btnGuardarPlan.setText("Guardar Nuevo Plan");
    }
    
private void actualizarTablaPersonal() {
    try {
        List<Usuario> empleados = service.getPersonal(); 
        DefaultTableModel model = (DefaultTableModel) tblPersonal.getModel();
        model.setRowCount(0);

        for (Usuario e : empleados) {
            model.addRow(new Object[]{
                e.id_usuario,
                e.nombre,
                e.apellido,
                e.dui,
                e.estado_usuario
            });
        }
    } catch (Exception e) {
        System.err.println("Error en personal: " + e.getMessage());
    }
}
private void actualizarTablaStatusSocios() {
    try {
        List<UsuarioStatus> lista = service.getEstatusSocios();
        DefaultTableModel model = (DefaultTableModel) tblStatusSocios.getModel();
        model.setRowCount(0);
        
        java.time.LocalDate hoy = java.time.LocalDate.now();
        
        for (UsuarioStatus u : lista) {
            String statusText = "MOROSO (S/P)";
            if (u.ultimaMembresia != null && !u.ultimaMembresia.contains("0001")) {
                java.time.LocalDate vence = java.time.LocalDate.parse(u.ultimaMembresia.substring(0, 10));
                statusText = vence.isBefore(hoy) ? "VENCIDO" : "SOLVENTE";
            }
            model.addRow(new Object[]{ u.dui, u.nombre, statusText });
        }
    } catch (Exception e) {
        System.err.println("Error en status socios: " + e.getMessage());
    }
}
private void actualizarTablaPlanes() {
    try {
        List<PlanMembresia> lista = service.getPlanes(); 
        DefaultTableModel model = (DefaultTableModel) tblPlanesMembresia.getModel();
        model.setRowCount(0); // Limpiar tabla
        
        for (PlanMembresia p : lista) {
            model.addRow(new Object[]{
                p.getId_plan(), 
                p.getNombre_plan(), 
                "$ " + p.getPrecio_simulado(), 
                p.getDuracion_dias() + " días",
                "$ " + p.getMonto_penalizacion()
            });
        }
    } catch (Exception e) {
        System.err.println("Error en planes: " + e.getMessage());
    }
}

    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblMaquinaria = new javax.swing.JTable();
        btnReportarFalla = new javax.swing.JButton();
        txtNomMaq = new javax.swing.JTextField();
        txtDescMaq = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnAgregarMaq = new javax.swing.JButton();
        btnRetirar = new javax.swing.JButton();
        btnElegirImagen = new javax.swing.JButton();
        lblRutaFoto = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtNombrePlan = new javax.swing.JTextField();
        txtPrecioMembresia = new javax.swing.JTextField();
        txtDiasDuracion = new javax.swing.JTextField();
        btnGuardarPlan = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPlanesMembresia = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnEditar = new javax.swing.JButton();
        btnDesactivar = new javax.swing.JButton();
        btnActivar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        lblTituloReporte = new javax.swing.JLabel();
        btnActualizarReporte = new javax.swing.JButton();
        lblMontoGanancias = new javax.swing.JLabel();
        btnGastos = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblPersonal = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblStatusSocios = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtEmpDui = new javax.swing.JTextField();
        txtEmpNombre = new javax.swing.JTextField();
        txtEmpApellido = new javax.swing.JTextField();
        txtEmpPass = new javax.swing.JTextField();
        txtEmpUser = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        btnRegistrarEmpleado = new javax.swing.JButton();
        btnDesactivarPersonal = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblHistorialPlanes = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tblMaquinaria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "id_maquina", "Nombre", "Estado", "Descripcion"
            }
        ));
        jScrollPane3.setViewportView(tblMaquinaria);

        btnReportarFalla.setText("Reportar Falla");
        btnReportarFalla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportarFallaActionPerformed(evt);
            }
        });

        jLabel1.setText("Nombre");

        jLabel2.setText("Descripción");

        btnAgregarMaq.setText("Añadir al Gimnasio");
        btnAgregarMaq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarMaqActionPerformed(evt);
            }
        });

        btnRetirar.setText("Retirar equipo");
        btnRetirar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRetirarActionPerformed(evt);
            }
        });

        btnElegirImagen.setText("Imagen");
        btnElegirImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnElegirImagenActionPerformed(evt);
            }
        });

        lblRutaFoto.setText("Ruta Foto");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDescMaq, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(btnAgregarMaq)
                    .addComponent(btnElegirImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRutaFoto)
                    .addComponent(txtNomMaq, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnReportarFalla)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRetirar))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNomMaq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(2, 2, 2)
                        .addComponent(txtDescMaq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(lblRutaFoto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnElegirImagen)
                        .addGap(60, 60, 60)
                        .addComponent(btnAgregarMaq))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnReportarFalla)
                    .addComponent(btnRetirar))
                .addContainerGap(401, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Gestión de Maquinaria", jPanel1);

        btnGuardarPlan.setText("Guardar Nuevo Plan");
        btnGuardarPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarPlanActionPerformed(evt);
            }
        });

        tblPlanesMembresia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "id_plan", "Nombre del plan", "Precio", "Duración", "Monto"
            }
        ));
        jScrollPane2.setViewportView(tblPlanesMembresia);

        jLabel5.setText("Nombre del plan");

        jLabel6.setText("Precio");

        jLabel7.setText("Días");

        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnDesactivar.setText("Desactivar Membresia");
        btnDesactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesactivarActionPerformed(evt);
            }
        });

        btnActivar.setText("Activar Membresia");
        btnActivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActivarActionPerformed(evt);
            }
        });

        jLabel8.setText("Monto");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnGuardarPlan)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditar)
                        .addGap(84, 84, 84)
                        .addComponent(btnDesactivar)
                        .addGap(18, 18, 18)
                        .addComponent(btnActivar))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 178, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtNombrePlan, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                                .addComponent(txtPrecioMembresia)
                                .addComponent(txtDiasDuracion))
                            .addComponent(jLabel7)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNombrePlan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPrecioMembresia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDiasDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardarPlan)
                    .addComponent(btnEditar)
                    .addComponent(btnDesactivar)
                    .addComponent(btnActivar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111))
        );

        jTabbedPane1.addTab("Membresías", jPanel2);

        lblTituloReporte.setText("Ganancia Mensuales");

        btnActualizarReporte.setText("Actualizar Reporte");
        btnActualizarReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarReporteActionPerformed(evt);
            }
        });

        btnGastos.setText("Aplicar Gastos Operativos (Luz/Limpieza)");
        btnGastos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGastosActionPerformed(evt);
            }
        });

        tblPersonal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Apellido", "DUI", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblPersonal);

        tblStatusSocios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "DUI", "Nombre", "Estado"
            }
        ));
        tblStatusSocios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblStatusSociosMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblStatusSocios);

        jLabel3.setText("Empeados");

        jLabel4.setText("Clientes");

        jLabel9.setText("Nombre");

        jLabel10.setText("Apellido");

        jLabel11.setText("DUI");

        jLabel12.setText("Usuario");

        jLabel13.setText("Contraseña");

        btnRegistrarEmpleado.setText("Registrar Empleado");
        btnRegistrarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarEmpleadoActionPerformed(evt);
            }
        });

        btnDesactivarPersonal.setText("Desactivar Empleado");
        btnDesactivarPersonal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesactivarPersonalActionPerformed(evt);
            }
        });

        tblHistorialPlanes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Fecha inicio", "Fecha vencimiento", "Estado"
            }
        ));
        jScrollPane6.setViewportView(tblHistorialPlanes);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 911, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnActualizarReporte)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGastos)
                        .addGap(665, 665, 665))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtEmpPass, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                                .addComponent(txtEmpUser, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtEmpDui, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtEmpApellido, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtEmpNombre, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jLabel13)
                            .addComponent(jLabel9)
                            .addComponent(btnRegistrarEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(201, 201, 201))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lblTituloReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblMontoGanancias, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnDesactivarPersonal, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 883, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(lblTituloReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblMontoGanancias, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnActualizarReporte)
                    .addComponent(btnGastos))
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmpNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmpApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmpDui, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmpUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addGap(2, 2, 2)
                        .addComponent(txtEmpPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRegistrarEmpleado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDesactivarPersonal)))
                .addGap(11, 11, 11)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(368, 368, 368))
        );

        jTabbedPane1.addTab("Reporte y Personal", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 882, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnReportarFallaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportarFallaActionPerformed
        int fila = tblMaquinaria.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un equipo de la lista.");
            return;
        }

        int id = (int) tblMaquinaria.getValueAt(fila, 0);
        String[] opciones = {"En Orden", "Mantenimiento", "Dañada"};
        String seleccion = (String) JOptionPane.showInputDialog(this, "Nuevo estado:", "Actualizar", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion != null) {
            try {
                Maquinaria m = new Maquinaria();
                m.setEstado(seleccion);
                m.setDescripcion(JOptionPane.showInputDialog("Detalle del reporte:"));
                String nombreActual = (String) tblMaquinaria.getValueAt(fila, 1); 
m.setNombre(nombreActual);
                
                service.actualizarMaquina(id, m);
                actualizarTablaMaquinaria();
                actualizarReporte(); // Refrescar si hubo gasto de reparación
                JOptionPane.showMessageDialog(this, "Estado actualizado en el sistema.");
            } catch (Exception e) { e.printStackTrace(); }
        }
    
    }//GEN-LAST:event_btnReportarFallaActionPerformed

    private void btnGuardarPlanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarPlanActionPerformed
      if (txtNombrePlan.getText().trim().isEmpty() || txtPrecioMembresia.getText().trim().isEmpty() || 
        txtDiasDuracion.getText().trim().isEmpty() || jTextField1.getText().trim().isEmpty()) {
        
        javax.swing.JOptionPane.showMessageDialog(this, "Todos los campos económicos son requeridos.");
        return;
    }

    try {
        double precio = Double.parseDouble(txtPrecioMembresia.getText());
        double multa = Double.parseDouble(jTextField1.getText());
        int dias = Integer.parseInt(txtDiasDuracion.getText()); 

        
        if (precio < 0 || multa < 0 || dias <= 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "ERROR: El precio y multa deben ser 0 o más, y los días deben ser mayores a 0.");
            return;
        }

        PlanMembresia p = new PlanMembresia();
        p.setNombre_plan(txtNombrePlan.getText().trim());
        p.setPrecio_simulado(precio);
        p.setDuracion_dias(dias);
        p.setMonto_penalizacion(multa);
        p.setId_usuario_admin(sv.arrupe.gym.model.Session.idUsuario);

        if (idPlanSeleccionado == -1) {
            service.crearPlanMembresia(p);
            javax.swing.JOptionPane.showMessageDialog(this, "Membresía creada satisfactoriamente.");
        } else {
            p.setId_plan(idPlanSeleccionado);
            service.editarPlan(idPlanSeleccionado, p);
            javax.swing.JOptionPane.showMessageDialog(this, "Cambios aplicados correctamente.");
            idPlanSeleccionado = -1;
        }
        
        limpiarCamposPlanes();
        actualizarTablaPlanes();

    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(this, "ERROR: Verifique que Precio, Multa y Días sean números válidos.");
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "API Offline.");
    }
    }//GEN-LAST:event_btnGuardarPlanActionPerformed

    private void btnAgregarMaqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarMaqActionPerformed
    String nombre = txtNomMaq.getText().trim();
        String desc = txtDescMaq.getText().trim();
        String url = lblRutaFoto.getText();

        if (nombre.isEmpty() || desc.isEmpty() || url.equals("Ruta Foto") || url.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "ERROR: Nombre, Descripción e Imagen son obligatorios.");
        return; 
    }


        try {
            Maquinaria m = new Maquinaria();
            m.setNombre(nombre);
            m.setDescripcion(desc);
            m.setEstado("En Orden");
            m.setImagen_url(url); 

            service.crearMaquinaria(m);
            JOptionPane.showMessageDialog(this, "Equipo registrado correctamente.");
            actualizarTablaMaquinaria();
            txtNomMaq.setText(""); txtDescMaq.setText(""); lblRutaFoto.setText("Sin foto");
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error de red."); }
  
    }//GEN-LAST:event_btnAgregarMaqActionPerformed

    private void btnRetirarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRetirarActionPerformed
        int fila = tblMaquinaria.getSelectedRow();
        if (fila == -1) return;
        int id = (int) tblMaquinaria.getValueAt(fila, 0);
        if (JOptionPane.showConfirmDialog(this, "¿Retirar este equipo permanentemente?") == 0) {
            try { service.eliminarMaquinaria(id); actualizarTablaMaquinaria(); } catch (Exception e) { }
        }
    
    }//GEN-LAST:event_btnRetirarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
    int fila = tblPlanesMembresia.getSelectedRow();
        if (fila == -1) return;
        idPlanSeleccionado = (int) tblPlanesMembresia.getValueAt(fila, 0);
        txtNombrePlan.setText(tblPlanesMembresia.getValueAt(fila, 1).toString());
        btnGuardarPlan.setText("Actualizar Seleccionado");
        JOptionPane.showMessageDialog(this, "Modifique los datos y presione Actualizar.");
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnDesactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesactivarActionPerformed
       int f = tblPlanesMembresia.getSelectedRow();
        if (f == -1) return;
        try {
            int id = (int) tblPlanesMembresia.getValueAt(f, 0);
            PlanMembresia p = new PlanMembresia();
            p.setNombre_plan(tblPlanesMembresia.getValueAt(f, 1).toString());
            service.desactivarPlan(id, p);
            actualizarTablaPlanes();
        } catch (Exception e) { }
    }//GEN-LAST:event_btnDesactivarActionPerformed

    private void btnActivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActivarActionPerformed
       actualizarReporte();
    }//GEN-LAST:event_btnActivarActionPerformed

    private void btnCargarSociosStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarSociosStatusActionPerformed
   
    }//GEN-LAST:event_btnCargarSociosStatusActionPerformed

    private void btnCargarPersonalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarPersonalActionPerformed
      
    }//GEN-LAST:event_btnCargarPersonalActionPerformed

    private void btnGastosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGastosActionPerformed
      int c = JOptionPane.showConfirmDialog(this, "¿Aplicar cobros de Luz y Limpieza al mes actual?");
        if (c == JOptionPane.YES_OPTION) {
            try {
                service.aplicarGastosFijos();
                actualizarReporte();
                JOptionPane.showMessageDialog(this, "Gastos registrados negativamente en contabilidad.");
            } catch (Exception e) { }
        }
    }//GEN-LAST:event_btnGastosActionPerformed

    private void btnActualizarReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarReporteActionPerformed
       try {
            com.google.gson.JsonObject data = service.getReporteContable();
            double balance = data.get("balanceTotal").getAsDouble();
            
            String t = "<html>Ingresos: <font color='blue'>$ " + data.get("ingresosSimulados").getAsString() + "</font><br>" +
                       "Gastos: <font color='red'>$ " + data.get("gastosOperativos").getAsString() + "</font><br>" +
                       "<b>NETO PROYECTADO: $ " + balance + "</b></html>";
            
            lblMontoGanancias.setText(t);
        } catch (Exception e) { lblMontoGanancias.setText("API Offline"); }
    }//GEN-LAST:event_btnActualizarReporteActionPerformed

    private void btnRegistrarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarEmpleadoActionPerformed
     String nom = txtEmpNombre.getText().trim();
    String ape = txtEmpApellido.getText().trim();
    String d = txtEmpDui.getText().trim();
    String usr = txtEmpUser.getText().trim();
    String pss = txtEmpPass.getText().trim();

    if (nom.isEmpty() || ape.isEmpty() || d.isEmpty() || usr.isEmpty() || pss.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Faltan datos personales o de acceso para el empleado.");
        return;
    }

    try {
        Usuario u = new Usuario();
        u.nombre = nom;
        u.apellido = ape;
        u.dui = d;
        u.username = usr;
        u.password = pss;

        service.registrarEmpleado(u);
        javax.swing.JOptionPane.showMessageDialog(this, "Empleado habilitado para iniciar sesión.");
        
        // Limpiar campos de registro
        txtEmpNombre.setText(""); txtEmpApellido.setText(""); txtEmpDui.setText(""); 
        txtEmpUser.setText(""); txtEmpPass.setText("");
        actualizarTablaPersonal();
        
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "No se pudo registrar: El DUI o el Usuario ya están registrados.");
    }
    }//GEN-LAST:event_btnRegistrarEmpleadoActionPerformed

    private void btnDesactivarPersonalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesactivarPersonalActionPerformed
   int fila = tblPersonal.getSelectedRow();
        if (fila == -1) return;

        int id = (int) tblPersonal.getValueAt(fila, 0);
        int conf = JOptionPane.showConfirmDialog(this, "¿Quitar el acceso a este empleado?");
        if (conf == JOptionPane.YES_OPTION) {
            try {
                service.desactivarUsuario(id);
                actualizarTablaPersonal();
                JOptionPane.showMessageDialog(this, "Cuenta inhabilitada.");
            } catch (Exception e) { e.printStackTrace(); JOptionPane.showMessageDialog(null, "Error: " + e.getMessage()); }
        }
    }//GEN-LAST:event_btnDesactivarPersonalActionPerformed

    private void tblStatusSociosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStatusSociosMouseClicked
     int fila = tblStatusSocios.getSelectedRow();
        if (fila == -1) return;
        String dui = tblStatusSocios.getValueAt(fila, 0).toString();
        try {
            UsuarioStatus s = service.buscarSocio(dui);
            if (s != null) {
                List<SuscripcionDTO> hist = service.getHistorialSocio(s.getId_usuario());
                DefaultTableModel m = (DefaultTableModel) tblHistorialPlanes.getModel();
                m.setRowCount(0);
                for(SuscripcionDTO h : hist) m.addRow(new Object[]{ h.fecha_inicio.substring(0,10), h.fecha_vencimiento.substring(0,10), h.estado_acceso });
            }
        } catch (Exception e) { }
    }//GEN-LAST:event_tblStatusSociosMouseClicked

    private void btnElegirImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnElegirImagenActionPerformed
  String url = JOptionPane.showInputDialog(this, "Pegue el link de la imagen:");
        if(url != null && !url.isEmpty()) lblRutaFoto.setText(url);
    }//GEN-LAST:event_btnElegirImagenActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActivar;
    private javax.swing.JButton btnActualizarReporte;
    private javax.swing.JButton btnAgregarMaq;
    private javax.swing.JButton btnDesactivar;
    private javax.swing.JButton btnDesactivarPersonal;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnElegirImagen;
    private javax.swing.JButton btnGastos;
    private javax.swing.JButton btnGuardarPlan;
    private javax.swing.JButton btnRegistrarEmpleado;
    private javax.swing.JButton btnReportarFalla;
    private javax.swing.JButton btnRetirar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblMontoGanancias;
    private javax.swing.JLabel lblRutaFoto;
    private javax.swing.JLabel lblTituloReporte;
    private javax.swing.JTable tblHistorialPlanes;
    private javax.swing.JTable tblMaquinaria;
    private javax.swing.JTable tblPersonal;
    private javax.swing.JTable tblPlanesMembresia;
    private javax.swing.JTable tblStatusSocios;
    private javax.swing.JTextField txtDescMaq;
    private javax.swing.JTextField txtDiasDuracion;
    private javax.swing.JTextField txtEmpApellido;
    private javax.swing.JTextField txtEmpDui;
    private javax.swing.JTextField txtEmpNombre;
    private javax.swing.JTextField txtEmpPass;
    private javax.swing.JTextField txtEmpUser;
    private javax.swing.JTextField txtNomMaq;
    private javax.swing.JTextField txtNombrePlan;
    private javax.swing.JTextField txtPrecioMembresia;
    // End of variables declaration//GEN-END:variables
}
