package legacy;

import algoritmos.Proceso;

import java.util.List;
import java.util.Queue;

public interface IAlgoritmo {
    public List<Proceso> schedule(Queue<Proceso> colaProcesos);
}
