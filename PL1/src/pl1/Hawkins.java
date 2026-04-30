/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

/**
 *
 * @author daniel
 */
public class Hawkins {
    private RadioWSQK radioWSBK;
    private CallePrincipal callePrincipal;
    private Sotano sotanoByers;
    
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
    
}
