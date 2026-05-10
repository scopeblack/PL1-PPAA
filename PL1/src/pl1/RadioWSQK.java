/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Zona de descanso en Hawkins donde los niños depositan la sangre de Vecna
 * recolectada en el Upside Down. El contador de sangre es compartido entre
 * todos los hilos niño y también lo lee GestorEventos durante el evento de
 * Intervención de Eleven para calcular cuántos niños puede liberar.
 * AtomicInteger garantiza la atomicidad de los incrementos.
 *
 * @author Alejandro
 */
public class RadioWSQK {
    private transient SistemaLog logger;
    private AtomicInteger contadorSangre = new AtomicInteger(0);
    private List<Nino> niños= new CopyOnWriteArrayList<>();
    public RadioWSQK(SistemaLog logger){
        this.logger=logger;
    }

    /**
     * Incrementa el contador global de sangre en 1 unidad. Llamado por cada
     * niño al volver del Upside Down; AtomicInteger garantiza atomicidad
     * sin necesidad de bloque synchronized.
     */
    public void depositarSangre(Nino n){
        logger.escribirLog("Sangre aumentada por: " +  n.getIdentificador() +
                " Sangre Total actual: " + contadorSangre.incrementAndGet());
    }

    /** Registra la llegada del niño a la Radio para descanso (2-4 s). */
    public void entrar(Nino n){
        niños.add(n);
        logger.escribirLog(n.getIdentificador() + " acaba de entrar a RadioWSQK. Niños en la RadioWSQK: " + niños.size());
    }

    /** Registra la salida del niño de la Radio para ir a la Calle Principal. */
    public void salir(Nino n){
        niños.remove(n);
        logger.escribirLog(n.getIdentificador() + " sale de la RadioWSQK. Niños en la RadioWSQK: " + niños.size());
    }

    /** Devuelve el total acumulado de unidades de sangre de Vecna recolectadas. */
    public int getSangre(){
        int sangre = contadorSangre.get();
        return sangre;
    }

    /**
     * Ajusta el contador de sangre. Llamado por GestorEventos al terminar
     * la Intervención de Eleven para descontar las unidades usadas en liberaciones.
     */
    public void setSangre(int n){
        contadorSangre.set(n);
    }

    /** Devuelve la lista viva de niños que descansan actualmente en la Radio. */
    public List getNiños(){
        return niños;
    }
}
