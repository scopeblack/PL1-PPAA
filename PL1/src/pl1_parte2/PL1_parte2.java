/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pl1_parte2;

import javax.swing.SwingUtilities;

/**
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

        // Utilizamos invokeLater para que la interfaz se ejecute en el hilo correcto
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Creamos la instancia del JFrame
                ModuloRemoto ventanaRemota = new ModuloRemoto();

                // Centramos la ventana en la pantalla (opcional)
                ventanaRemota.setLocationRelativeTo(null);

                // Hacemos visible la ventana
                ventanaRemota.setVisible(true);

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
