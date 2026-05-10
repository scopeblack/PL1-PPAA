/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Zona de preparación en Hawkins desde donde los niños acceden a los portales.
 * Los niños eligen su zona de destino y el Sótano los encamina 
 * al portal correspondiente mediante irUpsideDown().
 * El constructor recibe los portales mediante varargs:
 * [0]=Bosque, [1]=Alcantarillado, [2]=Centro Comercial, [3]=Laboratorio.
 *
 * @author Alejandro
 */
public class Sotano {
    private Portal portalBosque;
    private Portal portalAlcantarillado;
    private Portal portalLaboratorio;
    private Portal portalCentroComercial;
    private AtomicInteger contadorNiñosSotano = new AtomicInteger(0);
    private List<Nino> niños = new CopyOnWriteArrayList<>();
    private transient SistemaLog logger;

    public Sotano(SistemaLog logger,Portal... portal){
        this.portalBosque=portal[0];
        this.portalAlcantarillado=portal[1];
        this.portalCentroComercial=portal[2];
        this.portalLaboratorio=portal[3];
        this.logger=logger;
    }

    /** Devuelve el portal que conecta con el Bosque (tamaño de grupo: 2). */
    public Portal getPortalBosque() {
        return portalBosque;
    }

    /** Devuelve el portal que conecta con el Alcantarillado (tamaño de grupo: 2). */
    public Portal getPortalAlcantarillado() {
        return portalAlcantarillado;
    }

    /** Devuelve el portal que conecta con el Laboratorio (tamaño de grupo: 3). */
    public Portal getPortalLaboratorio() {
        return portalLaboratorio;
    }

    /** Devuelve el portal que conecta con el Centro Comercial (tamaño de grupo: 4). */
    public Portal getPortalCentroComercial() {
        return portalCentroComercial;
    }

    /** El niño sale del Sótano hacia el portal; se decrementa el contador. */
    public void salir(Nino n){
        niños.remove(n);
        logger.escribirLog(n.getIdentificador() + " sale del sótano. Niños en el sótano: " + contadorNiñosSotano.decrementAndGet());
    }

    /** Registra un regreso al Sótano. */
    public void regresar(Nino n){
        logger.escribirLog(n.getIdentificador() + " regresa al sótano. Niños en el sótano: " + contadorNiñosSotano.incrementAndGet());
    }

    /** El niño entra al Sótano para prepararse; se incrementa el contador. */
    public void entrar(Nino n){
        niños.add(n);
        String id = n.getIdentificador();
        logger.escribirLog(id + " acaba de entrar al sótano. Niños en el sótano: " + contadorNiñosSotano.incrementAndGet());
    }

    /**
     * Redirige al niño al portal correspondiente a la zona
     * elegida. El niño ya ha decidido su destino en Nino.run() antes de llamar aquí.
     */
    public void irUpsideDown(Nino n, String zona) throws InterruptedException, BrokenBarrierException{
        switch(zona){
            case "bosque":
                irBosque(n);
                break;
            case "alcantarillado":
                irAlcantarillado(n);
                break;
            case "laboratorio":
                irLaboratorio(n);
                break;
            case "centroComercial":
                irCentroComercial(n);
                break;
        }
    }

    /** Saca al niño del Sótano y lo encola en el portal del Bosque. */
    private void irBosque(Nino n) throws InterruptedException, BrokenBarrierException{
        salir(n);
        portalBosque.formarGrupoYEntrar(n);
    }

    /** Saca al niño del Sótano y lo encola en el portal del Alcantarillado. */
    private void irAlcantarillado(Nino n) throws InterruptedException, BrokenBarrierException{
        salir(n);
        portalAlcantarillado.formarGrupoYEntrar(n);
    }

    /** Saca al niño del Sótano y lo encola en el portal del Laboratorio. */
    private void irLaboratorio(Nino n) throws InterruptedException, BrokenBarrierException{
        salir(n);
        portalLaboratorio.formarGrupoYEntrar(n);
    }

    /** Saca al niño del Sótano y lo encola en el portal del Centro Comercial. */
    private void irCentroComercial(Nino n) throws InterruptedException, BrokenBarrierException{
        salir(n);
        portalCentroComercial.formarGrupoYEntrar(n);
    }

    /** Devuelve la lista de niños actualmente en el Sótano (usada por la Interfaz). */
    public List getNiños(){
        return niños;
    }
    
    
}
