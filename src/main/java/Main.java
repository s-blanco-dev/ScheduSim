import algoritmos.Proceso;
import scheduler.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Proceso> procesos = List.of(
                new Proceso("P1", 0, 6, 3),
                new Proceso("P2", 2, 4, 1),
                new Proceso("P3", 4, 2, 2)
        );

        Scheduler schedule = new MLFQ();
        Simulator sim = new Simulator(schedule, procesos);
        sim.runAuto(0);
    }
}
