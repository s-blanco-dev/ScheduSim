package algoritmos;

import enums.Prioridad;

public class Proceso {
    private String nombre;
    private int rafaga;
    private int tiempo_llegada;
    private int quantum;
    private Prioridad prioridad;

    public Proceso(String nombre, int rafaga, int llegada, Integer quantum, Prioridad prioridad) {
        this.nombre = nombre;
        this.rafaga = rafaga;
        this.tiempo_llegada = llegada;
        this.quantum = quantum;
        this.prioridad = prioridad;
    }
}
