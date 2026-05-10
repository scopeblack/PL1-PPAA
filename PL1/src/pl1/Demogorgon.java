/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hilo que representa a un Demogorgon. Deambula entre las zonas inseguras
 * del Upside Down atacando niños. Implementa Comparable para el ranking de
 * capturas y Serializable para poder ser transferido por RMI en el top-3.
 * Los campos transient (hawkins, upsideDown, zona, logger) se excluyen de la
 * serialización porque son referencias locales no transmisibles por red.
 *
 * @author Alejandro
 */
public class Demogorgon extends Thread implements Comparable<Demogorgon>, Serializable {

    private int id;
    private String identificador;
    private transient Hawkins hawkins;
    private transient UpsideDown upsideDown;
    private AtomicInteger capturas = new AtomicInteger(0);
    private AtomicBoolean paralizadoPortales = new AtomicBoolean(false); // Evento Apagón del Laboratorio
    private AtomicBoolean tormenta = new AtomicBoolean(false); // Evento Tormenta del Upside Down
    private AtomicBoolean conexionMindFlayer = new AtomicBoolean(false); // Evento La Red Mental
    private AtomicBoolean paralizado = new AtomicBoolean(false); // Evento Intervención de Eleven
    private transient ZonaUpsideDown zona;
    private String zonaNombre;
    private AtomicBoolean pausado = new AtomicBoolean(false);
    private transient SistemaLog logger;

    /**
     * Construye el Demogorgon con id numérico y genera el identificador "DXXXX"
     * con ceros a la izquierda (por ejemplo id=0 → "D0000").
     * El id=0 corresponde al Demogorgon Alpha creado al inicio del sistema.
     */
    public Demogorgon(int id, Hawkins h, UpsideDown u, SistemaLog logger) {
        this.id = id;
        int digitos = contarDigitos(id);
        int cantidadCeros = 4 - digitos;
        this.identificador = "D" + "0".repeat(cantidadCeros) + id;
        this.hawkins = h;
        this.upsideDown = u;
        this.logger = logger;
    }

    /** Devuelve el identificador con formatoo "DXXXX" usado en logs e Interfaz. */
    public String getIdentificador() {
        return identificador;
    }

    public void run() {
        String[] zonasUpsideDown = {"bosque", "alcantarillado", "laboratorio", "centroComercial"};
        int i = (int) (4 * Math.random());
        while (true) {
            try {
                synchronized (this) {   //Si está paralizado...
                    while (paralizado.get()) {
                        logger.escribirLog(identificador + " está paralizado en " + zonaNombre);
                        this.wait();
                    }
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                continue;
            }
            comprobarPausado();
            Nino niño = null;
            if (zona != null) {
                zona.getDemogorgons().remove(this);   // Salir de la zona anterior antes de calcular la próxima
            }
            // Evento La Red Mental: el Demogorgon ignora su ruta aleatoria y va
            // directamente a la zona con más niños en ese instante
            if (conexionMindFlayer.get()) {
                String z1 = "";
                int max1 = 0;
                String z2 = "";
                int max2 = 0;
                HashMap<String, Integer> cantidades = new HashMap<>();
                if (upsideDown.getBosque().getNiños().size() > upsideDown.getAlcantarillado().getNiños().size()) {
                    z1 = "bosque";
                    max1 = upsideDown.getBosque().getNiños().size();
                } else {
                    z1 = "alcantarillado";
                    max1 = upsideDown.getAlcantarillado().getNiños().size();
                }
                cantidades.put(z1, max1);
                if (upsideDown.getCentroComercial().getNiños().size() > upsideDown.getLaboratorio().getNiños().size()) {
                    z2 = "centroComercial";
                } else {
                    z2 = "laboratorio";
                }
                cantidades.put(z2, max2);
                if (cantidades.get(z1) > cantidades.get(z2)) {
                    zonaNombre = z1;
                } else {
                    zonaNombre = z2;
                }

                zona = upsideDown.getZona(zonaNombre);

            } else {
                zonaNombre = zonasUpsideDown[i];
                zona = upsideDown.getZona(zonaNombre);
            }
            zona.getDemogorgons().add(this);


            comprobarPausado();
            try {
                niño = zona.elegir();
                if (niño != null) {
                    double tAtaque = 500 + 1000 * Math.random();
                    if (tormenta.get()) { // Si el evento tormenta del Upside Down está activo
                        tAtaque = tAtaque / 2; // Para simular que el tiempo entre ataques se reduce a la mitad
                    }
                    // El tiempo de ataque se fija en el niño antes de interrumpirlo
                    // para que cuando el niño despierte en esperar() ya tenga el valor correcto
                    niño.setTiempo(tAtaque);
                    boolean exito = (Math.random() <= 1.0 / 3.0);

                    // La captura se marca antes del interrupt() y del sleep para que
                    // si el niño llama a estaCapturado() durante el ataque, ya sepa su destino.
                    if (exito) {
                        niño.setCapturado();     //Determinamos la captura antes del sleep.
                    }
                    niño.interrupt();
                    sleep((long) tAtaque);
                    comprobarPausado();

                    if (exito) {

                        if (upsideDown.enviarNiñoColmena(niño, zona)) {
                            logger.escribirLog("----------------------------" + identificador + ": Ha capturado a " + niño.getIdentificador()
                                    + " en: " + zonaNombre + " (capturas: " + capturas.incrementAndGet() + ")" + "----------------------------");
                        }
                        sleep((long)(500 + 500*Math.random())); // Tiempo dejando al niño en la colmena
                        // terminarAtaque() se llama después de depositar al niño en la Colmena
                        // para evitar que ningún otro Demogorgon pueda atacarlo durante el traslado.
                        niño.terminarAtaque();

                    } else {
                        niño.terminarAtaque();

                        logger.escribirLog("----------------------------" + identificador + ": Ha fallado al capturar a " + niño.getIdentificador()
                                + " en: " + zonaNombre + "----------------------------");

                    }
                } else {
                    //No encuentra ningún niño al que pueda perseguir.
                    sleep((long) (4000 + 1000 * Math.random()));    //Descansa antes de volver a intentarlo.
                }
            } catch (InterruptedException ex) {
                Thread.interrupted(); // Limpiar flag
                logger.escribirLog(identificador + ": Ha sido paralizado por Eleven.");
                if (niño != null) {
                    niño.terminarAtaque();    //El demogorgon deja de atacarlo
                    logger.escribirLog("----------------------------" + identificador + ": Ha sido paralizado mientras atacaba a " + niño.getIdentificador()
                            + " en: " + zonaNombre + "----------------------------");
                    if(niño.getCapturado()){
                        niño.setLiberado();         //Si estaba planeado que lo capturase, ya no lo hará.
                    }
                    continue;
                }
            }

            if (!paralizadoPortales.get()) {    // Si el evento de apagón del laboratorio está activo, se salta el cambio de zona y permanece en ella
                int j = i;
                while (j == i) {    // Le forzamos a cambiar de lugar
                    i = (int) (4 * Math.random());     //Elige su próximo destino
                }
            }

        }
    }

    /** Devuelve el número total de niños capturados por este Demogorgon. */
    public int getCapturas() {
        return capturas.get();
    }

    /**
     * Orden descendente por capturas para el ranking top-3.
     * Se compara d.getCapturas() contra this.getCapturas() (invertido) para
     * que Collections.sort() coloque al que más ha capturado en la posición 0.
     */
    @Override
    public int compareTo(Demogorgon d) {
        return Integer.compare(d.getCapturas(), this.getCapturas());
    }

    /**
     * Activado por GestorEventos durante el Apagón del Laboratorio.
     * Impide que el Demogorgon cambie de zona al final de cada ciclo.
     */
    public void setParalizadoPortales(boolean b) {
        paralizadoPortales.set(b);
    }

    /**
     * Activado por GestorEventos durante la Tormenta del Upside Down.
     * Reduce a la mitad el tiempo de ataque (mayor agresividad).
     */
    public void setTormenta(boolean b) {
        tormenta.set(b);
    }

    /**
     * Activado por GestorEventos durante La Red Mental.
     * Hace que el Demogorgon calcule en cada ciclo la zona con más niños
     * en lugar de moverse aleatoriamente.
     */
    public void setConexionMindFlayer(boolean b) {
        conexionMindFlayer.set(b);
    }

    /**
     * Paraliza al Demogorgon durante la Intervención de Eleven.
     * El hilo entra en wait() al inicio del siguiente ciclo del run().
     */
    public void setParalizado() {
        paralizado.set(true);
    }

    /**
     * Desparaliza al Demogorgon al terminar la Intervención de Eleven.
     * El notify() desbloquea el wait() del run().
     */
    public synchronized void Liberar() {
        paralizado.set(false);
        this.notify();
    }

    public String toString() {
        return identificador;
    }

    /**
     * Cuenta dígitos de n para calcular el relleno de ceros del identificador.
     * Ejemplo: n=12 ->  2 dígitos -> 2 ceros de relleno -> "D0012".
     */
    public int contarDigitos(int n) {
        if(n==0){
            return 1;   // Valor para el Alpha
        }
        else{
            int k = 0;
            while (n > 0) {
                k++;
                n = Math.floorDiv(n, 10);
            }
            return k;
        }
    }

    /**
     * Punto de comprobación de pausa: bloquea al Demogorgon en wait()
     * hasta que GestorRemoto.Reanudar() llame a notify().
     */
    public void comprobarPausado() {
        try {
            synchronized (this) {
                while (pausado.get()) {
                    this.wait();
                }
            }
        } catch (InterruptedException e) {
        }
    }

    /** Activa o desactiva la pausa del Demogorgon. */
    public void setPausado(boolean b) {
        pausado.set(b);
    }
}
