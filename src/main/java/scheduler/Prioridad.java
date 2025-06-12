package scheduler;

import algoritmos.Proceso;

import java.util.*;
/**
 * Modelo de prioridad APROPIATIVA
 */
public class Prioridad implements Scheduler {
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
            // ¿Hay alguno en readyQueue con prioridad estrictamente menor?
            Proceso candidato = readyQueue.stream()
                    .min(Comparator.comparingInt(Proceso::getPrioridad))
                    .orElse(null);

            if (candidato != null && candidato.getPrioridad() < actual.getPrioridad()) {
                readyQueue.offer(actual); // Se preempte al actual
                actual = candidato;
                readyQueue.remove(candidato);
            }
            // Si no, continúa el actual
        } else {
            // Si actual terminó o no existe, elegimos el mejor disponible
            actual = readyQueue.stream()
                    .min(Comparator.comparingInt(Proceso::getPrioridad))
                    .orElse(null);
            if (actual != null) {
                readyQueue.remove(actual);
            }
        }
        return actual;
    }


    public Proceso getActual() {
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
        return new ArrayList<>(finishedProcesses);
    }


}