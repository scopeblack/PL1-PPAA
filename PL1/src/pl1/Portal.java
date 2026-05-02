/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Alejandro
 */
public class Portal {
    private CyclicBarrier barrera;
    private String nombre;
    private List<Nino> niños;
    private List<Nino> niñosEnPortal =  new CopyOnWriteArrayList<>();
    private Semaphore entrando = new Semaphore(1);
    private Semaphore formar;
    private int CAP;
    private AtomicInteger k = new AtomicInteger(0);
    private AtomicBoolean apagon = new AtomicBoolean(false);
    private Semaphore semApagon = new Semaphore(0);
    public Portal(int CAP, String n, List<Nino> ni){
        this.barrera = new CyclicBarrier(CAP);
        formar = new Semaphore(CAP);
        this.CAP = CAP;
        this.nombre = n;
        this.niños = ni;
    }
    
public void formarGrupoYEntrar(Nino n) throws InterruptedException, BrokenBarrierException {
    System.out.println(n.getIdentificador() + " está esperando para entrar al portal del " + nombre);
    formar.acquire();
    niñosEnPortal.add(n);
    try {
        barrera.await(); // Espera a que el grupo esté completo
    } catch (BrokenBarrierException | InterruptedException e) {

    }
    
    if(apagon.get()){
        semApagon.acquire();
    }

    try {
        entrando.acquire();
        Thread.sleep(1000);
        synchronized (niños) {
            niños.add(n);
            niños.notifyAll();
        }
    } catch (InterruptedException e) {

    } finally {
        entrando.release();
        niñosEnPortal.remove(n);
        // Liberar el permiso de "formar" solo cuando todos han entrado
        if (k.incrementAndGet() == CAP) {
            k.set(0);
            formar.release(CAP);
        }
    }
}
    
    
    public void regresar(Nino n){
        niños.remove(n);
        System.out.println(n.getIdentificador() + " sale del Upside Down");
    }
    
    public List getNiños(){
        return niñosEnPortal;
    }
    
    public int getCAP(){
        return CAP;
    }
    
    public void setApagon(boolean b){
        apagon.set(b);
    }
    
    public Semaphore getSemApagon(){
        return semApagon;
    }
}
