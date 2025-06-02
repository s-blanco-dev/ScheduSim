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

        while (!colaProcesos.isEmpty() || !colaListos.isEmpty()) {

            // Agregar todos los procesos que ya llegaron a la cola
            while (!colaProcesos.isEmpty() && colaProcesos.peek().getLlegada() <= tiempoActual) {
                colaListos.add(colaProcesos.poll());
            }

            if (!colaListos.isEmpty()) {
                Proceso actual = colaListos.poll();
                int tiempoEjecutado = 0;

                // Ejecutar hasta terminar quantum o hasta que el proceso se derrita
                while (tiempoEjecutado < quantum && actual.getRafaga() > 0) {
                    listaFinal.add(actual);
                    actual.decrementar();
                    tiempoEjecutado++;
                    tiempoActual++;

                    // Me fijo si llegó algo nuevo
                    while (!colaProcesos.isEmpty() && colaProcesos.peek().getLlegada() <= tiempoActual) {
                        colaListos.add(colaProcesos.poll());
                    }
                }

                // Si no terminó, vuelve a la cola
                if (actual.getRafaga() > 0) {
                    colaListos.add(actual);
                }
            } else {
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