/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Alejandro
 */
public class RadioWSQK {
    private AtomicInteger contadorSangre = new AtomicInteger(0);
    private List<Nino> niños= new CopyOnWriteArrayList<>();
    public RadioWSQK(){    
    }
    
    public void depositarSangre(Nino n){
        
        System.out.println("Sangre aumentada por: " +  n.getIdentificador() + 
                " Sangre Total actual: " + contadorSangre.incrementAndGet());
        
    }
    
    public synchronized void entrar(Nino n){
        niños.add(n);
        System.out.println(n.getIdentificador() + " acaba de entrar a RadioWSQK. Niños en la RadioWSQK: " + niños.size());
    }
    
    public synchronized void salir(Nino n){
        niños.remove(n);
        System.out.println(n.getIdentificador() + " sale de la RadioWSQK. Niños en la RadioWSQK: " + niños.size());
    }
    
    public int getSangre(){
        int sangre = contadorSangre.get();
        return sangre;
    }
    
    public void setSangre(int n){
        contadorSangre.set(n);
    }
    
    public synchronized List getNiños(){
        return niños;
    }
}
