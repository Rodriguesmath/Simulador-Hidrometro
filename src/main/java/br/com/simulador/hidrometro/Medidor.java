package main.java.br.com.simulador.hidrometro;
import main.java.br.com.simulador.config.Bitola;

public class Medidor {
    // Variáveis de instância para acumular os valores (não podem ser 'final')
    private float m3;
    private float mm3;
    private float pressao;
    private Bitola bitola;

    /**
     * Cria um Medidor com um estado inicial. O consumo começa em zero.
     * @param entradaInicial Objeto com os dados iniciais (bitola, pressão, etc.)
     */
    public Medidor(Entrada entradaInicial) {
        this.m3 = 0.0f; // Consumo inicial é zero
        this.mm3 = 0.0f;
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

        // 4. Recalcula o valor total em mm³ com base no novo total de m³.
        this.mm3 = this.m3 * 1_000_000_000f;
    }

    public float getMm3() {
        return mm3;
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