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
    public class    Entrada {

        private final Bitola bitola;
        private final float pressao;
        private final float velocidade;
        private final float fluxo;
        private final Random random = new Random();


        public Entrada(int tempoAtualSegundos, SimulatorConfig config, List<PerfilDeConsumoStrategy> estrategias, ControleVazao controleVazao) {
            this.bitola = config.getBitola();

            float minPressao = config.getPressaoMinima();
            float maxPressao = config.getPressaoMaxima();
            this.pressao = minPressao + random.nextFloat() * (maxPressao - minPressao);

            float velocidadeBase;

            if (controleVazao.getPercentual() == 0 && config.isSimularAr()) {

                float minVelocidadeAr = 0.001f;
                float maxVelocidadeAr = 0.005f;
                velocidadeBase = minVelocidadeAr + random.nextFloat() * (maxVelocidadeAr - minVelocidadeAr);

            } else {

                velocidadeBase = getVelocidadeBase(tempoAtualSegundos, estrategias);
                double multiplicador = controleVazao.getMultiplicador();
                velocidadeBase = (float) (velocidadeBase * multiplicador);
            }


            float raio = this.bitola.getDiametro() / 2.0f;
            float area = (float) (Math.PI * raio * raio);
            float fluxoDesejado = area * velocidadeBase;
            float fluxoMaximo = this.bitola.getQmax();
            this.fluxo = Math.min(fluxoDesejado, fluxoMaximo);
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
            return 0.0f;
        }

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

