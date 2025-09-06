package main.java.br.com.simulador.hidrometro;
import main.java.br.com.simulador.config.Bitola;

public class HidrometroSimulator {

    private Medidor medidor;
    private Saida saidaDados;
    private Display display;
    private Entrada entradaDados;
    private int intervalo;
    private int quantidade;

    public HidrometroSimulator(Bitola bitola, int intervalo, int quantidade) {
        this.saidaDados = new Saida();
        this.display = new Display();
        this.intervalo = intervalo;
        this.quantidade = quantidade;
        // Inicializa a entrada com volume inicial 0
        this.entradaDados = new Entrada(bitola, 0.0f);
        // Inicializa o medidor com a entrada e tempo 0
        this.medidor = new Medidor(entradaDados, 0);
    }

    // Simula medições ao longo do tempo e retorna o último valor de volume medido (m³)
    public float medir() {
        float tempoTotal = 0;
        float volumeTotal = 0;
        for (int i = 0; i < quantidade; i++) {
            tempoTotal += intervalo;
            // Atualiza o medidor com o tempo acumulado
            this.medidor = new Medidor(entradaDados, tempoTotal);
            volumeTotal = medidor.getM3();
        }
        return volumeTotal;
    }
    public void exibirMedicao() {
        display.exibirImagem(medidor.getM3(), medidor.getMm3(), medidor.getPressao());
    }

}