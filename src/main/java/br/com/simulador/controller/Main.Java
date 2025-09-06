package br.com.simulador;

public class Main {
    public static void main(String[] args) {

        // passa config para o Controller
        Controller controller = new Controller(config);

        // roda o sistema
        controller.run();
    }
}
