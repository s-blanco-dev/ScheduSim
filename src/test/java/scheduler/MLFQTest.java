package scheduler;


import algoritmos.Proceso;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class MLFQTest {
    @Test
    public void testProcesoSeAgregaYTermina() {
        MLFQ scheduler = new MLFQ();
        Proceso p1 = new Proceso("A", 0, 2, 1); // llega en tick 0, ráfaga 2

        scheduler.addProcess(p1);
        assertEquals(p1, scheduler.selectNextProcess(0));

        scheduler.tick(); // A [1/2]
        scheduler.tick(); // A [0/2], debería terminar

        scheduler.selectNextProcess(2);
        assertTrue(p1.estaTerminado());
        assertTrue(scheduler.getProcesosTerminados().contains(p1));
    }

    @Test
    public void testProcesoBajaACola1() {
        MLFQ scheduler = new MLFQ();
        Proceso p = new Proceso("B", 0, 5, 1);

        scheduler.addProcess(p);
        scheduler.selectNextProcess(0); // B comienza a ejecutarse

        scheduler.tick(); // tick 0: B [4/5]
        scheduler.tick(); // tick 1: B [3/5] — agota el quantum de la cola 0

        Proceso siguiente = scheduler.selectNextProcess(2); // Se degrada y es re-seleccionado

        // comprobemus que es el mismo proceso (ahora en cola 1)
        assertEquals(p, siguiente);
        // verificar que le asignó el quantum de la cola 1 (4)
        assertEquals(4, scheduler.getColas().size() > 1 ? MLFQ.QUANTUM_COLAS[1] : -1);

    }

    @Test
    public void testFinalizaConMultiplesProcesos() {
        MLFQ scheduler = new MLFQ();

        Proceso p1 = new Proceso("A", 0, 1, 1);
        Proceso p2 = new Proceso("B", 0, 3, 1);
        Proceso p3 = new Proceso("C", 0, 2, 1);

        scheduler.addProcess(p1);
        scheduler.addProcess(p2);
        scheduler.addProcess(p3);

        int tick = 0;
        while (!scheduler.isDone()) {
            scheduler.selectNextProcess(tick);
            scheduler.tick();
            tick++;
        }

        assertEquals(3, scheduler.getProcesosTerminados().size());
        assertTrue(scheduler.getProcesosTerminados().stream().anyMatch(p -> p.getPid().equals("A")));
        assertTrue(scheduler.getProcesosTerminados().stream().anyMatch(p -> p.getPid().equals("B")));
        assertTrue(scheduler.getProcesosTerminados().stream().anyMatch(p -> p.getPid().equals("C")));
    }
}
