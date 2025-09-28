package main.java.br.com.simulador;

import main.java.br.com.simulador.hidrometro.display.SimuladorThreads;

/**
 * Classe principal para iniciar a aplicação de simulação de hidrômetro.
 */
public class Main {
    public static void main(String[] args) {

        String configPath = "config/config.txt";
        new SimuladorThreads(configPath).startThreads();
    }
}
