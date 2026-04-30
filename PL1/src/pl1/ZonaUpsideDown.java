/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public class ZonaUpsideDown {
    private Portal portal;
    private ArrayList<Nino> niños;
    private ArrayList<Demogorgon> demogorgons;
    public ZonaUpsideDown(Portal p, ArrayList<Nino> n){
        this.portal = p;
        this.niños = n;
    }
    
    public void salir(Nino n){
        portal.regresar(n);
    }
    
    public Nino elegir() throws InterruptedException{
        Nino n = null;
        synchronized(niños){
            while(niños.size() < 1){
                niños.wait((long)(4000 + 1000*Math.random()));
            }
            // Hay que implementar lo de la probabilidad de éxito, no se si irá aquí o donde
            int i = (int)(niños.size() * Math.random());
            n = niños.get(i);
            niños.remove(n);
        }
        return n;
    }
    
    public boolean atacar(Nino n){
        boolean capturado = (Math.random() <= 1);
        // probabilidad -> Modifica flag capturado
        if(!capturado){
            synchronized (niños) {
                niños.add(n);
            }
        }
        return capturado;
    }
}
