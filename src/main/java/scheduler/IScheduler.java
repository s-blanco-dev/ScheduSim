package scheduler;

import algoritmos.Proceso;

import java.util.List;
import java.util.Queue;

public interface IScheduler {
    public List<Proceso> schedule(Queue<Proceso> colaProcesos);
}
