package scheduler;

import algoritmos.Proceso;

import java.util.*;

public class SRJF implements Scheduler {
    private Queue<Proceso> readyQueue = new LinkedList<>();
    private List<Proceso> finishedProcesses = new ArrayList<>();
    private Proceso actual = null;

    @Override
    public void addProcess(Proceso p) {
        readyQueue.offer(p);
    }

    @Override
    public Proceso selectNextProcess(int tick) {
        if (actual != null && !actual.estaTerminado()) {
                readyQueue.offer(actual);
        }
        if (!readyQueue.isEmpty()) {
            actual = readyQueue.stream()
                    .min(Comparator.comparingInt(Proceso::getRafaga))
                    .orElse(null);
            readyQueue.remove(actual);
        } else {
            actual = null;
        }
        return actual;
    }

    @Override
    public void removeProcess(Proceso p) {
        finishedProcesses.add(p);
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
}