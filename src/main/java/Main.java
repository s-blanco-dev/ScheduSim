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
                new Proceso("P1", 0, 4, 1),
                new Proceso("P2", 1, 2, 2),
                new Proceso("P3", 3, 6, 3),
                new Proceso("P4", 8, 4, 4),
                new Proceso("P5", 12, 2, 5),
                new Proceso("P6", 16, 6, 6),
                new Proceso("P7", 19, 4, 7),
                new Proceso("P8", 22, 2, 8),
                new Proceso("P9", 23, 6, 1),
                new Proceso("P10", 25, 4, 5),
                new Proceso("P11", 27, 2, 6),
                new Proceso("P12", 30, 6, 9)
        );

        Scheduler schedule = new Prioridad();
        Simulator sim = new Simulator(schedule, procesos);
        sim.runAuto(0); // 1000 ms entre ticks para ver los pasos
        // PLANIFICADOR MULTICOLAS UNIX
        // PLANIFICADOR LINUX
        // TABLA DE TIEMPO
        // PROCESOS EN TIEMPO REAL, AL AZAR
        //
    }
}
