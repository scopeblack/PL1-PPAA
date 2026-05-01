/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alejandro
 */
public class Demogorgon extends Thread {
    private int id;
    private String identificador;
    private Hawkins hawkins;
    private UpsideDown upsideDown;
    private int capturas = 0;
    private Semaphore paralizado= new Semaphore(1);
    public Demogorgon(int id, Hawkins h, UpsideDown u){
        this.id=id;
        this.identificador= "D"+id;
        this.hawkins = h;
        this.upsideDown =  u;
    }
    
    public String getIdentificador(){
        return identificador;
    }
    
    public void run(){
    String[] zonasUpsideDown = {"bosque", "alcantarillado", "laboratorio", "centroComercial"};
    int i = (int)(4*Math.random());
    while(true){
        try{
            String zonaNombre = zonasUpsideDown[i];
            ZonaUpsideDown zona = upsideDown.getZona(zonaNombre);
            
            // Dentro del run del Demogorgon
            Nino niño = zona.elegir(); 
            if(niño != null) {
                double tAtaque = 500 + 1000 * Math.random();
                niño.setTiempo(tAtaque);
                niño.interrupt(); 

                sleep((long)tAtaque); 

                boolean exito = (Math.random() <= 1.0/3.0);
                if(exito) {
                    niño.setCapturado();
                    upsideDown.enviarNiñoColmena(niño);
                } else {
                    // AQUÍ ESTABA EL ERROR:
                    // Solo lo devolvemos si el niño no ha salido ya por el finally
                    zona.devolverSiNoCapturado(niño, false);
                }
            }
            i = (int)(4*Math.random());
        } catch(InterruptedException e) {
            // Manejo de parálisis (Eleven)
            try {
                paralizado.acquire(2);
            } catch (InterruptedException ex) {}
            finally { paralizado.release(1); }
        }
    }
}
    
}
