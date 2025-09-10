package main.java.br.com.simulador.hidrometro;

import main.java.br.com.simulador.config.Bitola;

public class Saida {

    public void logMedicao(float volume, float pressao, int tempo, Bitola bitola, int intervaloAtualizacao, int tempoExecucao) {
        System.out.println("=== LOG DE MEDIÇÃO ===");
        System.out.println("Tempo: " + tempo + "s");
        System.out.println("Volume: " + (volume * 1000) + " litros");
        System.out.println("Pressão: " + pressao + " bar");
        System.out.println("Bitola: " + (bitola.getDiametro() * 1000) + "mm (" + bitola.getPolegada() + "\")");
        System.out.println("Intervalo de Atualização: " + intervaloAtualizacao + "s");
        System.out.println("Tempo de Execução: " + tempoExecucao + "s");
        System.out.println("========================");
    }



    public void logInicioSimulacao(int quantidade, int intervalo) {
        System.out.println("Iniciando simulação:");
        System.out.println("Quantidade de medições: " + quantidade);
        System.out.println("Intervalo: " + intervalo + "s");
    }

    public void logFimSimulacao(float volumeTotal) {
        System.out.println("Simulação concluída. Volume total: " + volumeTotal + " m³");
    }
}
