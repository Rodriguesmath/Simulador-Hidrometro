package main.java.br.com.simulador.config;

/**
 * Enumeração que define as bitolas (diâmetros nominais) de hidrômetros disponíveis na simulação.
 *
 * Um 'enum' em Java é um tipo especial que representa um grupo de constantes pré-definidas.
 * Usar um enum para a bitola traz vantagens como:
 * - **Type Safety:** Garante que apenas valores válidos de bitola possam ser usados no código.
 * - **Centralização:** Todas as informações relacionadas a uma bitola (diâmetro em metros, vazão máxima, etc.)
 * ficam centralizadas em um único local, facilitando a manutenção.
 * - **Legibilidade:** O código se torna mais claro (ex: `medidor.getBitola() == Bitola.MEIA`).
 */
public enum Bitola {

    // --- Definição das Constantes (Instâncias do Enum) ---
    // Cada linha define uma constante do tipo Bitola, com seus atributos específicos.
    // O formato é: NOME_DA_CONSTANTE("representação em polegadas", diâmetro em metros, vazão máxima em m³/s).
    MEIA("1/2", 0.015f, 0.000694f),
    TRES_QUARTOS("3/4", 0.020f, 0.000972f),
    UM("1", 0.025f, 0.001389f),
    UM_E_MEIO("1 1/2", 0.040f, 0.003055f),
    DOIS("2", 0.050f, 0.005555f),
    TRES("3", 0.075f, 0.0125f),
    QUATRO("4", 0.100f, 0.0222f);

    // --- Campos (Atributos) de cada Constante ---
    /** A representação da bitola em formato de texto (polegadas). */
    private final String polegada;
    /** O diâmetro interno do hidrômetro, em metros. */
    private final float diametro; // em metros
    /** A vazão máxima (Qmax) suportada pelo hidrômetro, em metros cúbicos por segundo (m³/s). */
    private final float qmax;     // em m³/s

    /**
     * Construtor do enum. Em enums, o construtor é sempre 'private' por padrão.
     * Ele é chamado uma vez para cada constante definida acima, associando os valores aos campos.
     *
     * @param polegada A representação em polegadas.
     * @param diametro O diâmetro em metros.
     * @param qmax     A vazão máxima em m³/s.
     */
    Bitola(String polegada, float diametro, float qmax) {
        this.polegada = polegada;
        this.diametro = diametro;
        this.qmax = qmax;
    }

    // --- Métodos de Acesso (Getters) ---
    // Permitem que o resto da aplicação acesse os atributos de uma constante específica.
    // Exemplo de uso: float d = Bitola.MEIA.getDiametro(); // d será 0.015f

    public String getPolegada() {
        return polegada;
    }

    public float getDiametro() {
        return diametro;
    }

    public float getQmax() {
        return qmax;
    }

    /**
     * Método de fábrica estático para converter uma String em uma constante Bitola.
     *
     * Isso é extremamente útil para converter valores lidos de arquivos de configuração
     * ou de entradas de usuário para o tipo seguro do enum.
     *
     * @param polegada A representação em String da bitola (ex: "1/2", "3/4").
     * @return A constante {@link Bitola} correspondente.
     * @throws IllegalArgumentException se a String não corresponder a nenhuma bitola suportada.
     */
    public static Bitola fromString(String polegada) {
        // Itera sobre todos os valores possíveis do enum (MEIA, TRES_QUARTOS, etc.).
        for (Bitola b : Bitola.values()) {
            // Compara a string de entrada com a representação em polegada de cada constante.
            if (b.polegada.equals(polegada)) {
                // Se encontrar uma correspondência, retorna a constante.
                return b;
            }
        }
        // Se o loop terminar sem encontrar uma correspondência, lança uma exceção
        // para indicar que o valor de entrada é inválido. Isso ajuda a "falhar rápido".
        throw new IllegalArgumentException("Bitola não suportada: " + polegada);
    }
}