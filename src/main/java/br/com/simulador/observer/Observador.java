package main.java.br.com.simulador.observer;

import main.java.br.com.simulador.hidrometro.Medidor;

/**
 * Interface para o padrão Observer. Define os métodos que os observadores
 * devem implementar para receber atualizações do sujeito (HidrometroSimulator).
 */
public interface Observador {

    /**
     * Chamado a cada passo da simulação para atualizar o estado do observador.
     * @param medidor O estado atual do medidor.
     * @param tempoSimulado O tempo atual da simulação em segundos.
     */
    void atualizar(Medidor medidor, int tempoSimulado);

    /**
     * Chamado uma única vez quando a simulação é finalizada.
     * @param estadoFinal O estado final do medidor.
     */
    void simulacaoFinalizada(Medidor estadoFinal);
}

