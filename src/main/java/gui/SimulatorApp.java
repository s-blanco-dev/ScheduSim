package gui;

import algoritmos.Proceso;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import scheduler.*;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class SimulatorApp extends Application {

    private Simulator simulator;
    private Label estadoLabel;
    private Label cpuLabel;
    private Button avanzarBtn;
    private Button resetBtn;
    private TextArea salidaArea;
    private ComboBox<String> algoritmoSelector;
    private TextField pidField, llegadaField, duracionField, quantumField;
    private Button agregarProcesoBtn, iniciarSimulacionBtn;

    private List<Proceso> procesos = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        // Create main container with glassmorphism effect
        VBox mainContainer = new VBox(20);
        mainContainer.setStyle("""
            -fx-background-color: rgba(30, 30, 30, 0.85);
            -fx-background-radius: 20px;
            -fx-border-color: rgba(255, 255, 255, 0.1);
            -fx-border-width: 1px;
            -fx-border-radius: 20px;
            -fx-padding: 30px;
        """);

        // Add drop shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.color(0, 0, 0, 0.5));
        dropShadow.setRadius(20);
        dropShadow.setOffsetY(10);
        mainContainer.setEffect(dropShadow);

        // Title
        Label titleLabel = new Label("🖥 ScheduSim: Simulador de Scheduling");
        Label subtitle = new Label("Santiago Blanco, Martin Mujica - 2025");
        titleLabel.setStyle("""
            -fx-font-size: 28px;
            -fx-font-weight: bold;
            -fx-text-fill: linear-gradient(to right, #64b5f6, #42a5f5);
            -fx-effect: dropshadow(gaussian, rgba(100, 181, 246, 0.3), 5, 0, 0, 2);
        """);
        subtitle.setStyle("""
            -fx-font-size: 20px;
            -fx-font-weight: bold;
            -fx-text-fill: linear-gradient(to right, #64b5f6, #42a5f5);
            -fx-effect: dropshadow(gaussian, rgba(100, 181, 246, 0.3), 5, 0, 0, 2);
        """);


        // Process input fields with modern styling
        pidField = createStyledTextField("Process ID", "📋");
        llegadaField = createStyledTextField("Llegada", "⏰");
        duracionField = createStyledTextField("Rafaga", "⏱");
        quantumField = createStyledTextField("Quantum", "⚡");
        quantumField.setVisible(false);

        // Styled buttons
        agregarProcesoBtn = createStyledButton("➕ Agregar Proceso", "#4CAF50", "#45a049");
        agregarProcesoBtn.setOnAction(e -> agregarProceso());

        iniciarSimulacionBtn = createStyledButton("Iniciar simulación", "#2196F3", "#1976D2");
        iniciarSimulacionBtn.setOnAction(e -> iniciarSimulacion());

        // Algorithm selector with modern styling
        algoritmoSelector = new ComboBox<>();

        algoritmoSelector.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: rgba(50, 50, 50, 0.8);");
                }
            }
        });

        algoritmoSelector.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: rgba(40, 40, 40, 0.9);");
                }
            }
        });

        algoritmoSelector.getItems().addAll("MLFQ", "Round Robin", "SJF", "SRJF", "FCFS", "Priority");
        algoritmoSelector.setValue("FCFS");
        algoritmoSelector.setStyle("""
    -fx-background-color: rgba(50, 50, 50, 0.8);
    -fx-text-fill: #ffffff;
    -fx-border-color: rgba(255, 255, 255, 0.3);
    -fx-border-radius: 10px;
    -fx-background-radius: 10px;
    -fx-font-size: 14px;
    -fx-pref-width: 150px;
    -fx-pref-height: 40px;
    -fx-text-inner-color: #ffffff;
""");
        algoritmoSelector.setOnAction(e -> {
            quantumField.setVisible("Round Robin".equals(algoritmoSelector.getValue()));
        });

        // Process input section
        HBox processInputSection = new HBox(15);
        processInputSection.setAlignment(Pos.CENTER);
        processInputSection.getChildren().addAll(pidField, llegadaField, duracionField, agregarProcesoBtn);

        // Configuration section
        Label algorithmLabel = new Label("Algoritmo:");
        algorithmLabel.setStyle("""
            -fx-text-fill: #d79921;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
        """);

        HBox configSection = new HBox(15);
        configSection.setAlignment(Pos.CENTER);
        configSection.getChildren().addAll(algorithmLabel, algoritmoSelector, quantumField, iniciarSimulacionBtn);

        // Control section
        avanzarBtn = createStyledButton("▶ Avanzar", "#FF9800", "#F57C00");
        avanzarBtn.setStyle(avanzarBtn.getStyle() + "-fx-font-size: 18px; -fx-pref-width: 200px; -fx-pref-height: 50px;");
        avanzarBtn.setDisable(true);


        resetBtn = createStyledButton("Reset", "#D79921", "#efbf8e");
        resetBtn.setStyle(resetBtn.getStyle() + "-fx-font-size: 18px; -fx-pref-width: 200px; -fx-pref-height: 50px;");
        resetBtn.setDisable(true);

        // Status labels
        cpuLabel = new Label("CPU: IDLE");
        cpuLabel.setStyle("""
            -fx-font-size: 20px;
            -fx-font-weight: bold;
            -fx-text-fill: #ffffff;
            -fx-background-color: linear-gradient(to right, #424242, #616161);
            -fx-background-radius: 15px;
            -fx-padding: 15px 25px;
            -fx-border-color: rgba(255, 255, 255, 0.2);
            -fx-border-radius: 15px;
            -fx-border-width: 1px;
        """);

        estadoLabel = new Label("⚙ Listo para configurar...");
        estadoLabel.setStyle("""
            -fx-font-size: 18px;
            -fx-text-fill: #81C784;
            -fx-font-weight: bold;
            -fx-effect: dropshadow(gaussian, rgba(129, 199, 132, 0.3), 3, 0, 0, 1);
        """);
        // Output area with terminal-like styling
        salidaArea = new TextArea();
        salidaArea.setEditable(false);
        salidaArea.setPrefRowCount(15);
        salidaArea.setStyle("""
            -fx-font-family: 'Consolas', 'Monaco', monospace;
            -fx-font-size: 13px;
            -fx-background-color: rgba(18, 18, 18, 0.9);
            -fx-text-fill: #00ff41;
            -fx-border-color: rgba(0, 255, 65, 0.3);
            -fx-border-radius: 10px;
            -fx-background-radius: 10px;
            -fx-border-width: 2px;
            -fx-padding: 15px;
            -fx-control-inner-background: rgba(18, 18, 18, 0.9);
        """);

        // Add glow effect to output area
        DropShadow terminalGlow = new DropShadow();
        terminalGlow.setColor(Color.color(0, 1, 0.255, 0.3));
        terminalGlow.setRadius(10);
        salidaArea.setEffect(terminalGlow);

        avanzarBtn.setOnAction(e -> {
            simulator.runStep();
            Proceso current = simulator.getCurrent();
            cpuLabel.setText("CPU: " + (current != null ? current.getPid() : "IDLE"));

            // Update CPU label color based on state
            if (current != null) {
                cpuLabel.setStyle(cpuLabel.getStyle().replace("linear-gradient(to right, #424242, #616161)",
                    "linear-gradient(to right, #4CAF50, #66BB6A)"));
            } else {
                cpuLabel.setStyle(cpuLabel.getStyle().replace("linear-gradient(to right, #4CAF50, #66BB6A)",
                    "linear-gradient(to right, #424242, #616161)"));
            }

            salidaArea.appendText(generarEstadoTexto());

            if (simulator.isSimulationDone()) {
                estadoLabel.setText("✅ Listo el pollo!");
                estadoLabel.setStyle(estadoLabel.getStyle().replace("#81C784", "#4CAF50"));
                avanzarBtn.setDisable(true);
                salidaArea.appendText("\n💾 Diagrama de ejecución:\n");
                salidaArea.appendText(String.join(", ", simulator.getEjecucionCpu()));
                salidaArea.appendText("\n\n📊 TIEMPO DE ESPERA Y RETORNO:\n");
                for (Proceso p : simulator.getScheduler().getProcesosTerminados()) {
                    salidaArea.appendText(p + ": Espera = " + p.gettEspera() + ", Retorno = " + p.gettRetorno() + "\n");
                }
            }
        });

        resetBtn.setOnAction(e -> {
            simulator = null;
            procesos.clear();
            salidaArea.clear();

            pidField.clear();
            llegadaField.clear();
            duracionField.clear();
            quantumField.clear();

            algoritmoSelector.setValue("FCFS");
            quantumField.setVisible(false);
            avanzarBtn.setDisable(true);
            resetBtn.setDisable(true);

            cpuLabel.setText("CPU: IDLE");
            cpuLabel.setStyle("""
        -fx-font-size: 20px;
        -fx-font-weight: bold;
        -fx-text-fill: #ffffff;
        -fx-background-color: linear-gradient(to right, #424242, #616161);
        -fx-background-radius: 15px;
        -fx-padding: 15px 25px;
        -fx-border-color: rgba(255, 255, 255, 0.2);
        -fx-border-radius: 15px;
        -fx-border-width: 1px;
    """);

            estadoLabel.setText("⚙ Listo para configurar...");
            estadoLabel.setStyle("""
        -fx-font-size: 18px;
        -fx-text-fill: #81C784;
        -fx-font-weight: bold;
        -fx-effect: dropshadow(gaussian, rgba(129, 199, 132, 0.3), 3, 0, 0, 1);
    """);
        });


        // Arrange components in main container
        mainContainer.getChildren().addAll(
            titleLabel,
            subtitle,
            createSectionSeparator(),
            processInputSection,
            configSection,
            createSectionSeparator(),
            avanzarBtn,
            resetBtn,
            cpuLabel,
            estadoLabel,
            salidaArea
        );

        mainContainer.setAlignment(Pos.CENTER);

        // Root container with background
        StackPane root = new StackPane();

        // Background setup
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(getClass().getResource("/gui/fondum.png").toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        root.setBackground(new Background(backgroundImage));

        root.getChildren().add(mainContainer);
        root.setPadding(new Insets(30));

        Scene scene = new Scene(root, 900, 800);
        primaryStage.setTitle("CPU Scheduling Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TextField createStyledTextField(String placeholder, String icon) {
        TextField field = new TextField();
        field.setPromptText(icon + " " + placeholder);
        field.setStyle("""
            -fx-background-color: rgba(255, 255, 255, 0.1);
            -fx-text-fill: #ffffff;
            -fx-prompt-text-fill: rgba(255, 255, 255, 0.6);
            -fx-border-color: rgba(255, 255, 255, 0.3);
            -fx-border-radius: 10px;
            -fx-background-radius: 10px;
            -fx-font-size: 14px;
            -fx-pref-width: 150px;
            -fx-pref-height: 40px;
            -fx-padding: 0 15px;
        """);

        // Add focus effects
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(field.getStyle().replace("rgba(255, 255, 255, 0.3)", "#64b5f6"));
            } else {
                field.setStyle(field.getStyle().replace("#64b5f6", "rgba(255, 255, 255, 0.3)"));
            }
        });

        return field;
    }

    private Button createStyledButton(String text, String baseColor, String hoverColor) {
        Button button = new Button(text);
        button.setStyle(String.format("""
            -fx-background-color: %s;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-border-radius: 10px;
            -fx-background-radius: 10px;
            -fx-pref-height: 40px;
            -fx-padding: 0 20px;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 5, 0, 0, 2);
        """, baseColor));

        // Add hover effects
        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle().replace(baseColor, hoverColor));
        });
        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace(hoverColor, baseColor));
        });

        return button;
    }

    private Region createSectionSeparator() {
        Region separator = new Region();
        separator.setPrefHeight(1);
        separator.setStyle("""
            -fx-background-color: linear-gradient(to right, transparent, rgba(255, 255, 255, 0.3), transparent);
        """);
        return separator;
    }

    private void agregarProceso() {
        try {
            String pid = pidField.getText();
            int llegada = Integer.parseInt(llegadaField.getText());
            int duracion = Integer.parseInt(duracionField.getText());
            procesos.add(new Proceso(pid, llegada, duracion, 0));
            salidaArea.appendText("✅ Proceso agregado: " + pid + " (Llegada: " + llegada + ", Rafaga: " + duracion + ")\n");
            pidField.clear();
            llegadaField.clear();
            duracionField.clear();

            // Add success feedback
            agregarProcesoBtn.setStyle(agregarProcesoBtn.getStyle().replace("#4CAF50", "#66BB6A"));
            new Thread(() -> {
                try {
                    Thread.sleep(200);
                    javafx.application.Platform.runLater(() -> {
                        agregarProcesoBtn.setStyle(agregarProcesoBtn.getStyle().replace("#66BB6A", "#4CAF50"));
                    });
                } catch (InterruptedException ex) {}
            }).start();

        } catch (NumberFormatException e) {
            salidaArea.appendText("❌ Error: Arrival time and duration must be integers.\n");
        }
    }

    private void iniciarSimulacion() {
        Scheduler scheduler;
        if ("MLFQ".equals(algoritmoSelector.getValue())) {
            scheduler = new MLFQ();
        } else if ("SJF".equals(algoritmoSelector.getValue())){
            scheduler = new SJF();
        } else if ("SRJF".equals(algoritmoSelector.getValue())) {
            scheduler = new SRJF();
        } else if ("Round Robin".equals(algoritmoSelector.getValue())) {
            try {
                int quantum = Integer.parseInt(quantumField.getText());
                scheduler = new RoundRobin(quantum);
            } catch (NumberFormatException e) {
                salidaArea.appendText("❌ Error: Quantum tiene que ser un entero valido\n");
                return;
            }
        } else {
            scheduler = new FCFS();
        }
        simulator = new Simulator(scheduler, procesos);
        avanzarBtn.setDisable(false);
        resetBtn.setDisable(false);
        estadoLabel.setText("🔄 Ejecutando simulación...");
        estadoLabel.setStyle(estadoLabel.getStyle().replace("#81C784", "#FF9800"));
        salidaArea.clear();
        salidaArea.appendText("-> Simulación iniciada con algoritmo " + algoritmoSelector.getValue() + "\n");
        salidaArea.appendText("═══════════════════════════════════════════════════\n\n");
    }

    private String generarEstadoTexto() {
        StringBuilder sb = new StringBuilder();
        int tick = simulator.getTick();
        sb.append("┌─────────────────────────────────────────────────┐\n");
        sb.append("│ [Tick ").append(String.format("%3d", tick)).append("] → CPU: ");
        sb.append(String.format("%-25s", simulator.getCurrent() != null ? simulator.getCurrent() : "IDLE"));
        sb.append(" │\n");
        sb.append("├─────────────────────────────────────────────────┤\n");

        if (simulator.getScheduler() instanceof MLFQ mlfq) {
            sb.append("│ 🔄 MULTI-LEVEL FEEDBACK QUEUE (SOLARIS)        │\n");
            sb.append("├─────────────────────────────────────────────────┤\n");
            for (int i = 0; i < mlfq.getColas().size(); i++) {
                sb.append("│ Cola ").append(i).append(": ");
                String queueContent = mlfq.getColas().get(i).toString();
                sb.append(String.format("%-39s", queueContent.length() > 39 ? queueContent.substring(0, 36) + "..." : queueContent));
                sb.append(" │\n");
            }
        } else {
            List<Proceso> colaListos = simulator.getScheduler().getColaListos();
            sb.append("│ 📋 Cola de listos: ");
            String readyQueue = colaListos.isEmpty() ? "[]" : colaListos.toString();
            sb.append(String.format("%-30s", readyQueue.length() > 30 ? readyQueue.substring(0, 27) + "..." : readyQueue));
            sb.append(" │\n");
        }

        sb.append("├─────────────────────────────────────────────────┤\n");
        sb.append("│ ✅ Completados: ");
        String completed = simulator.getScheduler().getProcesosTerminados().toString();
        sb.append(String.format("%-32s", completed.length() > 32 ? completed.substring(0, 29) + "..." : completed));
        sb.append(" │\n");
        sb.append("└─────────────────────────────────────────────────┘\n\n");
        return sb.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}