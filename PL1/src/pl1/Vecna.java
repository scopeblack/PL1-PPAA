/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

/**
 * Hilo creador de nuevos Demogorgons. Permanece bloqueado en
 * UpsideDown.crearDemogorgon(), que internamente hace acquire(8) sobre un
 * semáforo: solo se desbloquea cuando se han acumulado 8 capturas en la
 * Colmena, momento en que instancia y arranca un nuevo Demogorgon.
 *
 * @author daniel
 */
public class Vecna extends Thread{
    private Hawkins hawkins;
    private UpsideDown upsideDown;
    public Vecna(Hawkins h, UpsideDown u){
        this.hawkins = h;
        this.upsideDown = u;
    }
    @Override
    public void run(){
        while(true){
            try{
                upsideDown.crearDemogorgon(hawkins);    //Vecna solo está pendiente de crear nuevos Demogorgons
            }catch(InterruptedException e){e.printStackTrace();}
        }
    }
}
