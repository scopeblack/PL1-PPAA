/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pl1;

public class PL1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        
        new Demogorgon(0).start();
        ZonaHawkins callePrincipal= new ZonaHawkins();
        ZonaHawkins radioWSQK= new ZonaHawkins();
        ZonaHawkins sotanoByers= new ZonaHawkins();
        
        Portal portal1
        
        
        ZonaUpsideDown bosque = new ZonaUpsideDown();
        ZonaUpsideDown laboratorio = new ZonaUpsideDown();
        ZonaUpsideDown centroComercial = new ZonaUpsideDown();
        ZonaUpsideDown alcantarillado = new ZonaUpsideDown();
        
        Colmena colmena = new Colmena();
        
        GestorEventos gestor = new GestorEventos();
        
        try{
            for(int i=0; i<1500; i++){

                new Nino(i).start();
                Thread.sleep(500 + (int)(Math.random()*1500));
            }
        }
        catch(InterruptedException ex){
            ex.printStackTrace();
        }

        
    }
    
}
