import algoritmos.Proceso;
import scheduler.FCFS;
import scheduler.SJF;
import scheduler.Scheduler;

import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        Proceso p1 = new Proceso("P1", 4, 0, null);
        Proceso p2 = new Proceso("P2", 6, 0, null);
        Proceso p3 = new Proceso("P3", 5, 3, null);

        Queue<Proceso> cola = new LinkedList<>();
        cola.add(p1);
        cola.add(p2);
        cola.add(p3);
        Scheduler scheduler = new Scheduler();
        scheduler.cambiarAlgoritmo(new FCFS());

        System.out.println(scheduler.schedule(cola));

        // PLANIFICADOR MULTICOLAS UNIX
        // PLANIFICADOR LINUX

    }
}
