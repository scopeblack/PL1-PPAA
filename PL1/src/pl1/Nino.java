/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Representa a un niño como hilo independiente
 * Cada niño sigue el ciclo de vida definido en el enunciado:
 * Calle Principal → Sótano Byers → Portal → Upside Down → Radio WSQK → Calle Principal → (repetir).
 * Si es capturado por un demogorgon, espera en la Colmena hasta ser liberado por Eleven.
 *
 * Los niños se identifican con el formato "NXXXX". 
 * 
 * @author Alejandro
 * 
 */
public class Nino extends Thread {

    private int id;                      // Número entero único (1..1500)
    private String identificador;        // Cadena formateada "NXXXX" usada en logs y UI
    private Hawkins hawkins;             // Referencia al mundo de Hawkins para acceder a sus zonas
    private UpsideDown upsideDown;       // Referencia al Upside Down para cruzar portales y ser capturado

    // Duración del ataque en curso, fijada por el demogorgon antes de interrumpir al niño.
    private double tiempo = 0;

    // Indica si el niño está actualmente capturado en la Colmena.
    private AtomicBoolean capturado = new AtomicBoolean(false);

    // True mientras el niño está físicamente en el Upside Down.
    private AtomicBoolean enUpsideDown = new AtomicBoolean(false);

    // Activado durante el evento Apagón del Laboratorio: impide cambiar de zona.
    private AtomicBoolean paralizadoPortales = new AtomicBoolean(false);

    // Activado durante el evento Tormenta del Upside Down: duplica el tiempo de deambulación.
    private AtomicBoolean tormenta = new AtomicBoolean(false);

    /*
    Flag auxiliar para el bloque finally exterior: evita llamar a zonaActual.salir()
    dos veces cuando el niño ya salió en el bloque finally interior.
     */
    private boolean yaFuera = false;

    // Activado por GestorRemoto.Pausar() para suspender al niño en comprobarPausado()
    private AtomicBoolean pausado = new AtomicBoolean(false);

    private transient SistemaLog logger;

    // Flag de exclusión mutua para ataques: garantiza que un único demogorgon
    // pueda atacar a este niño en cada momento.
    private AtomicBoolean enAtaque = new AtomicBoolean(false);

    /**
     * Construye el niño con id numérico y genera el identificador "NXXXX"
     * con ceros a la izquierda (Por ejemplo: id=5 -> "N0005").
     */
    public Nino(int id, Hawkins h, UpsideDown u, SistemaLog logger) {
        this.id = id;
        int digitos = contarDigitos(id);
        int cantidadCeros = 4 - digitos;
        this.identificador = "N" + "0".repeat(cantidadCeros) + id;
        this.hawkins = h;
        this.upsideDown = u;
        capturado.set(false);
        this.logger = logger;
    }

    /** Devuelve el identificador formateado "NXXXX". */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * El Demogorgon llama a este método justo antes de interrumpir al niño,
     * para que cuando el niño despierte en esperar() sepa cuánto tiempo debe
     * aguantar el ataque.
     */
    public void setTiempo(double t) {
        tiempo = t;
    }

    public void run() {
        String[] zonasUpsideDown = {"bosque", "alcantarillado", "laboratorio", "centroComercial"};
        // i se elige aquí fuera del bucle principal para que el destino inicial
        // del niño al salir del Sótano ya esté fijado desde el primer ciclo
        int i = (int) (4 * Math.random());

        
        // HAWKINS
        comprobarPausado();
        hawkins.getCallePrincipal().entrar(this);   //Spawn de los niños
        try {
            sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        comprobarPausado();
        hawkins.getCallePrincipal().salir(this);
        
        
        while (true) {
            try {
                // ESPERA EN COLMENA
                if(capturado.get()){
                    synchronized (this) {
                        while (capturado.get()) {
                            try {
                                this.wait();
                            } catch (InterruptedException e) {
                                Thread.interrupted(); // Limpiar flag y volver al while por si el niño ya fue capturado por otro Demogorgon. (No debería pasar)
                            }
                        }
                    }
                    try{
                        comprobarPausado();
                        hawkins.getCallePrincipal().entrar(this);
                        sleep(1000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }finally{
                        comprobarPausado();
                        hawkins.getCallePrincipal().salir(this);
                    }
                }

                comprobarPausado();

                enUpsideDown.set(false);
                hawkins.getSotanoByers().entrar(this);
                sleep((long) (1000 + 1000 * Math.random()));

                // ENTRADA AL UPSIDE DOWN
                String zonaNombre = zonasUpsideDown[i];
                ZonaUpsideDown zonaActual = upsideDown.getZona(zonaNombre);

                comprobarPausado();

                hawkins.getSotanoByers().irUpsideDown(this, zonaNombre);

                // Marcamos que entramos
                enUpsideDown.set(true);
                zonaActual.entrar(this);
                logger.escribirLog(identificador + " acaba de entrar a: " + zonaNombre + ". Niños en " + zonaNombre + ": " + zonaActual.niñosEnZona());
                try {
                    // El do-while exterior maneja el Apagón del Laboratorio:
                    // si el portal está bloqueado al terminar la estancia, el niño
                    // se queda en la zona hasta que el apagón termine y pueda salir.
                    do {
                        try {
                            double t1 = 600 + 400 * Math.random();

                            int j = 0;
                            double tiempoRestante = 5 * t1;
                            if (tormenta.get()) {
                                tiempoRestante = tiempoRestante * 2;
                            }
                            // tiempoRestante se descuenta iterativamente, 
                            // cada interrupción descuenta solo
                            // el tiempo ya transcurrido y retoma el sleep con el resto.
                            long tiempo1 = 0;
                            long tiempo2 = 0;
                            while (tiempoRestante > 0 && !capturado.get()) {
                                try {
                                    tiempo1 = 0;
                                    tiempo2 = 0;
                                    comprobarPausado();
                                    tiempo1 = System.currentTimeMillis();
                                    sleep((long) tiempoRestante);
                                } catch (InterruptedException e) {
                                    // El Demogorgon interrumpe al niño; se espera la duración
                                    // del ataque (fijada por el Demogorgon) y
                                    // luego se retoma el tiempo restante en el Upside Down
                                    tiempo2 = System.currentTimeMillis();
                                    esperar(tiempo); // Tiempo de ataque
                                    Thread.interrupted();   //Limpiar flag
                                    if (!capturado.get()) {
                                        /*System.out.println("----------------" + identificador + " Han fallado la captura " + ++j); */
                                    }
                                } finally {
                                    if (tiempo2 == 0) {
                                        tiempo2 = System.currentTimeMillis();
                                    }
                                    tiempoRestante = tiempoRestante - (tiempo2 - tiempo1); // Se le resta lo que haya durado el sleep realmente

                                }
                            }
                        } finally {
                            // yaFuera evita llamar a zonaActual.salir() dos veces:
                            // una desde el finally interior y otra desde el exterior
                            if (!paralizadoPortales.get()) {
                                comprobarPausado();
                                enUpsideDown.set(false);
                                zonaActual.salir(this);
                                yaFuera = true;
                            }
                            if (capturado.get()) {
                                continue;
                            }  //Entramos en el wait de la colmena

                        }
                    } while (paralizadoPortales.get());
                } finally {
                    if (!yaFuera) {
                        comprobarPausado();
                        zonaActual.salir(this);
                        enUpsideDown.set(false);
                    }
                    if (capturado.get()) {
                        continue;
                    }  //Entramos en el wait de la colmena

                }
                // REGRESO A HAWKINS

                try {
                    logger.escribirLog(identificador + " acaba de salir del Upside Down. Niños restantes en " + zonaNombre + ":" + zonaActual.niñosEnZona());

                    hawkins.getRadioWSBK().entrar(this);
                    hawkins.getRadioWSBK().depositarSangre(this);
                    sleep(2000 + (long) (Math.random() * 2000));
                } catch (InterruptedException e) {
                } finally {
                    comprobarPausado();
                    hawkins.getRadioWSBK().salir(this);
                }
                try {
                    hawkins.getCallePrincipal().entrar(this);
                    sleep((long) (3000 + 2000 * Math.random()));
                } catch (InterruptedException e) {
                } finally {
                    comprobarPausado();
                    hawkins.getCallePrincipal().salir(this);
                }
                i = (int) (4 * Math.random());  // Vuelve a elegir una zona del Upside Down.

            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
                Thread.interrupted();
            }
        }
    }

    /**
     * Duerme al niño durante el tiempo de ataque t (ms). Limpia el flag de
     * interrupción antes de dormir para que el sleep no se cancele por una
     * interrupción anterior ya procesada. Si vuelve a ser interrumpido durante
     * la espera, simplemente termina (el ataque ya ha concluido).
     */
    public void esperar(double t) {
        Thread.interrupted(); // Limpiar el flag de interrupción pendiente
        try {
            sleep((long) (t));
        } catch (InterruptedException e) {
            // Si lo interrumpen de nuevo durante la espera ignorar
            Thread.interrupted(); // Limpiar de nuevo por si acaso
        }
    }


    /**
     * Cuenta el número de dígitos de n para calcular cuántos ceros de relleno
     * necesita el identificador "NXXXX". Ejemplo: n=5 -> 1 dígito -> 3 ceros.
     */
    public int contarDigitos(int n) {
        int k = 0;
        while (n > 0) {
            k++;
            n = Math.floorDiv(n, 10);
        }
        return k;
    }

    /** Marca al niño como capturado antes de que comience el sleep del ataque. */
    public void setCapturado() {
        capturado.set(true);
    }

    /** Devuelve true si el niño está actualmente capturado en la Colmena. */
    public boolean getCapturado(){
        return capturado.get();
    }

    /**
     * Llamado por Eleven (GestorEventos) para liberar al niño de la Colmena.
     * Limpia el flag y despierta al hilo del niño que estaba bloqueado en
     * el wait() del ciclo principal.
     */
    public synchronized void setLiberado() {
        capturado.set(false);
        this.notify();
    }

    /** Representación textual. */
    public String toString() {
        return identificador;
    }

    /** Devuelve true si el niño se encuentra actualmente en el Upside Down. */
    public boolean estaEnUpsideDown() {
        return enUpsideDown.get();
    }

    /**
     * Activado por GestorEventos al iniciar el Apagón del Laboratorio.
     * Impide que el niño salga de la zona aunque haya terminado su tiempo
     * de exploración (el do-while en run() comprueba este flag).
     */
    public void setParalizadoPortales(boolean b) {
        paralizadoPortales.set(b);
    }

    /**
     * Activado por GestorEventos durante la Tormenta del Upside Down.
     * Duplica el tiempo que el niño permanece en cada zona del Upside Down.
     */
    public void setTormenta(boolean b) {
        tormenta.set(b);
    }

    /**
     * Punto de comprobación de pausa: si el sistema está pausado, el niño
     * se bloquea aquí en wait() hasta que GestorRemoto llame a notify().
     * Se invoca antes de cada transición importante de zona.
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

    /** Activa o desactiva la pausa del niño; el notify() lo hace GestorRemoto.Reanudar(). */
    public void setPausado(boolean b) {
        pausado.set(b);
    }

    /**
     * Intenta marcar a este niño como "bajo ataque" de forma atómica.
     * Devuelve true solo si el niño no estaba ya siendo atacado, evitando
     * que dos Demogorgons lo elijan a la vez.
     */
    public boolean iniciarAtaque() {
        return enAtaque.compareAndSet(false, true); // true solo si era false
    }

    /** Libera al niño para que pueda ser atacado de nuevo por otro Demogorgon. */
    public void terminarAtaque() {
        enAtaque.set(false);
    }

    /** Devuelve true si el niño está siendo atacado en este momento. */
    public boolean bajoAtaque() {
        return enAtaque.get();
    }
}
