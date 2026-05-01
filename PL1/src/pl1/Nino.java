/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private AtomicBoolean enUpsideDown = new AtomicBoolean(false);
    public Nino(int id, Hawkins h, UpsideDown u){
        this.id=id;
        int digitos = contarDigitos(id);
        int cantidadCeros = 4 - digitos;
        this.identificador= "N" + "0".repeat(cantidadCeros) +id;
        this.hawkins = h;
        this.upsideDown =  u;
        capturado.set(false);
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

    while(true) {
        try {
            // ESPERA EN COLMENA
            synchronized (this) {
                while (capturado.get()) {
                    System.out.println(identificador + " está esperando en la COLMENA...");
                    this.wait(); 
                }
            }

            // HAWKINS
            hawkins.getCallePrincipal().entrar(this);
            sleep(250);
            hawkins.getCallePrincipal().salir(this);

            hawkins.getSotanoByers().entrar(this);
            sleep((long)(1000 + 1000*Math.random()));

            // ENTRADA AL UPSIDE DOWN
                String zonaNombre = zonasUpsideDown[i];
                ZonaUpsideDown zonaActual = upsideDown.getZona(zonaNombre);
                
                hawkins.getSotanoByers().irUpsideDown(this, zonaNombre);
                
                // Marcamos que entramos
                enUpsideDown.set(true); 
                zonaActual.entrar(this);

                try {
                    sleep((long)(3000 + 2000 * Math.random())); 
                } catch (InterruptedException e) {
                    esperar(tiempo); // Tiempo de ataque
                } finally {
                    // Justo antes de salir, marcamos que ya NO estamos en el UD
                    enUpsideDown.set(false);
                    zonaActual.salir(this);
                }

                if (capturado.get()) continue;

            // REGRESO A HAWKINS
            hawkins.getRadioWSBK().entrar(this);
            hawkins.getRadioWSBK().depositarSangre(this);
            sleep(2000 + (long)(Math.random()*2000));
            hawkins.getRadioWSBK().salir(this);

            hawkins.getCallePrincipal().entrar(this);
            sleep((long)(3000 + 2000*Math.random()));
            hawkins.getCallePrincipal().salir(this);

            i = (int)(4*Math.random());

        } catch (InterruptedException | BrokenBarrierException e) {
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
}
