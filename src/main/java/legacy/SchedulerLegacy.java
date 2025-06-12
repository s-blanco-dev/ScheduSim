package legacy;

import algoritmos.Proceso;

import java.util.List;
import java.util.Queue;

public class SchedulerLegacy {
    private IAlgoritmo scheduler;
    public void cambiarAlgoritmo(IAlgoritmo scheduler) {
        this.scheduler = scheduler;
    }

    public List<Proceso> schedule(Queue<Proceso> colaProcesos) {
        return scheduler.schedule(colaProcesos);
    }
}
