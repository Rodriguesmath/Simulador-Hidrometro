package main.java.br.com.simulador.hidrometro;
import main.java.br.com.simulador.config.Bitola;

public class HidrometroSimulator {

    private Medidor medidor;
    private Saida saidaDados;
    private Display display;
    private Entrada entradaDados;
    private int intervalo;
    private int quantidade;
    private int medicaoAtual = 0;
    private float tempoTotal = 0;

    public HidrometroSimulator(Bitola bitola, int intervalo, int quantidade) {
        this.saidaDados = new Saida();
        this.display = new Display();
        this.intervalo = intervalo;
        this.quantidade = quantidade;

        // Medidor é criado uma única vez com o estado inicial
        Entrada entradaInicial = new Entrada(bitola, 0.0f);
        this.medidor = new Medidor(entradaInicial);
    }

    // Avança uma medição e atualiza o medidor
    public void medir() {
        if (medicaoAtual == 0) {
            saidaDados.logInicioSimulacao(quantidade, intervalo);
        }
        if (medicaoAtual < quantidade) {
            // --- AJUSTE APLICADO: Simula uma hora completa a cada chamada ---
            int segundosPorHora = 3600;

            // Loop interno para simular uma hora em pequenos intervalos, tornando o consumo realista
            for (int t = 0; t < segundosPorHora; t += intervalo) {
                tempoTotal += intervalo; // O tempo total da simulação avança corretamente

                // Cria uma nova instância de Entrada com dados para o momento atual
                Entrada novaEntrada = new Entrada(medidor.getBitola(), tempoTotal);

                // Atualiza o medidor com o consumo deste pequeno intervalo
                medidor.atualizarMedicao(novaEntrada, intervalo);
            }

            // O log é feito no final de cada hora simulada, com o valor acumulado
            saidaDados.logMedicao(getM3(), getPressao(), (int)tempoTotal, medidor.getBitola(), intervalo, quantidade);

            medicaoAtual++;
            if (medicaoAtual == quantidade) {
                saidaDados.logFimSimulacao(getM3());
            }
        }
    }


    public float getM3() {
        return medidor.getM3();
    }

    public float getMm3() {
        return medidor.getMm3();
    }

    public float getPressao() {
        return medidor.getPressao();
    }

    public void exibirMedicao() {
        // A chamada agora passa apenas o valor total em m³, que é o correto.
        display.exibirImagem(getM3());
    }

    public boolean isFinalizado() {
        return medicaoAtual >= quantidade;
    }
}

