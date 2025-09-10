package main.java.br.com.simulador.hidrometro;
import main.java.br.com.simulador.config.Bitola;
import main.java.br.com.simulador.config.PerfilDeConsumo;
import java.util.Random;

public class Entrada {
    private final Bitola bitola;
    private final float pressao;    // em bar
    private final float velocidade; // em m/s

    private static final float MIN_PRESSAO_BAR = 3.0f;
    private static final float MAX_PRESSAO_BAR = 6.0f;

    /**
     * Cria uma instância de Entrada com valores de pressão e velocidade baseados
     * em um perfil de consumo diário realista.
     * @param bitola A bitola do hidrômetro.
     * @param tempoAtual O tempo total da simulação em segundos.
     */
    public Entrada(Bitola bitola, float tempoAtual) {
        this.bitola = bitola;
        Random random = new Random((long) (tempoAtual * 1000));

        // Gera pressão dentro de uma faixa normal
        this.pressao = MIN_PRESSAO_BAR + random.nextFloat() * (MAX_PRESSAO_BAR - MIN_PRESSAO_BAR);

        // Determina o perfil de consumo com base na hora do dia
        int horaDoDia = (int) (tempoAtual / 3600) % 24;
        PerfilDeConsumo perfil = PerfilDeConsumo.getPerfil(horaDoDia);

        // Gera a velocidade da água com base no perfil de consumo
        float minVelocidade = perfil.getMinVelocidade();
        float maxVelocidade = perfil.getMaxVelocidade();
        this.velocidade = minVelocidade + random.nextFloat() * (maxVelocidade - minVelocidade);
    }

    /**
     * Calcula o fluxo de água em metros cúbicos por segundo (m³/s).
     * @return O fluxo em m³/s.
     */
    public float calcularFluxo() {
        float raio = bitola.getDiametro() / 2.0f;
        float area = (float) (Math.PI * raio * raio);
        return area * this.velocidade;
    }

    public float getPressao() {
        return pressao;
    }

    public Bitola getBitola() {
        return bitola;
    }
}

