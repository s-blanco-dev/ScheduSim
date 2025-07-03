package scheduler;

import algoritmos.Proceso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RoundRobin implements Scheduler {
    private Queue<Proceso> colaListos = new LinkedList<>();
    private List<Proceso> procesosTerminados = new ArrayList<>();
    private int quantum;
    private int quantumCounter = 0;
    private Proceso actual = null;
    private int tick = 0;

    public RoundRobin(int quantum) {
        this.quantum = quantum;
    }

    @Override
    public void addProcess(Proceso p) {
        colaListos.offer(p);
    }

    @Override
    public Proceso selectNextProcess(int tick) {
        // Si el actual terminó o agotó el quantum, cambiar
        if (actual == null || actual.estaTerminado() || quantumCounter >= quantum) {
            if (actual != null && !actual.estaTerminado()) {
                colaListos.offer(actual);
            }
            actual = colaListos.poll();
            quantumCounter = 0;
        }
        return actual;
    }

    @Override
    public void removeProcess(Proceso p) {
        procesosTerminados.add(p);
        p.settRetorno(tick - p.getLlegada());
        if (actual == p) {
            actual = null;
        }
    }

    @Override
    public void reset() {
        colaListos.clear();
        procesosTerminados.clear();
        actual = null;
        quantumCounter = 0;
    }

    @Override
    public boolean isDone() {
        return colaListos.isEmpty() && actual == null;
    }

    @Override
    public void tick() {
        tick++;
        for (Proceso p : colaListos) {
            p.aumentarTiempoEspera();
        }

        if (actual != null) {
            actual.decrementar();
            quantumCounter++;
            if (actual.estaTerminado()) {
                removeProcess(actual);
            }
        }
    }

    @Override
    public List<Proceso> getColaListos() {
        return new ArrayList<>(colaListos);
    }

    @Override
    public List<Proceso> getProcesosTerminados() {
        return procesosTerminados;
    }
    @Override
    public int getTick() {
        return tick;
    }
}