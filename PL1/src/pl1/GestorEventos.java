/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alejandro
 */
public class GestorEventos extends Thread {
    private Hawkins hawkins;
    private UpsideDown upsideDown;
    private String eventoActivo = null;
    private String ultimoEventoActivo = null;
    public GestorEventos(Hawkins h, UpsideDown u){
        this.hawkins = h;
        this.upsideDown = u;
    }
    
    public void apagonLaboratorio(){
        // Cambiamos el evento
        eventoActivo = "Apagón Del Laboratorio";
        
        // Todos los portales quedarán inultizables
        // Primero ponemos el flag del apagón en los portales a true
        hawkins.getSotanoByers().getPortalAlcantarillado().setApagon(true);
        hawkins.getSotanoByers().getPortalBosque().setApagon(true);
        hawkins.getSotanoByers().getPortalCentroComercial().setApagon(true);
        hawkins.getSotanoByers().getPortalLaboratorio().setApagon(true);
        
        // Le decimos a los demogorgons, mediante su flag paralizadoPortales, que no van a poder cambiar de zona
        
        ArrayList<Demogorgon> demos = upsideDown.getDemogorgons();
        for(Demogorgon d: demos){
            d.setParalizadoPortales(true);
        }
        // La duración, aunque no pone nada, será random
        try{
            sleep((long)(15000 + 1000+Math.random()));
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        
        // Después, ponemos el flag de apagón a false
        hawkins.getSotanoByers().getPortalAlcantarillado().setApagon(false);
        hawkins.getSotanoByers().getPortalBosque().setApagon(false);
        hawkins.getSotanoByers().getPortalCentroComercial().setApagon(false);
        hawkins.getSotanoByers().getPortalLaboratorio().setApagon(false);
        
        // Y liberamos tantos permisos como CAP tenga el portal, para que todos los niños que haya
        // intentado pasar puedan pasar sin bloquearse en el semáforo del apagón
        
        hawkins.getSotanoByers().getPortalAlcantarillado().getSemApagon().release(hawkins.getSotanoByers().getPortalAlcantarillado().getCAP());
        hawkins.getSotanoByers().getPortalBosque().getSemApagon().release(hawkins.getSotanoByers().getPortalBosque().getCAP());
        hawkins.getSotanoByers().getPortalCentroComercial().getSemApagon().release(hawkins.getSotanoByers().getPortalCentroComercial().getCAP());
        hawkins.getSotanoByers().getPortalLaboratorio().getSemApagon().release(hawkins.getSotanoByers().getPortalLaboratorio().getCAP());
        
        // Dejamos que los demogorgons se muevan libremente de nuevo
        demos = upsideDown.getDemogorgons();
        for(Demogorgon d: demos){
            d.setParalizadoPortales(false);
        }
        
        // EVENTO TERMINADO
        ultimoEventoActivo = "Apagón Del Laboratorio";
        eventoActivo = null;
    }
    
    public void tormentaUpsideDown(){
        eventoActivo = "Tormenta Del Upside Down";
        // Le decimos a los demogorgons, mediante el flag tormenta, que el evento está activo y que su velocidad
        // de ataque se verá aumentada al doble (Reduciendo el tiempo entre ataques)
        ArrayList<Demogorgon> demos = upsideDown.getDemogorgons();
        for(Demogorgon d: demos){
            d.setTormenta(true);
        }
        
        // Le decimos a los niños, mediante el flag tormenta, que el evento está activo y que su velocidad
        // recolección de sangre se verá disminuida a la mitad (Aumentando el tiempo que permanecen en cada zona del Upside Down)
        List<Nino> niños = upsideDown.getNiños();
        for(Nino n: niños){
            n.setTormenta(true);
        }
        // Tiempo aleatorio de la duración del evento
        try{
            sleep((long)(10000 + 1000*Math.random()));
        }catch(InterruptedException e){e.printStackTrace();}
        
        // Le decimos a los demogorgons y a los niños que el evento ha terminado
        demos = upsideDown.getDemogorgons();
        for(Demogorgon d: demos){
            d.setTormenta(false);
        }
        niños = upsideDown.getNiños();
        for(Nino n: niños){
            n.setTormenta(false);
        }
        
        ultimoEventoActivo = "Tormenta Del Upside Down";
        eventoActivo = null;
    }
    
    public void intervencionEleven(){
    eventoActivo = "Intervención De Eleven";
    int sangre = hawkins.getRadioWSBK().getSangre();
    List<Nino> listaColmena = upsideDown.getColmena().getNiños();
    
    System.out.println("Eleven liberando niños con " + sangre + " de sangre");
    int liberados = 0;
    
    // Usamos un bucle controlado para evitar ConcurrentModificationException
    while(listaColmena.size() > 0 && sangre > 0){
        Nino n = listaColmena.get(0); 
        upsideDown.getColmena().sacarNiñoColmena(n);
        n.setLiberado(); // Esto hace el notify() interno en el Nino
        synchronized (n) { // Es necesario notificar al niño para que vuelva a comprobar la condición del while
            n.notify();
        }
        sangre--;
        liberados++;
    }
    hawkins.getRadioWSBK().setSangre(hawkins.getRadioWSBK().getSangre() - liberados);
    
    // EVENTO TERMINADO
    ultimoEventoActivo = "Intervenvión De Eleven";
    eventoActivo = null;
}
    
    public void redMental(){
        eventoActivo = "La Red Mental";
        
        ArrayList<Demogorgon> demos = upsideDown.getDemogorgons();
        for(Demogorgon d: demos){
            d.setConexionMindFlayer(true);
        }
        try{
            sleep((long)(2000 + 3000*Math.random()));
        }catch(InterruptedException e){e.printStackTrace();}
        
        demos = upsideDown.getDemogorgons();
        for(Demogorgon d: demos){
            d.setConexionMindFlayer(false);
        }       
    }
    
    public String getEvento(){
        return eventoActivo;
    }
    
    public String getUltimoEventoActivo(){
        return ultimoEventoActivo;
    }
    public void run(){
        int i = (int)(4*Math.random());
        while(true){
            try{
                sleep((long)(Math.random()*30000));
                switch (i) {
                    case 0:
                        tormentaUpsideDown();
                        //apagonLaboratorio();
                        //intervencionEleven();
                        break;
                    case 1:
                        //apagonLaboratorio();
                        tormentaUpsideDown();
                        intervencionEleven();
                        break;
                    case 2:
                        tormentaUpsideDown();
                        apagonLaboratorio();
                        //intervencionEleven();
                        break;
                    case 3:
                        //tormentaUpsideDown();
                        //apagonLaboratorio();
                        //redMental();
                        //intervencionEleven();
                        redMental();
                        break;
                    default:
                        throw new AssertionError();
                }
                i = (int)(4*Math.random());
            }catch(InterruptedException e){e.printStackTrace();}
        }
    }
}
