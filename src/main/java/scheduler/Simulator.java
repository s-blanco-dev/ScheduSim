package scheduler;


import algoritmos.Proceso;

import java.util.*;


public class Simulator {
    private Scheduler scheduler;
    private List<Proceso> allProcesses;
    private int tick = 0;
    private Proceso current = null;
    private boolean isRunning = false;
    private List<String> ejecucionCpu = new ArrayList<>();

    public Scheduler getScheduler() {
        return scheduler;
    }

    public Proceso getCurrent() {
        return current;
    }

    public List<String> getEjecucionCpu() {
        return ejecucionCpu;
    }

    public int getTick() {
        return tick;
    }


    public Simulator(Scheduler scheduler, List<Proceso> procesos) {
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
        log("⚠\uFE0F Cambio de contexto: " +
            (current != null ? current.getPid() : "Ninguno") +
            " → " +
            (next != null ? next.getPid() : "Ninguno"));
        current = next;
    }

    // Ejecutar un tick
    scheduler.tick();
    ejecucionCpu.add(current != null ? current.getPid() : "IDLE");

    displayState();

    tick++; // Se incrementa al final
}

    private void log(String mensaje) {
        System.out.println("[Tick " + tick + "] " + mensaje);
    }

    private void displayState() {
        System.out.println("--------------------------------------------------");
        System.out.println("[Tick " + tick + "] → CPU: " + (current != null ? current : "IDLE"));
        System.out.println("--------------------------------------------------");

        if (scheduler instanceof MLFQ) {
            System.out.println("--------------------------------------------------");
            System.out.println("PLANIFICADOR SOLARIS:");
            for (int i = 0; i < 3; i++) {
                System.out.print("→ Cola " + i + ":");
                System.out.println(Arrays.toString(((MLFQ)scheduler).getColas().get(i).toArray()));

            }
        } else {
            System.out.print("→ Cola de listos: ");
            List<Proceso> cola = scheduler.getColaListos();
            if (cola.isEmpty()) {
                System.out.print("vacía");
            } else {
                System.out.println(cola);
            }
        }
        System.out.println();
        System.out.println("→ Terminados: " + scheduler.getProcesosTerminados());
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
        System.out.println("📝 Historial de ejecución en CPU:");
        System.out.println(String.join(", ", ejecucionCpu));
        System.out.println("TIEMPO ESPERA Y RETORNO:");
        for (Proceso p : scheduler.getProcesosTerminados()) {
            System.out.println(p + ": " + p.gettEspera() + " | " + p.gettRetorno());
        }
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