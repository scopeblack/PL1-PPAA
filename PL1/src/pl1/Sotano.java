/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.concurrent.BrokenBarrierException;
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
    
    public Sotano(Portal... portal){
        this.portalBosque=portal[0];
        this.portalAlcantarillado=portal[1];
        this.portalCentroComercial=portal[2];
        this.portalLaboratorio=portal[3];
    }
    
    public void salir(Nino n){
        System.out.println(n.getIdentificador() + " sale del sótano. Niños en el sótano: " + contadorNiñosSotano.decrementAndGet());
    }
    
    public void irUpsideDown(Nino n, String zona) throws InterruptedException, BrokenBarrierException{
        String id = n.getIdentificador();
        System.out.println(id + " acaba de entrar al sótano. Niños en el sótano: " + contadorNiñosSotano.incrementAndGet());
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
        portalBosque.entrarPortal(n);
    }
    private void irAlcantarillado(Nino n) throws InterruptedException, BrokenBarrierException{
        portalAlcantarillado.entrarPortal(n);
    }
    private void irLaboratorio(Nino n) throws InterruptedException, BrokenBarrierException{
        portalLaboratorio.entrarPortal(n);
    }
    private void irCentroComercial(Nino n) throws InterruptedException, BrokenBarrierException{
        portalCentroComercial.entrarPortal(n);
    }
    
    
}
