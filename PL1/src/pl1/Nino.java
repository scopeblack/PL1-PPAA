/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;
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
    private Semaphore encerrado = new Semaphore(1);
    public Nino(int id, Hawkins h, UpsideDown u){
        this.id=id;
        int digitos = contarDigitos(id);
        int cantidadCeros = 4 - digitos;
        this.identificador= "N" + "0".repeat(cantidadCeros) +id;
        this.hawkins = h;
        this.upsideDown =  u;
    }
    
    public String getIdentificador(){
        return identificador;
    }
    
    public void setTiempo(double t){
        tiempo = t;
    }
    
    public void run(){
        String[] zonasUpsideDown = {"bosque", "alcantarillado", "laboratorio", "centroComercial"};
        int i = (int)(4*Math.random());
        
        while(true){
            try{
                hawkins.getCallePrincipal().entrar(this);
                hawkins.getCallePrincipal().salir(this);
                String zonaUpsideDown = zonasUpsideDown[i];
                hawkins.getSotanoByers().entrar(this);
                sleep((long)(1000 + 1000*Math.random())); // Tiempo preparándose en el sótano
                hawkins.getSotanoByers().irUpsideDown(this, zonaUpsideDown);    // Empieza a formar grupo para entrar al Upside Down
                hawkins.getSotanoByers().salir(this);   // Sale del sótano (y decrementa el contador)
                System.out.println("N" + id + " Ha pasado el portal y ha llegado a: " + zonaUpsideDown);
                sleep((long)(3000 + 2000*Math.random()));   // Tiempo en el Upside Down
                upsideDown.getZona(zonaUpsideDown).salir(this);     // Salir del Upside Down
                hawkins.getRadioWSBK().entrar(this);
                hawkins.getRadioWSBK().depositarSangre(this);
                sleep(2000 + (long)(Math.random()*2000));
                hawkins.getRadioWSBK().salir(this);
                hawkins.getCallePrincipal().entrar(this);
                sleep((long)(3000 + 2000*Math.random()));   // Tiempo en la Calle Principal
                hawkins.getCallePrincipal().salir(this);
                i = (int)(4*Math.random());

            }catch(InterruptedException | BrokenBarrierException e){
                // e.printStackTrace();
                esperar((long)(tiempo));
                try {
                    encerrado.acquire(2);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                finally{
                    encerrado.release();
                }
                
                
                
            }
        }
    }
    
    public void esperar(double t){
        try{
            sleep((long)(t));
        }catch(InterruptedException e){e.printStackTrace();}
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
    
    public String toString(){
        return identificador;
    }
}
