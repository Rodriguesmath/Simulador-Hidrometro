package main.java.br.com.simulador.controller;

import main.java.br.com.simulador.controller.Controller;
// classe criada para testar o código (main)
public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller("Simulador-Hidrometro/config/config.txt");
        controller.startSimulacao();
    }
}
