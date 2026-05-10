/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pl1;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Punto de entrada de la Parte 1. Inicializa todos los componentes del sistema
 * en el siguiente orden (el orden importa porque hay dependencias entre objetos):
 *  1. SistemaLog (necesario para todo lo demás)
 *  2. Portales y zonas (ZonaUpsideDown comparte las listas con los portales)
 *  3. Hawkins y UpsideDown (contenedores de zonas)
 *  4. Vecna y GestorEventos (hilos de control global)
 *  5. GestorRemoto + registro RMI (servidor de la Parte 2)
 *  6. Interfaz gráfica + hilo de refresco a 100 ms
 *  7. Creación escalonada de los 1500 niños (0,5–2 s entre cada uno)
 */
public class PL1 {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            System.out.println("Error de codificación");
        }

        //Sistema de Logs (Walkie-talkies)
        SistemaLog logger = new SistemaLog();
        logger.escribirLog("---------------INICIO DE LOG---------------");
        // Arrays que tendrán los ids de los niños que hay en cada zona
        List<Nino> niñosBosque = new CopyOnWriteArrayList<>();
        List<Nino> niñosAlcantarillado = new CopyOnWriteArrayList<>();
        List<Nino> niñosLaboratorio = new CopyOnWriteArrayList<>();
        List<Nino> niñosCentroComercial = new CopyOnWriteArrayList<>();

        List<Demogorgon> demosBosque = new CopyOnWriteArrayList<>();
        List<Demogorgon> demosAlcantarillado = new CopyOnWriteArrayList<>();
        List<Demogorgon> demosLaboratorio = new CopyOnWriteArrayList<>();
        List<Demogorgon> demosCentroComercial = new CopyOnWriteArrayList<>();

        Portal portalBosque = new Portal(2, "Bosque", niñosBosque, logger);
        Portal portalCentroComercial = new Portal(4, "Centro Comercial", niñosCentroComercial, logger);
        Portal portalAlcantarillado = new Portal(2, "Alcantarillado", niñosAlcantarillado, logger);
        Portal portalLaboratorio = new Portal(3, "Laboratorio", niñosLaboratorio, logger);
        CallePrincipal callePrincipal = new CallePrincipal(logger);
        RadioWSQK radioWSQK = new RadioWSQK(logger);
        Sotano sotanoByers = new Sotano(logger, portalBosque, portalAlcantarillado, portalCentroComercial, portalLaboratorio);
        Hawkins hawkins = new Hawkins(callePrincipal, radioWSQK, sotanoByers);

        ZonaUpsideDown bosque = new ZonaUpsideDown(portalBosque, niñosBosque, demosBosque);
        ZonaUpsideDown laboratorio = new ZonaUpsideDown(portalLaboratorio, niñosLaboratorio, demosLaboratorio);
        ZonaUpsideDown centroComercial = new ZonaUpsideDown(portalCentroComercial, niñosCentroComercial, demosCentroComercial);
        ZonaUpsideDown alcantarillado = new ZonaUpsideDown(portalAlcantarillado, niñosAlcantarillado, demosAlcantarillado);

        Colmena colmena = new Colmena(logger);

        UpsideDown upsideDown = new UpsideDown(bosque, laboratorio, centroComercial, alcantarillado, colmena, logger);
        new Vecna(hawkins, upsideDown).start();

        GestorEventos gestor = new GestorEventos(hawkins, upsideDown, logger);
        gestor.start();

        
        GestorRemoto gestorRemoto = new GestorRemoto(hawkins, upsideDown, gestor, logger);
        java.rmi.registry.LocateRegistry.createRegistry(1099); // Inicia el registro en el puerto 1099
        java.rmi.Naming.rebind("//localhost/GestorRemoto", gestorRemoto);
        System.out.println("Servidor RMI listo.");

        
        AtomicInteger contadorNiños = new AtomicInteger(0);

        Interfaz interfaz = new Interfaz();

        interfaz.setInicial(hawkins, upsideDown, gestor, contadorNiños);
        interfaz.setVisible(true);

        // Hilo de refresco de la Interfaz: corre independientemente de los hilos
        // del modelo para no bloquear la simulación.
        new Thread(() -> {
            while (true) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    interfaz.actualizarDatos();
                });

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        try {
            // El Demogorgon Alpha (D0000) se crea antes de los niños
            upsideDown.crearAlpha(hawkins);
            // Los niños se crean de forma escalonada (0,5 a 2 s entre cada uno)
            // para simular la incorporación al sistema.
            for (int i = 0; i < 1500; i++) {
                Nino n = new Nino(i + 1, hawkins, upsideDown, logger);
                hawkins.almacenarNino(n);
                n.start();
                contadorNiños.incrementAndGet();
                Thread.sleep(500 + (int) (Math.random() * 1500));
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }

}
