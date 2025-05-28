package scheduler;

import algoritmos.Proceso;

import java.util.List;
import java.util.Queue;

public class RoundRobin implements IAlgoritmo {
    int quantum;

    @Override
    public List<Proceso> schedule(Queue<Proceso> colaProcesos) {
        return List.of();
    }
}
