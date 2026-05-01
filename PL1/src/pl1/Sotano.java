/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
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
    
    public Sotano(Portal... portal){
        this.portalBosque=portal[0];
        this.portalAlcantarillado=portal[1];
        this.portalCentroComercial=portal[2];
        this.portalLaboratorio=portal[3];
    }

    public Portal getPortalBosque() {
        return portalBosque;
    }

    public Portal getPortalAlcantarillado() {
        return portalAlcantarillado;
    }

    public Portal getPortalLaboratorio() {
        return portalLaboratorio;
    }

    public Portal getPortalCentroComercial() {
        return portalCentroComercial;
    }
    
    public void salir(Nino n){
        niños.remove(n);
        System.out.println(n.getIdentificador() + " sale del sótano. Niños en el sótano: " + contadorNiñosSotano.decrementAndGet());
    }
    public void regresar(Nino n){
        System.out.println(n.getIdentificador() + " regresa al sótano. Niños en el sótano: " + contadorNiñosSotano.incrementAndGet());
    }
    
    public void entrar(Nino n){
        niños.add(n);
        String id = n.getIdentificador();
        System.out.println(id + " acaba de entrar al sótano. Niños en el sótano: " + contadorNiñosSotano.incrementAndGet());
    }
    
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
    
    private void irBosque(Nino n) throws InterruptedException, BrokenBarrierException{
        salir(n);
        portalBosque.formarGrupoYEntrar(n);
    }
    private void irAlcantarillado(Nino n) throws InterruptedException, BrokenBarrierException{
        salir(n);
        portalAlcantarillado.formarGrupoYEntrar(n);
    }
    private void irLaboratorio(Nino n) throws InterruptedException, BrokenBarrierException{
        salir(n);
        portalLaboratorio.formarGrupoYEntrar(n);
    }
    private void irCentroComercial(Nino n) throws InterruptedException, BrokenBarrierException{
        salir(n);
        portalCentroComercial.formarGrupoYEntrar(n);
    }
    
    public List getNiños(){
        return niños;
    }
    
    
}
