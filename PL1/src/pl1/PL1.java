/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pl1;

import java.util.ArrayList;

public class PL1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            System.out.println("Error de codificación");
        }
        // TODO code application logic here
        
        // Arrays que tendrán los ids de los niños que hay en cada zona
        ArrayList<Nino> niñosBosque = new ArrayList<>();
        ArrayList<Nino> niñosAlcantarillado = new ArrayList<>();
        ArrayList<Nino> niñosLaboratorio = new ArrayList<>();
        ArrayList<Nino> niñosCentroComercial = new ArrayList<>();

        Portal portalBosque = new Portal(2, "Bosque", niñosBosque);
        Portal portalCentroComercial = new Portal(4, "Centro Comercial", niñosCentroComercial);
        Portal portalAlcantarillado = new Portal(2, "Alcantarillado", niñosAlcantarillado);
        Portal portalLaboratorio = new Portal(3, "Laboratorio", niñosLaboratorio);
        CallePrincipal callePrincipal= new CallePrincipal();
        RadioWSQK radioWSQK= new RadioWSQK();
        // CallePrincipal sotanoByers= new CallePrincipal();
        Sotano sotanoByers = new Sotano(portalBosque, portalAlcantarillado, portalCentroComercial, portalLaboratorio);
        Hawkins hawkins = new Hawkins(callePrincipal, radioWSQK, sotanoByers);
        

        
        
        ZonaUpsideDown bosque = new ZonaUpsideDown(portalBosque, niñosBosque);
        ZonaUpsideDown laboratorio = new ZonaUpsideDown(portalLaboratorio, niñosLaboratorio);
        ZonaUpsideDown centroComercial = new ZonaUpsideDown(portalCentroComercial, niñosCentroComercial);
        ZonaUpsideDown alcantarillado = new ZonaUpsideDown(portalAlcantarillado, niñosAlcantarillado);
        
        Colmena colmena = new Colmena();
        
        UpsideDown upsideDown = new UpsideDown(bosque, laboratorio, centroComercial, alcantarillado, colmena);
        new Vecna(hawkins, upsideDown).start();

        GestorEventos gestor = new GestorEventos(hawkins, upsideDown);
        gestor.start();
        new Demogorgon(0, hawkins, upsideDown).start();

        try{
            for(int i=0; i<1500; i++){

                new Nino(i, hawkins, upsideDown).start();
                Thread.sleep(500 + (int)(Math.random()*1500));
            }
        }
        catch(InterruptedException ex){
            ex.printStackTrace();
        }

        
    }
    
}
