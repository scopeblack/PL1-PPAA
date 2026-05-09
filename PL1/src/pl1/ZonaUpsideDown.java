/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.List;

/**
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

    public void entrar(Nino n) {
        synchronized (niños) {
            if (!niños.contains(n)) {
                niños.add(n);
            }
            niños.notifyAll();
        }
    }

    public void salir(Nino n) {
        synchronized (portal.getNiñosVolviendo()) {
            portal.getNiñosVolviendo().add(n); // El niño se añade a la lista de los que quieren volver
        }
        synchronized (niños) {
            niños.remove(n); // El niño sale
        }
        synchronized (portal.getNiñosVolviendo()) {
            portal.getNiñosVolviendo().remove(n);
            if(portal.getNiñosVolviendo().size() < 1){ // Si no queda nadie volviendo, notificamos a todos los niños que estén esperando en la lista
                portal.getNiñosVolviendo().notifyAll();
            }
        }
    }

    public void entrarDemo(Demogorgon d) {
        synchronized (demogorgons) {
            if (!demogorgons.contains(d)) {
                demogorgons.add(d);
            }
            demogorgons.notifyAll();
        }
    }

    public void salirDemo(Demogorgon d) {
        synchronized (demogorgons) {
            demogorgons.remove(d);
        }
    }

    public Nino elegir() throws InterruptedException {
        synchronized (niños) {
            if (niños.isEmpty()) {
                niños.wait((long) (4000 + 1000 * Math.random()));
            }
            if (!niños.isEmpty()) {
                int i = (int) (niños.size() * Math.random());
                Nino elegido = niños.get(i);
                niños.remove(elegido); // Sacamos al niño para que nadie más lo ataque
                if (!elegido.iniciarAtaque()) {
                    return null; // Ya está siendo atacado
                }
                return elegido;

            }
        }
        return null;
    }

    public List getNiños() {
        return niños;
    }

    public List getDemogorgons() {
        return demogorgons;
    }

    public int niñosEnZona() {
        return niños.size();
    }

    public void devolverSiNoCapturado(Nino n, boolean capturado) {
        synchronized (niños) {
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
