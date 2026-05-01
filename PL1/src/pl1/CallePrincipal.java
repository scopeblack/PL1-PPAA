/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Alejandro
 */
public class CallePrincipal {
    private List<Nino> niños= new CopyOnWriteArrayList<>();
    public CallePrincipal(){    
    }
    
    public synchronized void salir(Nino n){
        niños.remove(n);
        System.out.println(n.getIdentificador() + " sale de la Calle Principal. Niños en la Calle Principal: " + niños.size());
    }
    public synchronized void entrar(Nino n){
        niños.add(n);
        System.out.println(n.getIdentificador() + " entra a la Calle Principal. Niños en la Calle Principal: " + niños.size());
    }
    
    public synchronized List getNiños(){
        return niños;
    }
          
}
