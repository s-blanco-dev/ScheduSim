import algoritmos.Proceso;
import scheduler.FCFS;
import scheduler.SJF;
import scheduler.SRJF;
import scheduler.Scheduler;

import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        Proceso p1 = new Proceso("P1", 5, 0, null);
        Proceso p2 = new Proceso("P2", 2, 1, null);
        Proceso p3 = new Proceso("P3", 2, 4, null);

        Queue<Proceso> cola = new LinkedList<>();
        cola.add(p1);
        cola.add(p2);
        cola.add(p3);
        Scheduler scheduler = new Scheduler();
        scheduler.cambiarAlgoritmo(new SRJF());

        System.out.println(scheduler.schedule(cola));

        // PLANIFICADOR MULTICOLAS UNIX
        // PLANIFICADOR LINUX

    }
}
