package main.java.br.com.simulador.hidrometro;

import main.java.br.com.simulador.config.Bitola;

public class Entrada {

    private Bitola bitola;
    private float pressao;
    private float volume;

    public Entrada(Bitola bitola, float volume) {
        this.bitola = bitola;
        this.volume = volume;
        this.pressao = calcularPressao(bitola, volume);
    }

    // A pressão da água em um ponto de um sistema hidráulico pode ser estimada pela
    // fórmula:
    // P = (ρ * g * h), onde:
    // P = pressão (Pa)
    // ρ = densidade da água (~1000 kg/m³)
    // g = aceleração da gravidade (~9.81 m/s²)
    // h = altura da coluna de água (m)
    //
    // No entanto, se quisermos relacionar a pressão com o volume e a bitola do
    // cano,
    // podemos estimar a velocidade da água (v) usando a equação da continuidade:
    // Q = v * A, onde:
    // Q = vazão volumétrica (m³/s)
    // v = velocidade da água (m/s)
    // A = área da seção transversal do cano (m²)
    //
    // Se volume for o volume por segundo (vazão), então:
    // v = volume / A
    //
    // A pressão dinâmica (pressão devido ao movimento) pode ser estimada por:
    // P = 0.5 * ρ * v²
    //
    // Aqui, bitola é o diâmetro interno do cano (em metros).
    private float calcularPressao(Bitola bitola, float volume) {
        final float densidadeAgua = 1000.0f; // kg/m³
        final float pi = 3.1415927f;

        // Área da seção transversal do cano (A = π * r²)
        float raio = bitola.getDiametro() / 2.0f;
        float area = pi * raio * raio;

        // Velocidade da água (v = Q / A)
        float velocidade = volume / area;

        // Pressão dinâmica (P = 0.5 * ρ * v²)
        float pressaoCalculada = 0.5f * densidadeAgua * velocidade * velocidade;

        return pressaoCalculada; // em Pascal (Pa)
    }

    public float calcularFluxo() {
        final float densidadeAgua = 1000.0f;
        final float pi = 3.1415927f;
        float area = pi * (bitola.getDiametro() / 2.0f) * (bitola.getDiametro() / 2.0f);
        float velocidade = (float) Math.sqrt(2 * pressao / densidadeAgua);
        return velocidade * area;
    }

    public float getPressao() {
        return pressao; // em Pascal (Pa)
    }

}