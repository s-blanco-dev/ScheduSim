import algoritmos.Proceso;
import scheduler.*;

import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        Proceso p1 = new Proceso("P1", 2, 0, 3);
        Proceso p2 = new Proceso("P2",4, 0, 2);
        Proceso p3 = new Proceso("P3", 1, 2, 1);

        Queue<Proceso> cola = new LinkedList<>();
        cola.add(p1);
        cola.add(p2);
        cola.add(p3);
        Scheduler scheduler = new Scheduler();
        scheduler.cambiarAlgoritmo(new Prioridad());

        System.out.println(scheduler.schedule(cola));

        // PLANIFICADOR MULTICOLAS UNIX
        // PLANIFICADOR LINUX

    }
}
