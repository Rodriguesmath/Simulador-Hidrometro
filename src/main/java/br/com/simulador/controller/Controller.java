package main.java.br.com.simulador.controller;

import main.java.br.com.simulador.config.SimulatorConfig;
import main.java.br.com.simulador.hidrometro.HidrometroSimulator;

public class Controller {
    private SimulatorConfig config;
    private HidrometroSimulator simulator;

    public Controller(String configPath) {
        this.config = new SimulatorConfig(configPath);
        this.simulator = new HidrometroSimulator(
                config.getBitola(),
                config.getIntervaloAtualizacao(),
                (int)(config.getTempoExecucao() / 3600f) // horas simuladas
        );
    }

    public void startSimulacao() {
        printConfig();
        SimulationRunner runner = new SimulationRunner(config.getIntervaloAtualizacao());
        runner.run();
    }

    public void medir() {
        simulator.medir();
    }

    public void exibirMedicao() {
        simulator.exibirMedicao();
    }

    public float getVolumeAtual() {
        return simulator.getM3();
    }

    public float getPressaoAtual() {
        return simulator.getPressao();
    }

    public boolean isSimulacaoFinalizada() {
        return simulator.isFinalizado();
    }

    public void printConfig() {
        System.out.println("=== CONFIGURAÇÕES INICIAIS ===");
        System.out.println("Bitola: " + (config.getBitola().getDiametro() * 1000) + "mm (" + config.getBitola().getPolegada() + "\")");
        System.out.println("Intervalo de Atualização: " + config.getIntervaloAtualizacao() + "s");
        System.out.println("Tempo Total Simulado: " + (config.getTempoExecucao() / 3600) + " horas");
        System.out.println("===============================");
        System.out.println();
    }


    private class SimulationRunner {
        private final int intervaloAtualizacaoMs;

        public SimulationRunner(int intervaloAtualizacaoSegundos) {
            this.intervaloAtualizacaoMs = intervaloAtualizacaoSegundos * 1000;
        }

        public void run() {
            System.out.println("=== INICIANDO SIMULAÇÃO ===");
            int horaAtual = 1;

            while (!isSimulacaoFinalizada()) {
                System.out.println(">>> HORA " + horaAtual + " <<<");
                medir();
                exibirMedicao();
                horaAtual++;

                try {
                    Thread.sleep(intervaloAtualizacaoMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Simulação interrompida.");
                    break;
                }
            }

            System.out.println("=== SIMULAÇÃO FINALIZADA ===");
            System.out.println("Volume total medido: " + getVolumeAtual() + " m³");
            System.out.println("Pressão final: " + getPressaoAtual() + " bar");
        }
    }
}
