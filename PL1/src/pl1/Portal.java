/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Alejandro
 */
public class Portal {
    private CyclicBarrier barrera;
    private String nombre;
    private ArrayList<Nino> niños;
    public Portal(int CAP, String n, ArrayList<Nino> ni){
        this.barrera = new CyclicBarrier(CAP);
        this.nombre = n;
        this.niños = ni;
    }
    
    // Funcion para entrar al portal de la zona elegida desde el sótano
    public void entrarPortal(Nino n) throws InterruptedException, BrokenBarrierException{
        System.out.println(n.getIdentificador() + " está esperando para entrar al portal del " + nombre);
        barrera.await();
        synchronized(niños){
            niños.add(n);
            niños.notifyAll();
        }
    }
    
    public void regresar(Nino n){
        synchronized (niños) {
            niños.remove(n);
            System.out.println(n.getIdentificador() + " sale del Upside Down");
        }
    }
}
