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
public class ZonaUpsideDown {
    private Portal portal;
    private List<Nino> niños;
    public ZonaUpsideDown(Portal p, List<Nino> n){
        this.portal = p;
        this.niños = n;
    }
    
    public void salir(Nino n){
        portal.regresar(n);
    }
    
    public Nino elegir() throws InterruptedException{
        Nino n = null;
        synchronized(niños){
            if(niños.size() < 1){
                niños.wait((long)(4000 + 1000*Math.random()));
            }
            if(niños.size() > 0){
                // Hay que implementar lo de la probabilidad de éxito, no se si irá aquí o donde
                int i = (int)(niños.size() * Math.random());
                n = niños.get(i);
                niños.remove(n);
            }
        }
        return n;
    }
    
    public boolean atacar(Nino n){
        boolean capturado = (Math.random() <= 1.0/3.0);
        if(!capturado){
            niños.add(n);
        }
        return capturado;
    }
    
    public List getNiños(){
        return niños;
    }
}
