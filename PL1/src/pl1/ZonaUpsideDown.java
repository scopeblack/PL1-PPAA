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
    private final List<Nino> niños;

    public ZonaUpsideDown(Portal p, List<Nino> n){
        this.portal = p;
        this.niños = n;
    }

    public void entrar(Nino n) {
        synchronized(niños) {
            if (!niños.contains(n)) {
                niños.add(n);
            }
            niños.notifyAll();
        }
    }

    public void salir(Nino n) {
        synchronized(niños) {
            niños.remove(n);
        }
    }

    public Nino elegir() throws InterruptedException {
        synchronized(niños) {
            if (niños.isEmpty()) {
                niños.wait((long)(4000 + 1000 * Math.random()));
            }
            if (!niños.isEmpty()) {
                int i = (int)(niños.size() * Math.random());
                // IMPORTANTE: Sacamos al niño para que nadie más lo ataque
                return niños.remove(i); 
            }
        }
        return null;
    }
    public List getNiños(){
        return niños;
    }
        
    public void devolverSiNoCapturado(Nino n, boolean capturado) {
        synchronized(niños) {
            // Solo lo devolvemos si NO ha sido capturado Y si el niño
            // todavía considera que está en el Upside Down (no se ha ido a Hawkins)
            if (!capturado && n.estaEnUpsideDown()) {
                if (!niños.contains(n)) {
                    niños.add(n);
                    niños.notifyAll();
                }
            }
        }
    }
}
