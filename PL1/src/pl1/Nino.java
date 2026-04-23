/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

/**
 *
 * @author Alejandro
 */
public class Nino extends Thread{
    private int id;
    private String identificador;
    public Nino(int id){
        this.id=id;
        this.identificador="N"+id;
    }
}
