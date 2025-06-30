package scheduler;

import algoritmos.Proceso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FCFS implements Scheduler {
    private Queue<Proceso> readyQueue = new LinkedList<>();
    private List<Proceso> finishedProcesses = new ArrayList<>();
    private Proceso actual = null;
    private int tick = 0;

    @Override
    public void addProcess(Proceso p) {
        readyQueue.offer(p);
    }

    @Override
    public Proceso selectNextProcess(int tick) {
        // Si el actual terminó o agotó el quantum, cambiar
        if (actual == null || actual.estaTerminado()) {
            if (actual != null && !actual.estaTerminado()) {
                readyQueue.offer(actual);
            }
            actual = readyQueue.poll();
        }
        return actual;
    }

    @Override
    public void removeProcess(Proceso p) {
        finishedProcesses.add(p);
        p.settRetorno(tick - p.getLlegada());
        if (actual == p) {
            actual = null;
        }
    }

    @Override
    public void reset() {
        readyQueue.clear();
        finishedProcesses.clear();
        actual = null;
    }

    @Override
    public boolean isDone() {
        return readyQueue.isEmpty() && actual == null;
    }

    @Override
    public void tick() {
        tick++;
        for (Proceso p : readyQueue) {
            p.aumentarTiempoEspera();
        }

        if (actual != null) {
            actual.decrementar();
            if (actual.estaTerminado()) {
                removeProcess(actual);
            }
        }
    }

    @Override
    public List<Proceso> getColaListos() {
        return new ArrayList<>(readyQueue);
    }

    @Override
    public List<Proceso> getProcesosTerminados() {
        return finishedProcesses;
    }

    @Override
    public int getTick() {
        return tick;
    }
}