/*
 * Copyright (C) 2018 José Ramón Cuevas  https://www.linkedin.com/in/joseramoncuevasdiez
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package criptolabhash.vista;

import criptolabhash.control.APISeguimientoHash;
import criptolabhash.control.APIServerHash;
import criptolabhash.funciones.AlimentadorHash;
import criptolabhash.funciones.FabricaHash;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author José Ramón Cuevas https://www.linkedin.com/in/joseramoncuevasdiez
 */
public class CriptoLabHashGUI extends javax.swing.JPanel {

    private final APISeguimientoHash apiSeguimiento;
    private final APIServerHash apiservidor;

    /**
     * Creates new form CriptoLabHashGUI
     *
     * @param apiServidor Funciones accesibles del servidor.
     */
    public CriptoLabHashGUI(APIServerHash apiServidor) {
        apiSeguimiento = new AlimentadorHash();
        apiservidor = apiServidor;
        initComponents();
        apiservidor.setAlgoritmo((String) algoritmoa.getSelectedItem(), (Integer) sizemsg.getValue(), (Integer) sizehash.getValue());
        infoconexion.setText(apiservidor.getInfoConexion());
        Thread monitor = new monitorizaServidor();
        monitor.start();
    }

    /**
     * Cada segundo monitoriza el estado del servidor.
     */
    class monitorizaServidor extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    nclientes.setText(Integer.toString(apiservidor.nClientesConectados()));
                    long nhash = apiservidor.nHashCalculados();
                    long nhashold;
                    try {
                        nhashold = Long.parseLong(nhashes.getText());
                    } catch (NumberFormatException ex) {
                        nhashold = 0;
                    }
                    nhashes.setText(String.valueOf(nhash));
                    long nhs = nhash - nhashold;
                    nhs = (nhs < 0) ? 0 : nhs;
                    nhashseg.setText(String.valueOf(nhs));
                    ncolisiones.setText(String.valueOf(apiservidor.nColisiones()));
                    npseudocolisiones.setText(String.valueOf(apiservidor.nPseudoColisiones()));
                } catch (InterruptedException ex) {
                    break;
                }
            }
        }
    }

    /**
     * Ejecuta el proceso de calcular un hash en segundo plano para no bloquear
     * el GUI.
     */
    class procesaHash extends Thread {

        @Override
        public void run() {
            informeseguimiento.setText("");
            salidahash.setText("");
            tiempo.setText("Procesando la entrada....");
            botonprocesar.setEnabled(false);
            String hash;
            Boolean nivelinforme = (informeno.isSelected()) ? null : informepaso.isSelected();
            StringBuilder informe = new StringBuilder();
            String algoritmo = (String) algoritmoi.getSelectedItem();
            long ti = System.currentTimeMillis();
            try {

                if (earchivo.isSelected()) {
                    hash = apiSeguimiento.procesaFile(algoritmo, nivelinforme, new File(nombrearchivo.getText()), informe);
                } else if (etexto.isSelected()) {
                    hash = apiSeguimiento.procesaText(algoritmo, nivelinforme, entradateclado.getText(), informe);
                } else {
                    hash = apiSeguimiento.procesaHex(algoritmo, nivelinforme, entradateclado.getText(), informe);
                }
                long tproceso = System.currentTimeMillis() - ti;
                String duracion;
                if (tproceso > 59999) {
                    duracion = String.format("%.2f minutos", (double) tproceso / 60000);
                } else if (tproceso > 999) {
                    duracion = String.format("%.3f segundos", (double) tproceso / 1000);
                } else {
                    duracion = String.format("%d milisegundos", tproceso);
                }
                tiempo.setText(duracion);
                informeseguimiento.setText(informe.toString());
                salidahash.setText(hash);
            } catch (IOException iOException) {
                JOptionPane.showMessageDialog(null, "Se ha producido un error con el fichero: " + iOException.getMessage(), "Error de entrada", JOptionPane.ERROR_MESSAGE);
                tiempo.setText("");
            } catch (IllegalArgumentException illegalArgumentException) {
                JOptionPane.showMessageDialog(null, "Compruebe que la entrada esté en hexadecimal.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                tiempo.setText("");
            } finally {
                botonprocesar.setEnabled(true);
            }
        }
    }
   
    /**
     * Guarda un texto en un fichero, si no existe lo crea, si ya existe borra,
     * su contenido.
     * 
     * @param informe informe a guardar.
     */
    private void guardarenArchivo(JTextArea informe) {
        JFileChooser selectorarchivo = new JFileChooser();
        //FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.TXT", "txt");
        //selectorarchivo.setFileFilter(filtro);
        int resultado = selectorarchivo.showSaveDialog(null);
        try {
            if (resultado == JFileChooser.APPROVE_OPTION) {
                Path path = selectorarchivo.getSelectedFile().toPath();
                Files.write(path, informe.getText().getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                JOptionPane.showMessageDialog(null, "Texto guardado correctamente.", "Guardando archivo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar el texto: " + ex.getMessage(), "Guardando archivo", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        modoinforme = new javax.swing.ButtonGroup();
        tipoentrada = new javax.swing.ButtonGroup();
        acercade = new javax.swing.JDialog();
        jButton1 = new javax.swing.JButton();
        textoinformativo = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelAtaque = new javax.swing.JPanel();
        panelIzquierdoA = new javax.swing.JPanel();
        panelconfiguracion = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        sizemsg = new javax.swing.JSpinner();
        sizehash = new javax.swing.JSpinner();
        algoritmoa = new javax.swing.JComboBox<>();
        botonaplicar = new javax.swing.JButton();
        panelcontrol = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        nclientes = new javax.swing.JTextField();
        botonapagar = new javax.swing.JButton();
        botonreset = new javax.swing.JButton();
        botonarrancar = new javax.swing.JButton();
        botonparar = new javax.swing.JButton();
        infoconexion = new javax.swing.JLabel();
        panelmonitorizacion = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        nhashes = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        ncolisiones = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        npseudocolisiones = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        nhashseg = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        botoninforme = new javax.swing.JButton();
        panelderechoA = new javax.swing.JPanel();
        scrollpanelAtaque = new javax.swing.JScrollPane();
        informecolisiones = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        guardarA = new javax.swing.JButton();
        imprimirA = new javax.swing.JButton();
        panelSeguimiento = new javax.swing.JPanel();
        panelizquierdoS = new javax.swing.JPanel();
        panelopciones = new javax.swing.JPanel();
        algoritmoi = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        informeno = new javax.swing.JRadioButton();
        informebloque = new javax.swing.JRadioButton();
        informepaso = new javax.swing.JRadioButton();
        panelentrada = new javax.swing.JPanel();
        botonarchivo = new javax.swing.JButton();
        nombrearchivo = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        entradateclado = new javax.swing.JTextArea();
        earchivo = new javax.swing.JRadioButton();
        etexto = new javax.swing.JRadioButton();
        ehexadecimal = new javax.swing.JRadioButton();
        panelsalida = new javax.swing.JPanel();
        salidahash = new javax.swing.JTextField();
        botonprocesar = new javax.swing.JButton();
        tiempo = new javax.swing.JLabel();
        panelderechoS = new javax.swing.JPanel();
        scrollpanelSeguimiento = new javax.swing.JScrollPane();
        informeseguimiento = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        guardarS = new javax.swing.JButton();
        imprimirS = new javax.swing.JButton();

        acercade.setTitle("Acerca de");
        acercade.setUndecorated(true);
        acercade.setResizable(false);

        jButton1.setText("Aceptar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        textoinformativo.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        textoinformativo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textoinformativo.setText("Master Universitario en Seguridad Informática");

        javax.swing.GroupLayout acercadeLayout = new javax.swing.GroupLayout(acercade.getContentPane());
        acercade.getContentPane().setLayout(acercadeLayout);
        acercadeLayout.setHorizontalGroup(
            acercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textoinformativo, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(acercadeLayout.createSequentialGroup()
                .addGap(173, 173, 173)
                .addComponent(jButton1))
        );
        acercadeLayout.setVerticalGroup(
            acercadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(acercadeLayout.createSequentialGroup()
                .addComponent(textoinformativo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 169, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(24, 24, 24))
        );

        setMaximumSize(new java.awt.Dimension(1024, 610));
        setPreferredSize(new java.awt.Dimension(1024, 610));
        setSize(new java.awt.Dimension(1024, 610));
        setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(1024, 610));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1024, 610));

        panelAtaque.setLayout(new java.awt.BorderLayout());

        panelIzquierdoA.setPreferredSize(new java.awt.Dimension(378, 540));
        panelIzquierdoA.setSize(new java.awt.Dimension(378, 540));
        panelIzquierdoA.setLayout(new java.awt.BorderLayout());

        panelconfiguracion.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración del ataque"));
        panelconfiguracion.setMaximumSize(new java.awt.Dimension(378, 130));
        panelconfiguracion.setPreferredSize(new java.awt.Dimension(378, 130));
        panelconfiguracion.setSize(new java.awt.Dimension(378, 130));

        jLabel1.setText("Algoritmo");

        jLabel2.setText("Tamaño hash");

        jLabel3.setText("Tamaño mensaje");

        sizemsg.setModel(new javax.swing.SpinnerNumberModel(4, 1, 64, 1));
        sizemsg.setToolTipText("Tamaño en bytes de los mensajes aleatorios, que se generarán en cada cliente.");

        sizehash.setModel(new javax.swing.SpinnerNumberModel(4, 1, 64, 1));
        sizehash.setToolTipText("Tamaño en bytes que se utilizarán del resumen original del algoritmo, empezando del más a la izquierda.");
        sizehash.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        algoritmoa.setModel(new javax.swing.DefaultComboBoxModel<>(FabricaHash.arrayAlgoritmos()));
        algoritmoa.setToolTipText("Algoritmo que utilizarán los clientes para generar los códigos hash a partir de los mensajes.");

        botonaplicar.setText("Aplicar");
        botonaplicar.setToolTipText("Aplicar la configuración seleccionada arriba a todos los clientes.");
        botonaplicar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonaplicarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelconfiguracionLayout = new javax.swing.GroupLayout(panelconfiguracion);
        panelconfiguracion.setLayout(panelconfiguracionLayout);
        panelconfiguracionLayout.setHorizontalGroup(
            panelconfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelconfiguracionLayout.createSequentialGroup()
                .addGroup(panelconfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelconfiguracionLayout.createSequentialGroup()
                        .addGroup(panelconfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(botonaplicar, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                            .addComponent(algoritmoa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(63, 63, 63)
                        .addComponent(sizehash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelconfiguracionLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)))
                .addGap(18, 18, 18)
                .addGroup(panelconfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sizemsg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );
        panelconfiguracionLayout.setVerticalGroup(
            panelconfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelconfiguracionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelconfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelconfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(algoritmoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sizehash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sizemsg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonaplicar)
                .addContainerGap())
        );

        panelIzquierdoA.add(panelconfiguracion, java.awt.BorderLayout.NORTH);

        panelcontrol.setBorder(javax.swing.BorderFactory.createTitledBorder("Control de ejecución"));
        panelcontrol.setLocation(new java.awt.Point(-32751, -32729));
        panelcontrol.setMaximumSize(new java.awt.Dimension(378, 154));
        panelcontrol.setPreferredSize(new java.awt.Dimension(378, 154));
        panelcontrol.setSize(new java.awt.Dimension(378, 154));

        jLabel4.setText("Número de clientes conectados");

        nclientes.setEditable(false);
        nclientes.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        nclientes.setToolTipText("Número de clientes generadores de mensajes, que actualmente están conectados a esta aplicación.");
        nclientes.setFocusable(false);

        botonapagar.setText("Apagar clientes");
        botonapagar.setToolTipText("Ordena el apagado y desconexión de todos los clientes conectados actualmente.");
        botonapagar.setEnabled(false);
        botonapagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonapagarActionPerformed(evt);
            }
        });

        botonreset.setText("Resetear");
        botonreset.setToolTipText("Elimina las bases de datos en los clientes y el servidor, de todos los hash calculados hasta ahora.");
        botonreset.setEnabled(false);
        botonreset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonresetActionPerformed(evt);
            }
        });

        botonarrancar.setText("Arrancar");
        botonarrancar.setToolTipText("Envía la orden de comenzar a generar mensajes aleatorios y calcular hash, a todos los clientes.");
        botonarrancar.setEnabled(false);
        botonarrancar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonarrancarActionPerformed(evt);
            }
        });

        botonparar.setText("Parar");
        botonparar.setToolTipText("<html>\n<p>Envía la orden de parar la generación de mensajes a todos los clientes.</p>\n<p>Esta acción no elimina los mensajes generados hasta ahora, solo pausa su generación.</p>\n</html>");
        botonparar.setEnabled(false);
        botonparar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonpararActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelcontrolLayout = new javax.swing.GroupLayout(panelcontrol);
        panelcontrol.setLayout(panelcontrolLayout);
        panelcontrolLayout.setHorizontalGroup(
            panelcontrolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelcontrolLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelcontrolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelcontrolLayout.createSequentialGroup()
                        .addComponent(infoconexion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panelcontrolLayout.createSequentialGroup()
                        .addGroup(panelcontrolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelcontrolLayout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nclientes, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6))
                            .addGroup(panelcontrolLayout.createSequentialGroup()
                                .addGroup(panelcontrolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(botonapagar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(botonreset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(80, 87, Short.MAX_VALUE)
                                .addGroup(panelcontrolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(botonparar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(botonarrancar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(35, 35, 35))))
        );
        panelcontrolLayout.setVerticalGroup(
            panelcontrolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelcontrolLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelcontrolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(nclientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelcontrolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonreset)
                    .addComponent(botonarrancar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelcontrolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonapagar)
                    .addComponent(botonparar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(infoconexion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelIzquierdoA.add(panelcontrol, java.awt.BorderLayout.WEST);

        panelmonitorizacion.setBorder(javax.swing.BorderFactory.createTitledBorder("Monitorización del proceso"));
        panelmonitorizacion.setToolTipText("");
        panelmonitorizacion.setLocation(new java.awt.Point(-32751, -32729));
        panelmonitorizacion.setMaximumSize(new java.awt.Dimension(378, 265));
        panelmonitorizacion.setPreferredSize(new java.awt.Dimension(378, 265));
        panelmonitorizacion.setSize(new java.awt.Dimension(378, 200));

        jLabel5.setText("Número de hash calculados");

        nhashes.setEditable(false);
        nhashes.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        nhashes.setToolTipText("Número de hashes enviados desde los clientes hasta este servidor.");
        nhashes.setFocusable(false);

        jLabel6.setText("Grupos de colisiones encontradas");

        ncolisiones.setEditable(false);
        ncolisiones.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ncolisiones.setToolTipText("<html>\n<p>Grupos de colisiones encontradas hasta ahora.</p>\n<p>Cada grupo esta asociado a un hash que puede contener dos o más mensajes que lo han generado.</p>\n</html>");
        ncolisiones.setFocusable(false);

        jLabel7.setText("Pseudocolisiones ocurridas");

        npseudocolisiones.setEditable(false);
        npseudocolisiones.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        npseudocolisiones.setToolTipText("<html>\n<p>Número de colisiones no reales.</p>\n<p>Estas se producen, cuando se genera el mismo mensaje mas de una vez.</p>\n<p>Los clientes realizan un primer filtrado, ya que no envian mensajes repetidos.</p>\n<p>Pero puede darse el caso de que dos clientes distintos generen mensajes iguales.</p>\n</html>");
        npseudocolisiones.setFocusable(false);

        jLabel9.setText("Velocidad mensajes/segundo");

        nhashseg.setEditable(false);
        nhashseg.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        nhashseg.setToolTipText("Velocidad en hashes por segundo, que esta recibiendo este servidor desde los clientes.");
        nhashseg.setFocusable(false);

        jLabel10.setText("Obtener informe de colisiones actual");

        botoninforme.setText("Informe");
        botoninforme.setToolTipText("Imprime un informe en el panel de la derecha, con los grupos de colisiones encontrados hasta ahora.");
        botoninforme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botoninformeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelmonitorizacionLayout = new javax.swing.GroupLayout(panelmonitorizacion);
        panelmonitorizacion.setLayout(panelmonitorizacionLayout);
        panelmonitorizacionLayout.setHorizontalGroup(
            panelmonitorizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelmonitorizacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelmonitorizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelmonitorizacionLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nhashes))
                    .addGroup(panelmonitorizacionLayout.createSequentialGroup()
                        .addGroup(panelmonitorizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(panelmonitorizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelmonitorizacionLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(panelmonitorizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ncolisiones)
                                    .addComponent(npseudocolisiones)))
                            .addGroup(panelmonitorizacionLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nhashseg))))
                    .addGroup(panelmonitorizacionLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botoninforme)))
                .addContainerGap())
        );
        panelmonitorizacionLayout.setVerticalGroup(
            panelmonitorizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelmonitorizacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelmonitorizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhashes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelmonitorizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(nhashseg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelmonitorizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(ncolisiones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelmonitorizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(npseudocolisiones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelmonitorizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(botoninforme))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelIzquierdoA.add(panelmonitorizacion, java.awt.BorderLayout.SOUTH);

        panelAtaque.add(panelIzquierdoA, java.awt.BorderLayout.WEST);

        panelderechoA.setPreferredSize(new java.awt.Dimension(619, 540));
        panelderechoA.setSize(new java.awt.Dimension(619, 540));
        panelderechoA.setLayout(new java.awt.BorderLayout());

        scrollpanelAtaque.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpanelAtaque.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollpanelAtaque.setMaximumSize(new java.awt.Dimension(700, 520));
        scrollpanelAtaque.setPreferredSize(new java.awt.Dimension(619, 520));
        scrollpanelAtaque.setSize(new java.awt.Dimension(619, 520));

        informecolisiones.setEditable(false);
        informecolisiones.setFont(new java.awt.Font("Courier", 0, 14)); // NOI18N
        informecolisiones.setToolTipText("Panel donde aparecerá el informe.");
        scrollpanelAtaque.setViewportView(informecolisiones);

        panelderechoA.add(scrollpanelAtaque, java.awt.BorderLayout.NORTH);

        jPanel1.setMinimumSize(new java.awt.Dimension(619, 40));
        jPanel1.setPreferredSize(new java.awt.Dimension(619, 40));
        jPanel1.setSize(new java.awt.Dimension(619, 40));

        guardarA.setText("Guardar");
        guardarA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarAActionPerformed(evt);
            }
        });

        imprimirA.setText("Imprimir");
        imprimirA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprimirAActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(guardarA)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 212, Short.MAX_VALUE)
                .addComponent(imprimirA)
                .addGap(129, 129, 129))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(guardarA)
                    .addComponent(imprimirA))
                .addContainerGap())
        );

        panelderechoA.add(jPanel1, java.awt.BorderLayout.SOUTH);

        panelAtaque.add(panelderechoA, java.awt.BorderLayout.EAST);

        jTabbedPane1.addTab("Ataques a las funciones hash", panelAtaque);

        panelSeguimiento.setLayout(new java.awt.BorderLayout());

        panelizquierdoS.setMaximumSize(new java.awt.Dimension(378, 540));
        panelizquierdoS.setMinimumSize(new java.awt.Dimension(378, 540));
        panelizquierdoS.setPreferredSize(new java.awt.Dimension(378, 540));
        panelizquierdoS.setSize(new java.awt.Dimension(378, 540));
        panelizquierdoS.setLayout(new java.awt.BorderLayout());

        panelopciones.setBorder(javax.swing.BorderFactory.createTitledBorder("Opciones del algoritmo"));
        panelopciones.setMaximumSize(new java.awt.Dimension(378, 150));
        panelopciones.setMinimumSize(new java.awt.Dimension(378, 150));
        panelopciones.setPreferredSize(new java.awt.Dimension(378, 150));
        panelopciones.setSize(new java.awt.Dimension(378, 150));

        algoritmoi.setModel(new javax.swing.DefaultComboBoxModel<>(FabricaHash.arrayAlgoritmos()));
        algoritmoi.setToolTipText("Algoritmo que se empleará para realizar el cálculo del hash y los seguimientos.");

        jLabel8.setText("Algoritmo");

        modoinforme.add(informeno);
        informeno.setSelected(true);
        informeno.setText("Sin informe");
        informeno.setToolTipText("Cálculo del hash sin generar un informe.");

        modoinforme.add(informebloque);
        informebloque.setText("Informe a nivel de bloque");
        informebloque.setToolTipText("Informe a nivel de bloque de las operaciones realizadas, para calcular el hash.");

        modoinforme.add(informepaso);
        informepaso.setText("Informe paso a paso");
        informepaso.setToolTipText("Informe a nivel de paso a paso de las operaciones realizadas, para calcular el hash.");

        javax.swing.GroupLayout panelopcionesLayout = new javax.swing.GroupLayout(panelopciones);
        panelopciones.setLayout(panelopcionesLayout);
        panelopcionesLayout.setHorizontalGroup(
            panelopcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelopcionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelopcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(informepaso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(informebloque, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                    .addGroup(panelopcionesLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(algoritmoi, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(informeno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(132, Short.MAX_VALUE))
        );
        panelopcionesLayout.setVerticalGroup(
            panelopcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelopcionesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelopcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(algoritmoi, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(informeno)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(informebloque)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(informepaso)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelizquierdoS.add(panelopciones, java.awt.BorderLayout.NORTH);

        panelentrada.setBorder(javax.swing.BorderFactory.createTitledBorder("Entrada"));
        panelentrada.setMaximumSize(new java.awt.Dimension(378, 228));
        panelentrada.setMinimumSize(new java.awt.Dimension(378, 228));
        panelentrada.setPreferredSize(new java.awt.Dimension(378, 228));
        panelentrada.setSize(new java.awt.Dimension(378, 228));

        botonarchivo.setText("Archivo");
        botonarchivo.setToolTipText("Selecciona el archivo que se utilizará para calcular el hash.");
        botonarchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonarchivoActionPerformed(evt);
            }
        });

        nombrearchivo.setEditable(false);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Entrada por teclado"));

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        entradateclado.setLineWrap(true);
        entradateclado.setToolTipText("El texto que aquí se introduzca se utilizará para el cálculo del hash.");
        entradateclado.setEnabled(false);
        jScrollPane3.setViewportView(entradateclado);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
        );

        tipoentrada.add(earchivo);
        earchivo.setSelected(true);
        earchivo.setText("Archivo");
        earchivo.setToolTipText("Selecciona como entrada del algoritmo, el archivo que se indique más abajo.");
        earchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                earchivoActionPerformed(evt);
            }
        });

        tipoentrada.add(etexto);
        etexto.setText("Texto");
        etexto.setToolTipText("Selecciona el texto introducido en el panel inferior como entrada para el algoritmo.");
        etexto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                etextoActionPerformed(evt);
            }
        });

        tipoentrada.add(ehexadecimal);
        ehexadecimal.setText("Hexadecimal");
        ehexadecimal.setToolTipText("Selecciona el texto en hexadecimal introducido mas abajo como entrada para el algoritmo.");
        ehexadecimal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ehexadecimalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelentradaLayout = new javax.swing.GroupLayout(panelentrada);
        panelentrada.setLayout(panelentradaLayout);
        panelentradaLayout.setHorizontalGroup(
            panelentradaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelentradaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelentradaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelentradaLayout.createSequentialGroup()
                        .addComponent(earchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(etexto)
                        .addGap(34, 34, 34)
                        .addComponent(ehexadecimal)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelentradaLayout.createSequentialGroup()
                        .addComponent(botonarchivo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombrearchivo)))
                .addContainerGap())
        );
        panelentradaLayout.setVerticalGroup(
            panelentradaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelentradaLayout.createSequentialGroup()
                .addGroup(panelentradaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(earchivo)
                    .addComponent(etexto)
                    .addComponent(ehexadecimal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelentradaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonarchivo)
                    .addComponent(nombrearchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelizquierdoS.add(panelentrada, java.awt.BorderLayout.WEST);

        panelsalida.setBorder(javax.swing.BorderFactory.createTitledBorder("Salida"));
        panelsalida.setMaximumSize(new java.awt.Dimension(378, 180));
        panelsalida.setMinimumSize(new java.awt.Dimension(378, 162));
        panelsalida.setPreferredSize(new java.awt.Dimension(378, 162));
        panelsalida.setSize(new java.awt.Dimension(378, 162));

        salidahash.setEditable(false);
        salidahash.setToolTipText("Número hash calculado a partir de la información disponible en la entrada.");

        botonprocesar.setText("Procesar");
        botonprocesar.setToolTipText("Calcular el hash con los valores anteriormente introducidos.");
        botonprocesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonprocesarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelsalidaLayout = new javax.swing.GroupLayout(panelsalida);
        panelsalida.setLayout(panelsalidaLayout);
        panelsalidaLayout.setHorizontalGroup(
            panelsalidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelsalidaLayout.createSequentialGroup()
                .addGroup(panelsalidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(salidahash, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelsalidaLayout.createSequentialGroup()
                        .addComponent(botonprocesar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tiempo, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        panelsalidaLayout.setVerticalGroup(
            panelsalidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelsalidaLayout.createSequentialGroup()
                .addGroup(panelsalidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonprocesar)
                    .addComponent(tiempo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(salidahash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        panelizquierdoS.add(panelsalida, java.awt.BorderLayout.SOUTH);

        panelSeguimiento.add(panelizquierdoS, java.awt.BorderLayout.WEST);

        panelderechoS.setPreferredSize(new java.awt.Dimension(619, 540));
        panelderechoS.setSize(new java.awt.Dimension(619, 540));
        panelderechoS.setLayout(new java.awt.BorderLayout());

        scrollpanelSeguimiento.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpanelSeguimiento.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollpanelSeguimiento.setMaximumSize(new java.awt.Dimension(700, 520));
        scrollpanelSeguimiento.setPreferredSize(new java.awt.Dimension(619, 520));
        scrollpanelSeguimiento.setSize(new java.awt.Dimension(619, 520));

        informeseguimiento.setEditable(false);
        informeseguimiento.setFont(new java.awt.Font("Courier", 0, 14)); // NOI18N
        informeseguimiento.setToolTipText("Panel donde aparecerá el informe.");
        scrollpanelSeguimiento.setViewportView(informeseguimiento);

        panelderechoS.add(scrollpanelSeguimiento, java.awt.BorderLayout.NORTH);

        jPanel2.setMinimumSize(new java.awt.Dimension(619, 40));
        jPanel2.setSize(new java.awt.Dimension(619, 40));

        guardarS.setText("Guardar");
        guardarS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarSActionPerformed(evt);
            }
        });

        imprimirS.setText("Imprimir");
        imprimirS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprimirSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(guardarS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 212, Short.MAX_VALUE)
                .addComponent(imprimirS)
                .addGap(129, 129, 129))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(guardarS)
                    .addComponent(imprimirS))
                .addContainerGap())
        );

        panelderechoS.add(jPanel2, java.awt.BorderLayout.SOUTH);

        panelSeguimiento.add(panelderechoS, java.awt.BorderLayout.EAST);

        jTabbedPane1.addTab("Calculo y seguimiento de las funciones Hash", panelSeguimiento);

        add(jTabbedPane1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void botonarchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonarchivoActionPerformed
        JFileChooser selectorarchivo = new JFileChooser();
        int resultado = selectorarchivo.showOpenDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File fichero = selectorarchivo.getSelectedFile();
            nombrearchivo.setText(fichero.getPath());
        }
    }//GEN-LAST:event_botonarchivoActionPerformed

    private void botonresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonresetActionPerformed
        apiservidor.resetearSistema();
        algoritmoa.setEnabled(true);
        sizehash.setEnabled(true);
        sizemsg.setEnabled(true);
        botonaplicar.setEnabled(true);
        botonreset.setEnabled(false);
        botonapagar.setEnabled(false);
        botonarrancar.setEnabled(false);
        botonparar.setEnabled(false);
    }//GEN-LAST:event_botonresetActionPerformed

    private void etextoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_etextoActionPerformed
        entradateclado.setEnabled(true);
        botonarchivo.setEnabled(false);
        nombrearchivo.setEnabled(false);
    }//GEN-LAST:event_etextoActionPerformed

    private void earchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_earchivoActionPerformed
        entradateclado.setEnabled(false);
        botonarchivo.setEnabled(true);
        nombrearchivo.setEnabled(true);
    }//GEN-LAST:event_earchivoActionPerformed

    private void ehexadecimalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ehexadecimalActionPerformed
        entradateclado.setEnabled(true);
        botonarchivo.setEnabled(false);
        nombrearchivo.setEnabled(false);
    }//GEN-LAST:event_ehexadecimalActionPerformed

    private void botonprocesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonprocesarActionPerformed
        new procesaHash().start();
    }//GEN-LAST:event_botonprocesarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        acercade.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void botonaplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonaplicarActionPerformed
        apiservidor.setAlgoritmo((String) algoritmoa.getSelectedItem(), (Integer) sizemsg.getValue(), (Integer) sizehash.getValue());
        botonreset.setEnabled(false);
        botonapagar.setEnabled(false);
        botonarrancar.setEnabled(true);
        botonparar.setEnabled(false);
    }//GEN-LAST:event_botonaplicarActionPerformed

    private void botonarrancarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonarrancarActionPerformed
        apiservidor.arrancaAtaque();
        algoritmoa.setEnabled(false);
        sizehash.setEnabled(false);
        sizemsg.setEnabled(false);
        botonaplicar.setEnabled(false);
        botonreset.setEnabled(false);
        botonapagar.setEnabled(false);
        botonarrancar.setEnabled(false);
        botonparar.setEnabled(true);
    }//GEN-LAST:event_botonarrancarActionPerformed

    private void botonpararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonpararActionPerformed
        apiservidor.paraAtaque();
        botonreset.setEnabled(true);
        botonapagar.setEnabled(true);
        botonarrancar.setEnabled(true);
        botonparar.setEnabled(false);
    }//GEN-LAST:event_botonpararActionPerformed

    private void botonapagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonapagarActionPerformed
        apiservidor.desconectaClientes();
        algoritmoa.setEnabled(true);
        sizehash.setEnabled(true);
        sizemsg.setEnabled(true);
        botonaplicar.setEnabled(true);
        botonreset.setEnabled(false);
        botonapagar.setEnabled(false);
        botonarrancar.setEnabled(false);
        botonparar.setEnabled(false);
    }//GEN-LAST:event_botonapagarActionPerformed

    private void botoninformeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botoninformeActionPerformed
        informecolisiones.setText(apiservidor.getInformeColisiones());
    }//GEN-LAST:event_botoninformeActionPerformed

    private void guardarSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarSActionPerformed
        this.guardarenArchivo(informeseguimiento);
    }//GEN-LAST:event_guardarSActionPerformed

    private void guardarAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarAActionPerformed
        this.guardarenArchivo(informecolisiones);
    }//GEN-LAST:event_guardarAActionPerformed

    private void imprimirAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimirAActionPerformed
        try {
            informecolisiones.print();
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, "Error al imprimir: " + ex.getMessage(), "Imprimiendo informe", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_imprimirAActionPerformed

    private void imprimirSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimirSActionPerformed
        try {
            informeseguimiento.print();
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, "Error al imprimir: " + ex.getMessage(), "Imprimiendo informe", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_imprimirSActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog acercade;
    private javax.swing.JComboBox<String> algoritmoa;
    private javax.swing.JComboBox<String> algoritmoi;
    private javax.swing.JButton botonapagar;
    private javax.swing.JButton botonaplicar;
    private javax.swing.JButton botonarchivo;
    private javax.swing.JButton botonarrancar;
    private javax.swing.JButton botoninforme;
    private javax.swing.JButton botonparar;
    private javax.swing.JButton botonprocesar;
    private javax.swing.JButton botonreset;
    private javax.swing.JRadioButton earchivo;
    private javax.swing.JRadioButton ehexadecimal;
    private javax.swing.JTextArea entradateclado;
    private javax.swing.JRadioButton etexto;
    private javax.swing.JButton guardarA;
    private javax.swing.JButton guardarS;
    private javax.swing.JButton imprimirA;
    private javax.swing.JButton imprimirS;
    private javax.swing.JLabel infoconexion;
    private javax.swing.JRadioButton informebloque;
    private javax.swing.JTextArea informecolisiones;
    private javax.swing.JRadioButton informeno;
    private javax.swing.JRadioButton informepaso;
    private javax.swing.JTextArea informeseguimiento;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.ButtonGroup modoinforme;
    private javax.swing.JTextField nclientes;
    private javax.swing.JTextField ncolisiones;
    private javax.swing.JTextField nhashes;
    private javax.swing.JTextField nhashseg;
    private javax.swing.JTextField nombrearchivo;
    private javax.swing.JTextField npseudocolisiones;
    private javax.swing.JPanel panelAtaque;
    private javax.swing.JPanel panelIzquierdoA;
    private javax.swing.JPanel panelSeguimiento;
    private javax.swing.JPanel panelconfiguracion;
    private javax.swing.JPanel panelcontrol;
    private javax.swing.JPanel panelderechoA;
    private javax.swing.JPanel panelderechoS;
    private javax.swing.JPanel panelentrada;
    private javax.swing.JPanel panelizquierdoS;
    private javax.swing.JPanel panelmonitorizacion;
    private javax.swing.JPanel panelopciones;
    private javax.swing.JPanel panelsalida;
    private javax.swing.JTextField salidahash;
    private javax.swing.JScrollPane scrollpanelAtaque;
    private javax.swing.JScrollPane scrollpanelSeguimiento;
    private javax.swing.JSpinner sizehash;
    private javax.swing.JSpinner sizemsg;
    private javax.swing.JLabel textoinformativo;
    private javax.swing.JLabel tiempo;
    private javax.swing.ButtonGroup tipoentrada;
    // End of variables declaration//GEN-END:variables
}
