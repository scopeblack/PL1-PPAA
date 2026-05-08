/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Alejandro
 */
public class GestorEventos extends Thread {
    private Hawkins hawkins;
    private UpsideDown upsideDown;
    private String eventoActivo = null;
    private String ultimoEventoActivo = null;
    private AtomicBoolean pausado = new AtomicBoolean(false);
    private long tiempo;
    private long contador=0;
    private long t1;
    private long t2;
    private long tiempoEspera;
    private long contadorEspera=0;
    private long t1Espera;
    private long t2Espera;
    private transient SistemaLog logger;

    public GestorEventos(Hawkins h, UpsideDown u, SistemaLog logger){
        this.hawkins = h;
        this.upsideDown = u;
        this.logger = logger;
    }
    
    public void apagonLaboratorio(){
        
        while(tiempo-contador > 0){ //El catch regresa al while si todavía queda tiempo de evento.
            try{
                    synchronized (this){
                        while(pausado.get()){
                            this.wait();
                        }
                }
        t1=System.currentTimeMillis();
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
        
        
            sleep(tiempo-contador);
            t2=System.currentTimeMillis();
    
        
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
                catch(InterruptedException ex){
                t2=System.currentTimeMillis();
                logger.escribirLog("-----------Evento detenido.-----------");
                continue;
                
            }
            finally{
                contador+=t2-t1;
            }
        }
        
    }
    
    public void tormentaUpsideDown(){
        
        while(tiempo-contador > 0){ //El catch regresa al while si todavía queda tiempo de evento.
            try{
                    synchronized (this){
                        while(pausado.get()){
                            this.wait();
                        }
                }
        t1=System.currentTimeMillis();
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
        // Tiempo aleatorio restante de la duración del evento
       
            sleep(tiempo-contador);
            t2=System.currentTimeMillis();
            
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
                catch(InterruptedException ex){
                t2=System.currentTimeMillis();
                logger.escribirLog("-----------Evento detenido.-----------");
                continue;
            }
            finally{
                contador+=t2-t1;
            }
        }
        
    }
    
    public void intervencionEleven(){
        
        while(tiempo-contador > 0){ //El catch regresa al while si todavía queda tiempo de evento.
            try{
                    synchronized (this){
                        while(pausado.get()){
                            this.wait();
                        }
                }
        t1=System.currentTimeMillis();
        
        List<Demogorgon> demos = upsideDown.getDemogorgons();
        if(!(tiempo-contador<tiempo)){ //Para evitar volver a liberar niños/paralizar demogorgons tras reanudar el programa:
            eventoActivo = "Intervención De Eleven";
            int sangre = hawkins.getRadioWSBK().getSangre();
            List<Nino> listaColmena = upsideDown.getColmena().getNiños();
            logger.escribirLog("Eleven liberando niños con " + sangre + " de sangre");
            int liberados = 0;
            
                    
            for(Demogorgon d: demos){
                d.setParalizado();
                d.interrupt();
            }
            // Usamos un bucle controlado para evitar ConcurrentModificationException
            while(listaColmena.size() > 0 && sangre > 0){
                Nino n = listaColmena.get(0); 
                upsideDown.getColmena().sacarNiñoColmena(n);
                n.setLiberado(); 
                sangre--;
                liberados++;
                
                
            }
            hawkins.getRadioWSBK().setSangre(hawkins.getRadioWSBK().getSangre() - liberados);
        }


       
                sleep(tiempo-contador);
                t2=System.currentTimeMillis();
                

        
            for(Demogorgon d: demos){
                logger.escribirLog(d.getIdentificador() + " Deja de estar paralizado por Eleven.");
                d.Liberar();
            }
        // EVENTO TERMINADO
        ultimoEventoActivo = "Intervenvión De Eleven";
        eventoActivo = null;
            }
                catch(InterruptedException ex){
                t2=System.currentTimeMillis();
                logger.escribirLog("-----------Evento detenido.-----------");
                
            }
            finally{
                contador+=t2-t1;
            }
        }
        
    }
    
    public void redMental(){
        
        while(tiempo-contador > 0){ //El catch regresa al while si todavía queda tiempo de evento.
            try{
                    synchronized (this){
                        while(pausado.get()){
                            this.wait();
                        }
                }
        t1=System.currentTimeMillis();
        eventoActivo = "La Red Mental";
        
        ArrayList<Demogorgon> demos = upsideDown.getDemogorgons();
        for(Demogorgon d: demos){
            d.setConexionMindFlayer(true);
        }
        
            sleep(tiempo-contador);
            t2=System.currentTimeMillis();
            
        
        demos = upsideDown.getDemogorgons();
        for(Demogorgon d: demos){
            d.setConexionMindFlayer(false);
        }
        ultimoEventoActivo = "La Red Mental";
        eventoActivo = null;
            }
                catch(InterruptedException ex){
                t2=System.currentTimeMillis();
                logger.escribirLog("-----------Evento detenido.-----------");
                
            }
            finally{
                contador+=t2-t1;
            }
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
        tiempo=5000 + (long)(5000*Math.random());
        tiempoEspera=30000 + (long)(Math.random()*30000);
        while(true){
            
            try{
                synchronized (this){
                        while(pausado.get()){
                            this.wait();
                        }
                }
                t1Espera=System.currentTimeMillis();
                System.out.println("Tiempo de espera gestor:" + (tiempoEspera-contadorEspera));
                sleep(tiempoEspera-contadorEspera); 
                t2Espera=System.currentTimeMillis();
                
            }catch(InterruptedException ex){
                
                System.out.println("Se ha intentado interrumpir al gestor mientras eperaba...");
                t2Espera=System.currentTimeMillis();
                continue;
                
            }finally{
                
                contadorEspera+=t2Espera-t1Espera;
            }

                    switch (i) {
                        case 0 -> tormentaUpsideDown();
                        case 1 -> intervencionEleven();
                        case 2 -> apagonLaboratorio();
                        case 3 -> redMental();
                        default -> throw new AssertionError();
                    }
                             
            tiempo = 5000 + (long)(5000*Math.random()); //Se elige el tiempo aleatorio del siguiente evento.
            contador=0;
            i = (int)(4*Math.random()); //Se elige el siguiente evento aleatoriamente.
            tiempoEspera=30000 + (long)(Math.random()*30000);   //Proximo tiempo de espera.
            contadorEspera=0;
            
        }
    }
    
    public void comprobarPausado(){
        try{
            synchronized (this) {
                while(pausado.get()){
                    Thread.interrupted();   //Evita la interrupción en el wait por si acaso.
                    this.wait();
                }
            }
        }catch(InterruptedException e){}
    }
    
    public void setPausado(boolean b){
        pausado.set(b);
    }
    
    public int getTiempoRestante(){
        return (int)(tiempo - (contador+System.currentTimeMillis()-t1));
    }
}
