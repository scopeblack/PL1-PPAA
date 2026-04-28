/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.concurrent.BrokenBarrierException;

/**
 *
 * @author Alejandro
 */
public class Nino extends Thread{
    private int id;
    private String identificador;
    private Hawkins hawkins;
    private UpsideDown upsideDown;
    public Nino(int id, Hawkins h, UpsideDown u){
        this.id=id;
        this.identificador= "N"+id;
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
                String zonaUpsideDown = zonasUpsideDown[i];
                sleep((long)(1000 + 1000*Math.random())); // Tiempo preparándose en el sótano
                hawkins.getSotanoByers().irUpsideDown(this, zonaUpsideDown);
                sleep((long)(3000 + 2000*Math.random()));
                upsideDown.getZona(zonaUpsideDown).salir(this);
                hawkins.getSotanoByers().salir(this);
            }catch(InterruptedException | BrokenBarrierException e){e.printStackTrace();}
        }
    }
}
