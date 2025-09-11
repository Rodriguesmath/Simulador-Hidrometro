package main.java.br.com.simulador.hidrometro;

import main.java.br.com.simulador.config.Bitola;
import main.java.br.com.simulador.config.SimulatorConfig;
import main.java.br.com.simulador.strategy.PerfilDeConsumoStrategy;

import java.util.List;
import java.util.Random;

/**
 * Representa as condições de entrada da água no hidrômetro em um dado momento.
 * Utiliza o padrão Strategy para determinar a velocidade do fluxo.
 */
public class Entrada {
    private final Bitola bitola;
    private final float pressao;
    private final float velocidade;
    private final Random random = new Random();

    public Entrada(int tempoAtualSegundos, SimulatorConfig config, List<PerfilDeConsumoStrategy> estrategias) {
        this.bitola = config.getBitola();

        // Gera pressão dentro de uma faixa normal
        float minPressao = config.getPressaoMinima();
        float maxPressao = config.getPressaoMaxima();
        this.pressao = minPressao + random.nextFloat() * (maxPressao - minPressao);

        // Determina a velocidade da água usando a estratégia de consumo apropriada
        this.velocidade = getVelocidade(tempoAtualSegundos, estrategias);
    }

    /**
     * Encontra a estratégia de consumo ativa para a hora atual e calcula a velocidade do fluxo.
     *
     * @param tempoAtualSegundos O tempo total da simulação em segundos.
     * @param estrategias        A lista de estratégias de consumo disponíveis.
     * @return A velocidade do fluxo de água em m/s.
     */
    private float getVelocidade(int tempoAtualSegundos, List<PerfilDeConsumoStrategy> estrategias) {
        int horaDoDia = (tempoAtualSegundos / 3600) % 24;

        for (PerfilDeConsumoStrategy estrategia : estrategias) {
            if (estrategia.isAtivo(horaDoDia)) {
                return estrategia.getVelocidade(random);
            }
        }
        // Retorna um valor padrão (próximo de zero) se nenhum perfil for encontrado
        return 0.01f;
    }

    /**
     * Calcula o fluxo de água em metros cúbicos por segundo (m³/s).
     *
     * @return O fluxo em m³/s.
     */
    public float calcularFluxo() {
        float raio = bitola.getDiametro() / 2.0f;
        float area = (float) (Math.PI * raio * raio);
        // CORREÇÃO: A fórmula correta para o fluxo é area * velocidade.
        return area * this.velocidade;
    }

    public float getPressao() {
        return pressao;
    }

    public Bitola getBitola() {
        return bitola;
    }
}

