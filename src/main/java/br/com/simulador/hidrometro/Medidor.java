package main.java.br.com.simulador.hidrometro;

import main.java.br.com.simulador.config.Bitola;

/**
 * Representa o estado atual do hidrômetro (os dados que ele acumula e mede).
 */
public class Medidor {
    private float m3;
    private float pressao;
    private final Bitola bitola;

    /**
     * Cria um Medidor com estado inicial a partir de uma primeira leitura (Entrada).
     * @param entradaInicial O estado da rede no momento t=0.
     */
    public Medidor(Entrada entradaInicial) {
        this.m3 = 0.0f;
        this.pressao = entradaInicial.getPressao();
        this.bitola = entradaInicial.getBitola();
    }

    /**
     * Atualiza o estado do medidor, acumulando o consumo e atualizando a pressão.
     * @param novaEntrada Objeto com os novos dados de fluxo e pressão para o intervalo.
     * @param intervalo O tempo em segundos desde a última medição.
     */
    public void atualizarMedicao(Entrada novaEntrada, int intervalo) {
        // 1. Calcula o consumo que ocorreu apenas neste intervalo de tempo.
        float fluxo = novaEntrada.calcularFluxo(); // Pega o fluxo atual em m³/s
        float consumoNesteIntervalo = fluxo * intervalo; // Calcula o volume em m³

        // 2. Acumula (soma) o novo consumo ao total que já existia.
        this.m3 += consumoNesteIntervalo;

        // 3. Atualiza a pressão para o valor mais recente.
        this.pressao = novaEntrada.getPressao();
    }

    public float getM3() {
        return m3;
    }

    public float getPressao() {
        return pressao;
    }

    public Bitola getBitola() {
        return bitola;
    }
}

