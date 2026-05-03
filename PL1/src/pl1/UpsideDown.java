/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
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


    Semaphore niñosColmena = new Semaphore(0, true); // Semáforo para que Vecna cree un demogorgon por cada 8 niños capturados
    
    public UpsideDown(ZonaUpsideDown b, ZonaUpsideDown l, ZonaUpsideDown c, ZonaUpsideDown a, Colmena co){
        this.bosque = b;
        this.laboratorio = l;
        this.centroComercial = c;
        this.alcantarillado = a;
        this.colmena = co;
    }
    
    public synchronized ZonaUpsideDown getZona(String zona){
        switch(zona){
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

    public ZonaUpsideDown getBosque() {
        return bosque;
    }

    public ZonaUpsideDown getLaboratorio() {
        return laboratorio;
    }

    public ZonaUpsideDown getCentroComercial() {
        return centroComercial;
    }

    public ZonaUpsideDown getAlcantarillado() {
        return alcantarillado;
    }

    public Colmena getColmena() {
        return colmena;
    }
    
    
    // Cada 8 permisos en el semáforo, significa que 8 niños han sido capturados, por lo que Vecna crea un nuevo Demogorgon
    public void crearDemogorgon(Hawkins h) throws InterruptedException{
        niñosColmena.acquire(8);
        Demogorgon d = new Demogorgon(contadorDemogorgons.incrementAndGet(), h, this);
        d.start();
        demogorgons.add(d);
        System.out.println("Vecna ha creado a " + d.getIdentificador());
    }
    
    // Por cada niño capturado, se añade 1 permiso al semáforo
    // En la clase Colmena o UpsideDown
    public synchronized void enviarNiñoColmena(Nino n) {
        // Si el niño ya está marcado como capturado por OTRO Demogorgon,
        // o ya existe en la lista, ignoramos esta segunda captura.
        if (!colmena.getNiños().contains(n)) {
            colmena.enviarNiñoColmena(n);
            niñosColmena.release();
            System.out.println("LOG: " + n.getIdentificador() + " añadido a la colmena.");
        } else {
            System.out.println("OJO: Se intentó duplicar a " + n.getIdentificador() + " en la colmena.");
        }
    }
    
    public void crearAlpha(Hawkins h) throws InterruptedException{
        Demogorgon d = new Demogorgon(contadorDemogorgons.incrementAndGet(), h, this);
        d.start();
        demogorgons.add(d);
        System.out.println("Vecna ha creado al Demogorgon Alpha " + d.getIdentificador());
    }
    
    public synchronized ArrayList getDemogorgons(){
        return demogorgons;
    }
    
    public int getContadorDemogorgons(){
        return contadorDemogorgons.get();
    }
    
    public List getNiños(){
        // Usamos ArrayList normal; es más rápido para una operación de "leer y olvidar"
        List<Nino> todos = new ArrayList<>();

        // Sincroniza cada lista al copiarla para evitar que 
        // otro hilo la modifique mientras haces el addAll
        synchronized(bosque.getNiños()) { todos.addAll(bosque.getNiños()); }
        synchronized(alcantarillado.getNiños()) { todos.addAll(alcantarillado.getNiños()); }
        synchronized(centroComercial.getNiños()) { todos.addAll(centroComercial.getNiños()); }
        synchronized(laboratorio.getNiños()) { todos.addAll(laboratorio.getNiños()); }
        synchronized(colmena.getNiños()) { todos.addAll(colmena.getNiños()); }

        return todos;
    }
}
