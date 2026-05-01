/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Alejandro
 */
public class Colmena {
    private AtomicInteger contador = new AtomicInteger(0);
    private List<Nino> niños = new CopyOnWriteArrayList<>();
    public Colmena(){}
    
    public void enviarNiñoColmena(Nino n){
        niños.add(n);
        System.out.println(n.getIdentificador() + " ha sido retenido en la colmena. Total de niños: " + niños.size());
    }
    
    public void sacarNiñoColmena(Nino n){
        niños.remove(n);
        System.out.println(n.getIdentificador() + " Ha sido liberado de la colmena por Eleven. Niños restantes en la colmena: " + niños.size());
    }
    
    public List getNiños(){
        return niños;
    }
    
    public int getCantidadNiños(){
        return niños.size();
    }
}
