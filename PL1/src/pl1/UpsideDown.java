/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fachada de la dimensión del Upside Down. Agrupa las cuatro zonas inseguras
 * y la Colmena, y gestiona parte del ciclo de vida de los Demogorgons.
 * El semáforo niñosColmena actúa como contador de capturas: cada captura
 * exitosa añade 1 permiso (release), y Vecna consume 8 (acquire(8)),
 * bloqueándose hasta que se alcanza ese umbral.
 *
 * @author daniel
 */
public class UpsideDown {

    ZonaUpsideDown bosque;
    ZonaUpsideDown laboratorio;
    ZonaUpsideDown centroComercial;
    ZonaUpsideDown alcantarillado;
    Colmena colmena;
    private AtomicInteger contadorNiñosColmena = new AtomicInteger(0);
    private AtomicInteger contadorDemogorgons = new AtomicInteger(0);
    private ArrayList<Demogorgon> demogorgons = new ArrayList<>();
    private transient SistemaLog logger;

    Semaphore niñosColmena = new Semaphore(0, true); // Semáforo para que Vecna cree un demogorgon por cada 8 niños capturados

    public UpsideDown(ZonaUpsideDown b, ZonaUpsideDown l, ZonaUpsideDown c, ZonaUpsideDown a, Colmena co, SistemaLog logger) {
        this.bosque = b;
        this.laboratorio = l;
        this.centroComercial = c;
        this.alcantarillado = a;
        this.colmena = co;
        this.logger = logger;
    }

    /**
     * Devuelve la zona correspondiente al nombre dado.
     * Devuelve null si el nombre no coincide con ninguna zona válida.
     */
    public synchronized ZonaUpsideDown getZona(String zona) {
        switch (zona) {
            case "bosque":
                return bosque;
            case "laboratorio":
                return laboratorio;
            case "centroComercial":
                return centroComercial;
            case "alcantarillado":
                return alcantarillado;
        }
        return null;
    }

    /** Devuelve la zona del Bosque (portal de tamaño 2). */
    public ZonaUpsideDown getBosque() {
        return bosque;
    }

    /** Devuelve la zona del Laboratorio (portal de tamaño 3). */
    public ZonaUpsideDown getLaboratorio() {
        return laboratorio;
    }

    /** Devuelve la zona del Centro Comercial (portal de tamaño 4). */
    public ZonaUpsideDown getCentroComercial() {
        return centroComercial;
    }

    /** Devuelve la zona del Alcantarillado (portal de tamaño 2). */
    public ZonaUpsideDown getAlcantarillado() {
        return alcantarillado;
    }

    /** Devuelve la Colmena donde quedan retenidos los niños capturados. */
    public Colmena getColmena() {
        return colmena;
    }

    /**
     * Bloquea a Vecna hasta que se acumulen 8 capturas en el semáforo.
     * Cuando se desbloquea, instancia un nuevo Demogorgon con el siguiente id
     * disponible, lo arranca como hilo y lo registra en la lista global.
     */
    public void crearDemogorgon(Hawkins h) throws InterruptedException {
        niñosColmena.acquire(8);
        Demogorgon d = new Demogorgon(contadorDemogorgons.incrementAndGet(), h, this, logger);
        d.start();
        demogorgons.add(d);
        logger.escribirLog("Vecna ha creado a " + d.getIdentificador());
    }

    // Por cada niño capturado, se añade 1 permiso al semáforo.
    public synchronized boolean enviarNiñoColmena(Nino n, ZonaUpsideDown zona) {
        // Si el niño ya está marcado como capturado por OTRO Demogorgon,
        // o ya existe en la lista, ignoramos esta segunda captura.
        if (!colmena.getNiños().contains(n)) {
            zona.getNiños().remove(n);
            colmena.enviarNiñoColmena(n);
            niñosColmena.release();
            logger.escribirLog(" " + n.getIdentificador() + " añadido a la colmena.");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Crea el Demogorgon Alpha (D0000) con id=0 sin esperar en el semáforo.
     * Es el único Demogorgon generado directamente por PL1.main() al inicio.
     */
    public void crearAlpha(Hawkins h) throws InterruptedException {
        Demogorgon d = new Demogorgon(0, h, this, logger);
        d.start();
        demogorgons.add(d);
        logger.escribirLog("Vecna ha creado al Demogorgon Alpha " + d.getIdentificador());
    }

    /**
     * Devuelve la lista completa de Demogorgons.
     * Synchronized para evitar ConcurrentModificationException cuando Vecna
     * añade un nuevo Demogorgon mientras GestorEventos itera la lista.
     */
    public synchronized ArrayList getDemogorgons() {
        return demogorgons;
    }

    /**
     * Devuelve el número total de Demogorgons creados hasta ahora.
     * Suma 1 porque el Alpha usa id=0 (contadorDemogorgons arranca en 0).
     */
    public int getContadorDemogorgons() {
        return contadorDemogorgons.get()+1;
    }

    /**
     * Devuelve una lista con todos los niños presentes en el Upside Down,
     * incluyendo los capturados en la Colmena.
     */
    public List getNiños() {
        List<Nino> todos = new ArrayList<>();

        // Sincronizamos cada lista al copiarla para evitar que
        // otro hilo la modifique.
        synchronized (bosque.getNiños()) {
            todos.addAll(bosque.getNiños());
        }
        synchronized (alcantarillado.getNiños()) {
            todos.addAll(alcantarillado.getNiños());
        }
        synchronized (centroComercial.getNiños()) {
            todos.addAll(centroComercial.getNiños());
        }
        synchronized (laboratorio.getNiños()) {
            todos.addAll(laboratorio.getNiños());
        }
        synchronized (colmena.getNiños()) {
            todos.addAll(colmena.getNiños());
        }

        return todos;
    }

    /**
     * Devuelve la lista de Demogorgons ordenada de mayor a menor número de
     * capturas. Si hay menos de 3, devuelve todos los existentes.
     */
    public List rankingTop3Demos() {
        List<Demogorgon> ordenados = (List<Demogorgon>) demogorgons.clone();
        Collections.sort(ordenados);
        if (ordenados.size() < 3) {
            return ordenados;
        }
        List<Demogorgon> top3 = new ArrayList<>();
        top3.add(ordenados.get(0));
        top3.add(ordenados.get(1));
        top3.add(ordenados.get(2));

        return ordenados;
    }
}
