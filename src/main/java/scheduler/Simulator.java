package scheduler;


import algoritmos.Proceso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Simulator {
    private Schedu scheduler;
    private List<Proceso> allProcesses;
    private int tick = 0;
    private Proceso current = null;
    private boolean isRunning = false;

    public Simulator(Schedu scheduler, List<Proceso> procesos) {
        this.scheduler = scheduler;
        this.allProcesses = new ArrayList<>(procesos);
    }

    public void runStep() {
    // Llegada de procesos
    for (Iterator<Proceso> it = allProcesses.iterator(); it.hasNext(); ) {
        Proceso p = it.next();
        if (p.getLlegada() == tick) {
            scheduler.addProcess(p);
            log("OJO!, llegó el proceso " + p.getPid());
            it.remove();
        }
    }

    // Selección de proceso
    Proceso next = scheduler.selectNextProcess(tick);

    if (next != current) {
        log("Cambio de contexto: " +
            (current != null ? current.getPid() : "Ninguno") +
            " → " +
            (next != null ? next.getPid() : "Ninguno"));
        current = next;
    }

    // Ejecutar un tick
    scheduler.tick();

    displayState();

    tick++; // Se incrementa al final
}

    private void log(String mensaje) {
        System.out.println("[Tick " + tick + "] " + mensaje);
    }

    private void displayState() {
        System.out.println("[Tick " + tick + "] → CPU: " + (current != null ? current.getPid() : "IDLE"));
        System.out.print("→ Cola de listos: ");
        List<Proceso> cola = scheduler.getReadyQueue();
        if (cola.isEmpty()) {
            System.out.print("vacía");
        } else {
           System.out.println(cola);
        }
        System.out.println();
        System.out.println("→ Terminados: " + scheduler.getFinishedProcesses());
        System.out.println("--------------------------------------------------");
    }

    public void runAuto(int delayMs) {
        isRunning = true;
        while (!isSimulationDone()) {
            runStep();
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("✅ Listo el pollo.");
    }

    public void stop() {
        isRunning = false;
    }

    public void reset() {
        this.tick = 0;
        this.current = null;
        this.scheduler.reset();
    }

    public boolean isSimulationDone() {
        return allProcesses.isEmpty() && scheduler.isDone();
    }
}
