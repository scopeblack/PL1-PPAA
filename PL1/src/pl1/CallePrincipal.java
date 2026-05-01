/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public class CallePrincipal {
    private ArrayList<Nino> niños= new ArrayList<>();
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
    
    public synchronized ArrayList getNiños(){
        return niños;
    }
          
}
