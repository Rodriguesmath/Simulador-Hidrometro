    package main.java.br.com.simulador.hidrometro;

    import main.java.br.com.simulador.config.Bitola;
    import main.java.br.com.simulador.config.SimulatorConfig;
    import main.java.br.com.simulador.strategy.PerfilDeConsumoStrategy;

    import java.util.List;
    import java.util.Random;

    /**
     * Representa os dados de entrada para um determinado instante na simulação.
     * Calcula a velocidade e o fluxo da água, aplicando o controlo de vazão em tempo real
     * e validando contra o fluxo máximo da bitola, mantendo a dependência do SimulatorConfig.
     */
    public class Entrada {

        private final Bitola bitola;
        private final float pressao;
        private final float velocidade; // A velocidade final, consistente com o fluxo validado
        private final float fluxo;      // O fluxo final validado (m³/s)
        private final Random random = new Random();

        public Entrada(int tempoAtualSegundos, SimulatorConfig config, List<PerfilDeConsumoStrategy> estrategias, ControleVazao controleVazao) {
            // Mantém a obtenção da bitola a partir do config, como na sua arquitetura
            this.bitola = config.getBitola();

            // Gera pressão dentro da faixa definida no config
            float minPressao = config.getPressaoMinima();
            float maxPressao = config.getPressaoMaxima();
            this.pressao = minPressao + random.nextFloat() * (maxPressao - minPressao);

            // --- LÓGICA DO CONTROLO DE VAZÃO ---

            // 1. Obter a velocidade base do perfil de consumo ativo
            float velocidadeBase = getVelocidadeBase(tempoAtualSegundos, estrategias);

            // 2. Aplicar o multiplicador de vazão definido pelo utilizador
            double multiplicador = controleVazao.getMultiplicador();
            float velocidadeDesejada = (float) (velocidadeBase * multiplicador);

            // 3. Calcular o fluxo desejado com base na velocidade ajustada
            float raio = this.bitola.getDiametro() / 2.0f;
            float area = (float) (Math.PI * raio * raio);
            float fluxoDesejado = area * velocidadeDesejada;

            // 4. Obter o fluxo máximo (qmax) permitido pela bitola
            float fluxoMaximo = this.bitola.getQmax();

            // 5. VALIDAR: O fluxo final é o menor valor entre o desejado e o máximo permitido
            this.fluxo = Math.min(fluxoDesejado, fluxoMaximo);

            // 6. Recalcular a velocidade final para ser consistente com o fluxo validado
            this.velocidade = (area > 0) ? this.fluxo / area : 0;
        }

        /**
         * Encontra a estratégia de consumo ativa para a hora atual e retorna a velocidade base.
         */
        private float getVelocidadeBase(int tempoAtualSegundos, List<PerfilDeConsumoStrategy> estrategias) {
            int horaDoDia = (tempoAtualSegundos / 3600) % 24;
            for (PerfilDeConsumoStrategy estrategia : estrategias) {
                if (estrategia.isAtivo(horaDoDia)) {
                    return estrategia.getVelocidade(random);
                }
            }
            return 0.0f; // Retorno padrão
        }


        /**
         * Retorna o fluxo de água já calculado e validado em metros cúbicos por segundo (m³/s).
         * @return O fluxo validado em m³/s.
         */
        public float calcularFluxo() {
            return this.fluxo;
        }

        public float getPressao() {
            return pressao;
        }

        public Bitola getBitola() {
            return bitola;
        }
    }

