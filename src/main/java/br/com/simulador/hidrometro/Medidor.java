package main.java.br.com.simulador.hidrometro;

public class Medidor {
    private final float mm3;
    private final float m3;
    private final float pressao;

    /**
     * Cria um Medidor baseado nos valores de Entrada e em um tempo de medição (em segundos).
     * @param entrada Objeto Entrada com bitola, volume e pressão
     * @param tempoSegundos Tempo de medição em segundos
     */
    public Medidor(Entrada entrada, float tempoSegundos) {
        this.pressao = entrada.getPressao();
        float fluxo = entrada.calcularFluxo(); // m³/s
        this.m3 = fluxo * tempoSegundos;       // volume em m³
        this.mm3 = this.m3 * 1_000_000_000f;  // volume em mm³
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
}