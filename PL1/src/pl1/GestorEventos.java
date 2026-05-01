/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alejandro
 */
public class GestorEventos extends Thread {
    private Hawkins hawkins;
    private UpsideDown upsideDown;
    private String eventoActivo = null;
    public GestorEventos(Hawkins h, UpsideDown u){
        this.hawkins = h;
        this.upsideDown = u;
    }
    
    public void apagonLaboratorio(){
    }
    
    public void tormentaUpsideDown(){
    }
    
    public void intervencionEleven(){
    eventoActivo = "Intervención de Eleven";
    int sangre = hawkins.getRadioWSBK().getSangre();
    List<Nino> listaColmena = upsideDown.getColmena().getNiños();
    
    System.out.println("Eleven liberando niños con " + sangre + " de sangre");
    int liberados = 0;
    
    // Usamos un bucle controlado para evitar ConcurrentModificationException
    while(listaColmena.size() > 0 && sangre > 0){
        Nino n = listaColmena.get(0); 
        upsideDown.getColmena().sacarNiñoColmena(n);
        n.setLiberado(); // Esto hace el notify() interno en el Nino
        sangre--;
        liberados++;
    }
    hawkins.getRadioWSBK().setSangre(hawkins.getRadioWSBK().getSangre() - liberados);
}
    
    public void redMental(){
    }
    
    public String getEvento(){
        return eventoActivo;
    }
    public void run(){
        int i = (int)(4*Math.random());
        while(true){
            try{
                sleep((long)(30000 + Math.random()*30000));
                switch (i) {
                    case 0:
                        // apagonLaboratorio();
                        //intervencionEleven();
                        break;
                    case 1:
                        // tormentaUpsideDown();
                        //intervencionEleven();
                        break;
                    case 2:
                        //intervencionEleven();
                        break;
                    case 3:
                        // redMental();
                        //intervencionEleven();
                        break;
                    default:
                        throw new AssertionError();
                }
                i = (int)(4*Math.random());
            }catch(InterruptedException e){e.printStackTrace();}
        }
    }
}
