/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.List;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Objeto remoto RMI que muestra el estado del sistema al módulo de la Parte 2.
 * Pausar()/Reanudar() propagan el flag a todos los hilos activos (niños,
 * demogorgons, portales y gestor de eventos) para detener el sistema completo.
 *
 * @author Alejandro
 */
public class GestorRemoto extends UnicastRemoteObject implements Interface {

    private Hawkins hawkins;
    private UpsideDown upsideDown;
    private GestorEventos gestor;
    private AtomicBoolean pausa = new AtomicBoolean(false);
    private transient SistemaLog logger;

    public GestorRemoto(Hawkins h, UpsideDown u, GestorEventos g, SistemaLog logger) throws RemoteException {
        super();
        this.hawkins = h;
        this.upsideDown = u;
        this.gestor = g;
        this.logger = logger;
    }

    /**
     * Detiene todo el sistema: propaga el flag de pausa al gestor de eventos
     * (interrumpiéndolo para sacarlo de su sleep), a todos los niños,
     * demogorgons y portales. Cada hilo se bloqueará en su comprobarPausado().
     */
    @Override
    public void Pausar() throws RemoteException {
        List<Demogorgon> demos = upsideDown.getDemogorgons();
        List<Nino> niños = hawkins.getNiños();

        gestor.setPausado(true);
        gestor.interrupt();
        hawkins.setPausado(true);
        for (Demogorgon d : demos) {
            d.setPausado(true);
        }

        for (Nino n : niños) {
            n.setPausado(true);
        }
        hawkins.getSotanoByers().getPortalBosque().setPausado(true);
        hawkins.getSotanoByers().getPortalAlcantarillado().setPausado(true);
        hawkins.getSotanoByers().getPortalCentroComercial().setPausado(true);
        hawkins.getSotanoByers().getPortalLaboratorio().setPausado(true);
        logger.escribirLog("\n-----------------------Sistema Pausado Remotamente--------------------------------\n");
    }

    /**
     * Reanuda todo el sistema: desactiva el flag de pausa y llama a notify()
     * sobre cada objeto bloqueado para que salgan del wait() de comprobarPausado().
     */
    @Override
    public void Reanudar() throws RemoteException {
        List<Demogorgon> demos = upsideDown.getDemogorgons();
        List<Nino> niños = hawkins.getNiños();
        gestor.setPausado(false);
        synchronized (gestor) {
            gestor.notify();
        }

        for (Demogorgon d : demos) {
            d.setPausado(false);
            synchronized (d) {
                d.notify();
            }
        }

        for (Nino n : niños) {
            n.setPausado(false);
            synchronized (n) {
                n.notify();
            }
        }

        hawkins.setPausado(false);
        synchronized (hawkins) {
            hawkins.notify();
        }
        hawkins.getSotanoByers().getPortalBosque().setPausado(false);
        hawkins.getSotanoByers().getPortalAlcantarillado().setPausado(false);
        hawkins.getSotanoByers().getPortalCentroComercial().setPausado(false);
        hawkins.getSotanoByers().getPortalLaboratorio().setPausado(false);
        logger.escribirLog("\n-------------------------Sistema Reanudado Remotamente---------------------------------\n");
    }

    /** Número de niños retenidos en la Colmena en este instante. */
    @Override
    public int niñosColmena() throws RemoteException {
        return upsideDown.getColmena().getNiños().size();
    }

    /** Número de niños en las tres zonas de Hawkins (Calle, Sótano y Radio). */
    @Override
    public int niñosHawkins() throws RemoteException {
        return hawkins.numeroNinos();
    }

    /** Número de niños cruzando actualmente el portal del Bosque. */
    @Override
    public int niñosPortalBosque() throws RemoteException {
        return hawkins.getSotanoByers().getPortalBosque().getNiños().size();
    }

    /** Número de niños cruzando actualmente el portal del Alcantarillado. */
    @Override
    public int niñosPortalAlcantarillado() throws RemoteException {
        return hawkins.getSotanoByers().getPortalAlcantarillado().getNiños().size();
    }

    /** Número de niños cruzando actualmente el portal del Laboratorio. */
    @Override
    public int niñosPortalLaboratorio() throws RemoteException {
        return hawkins.getSotanoByers().getPortalLaboratorio().getNiños().size();
    }

    /** Número de niños cruzando actualmente el portal del Centro Comercial. */
    @Override
    public int niñosPortalCentroComercial() throws RemoteException {
        return hawkins.getSotanoByers().getPortalCentroComercial().getNiños().size();
    }

    /** Número de niños en la zona del Bosque del Upside Down. */
    @Override
    public int niñosBosque() throws RemoteException {
        return upsideDown.getBosque().getNiños().size();
    }

    /** Número de niños en la zona del Alcantarillado del Upside Down. */
    @Override
    public int niñosAlcantarillado() throws RemoteException {
        return upsideDown.getAlcantarillado().getNiños().size();
    }

    /** Número de niños en la zona del Laboratorio del Upside Down. */
    @Override
    public int niñosLaboratorio() throws RemoteException {
        return upsideDown.getLaboratorio().getNiños().size();
    }

    /** Número de niños en la zona del Centro Comercial del Upside Down. */
    @Override
    public int niñosCentroComercial() throws RemoteException {
        return upsideDown.getCentroComercial().getNiños().size();
    }

    /** Número de Demogorgons en la zona del Bosque. */
    @Override
    public int demosBosque() throws RemoteException {
        return upsideDown.getBosque().getDemogorgons().size();
    }

    /** Número de Demogorgons en la zona del Alcantarillado. */
    @Override
    public int demosAlcantarillado() throws RemoteException {
        return upsideDown.getAlcantarillado().getDemogorgons().size();
    }

    /** Número de Demogorgons en la zona del Laboratorio. */
    @Override
    public int demosLaboratorio() throws RemoteException {
        return upsideDown.getLaboratorio().getDemogorgons().size();
    }

    /** Número de Demogorgons en la zona del Centro Comercial. */
    @Override
    public int demosCentroComercial() throws RemoteException {
        return upsideDown.getCentroComercial().getDemogorgons().size();
    }

    /** Devuelve la lista de Demogorgons ordenada por capturas (máximo top-3). */
    @Override
    public List<String> rankingTop3Demos() throws RemoteException {
        return upsideDown.rankingTop3Demos();
    }

    /** Nombre del evento global activo, o null si no hay ninguno. */
    @Override
    public String estadoEvento() throws RemoteException {
        return gestor.getEvento();
    }

    /** Segundos restantes del evento activo. */
    @Override
    public int tiempoEvento() throws RemoteException {
        return (int) (gestor.getTiempoRestante() / 1000);
    }

}
