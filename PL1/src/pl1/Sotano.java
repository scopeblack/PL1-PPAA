/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

/**
 *
 * @author Alejandro
 */
public class Sotano {
    private Portal portalBosque;
    private Portal portalAlcantarillado;
    private Portal portalLaboratorio;
    private Portal portalCentroComercial;
    
    public Sotano(Portal... portal){
        this.portalBosque=portal[0];
        this.portalAlcantarillado=portal[1];
        this.portalCentroComercial=portal[2];
        this.portalLaboratorio=portal[3];
    }
}
