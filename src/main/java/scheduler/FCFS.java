package scheduler;

import algoritmos.Proceso;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Algoritmo de scheduling FCFS (First Come First Served)
 */

public class FCFS implements IAlgoritmo {

    @Override
     public List<Proceso> schedule(Queue<Proceso> colaProcesos) {
        List<Proceso> listaFinal = new LinkedList<>();
        Queue<Proceso> colaListos = new LinkedList<>();
        int tiempoActual = 0;

        /* Mientras existan procesos esperando en la cola */
        while (!colaProcesos.isEmpty() || !colaListos.isEmpty()) {
            while (!colaProcesos.isEmpty() && colaProcesos.peek().getLlegada() <= tiempoActual) {
                colaListos.add(colaProcesos.poll());
            }

            if (!colaListos.isEmpty()) {
                Proceso actual = colaListos.peek();
                /* Agrega hasta terminar rafaga */
                for (int i = 0; i < actual.getRafaga(); i++) {
                    listaFinal.add(actual);
                }
                tiempoActual += actual.getRafaga();
                colaListos.remove(actual); // elimino proceso completado
            } else {
                /* Si no hay procesongo listo, avanza el tiempo */
                tiempoActual++;
            }
        }
        return listaFinal;
    }
}
