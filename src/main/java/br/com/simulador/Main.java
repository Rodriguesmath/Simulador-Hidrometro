package main.java.br.com.simulador;

import main.java.br.com.simulador.controller.Controller;

/**
 * Classe principal para iniciar a aplicação de simulação de hidrômetro.
 */
public class Main {
    public static void main(String[] args) {

        String configPath = "Simulador-Hidrometro/config/config.txt";

        try {
            Controller controller = new Controller(configPath);
            controller.startSimulacao();
        } catch (Exception e) {
            System.err.println("Ocorreu um erro fatal durante a execução da simulação.");
            e.printStackTrace();
        }
    }
}

