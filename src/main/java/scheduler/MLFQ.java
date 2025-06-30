package scheduler;

import algoritmos.Proceso;

import java.util.*;

/**
 * MULTILEVEL FEEDBACK QUEUE - 3 COLAS
 * COLA 0 -> Round Robin con quantum 2
 * COLA 1 -> Round Robin con quantum 4
 * COLA 2 -> FCFS (En realidad RR con quantum infinito)
 */

 public class MLFQ implements Scheduler {
    private static final int NUM_COLAS = 3;
    public static final int[] QUANTUM_COLAS = {2, 4, Integer.MAX_VALUE};

    private List<Queue<Proceso>> colas = new ArrayList<>();
    private Map<Proceso, Integer> tablaQuantums;
    private Map<Proceso, Integer> tablaAging;
    private List<Proceso> procesosTerminados = new ArrayList<>();
    private Proceso actual = null;
    private int nivelActual = 0;
    private int tick = 0;


    public MLFQ() {
        for (int i = 0; i < 3; i++) {
            Queue<Proceso> nuevaCola = new LinkedList<>();
            colas.add(nuevaCola);
        }
        tablaQuantums = new HashMap<>();
        tablaAging = new HashMap<>();
    }

    @Override
    public void addProcess(Proceso p) {
        colas.get(0).offer(p);
        tablaQuantums.put(p, QUANTUM_COLAS[0]);
        tablaAging.put(p, 0);
    }


         @Override
    public Proceso selectNextProcess(int tick) {
        // Si terminó el actual, lo metemos en lista de terminados
        if (actual != null && actual.estaTerminado()) {
            removeProcess(actual);
            actual = null;
        }

        // Si agotó quantum pero no terminó, lo empujamos al principio de la cola de abajo
        if (actual != null && tablaQuantums.getOrDefault(actual, 0) == 0) {
            bajaleUnCambio(actual);
            actual = null;
        }

        /* SELECCIONA EL PROXIMO PROCESO */
        if (actual == null) {
            for (int i = 0; i < NUM_COLAS; i++) {
                while (!colas.get(i).isEmpty()) {
                    Proceso candidato = colas.get(i).poll();
                    if (candidato.estaTerminado()) {
                        removeProcess(candidato); // evita meter uno ya terminado
                        continue;
                    }
                    actual = candidato;
                    nivelActual = i;
                    tablaQuantums.put(actual, QUANTUM_COLAS[i]); // asigna el quantum correspondiente a la cola
                    break;
                }
                if (actual != null) {
                    break;
                }
            }
        }

        return actual;
    }


    public Proceso getActual() {
        return actual;
    }

    @Override
    public void removeProcess(Proceso p) {
        procesosTerminados.add(p);
        p.settRetorno(tick - p.getLlegada());
        tablaQuantums.remove(p);
        tablaAging.remove(p);
        if (actual == p) {
            actual = null;
        }
    }

    @Override
    public void reset() {
        colas.clear();
        for (int i = 0; i < NUM_COLAS; i++) {
            colas.add(new LinkedList<>());
        }

        tablaQuantums.clear();
        tablaAging.clear();
        procesosTerminados.clear();
        actual = null;
    }

    @Override
    public void tick() {
        tick++;
        for (int i = 0; i < NUM_COLAS; i++) {
            for (Proceso p : colas.get(i)) {
                p.aumentarTiempoEspera();
                tablaAging.put(p, tablaAging.get(p)+1); // aumento contador aging
            }
        }

        aplicarAging();

        if (actual != null) {
            actual.decrementar();
            tablaQuantums.put(actual, tablaQuantums.get(actual) -1); // reduzco quantum
            if (actual.estaTerminado()) {
                removeProcess(actual);
            }
        }
    }

      @Override
    public boolean isDone() {
        return colas.stream().allMatch(Queue::isEmpty) && actual == null;
    }

    @Override
    public List<Proceso> getColaListos() {
        List<Proceso> todos = new ArrayList<>();
        for (Queue<Proceso> q : colas) {
            todos.addAll(q);
        }
        return todos;
    }

    public List<Queue<Proceso>> getColas() {
        return colas;
    }

    @Override
    public List<Proceso> getProcesosTerminados() {
        return new ArrayList<>(procesosTerminados);
    }

   /* private Proceso algoritmoRR(int tick, int numCola) {
        if (actual == null || actual.estaTerminado() || tablaQuantums.get(actual) >= QUANTUM_COLAS[numCola]) {
            if (actual != null && !actual.estaTerminado()) {
                colas.get(numCola).offer(actual);
            }
            actual = colas.get(numCola).poll();
            tablaQuantums.put(actual, QUANTUM_COLAS[numCola]);
        }
        return actual;
    }

    private Proceso algoritmoFCFS(int tick, int numCola) {
        if (actual == null || actual.estaTerminado()) {
            if (actual != null && !actual.estaTerminado()) {
                colas.get(numCola).offer(actual);
            }
            actual = colas.get(numCola).poll();
        }
        return actual;
    }*/

     private void bajaleUnCambio(Proceso p) {
         int nuevoNivel = Math.min(nivelActual + 1, NUM_COLAS - 1);
         colas.get(nuevoNivel).offer(p);
         tablaQuantums.put(p, QUANTUM_COLAS[nuevoNivel]);
     }

    private void aplicarAging() {
        for (int i = 1; i < NUM_COLAS; i++) {
            Queue<Proceso> cola = colas.get(i);
            List<Proceso> paraSubir = new ArrayList<>();
            for (Proceso p : cola) {
                if (tablaAging.get(p) >= 5) {
                    paraSubir.add(p);
                }
            }

            for (Proceso p : paraSubir) {
                cola.remove(p);
                colas.get(i - 1).offer(p);
                tablaQuantums.put(p, QUANTUM_COLAS[i - 1]);
                tablaAging.put(p, 0); // reset ticks en nivel
            }
        }
    }

    @Override
    public int getTick() {
        return tick;
    }

}