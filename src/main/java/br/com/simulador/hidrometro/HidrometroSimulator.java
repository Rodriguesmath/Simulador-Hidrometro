package main.java.br.com.simulador.hidrometro;

public class HidrometroSimulator {

    private Medidor medidor;
    private Saida saidaDados;
    private Display display;
    private Entrada entradaDados;

    public HidrometroSimulator(double bitola, int intervalo, int quantidade) {
        this.medidor = new Medidor();
        this.saidaDados = new Saida();
        this.display = new Display();
        this.entradaDados = new Entrada((float) bitola, 0.0f, 0.0f);
    }

    public double medir() {
        // TODO: Implementar lógica de medição
        return 0.0;
    }
    public void exibirMedida() {
        // TODO: Implementar lógica de exibição
    }

}