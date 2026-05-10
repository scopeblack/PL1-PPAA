/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Contenedor de las tres zonas seguras de Hawkins (Calle Principal,
 * Sótano Byers y Radio WSQK). Además mantiene la lista maestra de todos
 * los niños del sistema, necesaria para que GestorEventos y GestorRemoto
 * puedan iterar sobre ellos y propagar flags de evento/pausa.
 *
 * @author daniel
 */
public class Hawkins {
    private RadioWSQK radioWSBK;
    private CallePrincipal callePrincipal;
    private Sotano sotanoByers;
    private List<Nino> niños = new CopyOnWriteArrayList<>();
    private AtomicBoolean pausado = new AtomicBoolean(false);

    public Hawkins(CallePrincipal c, RadioWSQK r, Sotano s){
        this.radioWSBK = r;
        this.callePrincipal = c;
        this.sotanoByers = s;
    }

    /** Devuelve la Radio WSQK, donde los niños depositan la sangre de Vecna. */
    public RadioWSQK getRadioWSBK() {
        return radioWSBK;
    }

    /** Devuelve la Calle Principal, zona de spawn y descanso de los niños. */
    public CallePrincipal getCallePrincipal() {
        return callePrincipal;
    }

    /** Devuelve el Sótano Byers, punto de acceso a los portales del Upside Down. */
    public Sotano getSotanoByers() {
        return sotanoByers;
    }

    /**
     * Devuelve la lista maestra de todos los niños del sistema.
     * GestorEventos y GestorRemoto la usan para propagar flags globales.
     */
    public List getNiños(){
        return niños;
    }
    
    // Solo cuenta los niños dentro de Hawkins (no los que están en portales ni en el Upside Down)
    public int numeroNinos(){
        return radioWSBK.getNiños().size() + callePrincipal.getNiños().size() + sotanoByers.getNiños().size();
    }

    /** 
     * Función para añadir nuevos niños desde el Main.
     * Bloquea la creación escalonada de niños mientras el sistema esté pausado,
     * de modo que no se instancien nuevos hilos durante la pausa
     */
    public void almacenarNino(Nino n){
        try{
            synchronized (this) {
                while(pausado.get()){
                    this.wait();
                }
            }
        }catch(InterruptedException e){}
        niños.add(n);
    }
    
    /**
     * Activa o desactiva la pausa del sistema para la creación escalonada
     * de niños. Al pausar, almacenarNino() queda bloqueado en wait().
     * El notify() lo hace GestorRemoto.Reanudar() sobre el monitor de Hawkins.
     */
    public void setPausado(boolean b){
        pausado.set(b);
    }
    
}
