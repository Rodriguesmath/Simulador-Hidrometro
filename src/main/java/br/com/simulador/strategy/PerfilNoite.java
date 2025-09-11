package main.java.br.com.simulador.strategy;

import java.util.Random;

/**
 * Estratégia de consumo para o período da noite.
 * Os parâmetros são injetados via construtor.
 */
public class PerfilNoite implements PerfilDeConsumoStrategy {

    private final float minVelocidade;
    private final float maxVelocidade;
    private final int horaInicio;
    private final int horaFim;

    /**
     * Construtor que recebe os parâmetros de configuração para este perfil.
     *
     * @param minVelocidade A velocidade mínima do fluxo em m/s.
     * @param maxVelocidade A velocidade máxima do fluxo em m/s.
     * @param horaInicio    A hora de início do período (inclusivo).
     * @param horaFim       A hora de fim do período (inclusivo).
     */
    public PerfilNoite(float minVelocidade, float maxVelocidade, int horaInicio, int horaFim) {
        this.minVelocidade = minVelocidade;
        this.maxVelocidade = maxVelocidade;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    @Override
    public float getVelocidade(Random random) {
        return minVelocidade + random.nextFloat() * (maxVelocidade - minVelocidade);
    }

    @Override
    public boolean isAtivo(int horaDoDia) {
        return horaDoDia >= horaInicio && horaDoDia <= horaFim;
    }
}

