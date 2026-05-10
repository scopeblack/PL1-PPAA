/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pl1;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.DefaultListModel;

/**
 * Interfaz gráfica local del sistema (Parte 1). Muestra en tiempo real el
 * estado de todas las zonas, portales, niños, demogorgons y el evento activo.
 * La actualización se realiza desde un hilo daemon en PL1.main() que llama
 * a actualizarDatos() cada 100 ms mediante SwingUtilities.invokeLater().
 *
 * @author daniel
 */
public class Interfaz extends javax.swing.JFrame {

    /**
     * Creates new form Interfaz
     */
    private Hawkins hawkins;
    private UpsideDown upsideDown;
    private GestorEventos gestor;
    private AtomicInteger contadorNiños;
    public Interfaz() {
        initComponents();
        this.getContentPane().setBackground(new java.awt.Color(15, 15, 15));
    
        // Cambiar fuente de los títulos grandes a algo más "retro"
        java.awt.Font retroFont = new java.awt.Font("Courier New", java.awt.Font.BOLD, 36);
        jLabel1.setFont(retroFont); jLabel1.setForeground(java.awt.Color.RED);
        jLabel2.setFont(retroFont); jLabel2.setForeground(new java.awt.Color(154, 48, 255));
        jLabel3.setFont(retroFont); jLabel3.setForeground(new java.awt.Color(255, 102, 0)); // Rojo oscuro
        jLabel4.setFont(retroFont); jLabel4.setForeground(new java.awt.Color(57, 255, 20)); // Rojo oscuro
        sangre.setForeground(java.awt.Color.RED); // Rojo oscuro

        // Blanco sucio / grisáceo
        java.awt.Color labelColor = new java.awt.Color(200, 200, 200);
        jLabel5.setForeground(labelColor);   // CALLE PRINCIPAL
        jLabel6.setForeground(labelColor);   // SOTANO BYERS
        jLabel7.setForeground(labelColor);   // RADIO WSQK
        jLabel8.setForeground(labelColor);   // PORTAL BOSQUE
        jLabel9.setForeground(labelColor);   // PORTAL LABORATORIO
        jLabel10.setForeground(labelColor);  // PORTAL CENTRO COMERCIAL
        jLabel11.setForeground(labelColor);  // PORTAL ALCANTARILLADO
        jLabel12.setForeground(labelColor);  // CENTRO COMERCIAL
        jLabel13.setForeground(labelColor);  // LABORATORIO
        jLabel14.setForeground(labelColor);  // ALCANTARILLADO
        jLabel15.setForeground(labelColor);  // BOSQUE
        jLabel16.setForeground(labelColor);  // NIÑOS
        jLabel19.setForeground(labelColor);  // DEMOGORGONS
        jLabel20.setForeground(labelColor);  // DEMOGORGONS
        jLabel21.setForeground(labelColor);  // DEMOGORGONS

        // Estos dos aparte por ser especiales
        jLabel17.setForeground(java.awt.Color.RED);                        // SANGRE
        jLabel18.setForeground(new java.awt.Color(255, 200, 0));           // EVENTO EN CURSO
        demogorgons.setForeground(java.awt.Color.RED);                        // Contador de Demogorgons
        cantidadNiñosColmena.setForeground(java.awt.Color.RED);                        // Contador de Demogorgons

        // Hawkins
        callePrincipal.setModel(new DefaultListModel());
        sotanoByers.setModel(new DefaultListModel());
        radioWSQK.setModel(new DefaultListModel());

        // Upside Down - Niños
        bosque.setModel(new DefaultListModel());
        laboratorio.setModel(new DefaultListModel());
        alcantarillado.setModel(new DefaultListModel());
        centroComercial.setModel(new DefaultListModel());

        // Upside Down - Demos
        demosBosque.setModel(new DefaultListModel());
        demosLaboratorio.setModel(new DefaultListModel());
        alcantarilladoDemos.setModel(new DefaultListModel());
        centroComercialDemos.setModel(new DefaultListModel());

        // Portales y Colmena
        niñosColmena.setModel(new DefaultListModel());
        portalBosque.setModel(new DefaultListModel());
        portalLaboratorio.setModel(new DefaultListModel());
        portalAlcantarillado.setModel(new DefaultListModel());
        portalCentroComercial.setModel(new DefaultListModel());
        
        esperandoBosque.setModel(new DefaultListModel());
        esperandoLaboratorio.setModel(new DefaultListModel());
        esperandoAlcantarillado.setModel(new DefaultListModel());
        esperandoCentroComercial.setModel(new DefaultListModel());
        
        // Colores temáticos
        java.awt.Color fondoOscuro = new java.awt.Color(15, 15, 15);
        java.awt.Color verdeNeon = new java.awt.Color(57, 255, 20);
        java.awt.Color rojoSangre = new java.awt.Color(200, 0, 0);
        java.awt.Font retroFont2 = new java.awt.Font("Courier New", java.awt.Font.PLAIN, 13);

        // JLists de NIÑOS (verde neón)
        javax.swing.JList[] listasNiños = {
            callePrincipal, sotanoByers, radioWSQK,
            bosque, laboratorio, alcantarillado, centroComercial,
            niñosColmena, portalBosque, portalLaboratorio,
            portalAlcantarillado, portalCentroComercial,
            esperandoBosque, esperandoLaboratorio,
            esperandoAlcantarillado, esperandoCentroComercial
        };

        for (javax.swing.JList lista : listasNiños) {
            lista.setBackground(fondoOscuro);
            lista.setForeground(verdeNeon);
            lista.setFont(retroFont2);
            lista.setSelectionBackground(new java.awt.Color(30, 80, 30));
            lista.setSelectionForeground(verdeNeon);
        }

        // JLists de DEMOGORGONS (rojo sangre)
        javax.swing.JList[] listasDemos = {
            demosBosque, demosLaboratorio,
            alcantarilladoDemos, centroComercialDemos
        };

        for (javax.swing.JList lista : listasDemos) {
            lista.setBackground(fondoOscuro);
            lista.setForeground(rojoSangre);
            lista.setFont(retroFont2);
            lista.setSelectionBackground(new java.awt.Color(80, 10, 10));
            lista.setSelectionForeground(rojoSangre);
        }
    }
    
    /**
     * Introduce las referencias del modelo tras la construcción del JFrame.
     * Se separa del constructor porque NetBeans genera initComponents() primero
     * y las referencias del modelo aún no existen en ese momento.
     * a es un contador atómico de niños creados hasta ahora (actualizado por PL1.main).
     */
    public void setInicial(Hawkins h, UpsideDown u, GestorEventos g, AtomicInteger a){
        this.hawkins = h;
        this.upsideDown = u;
        this.gestor = g;
        this.contadorNiños = a;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        sangre = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        evento = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        demogorgons = new javax.swing.JLabel();
        cantidadNiñosColmena = new javax.swing.JLabel();
        niños = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        ultimoEventoActivo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        callePrincipal = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        sotanoByers = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        radioWSQK = new javax.swing.JList<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        portalBosque = new javax.swing.JList<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        portalLaboratorio = new javax.swing.JList<>();
        jScrollPane6 = new javax.swing.JScrollPane();
        portalAlcantarillado = new javax.swing.JList<>();
        jScrollPane7 = new javax.swing.JScrollPane();
        portalCentroComercial = new javax.swing.JList<>();
        jScrollPane8 = new javax.swing.JScrollPane();
        bosque = new javax.swing.JList<>();
        jScrollPane9 = new javax.swing.JScrollPane();
        demosBosque = new javax.swing.JList<>();
        jScrollPane10 = new javax.swing.JScrollPane();
        laboratorio = new javax.swing.JList<>();
        jScrollPane11 = new javax.swing.JScrollPane();
        demosLaboratorio = new javax.swing.JList<>();
        jScrollPane12 = new javax.swing.JScrollPane();
        alcantarillado = new javax.swing.JList<>();
        jScrollPane13 = new javax.swing.JScrollPane();
        alcantarilladoDemos = new javax.swing.JList<>();
        jScrollPane14 = new javax.swing.JScrollPane();
        centroComercial = new javax.swing.JList<>();
        jScrollPane15 = new javax.swing.JScrollPane();
        centroComercialDemos = new javax.swing.JList<>();
        jScrollPane16 = new javax.swing.JScrollPane();
        niñosColmena = new javax.swing.JList<>();
        jScrollPane17 = new javax.swing.JScrollPane();
        esperandoBosque = new javax.swing.JList<>();
        jScrollPane18 = new javax.swing.JScrollPane();
        esperandoLaboratorio = new javax.swing.JList<>();
        jScrollPane19 = new javax.swing.JScrollPane();
        esperandoAlcantarillado = new javax.swing.JList<>();
        jScrollPane20 = new javax.swing.JScrollPane();
        esperandoCentroComercial = new javax.swing.JList<>();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        jLabel1.setText("HAWKINS");

        jLabel2.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        jLabel2.setText("PORTALES");

        jLabel3.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        jLabel3.setText("UPSIDE DOWN");

        jLabel4.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        jLabel4.setText("COLMENA");

        jLabel5.setText("CALLE PRINCIPAL");

        jLabel6.setText("SOTANO BYERS");

        jLabel7.setText("RADIO WSQK");

        jLabel8.setText("PORTAL BOSQUE");

        jLabel9.setText("PORTAL LABORATORIO");

        jLabel10.setText("PORTAL CENTRO COMERCIAL");

        jLabel11.setText("PORTAL ALCANTARILLADO");

        jLabel12.setText("CENTRO COMERCIAL");

        jLabel13.setText("LABORATORIO");

        jLabel14.setText("ALCANTARILLADO");

        jLabel15.setText("BOSQUE");

        jLabel16.setText("NIÑOS");

        jLabel17.setText("SANGRE");

        sangre.setText("jLabel18");

        jLabel18.setText("EVENTO EN CURSO");
        jLabel18.setPreferredSize(new java.awt.Dimension(150, 40));

        evento.setText("jTextField1");

        jLabel19.setText("DEMOGORGONS");

        demogorgons.setText("jLabel20");

        cantidadNiñosColmena.setText("jLabel20");

        niños.setText("jLabel20");

        jLabel20.setText("NIÑOS");

        jLabel21.setText("ULTIMO EVENTO");

        ultimoEventoActivo.setText("jTextField1");
        ultimoEventoActivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ultimoEventoActivoActionPerformed(evt);
            }
        });

        callePrincipal.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(callePrincipal);

        sotanoByers.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(sotanoByers);

        radioWSQK.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(radioWSQK);

        portalBosque.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(portalBosque);

        portalLaboratorio.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(portalLaboratorio);

        portalAlcantarillado.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane6.setViewportView(portalAlcantarillado);

        portalCentroComercial.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane7.setViewportView(portalCentroComercial);

        bosque.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane8.setViewportView(bosque);

        demosBosque.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane9.setViewportView(demosBosque);

        laboratorio.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane10.setViewportView(laboratorio);

        demosLaboratorio.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane11.setViewportView(demosLaboratorio);

        alcantarillado.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane12.setViewportView(alcantarillado);

        alcantarilladoDemos.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane13.setViewportView(alcantarilladoDemos);

        centroComercial.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane14.setViewportView(centroComercial);

        centroComercialDemos.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane15.setViewportView(centroComercialDemos);

        niñosColmena.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane16.setViewportView(niñosColmena);

        esperandoBosque.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane17.setViewportView(esperandoBosque);

        esperandoLaboratorio.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane18.setViewportView(esperandoLaboratorio);

        esperandoAlcantarillado.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane19.setViewportView(esperandoAlcantarillado);

        esperandoCentroComercial.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane20.setViewportView(esperandoCentroComercial);

        jLabel22.setText("ESPERANDO");

        jLabel23.setText("FORMANDO");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(42, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(86, 86, 86))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sangre)
                                    .addComponent(jLabel17))
                                .addGap(85, 85, 85)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20)
                                    .addComponent(niños)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(10, 10, 10)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel9)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel22)
                                        .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(57, 57, 57)
                                            .addComponent(jLabel23))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(18, 18, 18)
                                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(121, 121, 121)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel8)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(106, 106, 106)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel15)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel12)
                            .addComponent(jLabel14)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel19)
                    .addComponent(demogorgons)
                    .addComponent(cantidadNiñosColmena)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(ultimoEventoActivo, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(evento, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel8))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(cantidadNiñosColmena)
                        .addGap(82, 82, 82)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(evento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(jLabel21)
                        .addGap(18, 18, 18)
                        .addComponent(ultimoEventoActivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jLabel19)
                        .addGap(3, 3, 3)
                        .addComponent(demogorgons))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(7, 7, 7)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel13)
                                        .addComponent(jLabel9)))
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(22, 22, 22)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(10, 10, 10)))
                            .addComponent(jLabel14)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(66, 66, 66)
                                    .addComponent(jLabel12)
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel10)
                                    .addGap(35, 35, 35)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(11, 11, 11)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel6)
                            .addGap(28, 28, 28)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(73, 73, 73)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addGap(18, 18, 18)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(97, 97, 97)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel17)
                                        .addComponent(jLabel20))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(sangre)
                                        .addComponent(niños)))
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ultimoEventoActivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ultimoEventoActivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ultimoEventoActivoActionPerformed
    
    // Refresca todas las JLists y etiquetas con el estado actual del sistema.
    public void actualizarDatos(){
        actualizarLista(callePrincipal, hawkins.getCallePrincipal().getNiños());
        actualizarLista(sotanoByers, hawkins.getSotanoByers().getNiños());
        actualizarLista(radioWSQK, hawkins.getRadioWSBK().getNiños());
        sangre.setText(hawkins.getRadioWSBK().getSangre() + "");

        // UPSIDE DOWN - Niños
        actualizarLista(bosque, upsideDown.getBosque().getNiños());
        actualizarLista(laboratorio, upsideDown.getLaboratorio().getNiños());
        actualizarLista(alcantarillado, upsideDown.getAlcantarillado().getNiños());
        actualizarLista(centroComercial, upsideDown.getCentroComercial().getNiños());

        // UPSIDE DOWN - Demogorgons
        actualizarLista(demosBosque, upsideDown.getBosque().getDemogorgons());
        actualizarLista(demosLaboratorio, upsideDown.getLaboratorio().getDemogorgons());
        actualizarLista(alcantarilladoDemos, upsideDown.getAlcantarillado().getDemogorgons());
        actualizarLista(centroComercialDemos, upsideDown.getCentroComercial().getDemogorgons());

        // COLMENA y PORTALES
        actualizarLista(niñosColmena, upsideDown.getColmena().getNiños());
        actualizarLista(portalBosque, hawkins.getSotanoByers().getPortalBosque().getNiños());
        actualizarLista(portalLaboratorio, hawkins.getSotanoByers().getPortalLaboratorio().getNiños());
        actualizarLista(portalAlcantarillado, hawkins.getSotanoByers().getPortalAlcantarillado().getNiños());
        actualizarLista(portalCentroComercial, hawkins.getSotanoByers().getPortalCentroComercial().getNiños());
        
        actualizarLista(esperandoBosque, hawkins.getSotanoByers().getPortalBosque().getNiñosEsperando());
        actualizarLista(esperandoLaboratorio, hawkins.getSotanoByers().getPortalLaboratorio().getNiñosEsperando());
        actualizarLista(esperandoAlcantarillado, hawkins.getSotanoByers().getPortalAlcantarillado().getNiñosEsperando());
        actualizarLista(esperandoCentroComercial, hawkins.getSotanoByers().getPortalCentroComercial().getNiñosEsperando());
        evento.setText(gestor.getEvento());
        demogorgons.setText(upsideDown.getContadorDemogorgons() + "");
        cantidadNiñosColmena.setText(upsideDown.getColmena().getCantidadNiños() + "");
        niños.setText(contadorNiños.get() + "");
        ultimoEventoActivo.setText(gestor.getUltimoEventoActivo());
    }
    
    /**
     * Vacía el modelo de la JList dada y lo rellena con los elementos actuales.
     *  lista es el JList de destino.
     *  elementos es la lista del modelo (niños o demogorgons de una zona).
     */
    private void actualizarLista(javax.swing.JList lista, List elementos) {
        DefaultListModel modelo = (DefaultListModel) lista.getModel();
        modelo.clear();
        for (Object elemento : elementos) {
            if (elemento instanceof Nino) {
                modelo.addElement("👦 " + ((Nino) elemento).getIdentificador());
            } else {
                modelo.addElement("👾 " + ((Demogorgon)elemento).getIdentificador());
            }
        }
    }
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
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> alcantarillado;
    private javax.swing.JList<String> alcantarilladoDemos;
    private javax.swing.JList<String> bosque;
    private javax.swing.JList<String> callePrincipal;
    private javax.swing.JLabel cantidadNiñosColmena;
    private javax.swing.JList<String> centroComercial;
    private javax.swing.JList<String> centroComercialDemos;
    private javax.swing.JLabel demogorgons;
    private javax.swing.JList<String> demosBosque;
    private javax.swing.JList<String> demosLaboratorio;
    private javax.swing.JList<String> esperandoAlcantarillado;
    private javax.swing.JList<String> esperandoBosque;
    private javax.swing.JList<String> esperandoCentroComercial;
    private javax.swing.JList<String> esperandoLaboratorio;
    private javax.swing.JTextField evento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JList<String> laboratorio;
    private javax.swing.JLabel niños;
    private javax.swing.JList<String> niñosColmena;
    private javax.swing.JList<String> portalAlcantarillado;
    private javax.swing.JList<String> portalBosque;
    private javax.swing.JList<String> portalCentroComercial;
    private javax.swing.JList<String> portalLaboratorio;
    private javax.swing.JList<String> radioWSQK;
    private javax.swing.JLabel sangre;
    private javax.swing.JList<String> sotanoByers;
    private javax.swing.JTextField ultimoEventoActivo;
    // End of variables declaration//GEN-END:variables
}
