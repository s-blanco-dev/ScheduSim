package scheduler;

import algoritmos.Proceso;

import java.util.*;

public class SRTF implements Scheduler{
    private Queue<Proceso> colaListos = new LinkedList<>();
    private List<Proceso> procesosTerminados = new ArrayList<>();
    private Proceso actual = null;

    @Override
    public void addProcess(Proceso p) {
        colaListos.offer(p);
    }

    @Override
    public Proceso selectNextProcess(int tick) {
        if (actual == null || actual.estaTerminado() || (!colaListos.isEmpty() && Objects.requireNonNull(obtenerMasChico(colaListos)).getRafagaRestante() < actual.getRafagaRestante())){
            if (actual != null && !actual.estaTerminado()) {
                colaListos.offer(actual);
            }
            actual = colaListos.poll();
        }
        return actual;
    }

    @Override
    public void removeProcess(Proceso p) {
        procesosTerminados.add(p);
        if (actual == p) {
            actual = null;
        }
    }

   @Override
    public void reset() {
        colaListos.clear();
        procesosTerminados.clear();
        actual = null;
    }

    @Override
    public boolean isDone() {
        return colaListos.isEmpty() && actual == null;
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
        return new ArrayList<>(colaListos);
    }

    @Override
    public List<Proceso> getProcesosTerminados() {
        return procesosTerminados;
    }

    private Proceso obtenerMasChico(Queue<Proceso> cola) {
        if (cola.size() == 0) {
            return null;
        }
        Proceso chicuelo = cola.stream()
                .min(Comparator.comparingInt(Proceso::getRafaga))
                .orElseThrow(NegativeArraySizeException::new);

        return chicuelo;
    }
}
