package main.java.br.com.simulador.strategy;

import java.util.Random;

/**
 * Interface para o padrão Strategy. Define o contrato para os diferentes
 * perfis de consumo de água ao longo do dia.
 */
public interface PerfilDeConsumoStrategy {

    /**
     * Calcula e retorna uma velocidade de fluxo de água aleatória,
     * baseada nos limites mínimo e máximo do perfil.
     *
     * @param random Uma instância de Random para garantir a aleatoriedade.
     * @return A velocidade do fluxo em m/s.
     */
    float getVelocidade(Random random);

    /**
     * Verifica se este perfil de consumo está ativo para a hora do dia fornecida.
     *
     * @param horaDoDia A hora do dia a ser verificada (0-23).
     * @return {@code true} se o perfil estiver ativo, {@code false} caso contrário.
     */
    boolean isAtivo(int horaDoDia);
}

