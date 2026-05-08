/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
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

    public RadioWSQK getRadioWSBK() {
        return radioWSBK;
    }

    public CallePrincipal getCallePrincipal() {
        return callePrincipal;
    }

    public Sotano getSotanoByers() {
        return sotanoByers;
    }
    
    public List getNiños(){  
        return niños;

    }
    
    public int numeroNinos(){
        return radioWSBK.getNiños().size() + callePrincipal.getNiños().size() + sotanoByers.getNiños().size();
    }
    
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
    
    public void setPausado(boolean b){
        pausado.set(b);
    }
    
}
