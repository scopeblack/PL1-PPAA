/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.ArrayList;
import java.util.List;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Alejandro
 */
public class GestorPausa extends UnicastRemoteObject implements Interface {   //Esta clase se usará para reemplazar el apaño de Pausa/Reanudar en la interfaz.
    private Hawkins hawkins;
    private UpsideDown upsideDown;
    private GestorEventos gestor;
    private AtomicBoolean pausa = new AtomicBoolean(false);
    
    public GestorPausa(Hawkins h, UpsideDown u, GestorEventos g) throws RemoteException {
        super(); 
        this.hawkins = h;
        this.upsideDown = u;
        this.gestor = g;
    }

    
    

    @Override
    public void Pausar() throws RemoteException {
                List<Demogorgon> demos = upsideDown.getDemogorgons();
            List<Nino> niños = hawkins.getNiños();
            
            gestor.setPausado(true);
            gestor.interrupt();
            hawkins.setPausado(true);
            for(Demogorgon d: demos){
                d.setPausado(true);
            }
            
            for(Nino n: niños){
                n.setPausado(true);
            }
            System.out.println("Sistema Pausado Remotamente");
    }
        
    @Override
    public void Reanudar() throws RemoteException {
                List<Demogorgon> demos = upsideDown.getDemogorgons();
            List<Nino> niños = hawkins.getNiños();
            gestor.setPausado(false);
            synchronized (gestor) {
                gestor.notify();
            }
            
            for(Demogorgon d: demos){
                d.setPausado(false);
                synchronized (d) {
                    d.notify();
                }
            }
            
            for(Nino n: niños){
                n.setPausado(false);
                synchronized (n) {
                    n.notify();
                }
            }
            
            hawkins.setPausado(false);
            synchronized (hawkins) {
                hawkins.notify();
            }
            System.out.println("Sistema Reanudado Remotamente");
            
    }
    
    

}
