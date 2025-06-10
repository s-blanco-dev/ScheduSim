package scheduler;

import algoritmos.Proceso;

import java.util.List;
import java.util.Queue;

public interface Scheduler {
    public void addProcess(Proceso p);
    public Proceso selectNextProcess(int  tick);
    public void removeProcess(Proceso p);
    public void reset();
    public boolean isDone();
    public void tick();
    public List<Proceso> getColaListos();
    public List<Proceso> getProcesosTerminados();
}