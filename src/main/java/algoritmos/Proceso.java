package algoritmos;

import enums.Prioridad;

public class Proceso {
    private String nombre;
    private int rafaga;
    private int tiempo_llegada;
    private Prioridad prioridad;


    public String getNombre() {
        return nombre;
    }

    public int getRafaga() {
        return rafaga;
    }

    public void setRafaga(int raf) {
        this.rafaga = raf;
    }

    public int getLlegada() {
        return tiempo_llegada;
    }

    public Prioridad getPrioridad(){
        return prioridad;
    }

    // -------------------------------------------------------------------

    public Proceso(String nombre, int rafaga, int llegada, Prioridad prioridad) {
        this.nombre = nombre;
        this.rafaga = rafaga;
        this.tiempo_llegada = llegada;
        this.prioridad = prioridad;
    }

    public String toString() {
        return nombre;
    }
}
