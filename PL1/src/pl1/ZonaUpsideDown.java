/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Representa una zona insegura del Upside Down (Bosque, Laboratorio, Centro
 * Comercial o Alcantarillado). Gestiona las listas de niños y Demogorgons
 * presentes, y coordina la prioridad de salida mediante la lista niñosVolviendo
 * del portal asociado.
 *
 * @author Alejandro
 */
public class ZonaUpsideDown {

    private Portal portal;
    private final List<Nino> niños;
    private final List<Demogorgon> demogorgons;

    public ZonaUpsideDown(Portal p, List<Nino> n, List<Demogorgon> d) {
        this.portal = p;
        this.niños = n;
        this.demogorgons = d;
    }

    /**
     * Registra al niño en la zona. notifyAll() despierta a los Demogorgons que
     * puedan estar bloqueados en elegir() esperando que haya niños. La
     * comprobación contains() evita duplicados si el niño ya está registrado.
     */
    public void entrar(Nino n) {
        synchronized (niños) {
            if (!niños.contains(n)) {
                niños.add(n);
            }
            niños.notifyAll();
        }
    }

    // Implementa la prioridad de regreso: el niño se anota en niñosVolviendo
    // antes de salir de la zona. El portal comprueba esta lista antes de
    // dejar cruzar al siguiente grupo entrante, garantizando que los que
    // regresan a Hawkins no sean bloqueados por los que quieren entrar.
    public void salir(Nino n) {
        synchronized (portal.getNiñosVolviendo()) {
            portal.getNiñosVolviendo().add(n); // El niño se añade a la lista de los que quieren volver
        }
        synchronized (niños) {
            niños.remove(n); // El niño sale
        }
        synchronized (portal.getNiñosVolviendo()) {
            portal.getNiñosVolviendo().remove(n);
            if (portal.getNiñosVolviendo().size() < 1) { // Si no queda nadie volviendo, notificamos a todos los niños que estén esperando en la lista
                portal.getNiñosVolviendo().notifyAll();
            }
        }
    }

    /**
     * Registra al Demogorgon en la zona. Sigue el mismo patrón que entrar()
     * para evitar duplicados y notificar a posibles observadores.
     */
    public void entrarDemo(Demogorgon d) {
        synchronized (demogorgons) {
            if (!demogorgons.contains(d)) {
                demogorgons.add(d);
            }
            demogorgons.notifyAll();
        }
    }

    /**
     * Elimina al Demogorgon de la zona cuando cambia de destino.
     */
    public void salirDemo(Demogorgon d) {
        synchronized (demogorgons) {
            demogorgons.remove(d);
        }
    }

    /** El Demogorgon espera hasta 4-5 s si la zona está vacía. Una vez que hay
    * niños, mezcla aleatoriamente la lista y devuelve el primero al que pueda
    * "iniciarAtaque()" , evitando que dos Demogorgons elijan
    * al mismo niño simultáneamente.
    */
    public Nino elegir() throws InterruptedException {
        List<Nino> niñosCopia;
        synchronized (niños) {
            if (niños.isEmpty()) {
                niños.wait((long) (4000 + 1000 * Math.random()));
            }
            niñosCopia = new ArrayList<>(niños);
        }

        if (!niñosCopia.isEmpty()) {
            for (Nino n : niños) {
                niñosCopia.add(n);
            }
            Collections.shuffle(niñosCopia);    //Reordenamos aleatoriamente
            for (Nino n : niñosCopia) {
                if (n.iniciarAtaque()) {
                        // Verificar que sigue en la lista real
                    synchronized (niños) {
                        if (niños.contains(n)) {
                            return n; // Sigue aquí, todo bien
                        }
                    }
                    // Se fue entre medias, liberar la flag
                    n.terminarAtaque();
                }

            }
        }
        return null;
    }

    /**
     * Devuelve la lista de niños en la zona (usada por Demogorgon y la Interfaz).
     */
    public List getNiños() {
        return niños;
    }

    /**
     * Devuelve la lista de Demogorgons en la zona (usada por la Interfaz).
     */
    public List getDemogorgons() {
        return demogorgons;
    }

    /**
     * Devuelve cuántos niños hay actualmente en la zona.
     */
    public int niñosEnZona() {
        return niños.size();
    }

}
