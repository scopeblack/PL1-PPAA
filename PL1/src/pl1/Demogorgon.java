/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
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
    private Semaphore paralizado= new Semaphore(1); // Evento Intervención de Eleven
    private AtomicBoolean paralizadoPortales = new AtomicBoolean(false); // Evento Apagón del Laboratorio
    private AtomicBoolean tormenta = new AtomicBoolean(false); // Evento Tormenta del Upside Down
    private AtomicBoolean conexionMindFlayer = new AtomicBoolean(false); // Evento La Red Mental

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
            String zonaNombre = "";
            ZonaUpsideDown zona;
            if(conexionMindFlayer.get()){ // Evento del MindFlayer. Se calcula el máximo de niños de las 4 zonas
                String z1 = "";
                int max1 = 0;
                String z2 = "";
                int max2 = 0;
                HashMap<String, Integer> cantidades = new HashMap<>();
                if(upsideDown.getBosque().getNiños().size() > upsideDown.getAlcantarillado().getNiños().size()){
                    z1 = "bosque";
                    max1 = upsideDown.getBosque().getNiños().size();
                }else{
                    z1 = "alcantarillado";
                    max1 = upsideDown.getAlcantarillado().getNiños().size();
                }
                cantidades.put(z1, max1);
                if(upsideDown.getCentroComercial().getNiños().size() > upsideDown.getLaboratorio().getNiños().size()){
                    z2 = "centroComercial";
                }else{
                    z2 = "laboratorio";
                }
                cantidades.put(z2, max2);
                if(cantidades.get(z1) > cantidades.get(z2)){
                    zonaNombre = z1;
                }else{
                    zonaNombre = z2;
                }

                zona = upsideDown.getZona(zonaNombre);

            }else{
                zonaNombre = zonasUpsideDown[i];
                zona = upsideDown.getZona(zonaNombre);
            }
            // Dentro del run del Demogorgon
            Nino niño = zona.elegir(); 
            if(niño != null) {
                double tAtaque = 500 + 1000 * Math.random();
                if(tormenta.get()){ // Si el evento tormenta del Upside Down está activo
                    tAtaque = tAtaque / 2; // Para simular que el tiempo entre ataques se reduce a la mitad
                }
                niño.setTiempo(tAtaque);
                niño.interrupt(); 
                
                boolean exito = (Math.random() <= 1.0/3.0);
                if(exito) niño.setCapturado();  //Determinamos la captura antes del sleep para que no haya condiciones de carrera.
                
                sleep((long)tAtaque); 


                if(exito) {
                    System.out.println("----------------------------" + identificador + ": Ha capturado a " + niño.getIdentificador() + 
                            " en: " + zonaNombre + "----------------------------");
                    
                    //niño.setCapturado();
                    upsideDown.enviarNiñoColmena(niño);
                    capturas++;
                } else {
                    // AQUÍ ESTABA EL ERROR:
                    // Solo lo devolvemos si el niño no ha salido ya por el finally
                    System.out.println("----------------------------" + identificador + ": Ha fallado al capturar a " + niño.getIdentificador() + 
                            " en: " + zonaNombre + "----------------------------");
                    
                    zona.devolverSiNoCapturado(niño, false);    //El demogorgon deja de perseguirlo
                }
            }
            if(!paralizadoPortales.get()){ // Si el evento de apagón del laboratorio está activo, se salta el cambio de zona y permanece en ella
                i = (int)(4*Math.random());     //Elige su próximo destino
            }
        } catch(InterruptedException e) {
            // Manejo de parálisis (Eleven)
            try {
                paralizado.acquire(2);
            } catch (InterruptedException ex) {}
            finally { paralizado.release(1); }
        }
    }
}
    public void setParalizadoPortales(boolean b){
        paralizadoPortales.set(b);
    }
    
    public void setTormenta(boolean b){
        tormenta.set(b);
    }
    
    public void setConexionMindFlayer(boolean b){
        conexionMindFlayer.set(b);
    }
    
}
