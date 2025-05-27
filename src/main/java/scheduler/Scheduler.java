package scheduler;

import algoritmos.Proceso;

import java.util.List;
import java.util.Queue;

public class Scheduler {
    private IScheduler scheduler;
    int tiempoActual;

    public void cambiarAlgoritmo(IScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public List<Proceso> schedule(Queue<Proceso> colaProcesos){
        return scheduler.schedule(colaProcesos);
    }
}
