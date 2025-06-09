package scheduler;

import algoritmos.Proceso;

import java.util.List;

public interface Schedu {
    public void addProcess(Proceso p);
    public Proceso selectNextProcess(int  tick);
    public void removeProcess(Proceso p);
    public void reset();
    public boolean isDone();
    public void tick();
    public List<Proceso> getReadyQueue();
    public List<Proceso> getFinishedProcesses();
}