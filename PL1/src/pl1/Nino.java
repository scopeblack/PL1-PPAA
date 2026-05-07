/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Alejandro
 */
public class Nino extends Thread{
    private int id;
    private String identificador;
    private Hawkins hawkins;
    private UpsideDown upsideDown;
    private double tiempo = 0;
    private AtomicBoolean capturado= new AtomicBoolean(false);
    private Semaphore encerrado = new Semaphore(1);
    //private Semaphore perseguido = new Semaphore(0);
    private AtomicBoolean enUpsideDown = new AtomicBoolean(false);
    private AtomicBoolean paralizadoPortales = new AtomicBoolean(false); // Evento Apagón del Laboratorio
    private AtomicBoolean tormenta = new AtomicBoolean(false); // Evento Tormenta del Upside Down
    private boolean yaFuera = false;
    private AtomicBoolean pausado = new AtomicBoolean(false);
    private SistemaLog logger;
    public Nino(int id, Hawkins h, UpsideDown u, SistemaLog logger){
        this.id=id;
        int digitos = contarDigitos(id);
        int cantidadCeros = 4 - digitos;
        this.identificador= "N" + "0".repeat(cantidadCeros) +id;
        this.hawkins = h;
        this.upsideDown =  u;
        capturado.set(false);
        this.logger = logger;
    }
    
    public String getIdentificador(){
        return identificador;
    }
    
    public void setTiempo(double t){
        tiempo = t;
    }
    
    public void run() {
    String[] zonasUpsideDown = {"bosque", "alcantarillado", "laboratorio", "centroComercial"};
    int i = (int)(4*Math.random());
    
    // HAWKINS
            comprobarPausado();
            hawkins.getCallePrincipal().entrar(this);   //Spawn de los niños
            try{
            sleep(1000);
            }
            catch(InterruptedException ex){
                ex.printStackTrace();
            }
            comprobarPausado();
            hawkins.getCallePrincipal().salir(this);
            
    while(true) {
        try {
            // ESPERA EN COLMENA
            synchronized (this) {
                while (capturado.get()) {
                    System.out.println(identificador + " está esperando en la COLMENA...");
                    this.wait(); 
                }
            }

            comprobarPausado();
            
            enUpsideDown.set(false);
            hawkins.getSotanoByers().entrar(this);
            sleep((long)(1000 + 1000*Math.random()));
            
            // ENTRADA AL UPSIDE DOWN
            String zonaNombre = zonasUpsideDown[i];
            ZonaUpsideDown zonaActual = upsideDown.getZona(zonaNombre);
            
            comprobarPausado();

            hawkins.getSotanoByers().irUpsideDown(this, zonaNombre);

            // Marcamos que entramos
            enUpsideDown.set(true); 
            zonaActual.entrar(this);
            try{
                do{ // Mientras esté activo el evento del apagón del laboratorio, no se puede mover de la zona
                    try {
                        double t1 = 600 + 400 * Math.random();
                        
                        int j = 0;
                        double tiempoRestante = 5*t1;
                        if(tormenta.get()){ tiempoRestante = tiempoRestante*2; }
                        long tiempo1 = 0;
                        long tiempo2 = 0;
                        while(tiempoRestante > 0 && !capturado.get()){
                            try{
                                tiempo1 = 0;
                                tiempo2 = 0;
                                comprobarPausado();
                                tiempo1 = System.currentTimeMillis();
                                sleep((long)tiempoRestante);
                            }catch(InterruptedException e){
                                tiempo2 = System.currentTimeMillis();
                                esperar(tiempo); // Tiempo de ataque
                                Thread.interrupted();   //Limpiar flag
                                //perseguido.acquire();   //Esperamos a que el Demogorgon nos capture o desista
                                // Imprime un mensaje y las veces que le han intentado capturar (j)
                                if(!capturado.get()){ System.out.println("----------------" + identificador + " Han fallado la captura " + ++j); }
                            }finally{
                                if(tiempo2 == 0){
                                    tiempo2 = System.currentTimeMillis();
                                }
                                tiempoRestante = tiempoRestante - (tiempo2-tiempo1); // Se le resta lo que haya durado el sleep realmente
                                i++;
                            }
                        }
                    }finally {
                        if(!paralizadoPortales.get()){
                            comprobarPausado();
                            enUpsideDown.set(false);
                            zonaActual.salir(this);
                            yaFuera = true;
                        }
                        if (capturado.get()){ System.out.println(identificador + " He sido capturado me salto el while restante"); continue;}  //Entramos en el wait de la colmena

                    }
                }while(paralizadoPortales.get());
            }finally{
                if(!yaFuera){
                    comprobarPausado();
                    zonaActual.salir(this);
                    enUpsideDown.set(false);
                }
                if (capturado.get()){ System.out.println(identificador + " He sido capturado me salto el while restante"); continue;}  //Entramos en el wait de la colmena

            }
            // REGRESO A HAWKINS
            
            try{
                hawkins.getRadioWSBK().entrar(this);
                hawkins.getRadioWSBK().depositarSangre(this);
                sleep(2000 + (long)(Math.random()*2000));
            }catch(InterruptedException e){}
            finally{
                comprobarPausado();
                hawkins.getRadioWSBK().salir(this);
            }
            try{
                hawkins.getCallePrincipal().entrar(this);
                sleep((long)(3000 + 2000*Math.random()));
            }catch(InterruptedException e){}
            finally{
                comprobarPausado();
                hawkins.getCallePrincipal().salir(this);
            } 
            i = (int)(4*Math.random());

        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
            Thread.interrupted();
        }
    }
}
    
    public void esperar(double t){
        Thread.interrupted(); // Limpiar el flag de interrupción pendiente
        try{
            sleep((long)(t));
        }catch(InterruptedException e){
            // Si lo interrumpen de nuevo durante la espera, ignorar silenciosamente
            Thread.interrupted(); // Limpiar de nuevo por si acaso
        }
    }
    
    public Semaphore getSemaphore(){
        return encerrado;
    }
    
    public int contarDigitos(int n){
        int k = 0;
        while(n > 0){
            k++;
            n = Math.floorDiv(n, 10);
        }
        return k;
    }
    
    public void setCapturado(){
        capturado.set(true);
    }
    
    public synchronized void setLiberado(){
        
        capturado.set(false);
        this.notify();
    }
    
    public String toString(){
        return identificador;
    }
    
    public boolean estaEnUpsideDown() {
        return enUpsideDown.get();
    }
    
    public void setParalizadoPortales(boolean b){
        paralizadoPortales.set(b);
    }
    
    public void setTormenta(boolean b){
        tormenta.set(b);
    }
    
    public void comprobarPausado(){
        try{
            synchronized (this) {
                while(pausado.get()){
                    this.wait();
                }
            }
        }catch(InterruptedException e){}
    }
    
    public void setPausado(boolean b){
        pausado.set(b);
    }
}
