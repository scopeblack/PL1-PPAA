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

        // Por defecto el Logger ya es thread-safe
    public void escribirLog(String mensaje) {
        logger.info(mensaje);
    }

   
}
    

