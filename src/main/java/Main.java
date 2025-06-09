import algoritmos.Proceso;
import scheduler.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
       /* Proceso p1 = new Proceso("P1", 2, 0, 3);
        Proceso p2 = new Proceso("P2",4, 0, 2);
        Proceso p3 = new Proceso("P3", 1, 2, 1);

        Queue<Proceso> cola = new LinkedList<>();
        cola.add(p1);
        cola.add(p2);
        cola.add(p3);
        Scheduler scheduler = new Scheduler();
        scheduler.cambiarAlgoritmo(new Prioridad());

        System.out.println(scheduler.schedule(cola));*/

        List<Proceso> procesos = List.of(
                new Proceso("P1", 0, 5, 0),
                new Proceso("P2", 2, 3, 0),
                new Proceso("P3", 4, 6, 0),
                new Proceso("P4", 1, 6, 0)
        );

        Scheduler schedule = new RoundRobin( 2);
        Simulator sim = new Simulator(schedule, procesos);
        sim.runAuto(1000); // 1000 ms entre ticks para ver los pasos
        // PLANIFICADOR MULTICOLAS UNIX
        // PLANIFICADOR LINUX
        // TABLA DE TIEMPO
        // PROCESOS EN TIEMPO REAL, AL AZAR
        //

    }
}
