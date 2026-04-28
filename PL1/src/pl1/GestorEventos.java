/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

/**
 *
 * @author Alejandro
 */
public class GestorEventos extends Thread {
    private Hawkins hawkins;
    private UpsideDown upsideDown;
    public GestorEventos(Hawkins h, UpsideDown u){
        this.hawkins = h;
        this.upsideDown = u;
    }
    
    public void apagonLaboratorio(){
    }
    
    public void tormentaUpsideDown(){
    }
    
    public void intervencionEleven(){
    }
    
    public void redMental(){
    }
    
    public void run(){
        int i = (int)(4*Math.random());
        while(true){
            try{
                sleep((long)(30000 + Math.random()*30000));
                switch (i) {
                    case 0:
                        apagonLaboratorio();
                        break;
                    case 1:
                        tormentaUpsideDown();
                        break;
                    case 2:
                        intervencionEleven();
                        break;
                    case 3:
                        redMental();
                        break;
                    default:
                        throw new AssertionError();
                }
            }catch(InterruptedException e){e.printStackTrace();}
        }
    }
}
