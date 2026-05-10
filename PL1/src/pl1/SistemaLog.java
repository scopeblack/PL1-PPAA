/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;
import java.util.logging.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author Alejandro
 */

/**
 * Recurso compartido para escritura de logs en "hawkins.txt".
 * java.util.logging.Logger ya es thread-safe internamente, por lo que no
 * se necesita sincronización adicional en escribirLog(). El bloque estático
 * configura el FileHandler una sola vez al cargar la clase.
 */
public class SistemaLog {


    private static final Logger logger = Logger.getLogger("Logger");

    static {
        try {
            // El true para que haga -> append
            FileHandler fh = new FileHandler("hawkins.txt", true);

            // Configuramos el texto que queremos.
            fh.setFormatter(new Formatter() {
                            @Override
                            public String format(LogRecord record) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                String fecha = sdf.format(new Date(record.getMillis()));

                                return String.format("%s: %s%n", 
                                        fecha,  
                                        record.getMessage() + "\n");
                            }
                        });
            logger.addHandler(fh);

            logger.setUseParentHandlers(false); //<- Evita que salgan por terminal, poner a true si se quiere
        } catch (IOException e) {
            System.out.println("No se pudo crear el archivo de log: " + e.getMessage());
        }
    }

    /**
     * Escribe una línea en el fichero hawkins.txt con marca de tiempo.
     * java.util.logging.Logger serializa internamente las escrituras, por lo
     * que múltiples hilos pueden llamar a este método sin riesgo de condiciones de carrera.
     */
    public void escribirLog(String mensaje) {
        logger.info(mensaje);
    }

   
}
    

