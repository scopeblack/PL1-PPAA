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
    private List<Nino> niñosEnPortal = new CopyOnWriteArrayList<>();
    private List<Nino> niñosEsperando = new CopyOnWriteArrayList<>(); // Niños que están esperando, pero no formando grupo todavía
    private List<Nino> niñosVolviendo = new ArrayList<>(); // ArrayList con los niños que desean volver del UD, para darles prioridad
    private Semaphore entrando = new Semaphore(1);
    private Semaphore formar;
    private int CAP;
    private AtomicInteger k = new AtomicInteger(0);
    private AtomicBoolean apagon = new AtomicBoolean(false);
    private Semaphore semApagon = new Semaphore(0);
    private transient SistemaLog logger;
    private AtomicBoolean pausado = new AtomicBoolean(false);

    public Portal(int CAP, String n, List<Nino> ni, SistemaLog logger) {
        this.barrera = new CyclicBarrier(CAP);
        formar = new Semaphore(CAP);
        this.CAP = CAP;
        this.nombre = n;
        this.niños = ni;
        this.logger = logger;
    }

    public void formarGrupoYEntrar(Nino n) throws InterruptedException, BrokenBarrierException {
        logger.escribirLog(n.getIdentificador() + " está esperando para entrar al portal del " + nombre);
        niñosEsperando.add(n);
        comprobarPausado(); // Antes de intentar formar grupo

        formar.acquire();
        comprobarPausado();
        niñosEsperando.remove(n);
        niñosEnPortal.add(n);
        try {
            barrera.await(); // Espera a que el grupo esté completo con el CyclicBarrier
        } catch (BrokenBarrierException | InterruptedException e) {}
        comprobarPausado();

        if (apagon.get()) {
            semApagon.acquire();
        }

        try {
            entrando.acquire();
            comprobarPausado();

            Thread.sleep(1000);
            // Monitor dentro de monitor para evitar que la lista de niñosVolviendo se modifique después de haber comprobado que está vacía
            synchronized (niñosVolviendo) {
                while (niñosVolviendo.size() > 0) { // Si hay algún niño queriendo volver, esperamos
                    niñosVolviendo.wait();
                }
                synchronized (niños) {
                    niños.add(n);
                    niños.notifyAll();
                }
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

    public void regresar(Nino n) {
        niños.remove(n);
        logger.escribirLog(n.getIdentificador() + " sale del Upside Down");
    }

    public List getNiños() {
        return niñosEnPortal;
    }

    public int getCAP() {
        return CAP;
    }

    public void setApagon(boolean b) {
        apagon.set(b);
    }

    public Semaphore getSemApagon() {
        return semApagon;
    }

    public void comprobarPausado() throws InterruptedException {
        synchronized (this) {
            while (pausado.get()) {
                this.wait();
            }
        }
    }

    public synchronized void setPausado(boolean b) {
        pausado.set(b);
        if (!b) {
            this.notifyAll();
        }
    }

    public List getNiñosEsperando() {
        return niñosEsperando;
    }

    public List getNiñosVolviendo() {
        return niñosVolviendo;
    }
}
