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
    private SistemaLog logger;
    
    public GestorPausa(Hawkins h, UpsideDown u, GestorEventos g, SistemaLog logger) throws RemoteException {
        super(); 
        this.hawkins = h;
        this.upsideDown = u;
        this.gestor = g;
        this.logger = logger;
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
            logger.escribirLog("\n-----------------------Sistema Pausado Remotamente--------------------------------\n");
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
            logger.escribirLog("\n-------------------------Sistema Reanudado Remotamente---------------------------------\n");
            
    }
    
    @Override
    public int niñosColmena() throws RemoteException{
        return upsideDown.getColmena().getNiños().size();
    }
    
    @Override
    public int niñosHawkins() throws RemoteException {
        return hawkins.getNiños().size();
    }

    @Override
    public int niñosPortalBosque() throws RemoteException {
        return hawkins.getSotanoByers().getPortalBosque().getNiños().size();
    }

    @Override
    public int niñosPortalAlcantarillado() throws RemoteException {
        return hawkins.getSotanoByers().getPortalAlcantarillado().getNiños().size();
    }

    @Override
    public int niñosPortalLaboratorio() throws RemoteException {
        return hawkins.getSotanoByers().getPortalLaboratorio().getNiños().size();
    }

    @Override
    public int niñosPortalCentroComercial() throws RemoteException {
        return hawkins.getSotanoByers().getPortalCentroComercial().getNiños().size();
    }

    @Override
    public int niñosBosque() throws RemoteException {
        return upsideDown.getBosque().getNiños().size();
    }

    @Override
    public int niñosAlcantarillado() throws RemoteException {
        return upsideDown.getAlcantarillado().getNiños().size();
    }

    @Override
    public int niñosLaboratorio() throws RemoteException {
        return upsideDown.getLaboratorio().getNiños().size();
    }

    @Override
    public int niñosCentroComercial() throws RemoteException {
        return upsideDown.getCentroComercial().getNiños().size();
    }

    @Override
    public int demosBosque() throws RemoteException {
        return upsideDown.getBosque().getDemogorgons().size();
    }

    @Override
    public int demosAlcantarillado() throws RemoteException {
        return upsideDown.getAlcantarillado().getDemogorgons().size();
    }

    @Override
    public int demosLaboratorio() throws RemoteException {
        return upsideDown.getLaboratorio().getDemogorgons().size();
    }

    @Override
    public int demosCentroComercial() throws RemoteException {
        return upsideDown.getCentroComercial().getDemogorgons().size();
    }

    @Override
    public List<String> rankingTop3Demos() throws RemoteException {
        return upsideDown.rankingTop3Demos();
    }

    @Override
    public String estadoEvento() throws RemoteException {
        return gestor.getEvento();
    }
    
    @Override
    public int tiempoEvento() throws RemoteException{
        return (int)(gestor.getTiempoRestante()/1000);
    }
    
    

}
