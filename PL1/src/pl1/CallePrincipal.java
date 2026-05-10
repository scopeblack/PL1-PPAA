/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Zona segura de Hawkins donde los niños hacen spawn inicial y descansan
 * entre ciclos para no levantar sospechas. No requiere exclusión mutua propia
 * porque CopyOnWriteArrayList garantiza visibilidad segura entre hilos.
 *
 * @author Alejandro
 */
public class CallePrincipal {
    private List<Nino> niños= new CopyOnWriteArrayList<>();
    private transient SistemaLog logger;
    public CallePrincipal(SistemaLog logger){
        this.logger=logger;
    }

    /** Elimina al niño de la Calle Principal al iniciar el ciclo hacia el Sótano. */
    public void salir(Nino n){
        niños.remove(n);
        logger.escribirLog(n.getIdentificador() + " sale de la Calle Principal. Niños en la Calle Principal: " + niños.size());
    }

    /**
     * Añade al niño a la Calle Principal. Se llama en dos situaciones:
     * al hacer spawn inicial y al regresar del ciclo completo.
     */
    public void entrar(Nino n){
        niños.add(n);
        logger.escribirLog(n.getIdentificador() + " entra a la Calle Principal. Niños en la Calle Principal: " + niños.size());
    }

    /** Devuelve la lista de niños en la Calle Principal (usada por la Interfaz). */
    public List getNiños(){
        return niños;
    }
          
}
