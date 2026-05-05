/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

/**
 *
 * @author Alejandro
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Interface extends Remote {
    // Funciones de control
    void Pausar() throws RemoteException;
    void Reanudar() throws RemoteException;
    
    // Funciones de consultas
    int niñosHawkins() throws RemoteException;
    int niñosPortalBosque() throws RemoteException;
    int niñosPortalAlcantarillado() throws RemoteException;
    int niñosPortalLaboratorio() throws RemoteException;
    int niñosPortalCentroComercial() throws RemoteException;
    int niñosBosque() throws RemoteException;
    int niñosAlcantarillado() throws RemoteException;
    int niñosLaboratorio() throws RemoteException;
    int niñosCentroComercial() throws RemoteException;
    int demosBosque() throws RemoteException;
    int demosAlcantarillado() throws RemoteException;
    int demosLaboratorio() throws RemoteException;
    int demosCentroComercial() throws RemoteException;
    int niñosColmena() throws RemoteException;
    int tiempoEvento() throws RemoteException;
    List rankingTop3Demos() throws RemoteException;
    String estadoEvento() throws RemoteException;
}