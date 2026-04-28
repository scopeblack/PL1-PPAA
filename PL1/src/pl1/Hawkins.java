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
    private ZonaHawkins radioWSBK;
    private ZonaHawkins callePrincipal;
    private Sotano sotanoByers;
    
    public Hawkins(ZonaHawkins c, ZonaHawkins r, Sotano s){
        this.radioWSBK = r;
        this.callePrincipal = c;
        this.sotanoByers = s;
    }

    public ZonaHawkins getRadioWSBK() {
        return radioWSBK;
    }

    public ZonaHawkins getCallePrincipal() {
        return callePrincipal;
    }

    public Sotano getSotanoByers() {
        return sotanoByers;
    }
    
}
