/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Alejandro
 */
public class Colmena {
    private AtomicInteger contador = new AtomicInteger(0);
    public Colmena(){}
    
    public void enviarNiñoColmena(Nino n){
        System.out.println(n.getIdentificador() + " ha sido retenido en la colmena. Total de niños: " + contador.incrementAndGet());
    }
}
