package main.java.br.com.simulador.hidrometro;

import main.java.br.com.simulador.config.Bitola;
import main.java.br.com.simulador.config.SimulatorConfig;
import main.java.br.com.simulador.observer.Observador;

/**
 * Responsável por gerar os logs de texto no console.
 * Implementa a interface Observador para ser notificado sobre atualizações.
 */
public record Saida(SimulatorConfig config) implements Observador {

    @Override
    public void atualizar(Medidor medidor, int tempoSimulado) {
        if (tempoSimulado == 0) {
            logInicioSimulacao(medidor);
        } else {
            logMedicao(medidor, tempoSimulado);
        }
    }

    @Override
    public void simulacaoFinalizada(Medidor estadoFinal) {
        System.out.println("=============================================");
        System.out.println("           SIMULAÇÃO FINALIZADA");
        System.out.println("=============================================");
        System.out.printf("Volume Total Medido: %.4f m³%n", estadoFinal.getM3());
        System.out.printf("Pressão Final: %.2f bar%n", estadoFinal.getPressao());
        System.out.println("=============================================");
    }

    private void logInicioSimulacao(Medidor medidor) {
        System.out.println("=============================================");
        System.out.println("           INICIANDO SIMULAÇÃO");
        System.out.println("=============================================");
        Bitola bitola = medidor.getBitola();
        System.out.println("Bitola: " + (bitola.getDiametro() * 1000) + "mm (" + bitola.getPolegada() + "\")");

        int tempoExec = config.getTempoExecucao();
        String tempoTotalStr = tempoExec == -1 ? "Infinita" : (tempoExec / 3600) + " horas";

        System.out.println("Tempo Total Simulado: " + tempoTotalStr);
        System.out.println("Intervalo de Atualização Visual: " + config.getIntervaloAtualizacao() + "ms");
        System.out.println("Escala de Tempo: 1 frame = " + config.getEscalaDeTempo() + "s");
        System.out.println("=============================================");
    }

    private void logMedicao(Medidor medidor, int tempoSimulado) {
        System.out.println("-------------------------");
        int horas = tempoSimulado / 3600;
        System.out.printf("Tempo Simulado: %ds (%dh)%n", tempoSimulado, horas);
        System.out.printf("Volume: %.4f m³ (%.2f litros)%n", medidor.getM3(), medidor.getM3() * 1000);
        System.out.printf("Pressão: %.2f bar%n", medidor.getPressao());
    }
}

