package main.java.br.com.simulador.hidrometro;

/**
 * Classe thread-safe para armazenar e controlar o multiplicador de vazão em tempo real.
 * Este objeto é compartilhado entre a interface (Display) e o motor da simulação (Entrada).
 */
public class ControleVazao {
    // volatile garante que as mudanças feitas por uma thread sejam visíveis para outras.
    private volatile double multiplicador = 1.0;

    /**
     * Define o novo multiplicador de vazão.
     * @param multiplicador O valor do multiplicador (ex: 1.0 para normal, 2.0 para o dobro).
     */
    public synchronized void setMultiplicador(double multiplicador) {
        this.multiplicador = multiplicador;
    }

    /**
     * Obtém o valor atual do multiplicador de vazão.
     * @return O multiplicador atual.
     */
    public synchronized double getMultiplicador() {
        return multiplicador;
    }
}
