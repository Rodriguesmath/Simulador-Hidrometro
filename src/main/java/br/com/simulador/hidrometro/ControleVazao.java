package main.java.br.com.simulador.hidrometro;

/**
 * Classe thread-safe para armazenar e controlar o multiplicador de vazão em tempo real.
 * Agora trabalha em percentual (0 a 100), mas internamente guarda normalizado (0.0 a 1.0).
 */
public class ControleVazao {
    private volatile double multiplicador = 1.0; // 100% por padrão

    /**
     * Define o multiplicador em percentual (0 a 100).
     */
    public synchronized void setMultiplicador(double percentual) {
        if (percentual < 0) percentual = 0;
        if (percentual > 100) percentual = 100;
        this.multiplicador = percentual / 100.0;
    }

    /**
     * Retorna o multiplicador já normalizado (0.0 a 1.0).
     */
    public synchronized double getMultiplicador() {
        return multiplicador;
    }

    /**
     * Retorna o percentual "cru" (0 a 100) — útil para exibir na interface.
     */
    public synchronized int getPercentual() {
        return (int) Math.round(multiplicador * 100);
    }
}
