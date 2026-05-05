/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pl1;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.rmi.RemoteException;
import java.util.logging.Logger;

public class PL1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            System.out.println("Error de codificación");
        }
        // TODO code application logic here
        
        // Arrays que tendrán los ids de los niños que hay en cada zona
        List<Nino> niñosBosque = new CopyOnWriteArrayList<>();
        List<Nino> niñosAlcantarillado = new CopyOnWriteArrayList<>();
        List<Nino> niñosLaboratorio = new CopyOnWriteArrayList<>();
        List<Nino> niñosCentroComercial = new CopyOnWriteArrayList<>();
        
        List<Demogorgon> demosBosque = new CopyOnWriteArrayList<>();
        List<Demogorgon> demosAlcantarillado = new CopyOnWriteArrayList<>();
        List<Demogorgon> demosLaboratorio = new CopyOnWriteArrayList<>();
        List<Demogorgon> demosCentroComercial = new CopyOnWriteArrayList<>();

        Portal portalBosque = new Portal(2, "Bosque", niñosBosque);
        Portal portalCentroComercial = new Portal(4, "Centro Comercial", niñosCentroComercial);
        Portal portalAlcantarillado = new Portal(2, "Alcantarillado", niñosAlcantarillado);
        Portal portalLaboratorio = new Portal(3, "Laboratorio", niñosLaboratorio);
        CallePrincipal callePrincipal= new CallePrincipal();
        RadioWSQK radioWSQK= new RadioWSQK();
        // CallePrincipal sotanoByers= new CallePrincipal();
        Sotano sotanoByers = new Sotano(portalBosque, portalAlcantarillado, portalCentroComercial, portalLaboratorio);
        Hawkins hawkins = new Hawkins(callePrincipal, radioWSQK, sotanoByers);
        

        
        
        ZonaUpsideDown bosque = new ZonaUpsideDown(portalBosque, niñosBosque, demosBosque);
        ZonaUpsideDown laboratorio = new ZonaUpsideDown(portalLaboratorio, niñosLaboratorio, demosLaboratorio);
        ZonaUpsideDown centroComercial = new ZonaUpsideDown(portalCentroComercial, niñosCentroComercial, demosCentroComercial);
        ZonaUpsideDown alcantarillado = new ZonaUpsideDown(portalAlcantarillado, niñosAlcantarillado, demosAlcantarillado);
        
        Colmena colmena = new Colmena();
        
        UpsideDown upsideDown = new UpsideDown(bosque, laboratorio, centroComercial, alcantarillado, colmena);
        new Vecna(hawkins, upsideDown).start();

        GestorEventos gestor = new GestorEventos(hawkins, upsideDown);
        gestor.start();
        
        //GestorPausa gestorPausa = new GestorPausa(hawkins, upsideDown, gestor);
        
        //try {
            GestorPausa gestorPausa = new GestorPausa(hawkins, upsideDown, gestor);
            java.rmi.registry.LocateRegistry.createRegistry(1099); // Inicia el registro en el puerto 1099
            java.rmi.Naming.rebind("//localhost/GestorRemoto", gestorPausa);
            System.out.println("Servidor RMI listo.");
            
        // catch (Exception e) {
        //    e.printStackTrace();
        //}
        
        AtomicInteger contadorNiños = new AtomicInteger(0);

        
        Interfaz interfaz = new Interfaz();

        interfaz.setInicial(hawkins, upsideDown, gestor, contadorNiños, gestorPausa);
        interfaz.setVisible(true);


        new Thread(() -> {
            while(true) {
                interfaz.actualizarDatos();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    // break; 
                }
            }
        }).start();
        

        try{
            upsideDown.crearAlpha(hawkins);
            for(int i=0; i<1500; i++){
                Nino n = new Nino(i+1, hawkins, upsideDown);
                hawkins.almacenarNino(n);
                n.start();
                contadorNiños.incrementAndGet();
                Thread.sleep(500 + (int)(Math.random()*1500));
            }
        }
        catch(InterruptedException ex){
            ex.printStackTrace();
        }

        
    }
    
}
