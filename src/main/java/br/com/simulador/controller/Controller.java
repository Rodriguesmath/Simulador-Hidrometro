package main.java.br.com.simulador.controller;

import main.java.br.com.simulador.config.SimulatorConfig;
import main.java.br.com.simulador.hidrometro.Display;
import main.java.br.com.simulador.hidrometro.HidrometroSimulator;
import main.java.br.com.simulador.hidrometro.Saida;

/**
 * Classe principal que gerencia o fluxo da simulação.
 */
public class Controller {
    private final SimulatorConfig config;
    private final HidrometroSimulator simulator;

    public Controller(String configPath) {
        this.config = new SimulatorConfig(configPath);
        this.simulator = new HidrometroSimulator(config);

        // Cria e registra os observadores
        simulator.adicionarObservador(new Display());
        simulator.adicionarObservador(new Saida(this.config));
    }

    /**
     * Inicia e controla o loop principal da simulação.
     */
    public void startSimulacao() {
        // Exibe o estado inicial (t=0) antes de começar o loop
        simulator.notificarObservadores();

        while (!simulator.isFinalizado()) {
            simulator.avancarSimulacao();

            try {
                // Usa o intervalo de atualização do config (em ms) para a pausa
                Thread.sleep(config.getIntervaloAtualizacao());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Simulação interrompida.");
                break;
            }
        }

        simulator.notificarFimSimulacao();
    }
}

