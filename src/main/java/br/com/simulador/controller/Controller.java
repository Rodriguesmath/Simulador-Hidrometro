package main.java.br.com.simulador.controller;

import main.java.br.com.simulador.config.SimulatorConfig;
import main.java.br.com.simulador.hidrometro.ControleVazao;
import main.java.br.com.simulador.hidrometro.Display;
import main.java.br.com.simulador.hidrometro.HidrometroSimulator;
import main.java.br.com.simulador.hidrometro.Saida;

/**
 * Classe principal que gere o fluxo da simulação.
 */
public class Controller {
    private final SimulatorConfig config;
    private final HidrometroSimulator simulator;
    private final ControleVazao controleVazao; // 1. Adicionar o campo

    public Controller(String configPath) {
        this.config = new SimulatorConfig(configPath);
        this.controleVazao = new ControleVazao(); // 2. Criar a instância única

        // 3. Partilhar a mesma instância com os outros componentes
        this.simulator = new HidrometroSimulator(config, this.controleVazao);
        simulator.adicionarObservador(new Display(this.controleVazao));
        simulator.adicionarObservador(new Saida(this.config));
    }

    /**
     * Inicia e controla o ciclo principal da simulação.
     */
    public void startSimulacao() {
        // Exibe o estado inicial (t=0) antes de começar o ciclo
        simulator.notificarObservadores();

        // Ciclo principal da simulação
        while (!simulator.isFinalizado()) {
            simulator.avancarSimulacao();

            try {
                Thread.sleep(config.getIntervaloAtualizacao());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Simulação interrompida.");
                break;
            }
        }

        // Após o fim do ciclo, notifica todos os observadores sobre o término
        simulator.notificarFimSimulacao();

    }
}

