package main.java.br.com.simulador.config;

/**
 * Enumeração que define diferentes perfis de consumo de água ao longo do dia,
 * com faixas de velocidade de fluxo correspondentes.
 */
public enum PerfilDeConsumo {
    // Madrugada: consumo muito baixo (ex: vazamentos, uso mínimo)
    MADRUGADA(0.01f, 0.2f),
    // Manhã: pico de consumo
    MANHA(1.5f, 2.8f),
    // Tarde: consumo moderado e intermitente
    TARDE(0.5f, 1.5f),
    // Noite: segundo pico de consumo
    NOITE(1.2f, 2.5f);

    private final float minVelocidade;
    private final float maxVelocidade;

    PerfilDeConsumo(float minVelocidade, float maxVelocidade) {
        this.minVelocidade = minVelocidade;
        this.maxVelocidade = maxVelocidade;
    }

    public float getMinVelocidade() {
        return minVelocidade;
    }

    public float getMaxVelocidade() {
        return maxVelocidade;
    }

    /**
     * Retorna o perfil de consumo apropriado com base na hora do dia.
     * @param horaDoDia A hora do dia (0-23).
     * @return O PerfilDeConsumo correspondente.
     */
    public static PerfilDeConsumo getPerfil(int horaDoDia) {
        if (horaDoDia >= 0 && horaDoDia <= 5) {
            return MADRUGADA;
        } else if (horaDoDia >= 6 && horaDoDia <= 9) {
            return MANHA;
        } else if (horaDoDia >= 18 && horaDoDia <= 23) {
            return NOITE;
        } else { // 10h às 17h
            return TARDE;
        }
    }
}
