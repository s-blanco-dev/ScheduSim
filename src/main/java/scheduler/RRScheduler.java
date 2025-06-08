package scheduler;

import algoritmos.Proceso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RRScheduler implements Schedu{
    private Queue<Proceso> readyQueue = new LinkedList<>();
    private List<Proceso> finishedProcesses = new ArrayList<>();
    private int quantum;
    private int quantumCounter = 0;
    private Proceso actual = null;

    public RRScheduler(int quantum) {
        this.quantum = quantum;
    }

    @Override
    public void addProcess(Proceso p) {
        readyQueue.offer(p);
    }

    @Override
    public Proceso selectNextProcess(int tick) {
        // Si el actual terminó o agotó el quantum, cambiar
        if (actual == null || actual.estaTerminado() || quantumCounter >= quantum) {
            if (actual != null && !actual.estaTerminado()) {
                readyQueue.offer(actual);
            }
            actual = readyQueue.poll();
            quantumCounter = 0;
        }
        return actual;
    }

    @Override
    public void removeProcess(Proceso p) {
        finishedProcesses.add(p);
        if (actual == p) actual = null;
    }

    @Override
    public void reset() {
        readyQueue.clear();
        finishedProcesses.clear();
        actual = null;
        quantumCounter = 0;
    }

    @Override
    public boolean isDone() {
        return readyQueue.isEmpty() && actual == null;
    }

    @Override
    public void tick() {
        if (actual != null) {
            actual.decrementar();
            quantumCounter++;
            if (actual.estaTerminado()) {
                removeProcess(actual);
            }
        }
    }

    @Override
    public List<Proceso> getReadyQueue() {
        return new ArrayList<>(readyQueue);
    }

    @Override
    public List<Proceso> getFinishedProcesses() {
        return finishedProcesses;
    }
}