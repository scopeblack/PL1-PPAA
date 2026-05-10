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
 * Portal que conecta Hawkins con una zona del Upside Down. Implementa tres
 * capas de control de concurrencia:
 *
 *  1. Semáforo "formar" (CAP número de permisos): limita cuántos niños pueden estar
 *     formando grupo a la vez, se libera completo solo cuando el grupo entero
 *     ha cruzado (contador k), para que no entre el siguiente grupo antes.
 *  2. CyclicBarrier: sincroniza a los CAP niños del grupo para que ninguno
 *     cruce hasta que todos hayan llegado al portal.
 *  3. Semáforo "entrando" (1 permiso): garantiza el cruce de uno en uno,
 *     bloqueando primero si hay niños en niñosVolviendo (prioridad de regreso).
 *
 *  semApagon (0 permisos inicial) bloquea el cruce durante el evento
 *  Apagón del Laboratorio, al finalizar el apagón se liberan CAP permisos
 *  para desbloquear a todos los niños que esperasen.
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

    /**
     * CAP es el tamaño del grupo requerido para cruzar (2/3/4 según la zona).
     *  n es el Nombre de la zona destino, usado en los logs.
     * ni es la Lista compartida de niños actualmente en la zona destino.
     */
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

        // El niño compite por uno de los CAP permisos del grupo actual;
        // si el grupo está lleno, queda en espera del siguiente
        formar.acquire();
        comprobarPausado();
        niñosEsperando.remove(n);
        niñosEnPortal.add(n);
        try {
            barrera.await(); // Espera a que el grupo esté completo con el CyclicBarrier
        } catch (BrokenBarrierException | InterruptedException e) { 
            e.printStackTrace();
        }
        comprobarPausado();

        // Si el apagón está activo al saltar la barrera, el niño se bloquea
        // aquí hasta que GestorEventos libere semApagon al final del apagón
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
            // Se liberan todos los permisos de "formar" solo cuando el grupo
            // completo ha cruzado, impidiendo que el siguiente grupo empiece antes. (exclusividad de grupos)
            if (k.incrementAndGet() == CAP) {
                k.set(0);
                formar.release(CAP);
            }
        }
    }

    /**
     * Elimina al niño de la lista de la zona del Upside Down, indicando que
     * ha regresado a Hawkins a través de este portal.
     */
    public void regresar(Nino n) {
        niños.remove(n);
        logger.escribirLog(n.getIdentificador() + " sale del Upside Down");
    }

    /**
     * Devuelve la lista de niños que están cruzando el portal en este momento
     * (ya han formado grupo y están en tránsito, uno a uno).
     */
    public List getNiños() {
        return niñosEnPortal;
    }

    /** Devuelve el tamaño de grupo requerido por este portal (2, 3 o 4). */
    public int getCAP() {
        return CAP;
    }

    /**
     * Activa o desactiva el bloqueo de cruce por Apagón del Laboratorio.
     * Al activarse, los niños que completen la barrera se bloquearán en
     * semApagon.acquire() hasta que el apagón termine.
     */
    public void setApagon(boolean b) {
        apagon.set(b);
    }

    /**
     * Devuelve el semáforo de apagón para que GestorEventos pueda liberarlo
     * (release(CAP)) al finalizar el Apagón del Laboratorio.
     */
    public Semaphore getSemApagon() {
        return semApagon;
    }

    /**
     * Bloquea al niño que está atravesando el portal si el sistema está pausado.
     * Lanza InterruptedException para que el Portal pueda propagarla correctamente.
     */
    public void comprobarPausado() throws InterruptedException {
        synchronized (this) {
            while (pausado.get()) {
                this.wait();
            }
        }
    }

    /**
     * Activa o desactiva la pausa del portal. Al reanudar (b=false) notifica a
     * todos los niños bloqueados en comprobarPausado() para que continúen.
     */
    public synchronized void setPausado(boolean b) {
        pausado.set(b);
        if (!b) {
            this.notifyAll();
        }
    }

    /**
     * Devuelve la lista de niños que esperan para comenzar a formar grupo
     * (aún no han adquirido el semáforo "formar"). Usada por la interfaz.
     */
    public List getNiñosEsperando() {
        return niñosEsperando;
    }

    /**
     * Devuelve la lista de niños que están regresando del Upside Down.
     * La comprueba formarGrupoYEntrar() para dar prioridad al regreso.
     */
    public List getNiñosVolviendo() {
        return niñosVolviendo;
    }
}
