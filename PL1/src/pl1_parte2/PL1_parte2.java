/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pl1_parte2;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada del cliente remoto (Parte 2). Crea la ventana ModuloRemoto
 * y arranca un hilo que consulta el estado del servidor
 * cada 100 ms. Debe ejecutarse con el servidor PL1 ya en marcha y el registro
 * RMI en el puerto 1099 disponible.
 *
 * @author Alejandro
 */
public class PL1_parte2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            System.out.println("Error de codificación");
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ModuloRemoto ventanaRemota = new ModuloRemoto();

                // Centramos la ventana en la pantalla
                ventanaRemota.setLocationRelativeTo(null);

                ventanaRemota.setVisible(true);

                // Nuevo hilo que actualiza la interfaz con datos remotos cada 100 ms
                new Thread(() -> {
                    while (true) {
                        ventanaRemota.actualizarDatos();

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}
