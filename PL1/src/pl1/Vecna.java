/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

/**
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
