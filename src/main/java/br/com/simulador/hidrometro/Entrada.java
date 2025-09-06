package main.java.br.com.simulador.hidrometro;

public class Entrada {

    private float bitola;
    private float pressao;
    private float volume;

    public Entrada(float bitola, float pressao, float volume) {
        this.bitola = bitola;
        this.pressao = pressao;
        this.volume = volume;
    }

    float fluxo() {
        // TODO: Implementar lógica de cálculo de fluxo
        return 0.0f;
    }

}