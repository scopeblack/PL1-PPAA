/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Alejandro
 */
public class Portal {
    private CyclicBarrier barrera;
    private String nombre;
    private ArrayList<Nino> niños;
    private ArrayList<Nino> niñosEnPortal = new ArrayList<>();
    private Semaphore entrando = new Semaphore(1);
    private Semaphore formar;
    private int CAP;
    private AtomicInteger k = new AtomicInteger(0);
    public Portal(int CAP, String n, ArrayList<Nino> ni){
        this.barrera = new CyclicBarrier(CAP);
        formar = new Semaphore(CAP);
        this.CAP = CAP;
        this.nombre = n;
        this.niños = ni;
    }
    
    public void formarGrupoYEntrar(Nino n) throws InterruptedException, BrokenBarrierException{
        System.out.println(n.getIdentificador() + " está esperando para entrar al portal del " + nombre);
        formar.acquire();
        synchronized (niñosEnPortal) {
            niñosEnPortal.add(n);
        }
        barrera.await();
        entrando.acquire();
        k.incrementAndGet();
        Thread.sleep(1000); //Cada niño del grupo tarda 1 segundo en entrar
        synchronized(niños){
            niños.add(n);
            niños.notifyAll();
        }
        synchronized (niñosEnPortal) {
            niñosEnPortal.remove(n);
        }
        entrando.release();
        if(k.get() == CAP){
            k.set(0);
            formar.release(CAP);
        }
    }
    
    
    public void regresar(Nino n){
        synchronized (niños) {
            niños.remove(n);
            System.out.println(n.getIdentificador() + " sale del Upside Down");
        }
    }
    
    public synchronized ArrayList getNiños(){
        return niñosEnPortal;
    }
}
