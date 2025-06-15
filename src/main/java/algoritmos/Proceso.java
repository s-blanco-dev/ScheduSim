package algoritmos;

import enums.Prioridad;

import java.util.Objects;


public class Proceso {
    private String pid;
    private int llegada;
    private int rafaga;
    private int prioridad;
    private int rafagaRestante;
    private int tEspera;
    private int tRetorno;

    public Proceso(String pid, int llegada, int rafaga, int prioridad) {
        this.pid = pid;
        this.llegada = llegada;
        this.rafaga = rafaga;
        this.rafagaRestante = rafaga;
        this.prioridad = prioridad;
        this.tEspera = 0;
        this.tRetorno = 0;
    }

    public String getPid() {
        return pid;
    }

    public int getLlegada() {
        return llegada;
    }

    public int getRafaga() {
        return rafaga;
    }

    public int getRafagaRestante() {
        return rafagaRestante;
    }

    public void decrementar() {
        if (rafagaRestante > 0){
            rafagaRestante--;
        }
    }

    public boolean estaTerminado() {
        return rafagaRestante == 0;
    }

    @Override
    public String toString() {
        return pid + " [" + rafagaRestante + "/" + rafaga + "]";
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void aumentarTiempoEspera() {
        this.tEspera++;
    }

    public int gettEspera() {
        return tEspera;
    }

    public int gettRetorno() {
        return this.tRetorno;
    }

    public void settRetorno(int tiempo) {
        this.tRetorno = tiempo;
    }


    // -----------------------------------
    // PARA COMPARAR PROCESOS
    // -----------------------------------

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Proceso proceso = (Proceso) obj;
        return this.getPid().equals(proceso.getPid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPid());
    }
}
