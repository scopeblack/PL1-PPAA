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

public interface Interface extends Remote {
    void Pausar() throws RemoteException;
    void Reanudar() throws RemoteException;
}