package main.java.br.com.simulador.controller;
import main.java.br.com.simulador.config.SimulatorConfig;
import main.java.br.com.simulador.hidrometro.HidrometroSimulator;


public class Controller {
    private SimulatorConfig config;
    private HidrometroSimulator simulator;

    public Controller(SimulatorConfig config) {
        this.config = config;
    }

    public void run() {
        // Ajuste os métodos conforme os nomes corretos dos getters em SimulatorConfig
        simulator = new HidrometroSimulator(
            config.getBitola(),
            config.getIntervaloAtualizacao(),
            config.getTempoExecucao()
        );
    }
}
