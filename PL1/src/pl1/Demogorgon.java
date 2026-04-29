/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl1;

/**
 *
 * @author Alejandro
 */
public class Demogorgon extends Thread {
    private int id;
    private String identificador;
    private Hawkins hawkins;
    private UpsideDown upsideDown;
    private int capturas = 0;
    public Demogorgon(int id, Hawkins h, UpsideDown u){
        this.id=id;
        this.identificador= "D"+id;
        this.hawkins = h;
        this.upsideDown =  u;
    }
    
    public String getIdentificador(){
        return identificador;
    }
    
    public void run(){
        String[] zonasUpsideDown = {"bosque", "alcantarillado", "laboratorio", "centroComercial"};
        int i = (int)(4*Math.random());
        while(true){
            try{
                String zonaUpsideDown = zonasUpsideDown[i];
                ZonaUpsideDown zona = upsideDown.getZona(zonaUpsideDown);
                // Lo de atacar no creo que sea así, ya que lo captura y luego espera, en vez de capturarlo durante 0.5-1.5 segundos, pero ya se hará
                Nino niño = zona.elegir();
                double tiempo = 500 + 1000*Math.random();
                if(niño != null){
                    niño.setTiempo(tiempo);
                    niño.interrupt();
                    boolean capturado = zona.atacar(niño);
                    sleep((long)(tiempo));
                    niño.isInterrupted();
                    if(capturado){
                        System.out.println("----------------------------------");
                        System.out.println(identificador + " acaba de capturar a " + niño.getIdentificador());
                        System.out.println("----------------------------------");
                        capturas++;
                        upsideDown.enviarNiñoColmena(niño);
                        sleep((long)(500 + 500*Math.random())); // Tiempo en llevar al niño a la colmena
                    }else{
                        System.out.println("El ataque de " +  identificador + " no tuvo éxito");
                    }
                }else{
                    System.out.println("El ataque de " +  identificador + " no tuvo éxito");
                }
                i = (int)(4*Math.random());
            }catch(InterruptedException e){e.printStackTrace();}
        }
    }
    
}
