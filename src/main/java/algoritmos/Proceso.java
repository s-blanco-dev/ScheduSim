package algoritmos;

import enums.Prioridad;


public class Proceso {
    private String pid;
    private int llegada;
    private int rafaga;
    private int prioridad;
    private int rafagaRestante;

    public Proceso(String pid, int llegada, int rafaga, int prioridad) {
        this.pid = pid;
        this.llegada = llegada;
        this.rafaga = rafaga;
        this.rafagaRestante = rafaga;
    }

    public String getPid() { return pid; }
    public int getLlegada() { return llegada; }
    public int getRafaga() { return rafaga; }
    public int getRafagaRestante() { return rafagaRestante; }

    public void decrementar() {
        if (rafagaRestante > 0) rafagaRestante--;
    }

    public boolean estaTerminado() {
        return rafagaRestante <= 0;
    }

    @Override
    public String toString() {
        return pid + " [" + rafagaRestante + "/" + rafaga + "]";
    }

    public int getPrioridad() {
        return prioridad;
    }
}
