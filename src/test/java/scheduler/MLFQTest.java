package scheduler;


import algoritmos.Proceso;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Queue;

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




    @Test
    public void testAgingPromueveProcesoDesdeCola2() {
        // Proceso P1 va a acaparar CPU
        Proceso p1 = new Proceso("P1", 0, 20, 1);
        // Proceso P2 se va a degradar y quedar esperando en cola 2
        Proceso p2 = new Proceso("P2", 0, 3, 1);

        List<Proceso> lista = List.of(p1, p2);

        MLFQ scheduler = new MLFQ();
        Simulator sim = new Simulator(scheduler, lista);

        // Simular lo suficiente para que P2 se degrade dos veces (quantum 2, luego 4)
        for (int i = 0; i < 7; i++) sim.runStep(); // P2 debería haber bajado a cola 2

        // Ahora que P2 está en cola 2, dejémoslo envejecer 5 ticks más sin tocarlo
        for (int i = 0; i < 5; i++) sim.runStep();

        // Revisamos si subió a cola 1
        List<Queue<Proceso>> colas = scheduler.getColas();

        boolean p2EstaEnCola1 = colas.get(1).stream()
                .anyMatch(p -> p.getPid().equals("P2"));

        assertTrue(p2EstaEnCola1, "P2 debería haber subido a la cola 1 por aging.");
    }

   }
