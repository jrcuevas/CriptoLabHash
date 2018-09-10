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
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        modoinforme = new javax.swing.ButtonGroup();
        tipoentrada = new javax.swing.ButtonGroup();
        entradaerronea = new javax.swing.JDialog();
        jButton1 = new javax.swing.JButton();
        textoinformativo = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        informeseguimiento = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        algoritmoi = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        informeno = new javax.swing.JRadioButton();
        informebloque = new javax.swing.JRadioButton();
        informepaso = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        botonarchivo = new javax.swing.JButton();
        nombrearchivo = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        entradateclado = new javax.swing.JTextArea();
        earchivo = new javax.swing.JRadioButton();
        etexto = new javax.swing.JRadioButton();
        ehexadecimal = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        salidahash = new javax.swing.JTextField();
        botonprocesar = new javax.swing.JButton();
        tiempo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        informecolisiones = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        sizemsg = new javax.swing.JSpinner();
        sizehash = new javax.swing.JSpinner();
        algoritmoa = new javax.swing.JComboBox<>();
        botonaplicar = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        nclientes = new javax.swing.JTextField();
        botonapagar = new javax.swing.JButton();
        botonreset = new javax.swing.JButton();
        botonarrancar = new javax.swing.JButton();
        botonparar = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
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
        infoconexion = new javax.swing.JLabel();

        entradaerronea.setTitle("Entrada");

        jButton1.setText("Aceptar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout entradaerroneaLayout = new javax.swing.GroupLayout(entradaerronea.getContentPane());
        entradaerronea.getContentPane().setLayout(entradaerroneaLayout);
        entradaerroneaLayout.setHorizontalGroup(
            entradaerroneaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textoinformativo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(entradaerroneaLayout.createSequentialGroup()
                .addGap(145, 145, 145)
                .addComponent(jButton1)
                .addContainerGap(163, Short.MAX_VALUE))
        );
        entradaerroneaLayout.setVerticalGroup(
            entradaerroneaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, entradaerroneaLayout.createSequentialGroup()
                .addComponent(textoinformativo, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        informeseguimiento.setEditable(false);
        informeseguimiento.setColumns(20);
        informeseguimiento.setFont(new java.awt.Font("Courier", 0, 14)); // NOI18N
        informeseguimiento.setRows(5);
        informeseguimiento.setToolTipText("Panel donde aparecerá el informe.");
        jScrollPane2.setViewportView(informeseguimiento);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Opciones del algoritmo"));

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(algoritmoi, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(informepaso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(informebloque, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(informeno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(algoritmoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(informeno)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(informebloque)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(informepaso)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Entrada"));

        botonarchivo.setText("Archivo");
        botonarchivo.setToolTipText("Selecciona el archivo que se utilizará para calcular el hash.");
        botonarchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonarchivoActionPerformed(evt);
            }
        });

        nombrearchivo.setEditable(false);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Entrada por teclado"));

        entradateclado.setColumns(20);
        entradateclado.setRows(5);
        entradateclado.setToolTipText("El texto que aquí se introduzca se utilizará para el cálculo del hash.");
        entradateclado.setEnabled(false);
        jScrollPane3.setViewportView(entradateclado);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(botonarchivo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombrearchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(earchivo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(etexto)
                        .addGap(34, 34, 34)
                        .addComponent(ehexadecimal)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(earchivo)
                    .addComponent(etexto)
                    .addComponent(ehexadecimal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonarchivo)
                    .addComponent(nombrearchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Salida"));

        salidahash.setEditable(false);
        salidahash.setToolTipText("Número hash calculado a partir de la información disponible en la entrada.");

        botonprocesar.setText("Procesar");
        botonprocesar.setToolTipText("Calcular el hash con los valores anteriormente introducidos.");
        botonprocesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonprocesarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(salidahash, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(botonprocesar)
                        .addGap(18, 18, 18)
                        .addComponent(tiempo, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(salidahash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonprocesar)
                    .addComponent(tiempo)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Calculo y seguimiento de las funciones Hash", jPanel1);

        informecolisiones.setEditable(false);
        informecolisiones.setColumns(20);
        informecolisiones.setFont(new java.awt.Font("Courier", 0, 14)); // NOI18N
        informecolisiones.setRows(5);
        informecolisiones.setToolTipText("Panel donde aparecerá el informe.");
        jScrollPane1.setViewportView(informecolisiones);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración del ataque"));

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

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(algoritmoa, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sizehash, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(sizemsg, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(botonaplicar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(algoritmoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sizehash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sizemsg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonaplicar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Control de ejecución"));

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

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nclientes, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botonapagar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonreset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botonparar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonarrancar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(35, 35, 35))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(nclientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonreset)
                    .addComponent(botonarrancar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonapagar)
                    .addComponent(botonparar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Monitorización del proceso"));
        jPanel9.setToolTipText("");

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

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(infoconexion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nhashes))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ncolisiones)
                                    .addComponent(npseudocolisiones)))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nhashseg))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botoninforme)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhashes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(nhashseg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(ncolisiones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(npseudocolisiones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(botoninforme))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(infoconexion)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Ataques a las funciones hash", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
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
        entradaerronea.dispose();
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private javax.swing.JDialog entradaerronea;
    private javax.swing.JTextArea entradateclado;
    private javax.swing.JRadioButton etexto;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.ButtonGroup modoinforme;
    private javax.swing.JTextField nclientes;
    private javax.swing.JTextField ncolisiones;
    private javax.swing.JTextField nhashes;
    private javax.swing.JTextField nhashseg;
    private javax.swing.JTextField nombrearchivo;
    private javax.swing.JTextField npseudocolisiones;
    private javax.swing.JTextField salidahash;
    private javax.swing.JSpinner sizehash;
    private javax.swing.JSpinner sizemsg;
    private javax.swing.JLabel textoinformativo;
    private javax.swing.JLabel tiempo;
    private javax.swing.ButtonGroup tipoentrada;
    // End of variables declaration//GEN-END:variables
}
