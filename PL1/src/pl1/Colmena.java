/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Almacén de víctimas del Upside Down. Los niños capturados esperan aquí
 * bloqueados (via wait() en Nino) hasta que Eleven los libera durante
 * el evento de Intervención de Eleven. CopyOnWriteArrayList permite que
 * GestorEventos itere la lista mientras Demogorgon la modifica.
 *
 * @author Alejandro
 */
public class Colmena {
    private AtomicInteger contador = new AtomicInteger(0);
    private transient SistemaLog logger;
    private List<Nino> niños = new CopyOnWriteArrayList<>();
    public Colmena(SistemaLog logger){
        this.logger=logger;
    }

    /**
     * Añade al niño a la Colmena. Llamado por UpsideDown.enviarNiñoColmena().
     * El niño permanece en wait() hasta que Eleven lo libere con sacarNiñoColmena().
     */
    public void enviarNiñoColmena(Nino n){
        niños.add(n);
        logger.escribirLog(n.getIdentificador() + " ha sido retenido en la colmena. Total de niños: " + niños.size());
    }

    /**
     * Extrae al niño de la Colmena durante la Intervención de Eleven.
     * Tras este llamado, GestorEventos invoca n.setLiberado() para
     * despertar al hilo del niño de su wait().
     */
    public void sacarNiñoColmena(Nino n){
        niños.remove(n);
        logger.escribirLog(n.getIdentificador() + " Ha sido liberado de la colmena por Eleven. Niños restantes en la colmena: " + niños.size());
    }

    /** Devuelve la lista de niños capturados actualmente en la Colmena. */
    public List getNiños(){
        return niños;
    }

    /** Devuelve el número de niños retenidos en la Colmena. */
    public int getCantidadNiños(){
        return niños.size();
    }
}
