package scheduler;

import algoritmos.Proceso;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RoundRobin implements IAlgoritmo {

    private int quantum;

    public RoundRobin(int quantum) {
        this.quantum = quantum;
    }

    @Override
    public List<Proceso> schedule(Queue<Proceso> colaProcesos) {
        List<Proceso> listaFinal = new LinkedList<>();
        Queue<Proceso> colaListos = new LinkedList<>();
        int tiempoActual = 0;

        /* Mientras existan procesos esperando en la cola */
        while (!colaProcesos.isEmpty() || !colaListos.isEmpty()) {
            while (!colaProcesos.isEmpty() && colaProcesos.peek().getLlegada() == tiempoActual) {
                colaListos.add(colaProcesos.poll());
            }

            if (!colaListos.isEmpty()) {
                Proceso actual = colaListos.poll();
                int quantumRestante = this.quantum;

                // Ejecuta el proceso durante el quantum o hasta que termine su ráfaga
                while (quantumRestante > 0 && actual.getRafaga() > 0) {
                    listaFinal.add(actual);
                    actual.decrementar();
                    tiempoActual++;
                    quantumRestante--;
                }

                if (actual.getRafaga() > 0) {
                    colaProcesos.add(actual);
                }

                if (actual.getRafaga() == 0) {
                    colaListos.remove(actual); // elimino proceso completado
                }
            } else {
                /* Si no hay procesongo listo, avanza el tie
                mpo */
                tiempoActual++;
            }
        }
        return listaFinal;
    }

    private Proceso obtenerMasChico(Queue<Proceso> cola) {
        Proceso chicuelo = cola.stream()
                .min(Comparator.comparingInt(Proceso::getRafaga))
                .orElseThrow(NegativeArraySizeException::new);

        return chicuelo;
    }
}